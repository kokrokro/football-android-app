package baikal.web.footballapp.user.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.User;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationUser extends AppCompatActivity {

    private final Logger log = LoggerFactory.getLogger(RegistrationUser.class);
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
        imageSave.setOnClickListener(v -> SignUp());
        fragmentManager.beginTransaction().add(R.id.registrationViewPager, personalInfo, "personalInfo").show(personalInfo).commit();

        personalInfo.setParentContext(this);
    }

    public void showAlertDialogButtonClicked() {
        Toast.makeText(getBaseContext(), "Выберите регион", Toast.LENGTH_SHORT);
    }

    private void SignUp() {
        String name = PersonalInfo.textName.getText().toString();
        String surName = PersonalInfo.textSurname.getText().toString();
        String patronymic = PersonalInfo.textPatronymic.getText().toString();
        String login = PersonalInfo.textLogin.getText().toString();
        String password = PersonalInfo.textPassword.getText().toString();
        String DOB = "";

        DateFormat formatParse = new SimpleDateFormat("dd.MM.yyyy", new Locale("ru"));
        DateFormat formatForServer = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSZ");

        try {
            Date d = formatParse.parse(PersonalInfo.textDOB.getText().toString());

            DOB = formatForServer.format(d);
        } catch (Exception e) {
            log.error("RegistrationUser:", e);
            DOB = formatParse.format(new Date());
        }
        String region = "";

        if (PersonalInfo.spinnerRegion.getSelectedItemPosition() == -1)
            showAlertDialogButtonClicked();
        else
            region = PersonalInfo.regionsId.get(PersonalInfo.spinnerRegion.getSelectedItemPosition());

        Bitmap photo = PersonalInfo.myBitmap;
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
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);
            map.put("type", requestType);
            map.put("name", requestName);
            map.put("surname", requestSurname);
            map.put("lastname", requestPatronymic);
            map.put("login", requestLogin);
            map.put("password", requestPass);
            map.put("region", requestRegion);
            map.put("birthdate",requestDOB);
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
                            User user = response.body();
                            Log.d("dfdF_________", response.body().toString());
                            Person person = response.body().getUser();
                            UserPage.auth = true;
                            Intent intent = new Intent();
                            intent.putExtra("PERSONREGINFO", user);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                    else {
                        try {
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
                public void onFailure(Call<User> call, Throwable t) {
                    log.error("ERROR: ", t);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
