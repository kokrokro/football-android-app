package baikal.web.footballapp.user.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.User;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationUser extends AppCompatActivity {
    private final Logger log = LoggerFactory.getLogger(RegistrationUser.class);
    Fragment authoUser = new AuthoUser();
    private boolean check = false;
    private static ImageButton imageSave;
    private final PersonalInfo personalInfo = new PersonalInfo();
    private final FragmentManager fragmentManager = this.getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ImageButton imageClose;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_user);
        imageClose = findViewById(R.id.registrationClose);
        imageSave = findViewById(R.id.registrationSave);
        imageClose.setOnClickListener(v -> finish());
        imageSave.setOnClickListener(v -> {
//                getSupportFragmentManager().beginTransaction().replace(R.id.pageContainer, authoUser).addToBackStack(null).commit();



//                FragmentManager fragmentManager = getSupportFragmentManager();
//
//                fragmentManager.beginTransaction().replace(R.id.pageContainer, authoUser).addToBackStack(null).commit();


//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.pageContainer, authoUser)
//                        .show(authoUser)
//                        .commit();
            SignUp();

        });
//        fragmentManager.beginTransaction().add(R.id.registrationViewPager, phoneVerification, "phoneVerification").commit();
//        fragmentManager.beginTransaction().add(R.id.registrationViewPager, personalInfo, "personalInfo").hide(personalInfo).commit();
        fragmentManager.beginTransaction().add(R.id.registrationViewPager, personalInfo, "personalInfo").show(personalInfo).commit();
    }


    private void SignUp() {
        String name = PersonalInfo.textName.getText().toString();
        String surName = PersonalInfo.textSurname.getText().toString();
        String patronymic = PersonalInfo.textPatronymic.getText().toString();
        String login = PersonalInfo.textLogin.getText().toString();
        String password = PersonalInfo.textPassword.getText().toString();
        String DOB = PersonalInfo.textDOB.getText().toString();
//        Bitmap photo = ((BitmapDrawable)drawable).getBitmap();
        Bitmap photo = PersonalInfo.myBitmap;
        String type = "player";
        Map<String, RequestBody> map = new HashMap<>();
        DateFormat format = new SimpleDateFormat("dd MMMM yyyy", new Locale("ru"));
        try {
            Date date = format.parse(DOB);

            RequestBody requestDOB = RequestBody.create(MediaType.parse("text/plain"), date.toString());
            map.put("birthdate", requestDOB);
//            DateTimeFormatter dtf = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
//            DOB.format(dtf);
        } catch (ParseException e) { e.printStackTrace(); }
        RequestBody requestType = RequestBody.create(MediaType.parse("text/plain"), type);
        RequestBody requestName = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody requestSurname = RequestBody.create(MediaType.parse("text/plain"), surName);
        RequestBody requestPatronymic = RequestBody.create(MediaType.parse("text/plain"), patronymic);
        RequestBody requestLogin = RequestBody.create(MediaType.parse("text/plain"), login);
        RequestBody requestPass = RequestBody.create(MediaType.parse("text/plain"), password);
        if (photo == null){
            photo = BitmapFactory.decodeResource(getResources(), R.drawable.ic_logo2);
            log.info("INFO: photo is null");
        }

        try {
            //create a file to write bitmap data
            File file = new File(getCacheDir(), "photo");
            file.createNewFile();
            //Convert bitmap to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 80 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();
            //write the bytes in file
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            //        File file = new File(String.valueOf(PersonalInfo.picUri));
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);

            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("photo", file.getName(), requestFile);
//            Call<LoginResult> call = Controller.getApi().postSignUp(new LoginData(login, password, type));
            map.put("type", requestType);
            map.put("name", requestName);
            map.put("surname", requestSurname);
            map.put("lastname", requestPatronymic);
            map.put("login", requestLogin);
            map.put("password", requestPass);
//            map.put("photo", requestFile);
            Call<User> call2 = Controller.getApi().signUp(map, body);
            call2.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    log.info("INFO: check response");
                    if (response.isSuccessful()) {
                        log.info("INFO: response isSuccessful");
                        if (response.body() == null) {
                            log.error("ERROR: body is null");
                        } else {
                            //response.body() have your LoginResult fields and methods  (baikal you have to access error then try like this response.body().getError() )
//                            String msg = response.body().getMessage();
                            User user = response.body();
                            Person person = response.body().getUser();
//                            Intent resultIntent = new Intent();
//                            Bundle bundle = new Bundle();
//                            bundle.putSerializable("PERSONREGINFO", person);
//                            resultIntent.putExtra("person", bundle);
//                            setResult(Activity.RESULT_OK, resultIntent);
//                            FragmentManager fragmentManager = getSupportFragmentManager();
//                            fragmentManager.beginTransaction().add(R.id.pageContainer, authoUser).hide(PersonalActivity.active).show(authoUser).commit();
//                            PersonalActivity.active = authoUser;
                            UserPage.auth = true;
                            Intent intent = new Intent();
//                            Bundle bundle = new Bundle();
//                            bundle.putSerializable("PERSONREGINFO", person);
//                            intent.putExtra("PERSONREGINFO", person);
                            intent.putExtra("PERSONREGINFO", user);
                            setResult(RESULT_OK, intent);
                            check = true;
                            //all is ok
                            finish();
                            //post
//                        }
                        }
                    }
                    else {
                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            String str = "Ошибка! ";
                            str += jsonObject.getString("message");
                            Toast.makeText(RegistrationUser.this, str, Toast.LENGTH_LONG).show();
                            finish();
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    log.error("ERROR: ", t);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
