package baikal.web.footballapp.user.activity;

import android.content.Intent;
import android.graphics.Bitmap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.DateToString;
import baikal.web.footballapp.PersonalActivity;

import baikal.web.footballapp.R;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.model.EditProfile;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.Region;
import baikal.web.footballapp.model.User;
import baikal.web.footballapp.players.activity.PlayersPage;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static baikal.web.footballapp.Controller.BASE_URL;

public class UserInfo extends AppCompatActivity {
    private static final String TAG = "UserInfo: ";
    private final Logger log = LoggerFactory.getLogger(UserInfo.class);
    private static final int EDIT_PROFILE_DATA_SUCCESS = 32443; //magic number

    private final PersonalInfo personalInfo = new PersonalInfo(this, true);

    private User user;
    private String token;
    private Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "on create ...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info);
        ImageButton buttonClose = findViewById(R.id.userProfileClose);
        ImageButton buttonSave = findViewById(R.id.userProfileSave);

        buttonClose.setOnClickListener(v -> finish());
        buttonSave.setOnClickListener(v -> saveData());

        this.getSupportFragmentManager().beginTransaction().add(R.id.userInfoViewPager, personalInfo, "PersonalInfo").show(personalInfo).commit();
    }



    private void saveData ()
    {
        String name = personalInfo.textName.getText().toString();
        String surName = personalInfo.textSurname.getText().toString();
        String patronymic = personalInfo.textPatronymic.getText().toString();
        String login = personalInfo.textLogin.getText().toString();
        String DOB = (new DateToString()).TimeForServer(personalInfo.textDOB.getText().toString(), "dd.MM.yyyy", "ru");
        String region = personalInfo.regionsId.get(personalInfo.spinnerRegion.getSelectedItemPosition());

        Map<String, RequestBody> map = new HashMap<>();
        RequestBody requestName = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody requestSurname = RequestBody.create(MediaType.parse("text/plain"), surName);
        RequestBody requestPatronymic = RequestBody.create(MediaType.parse("text/plain"), patronymic);
        RequestBody requestLogin = RequestBody.create(MediaType.parse("text/plain"), login);
        RequestBody requestRegion = RequestBody.create(MediaType.parse("text/plain"),region);
        RequestBody requestDOB = RequestBody.create(MediaType.parse("text/plain"),DOB);
        RequestBody requestId = RequestBody.create(MediaType.parse("text/plain"), PersonalActivity.id);

        map.put("name", requestName);
        map.put("surname", requestSurname);
        map.put("lastname", requestPatronymic);
        map.put("login", requestLogin);
        map.put("region", requestRegion);
        map.put("birthdate",requestDOB);
        map.put("_id", requestId);

        Bitmap photo = personalInfo.myBitmap;
        if (photo == null){
            photo = BitmapFactory.decodeResource(getResources(), R.drawable.ic_logo2);
            log.info("INFO: photo is null");
        }

        File file = new File(getCacheDir(), "photo");

        try {
//            if (!file.createNewFile())
//                throw new IOException();

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 80 /*ignored for PNG*/, bos);

            byte[] bitmapData = bos.toByteArray();
            //write the bytes in file
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapData);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            log.error(TAG, e);
        }

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);

        final Bitmap myBitmap = photo;
        Controller.getApi().editProfile(person.getId(), PersonalActivity.token, map, body).enqueue(new Callback<EditProfile>() {
            @Override
            public void onResponse(Call<EditProfile> call, Response<EditProfile> response) {
                if (response.isSuccessful())
                    if (response.body() != null) {
                        Person p = response.body().getPerson();

                        user.setUser(p);

                        //all is ok
                        if (person.getType().equals("player")) {
                            Person man1 = new Person();
                            for (Person man : PersonalActivity.people) {
                                if (man.getId().equals(person.getId())) {
                                    man1 = man;
                                }
                            }
                            PersonalActivity.people.remove(man1);
                            PersonalActivity.people.add(user.getUser());
                            PlayersPage.adapter.notifyDataSetChanged();
                        }
                        String str = "Изменения сохранены.";

                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("newUserData", user);
                        setResult(RESULT_OK, intent);

                        Toast.makeText(UserInfo.this, str, Toast.LENGTH_LONG).show();
                        finish();
                    }
            }

            @Override
            public void onFailure(Call<EditProfile> call, Throwable t) {

            }
        });

        finish();
    }

}
