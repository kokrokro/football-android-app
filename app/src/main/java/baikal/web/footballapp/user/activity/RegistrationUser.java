package baikal.web.footballapp.user.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.DateToString;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.User;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationUser extends AppCompatActivity {

    private final Logger log = LoggerFactory.getLogger(RegistrationUser.class);
//    private final PersonalInfo personalInfo = new PersonalInfo(this, false, null);
    private final PersonalInfo personalInfo = new PersonalInfo(this, false);
    private final FragmentManager fragmentManager = this.getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_user);
        ImageButton imageClose = findViewById(R.id.registrationClose);
        ImageButton imageSave = findViewById(R.id.registrationSave);

        imageClose.setOnClickListener(v -> finish());
        imageSave.setOnClickListener(v -> SignUp());
        fragmentManager.beginTransaction().add(R.id.registrationViewPagerFragment, personalInfo, "personalInfo").show(personalInfo).commit();
    }

    public void showAlertDialogButtonClicked() {
        Toast.makeText(getBaseContext(), "Выберите регион", Toast.LENGTH_SHORT).show();
    }

    private void SignUp() {
        String name = personalInfo.textName.getText().toString();
        String surName = personalInfo.textSurname.getText().toString();
        String patronymic = personalInfo.textPatronymic.getText().toString();
        String login = personalInfo.textLogin.getText().toString();
        String password = personalInfo.textPassword.getText().toString();
        String DOB = DateToString.TimeForServer(personalInfo.textDOB.getText().toString(), "dd.MM.yyyy", "ru");

        String region = personalInfo.region==null?null:personalInfo.region.getId();

        if (region == null) {
            showAlertDialogButtonClicked();
            return;
        }

        String type = "player";
        Map<String, RequestBody> map = new HashMap<>();
        RequestBody requestType = RequestBody.create(MediaType.parse("text/plain"), type);
        RequestBody requestName = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody requestSurname = RequestBody.create(MediaType.parse("text/plain"), surName);
        RequestBody requestPatronymic = RequestBody.create(MediaType.parse("text/plain"), patronymic);
        RequestBody requestLogin = RequestBody.create(MediaType.parse("text/plain"), login);
        RequestBody requestPass = RequestBody.create(MediaType.parse("text/plain"), password);
        RequestBody requestDOB = RequestBody.create(MediaType.parse("text/plain"),DOB);
        RequestBody requestRegion = RequestBody.create(MediaType.parse("text/plain"),region);

        map.put("type", requestType);
        map.put("name", requestName);
        map.put("surname", requestSurname);
        map.put("lastname", requestPatronymic);
        map.put("login", requestLogin);
        map.put("password", requestPass);
        map.put("region", requestRegion);
        map.put("birthdate",requestDOB);

        Bitmap photo = personalInfo.myBitmap;
        if (photo == null){
            photo = BitmapFactory.decodeResource(getResources(), R.drawable.ic_logo2);
            log.info("INFO: photo is null");
        }

        File file = new File(getCacheDir(), "photo");

        try {
            if (file.createNewFile())
                throw new IOException();

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 80 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();
            //write the bytes in file
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);

        Controller.getApi().signUp(map, body).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        User user = response.body();
                        UserPage.auth = true;
                        Intent intent = new Intent();
                        intent.putExtra("PERSONREGINFO", user);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
                else {
                    try {
                        assert response.errorBody() != null;
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        String str = "Ошибка! ";
                        str += jsonObject.getString("message");

                        if (str.contains("Не удалось создать пользователя")) {
                            str = "Такой логин уже существует...";
                            Toast.makeText(RegistrationUser.this, str, Toast.LENGTH_LONG).show();
                        }
                        else if (str.contains("Минимальная длина пароля - 6 символов")) {
                            str = "Минимальная длина пароля - 6 символов";
                            Toast.makeText(RegistrationUser.this, str, Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(RegistrationUser.this, str, Toast.LENGTH_LONG).show();
                            finish();
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                log.error("ERROR: ", t);
            }
        });

    }
}
