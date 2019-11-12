package baikal.web.footballapp.registration;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.Region;
import baikal.web.footballapp.model.User;
import baikal.web.footballapp.user.activity.DatePicker;
import baikal.web.footballapp.user.activity.PersonalInfo;
import baikal.web.footballapp.user.activity.UserPage;
import baikal.web.footballapp.user.adapter.SpinnerRegionAdapter;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationUserActivity extends AppCompatActivity {

    private final Logger log = LoggerFactory.getLogger(RegistrationUserActivity.class);
    private static ImageButton imageSave;
    private static ImageButton imageClose;
    private static ProgressBar progressBarRegistration;

    private static EditText textName;
    private static EditText textSurname;
    private static EditText textPatronymic;
    private static Spinner spinnerRegion;
    private static EditText textLogin;
    private static EditText textPassword;
    public static TextView textDOB;

    private SpinnerRegionAdapter adapterRegion;
    private static List<Region> regions = new ArrayList<Region>();
    private static ArrayList <String> regionsId = new ArrayList<String>();
    private ArrayList<String> regionsName = new ArrayList<String>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_page);
        imageClose = findViewById(R.id.registrationClose);
        imageSave = findViewById(R.id.registrationSave);
        progressBarRegistration = findViewById(R.id.progressBarRegistration);


        textName = findViewById(R.id.registrationInfoName);
        textSurname = findViewById(R.id.registrationInfoSurname);
        textPatronymic = findViewById(R.id.registrationInfoPatronymic);
        spinnerRegion = findViewById(R.id.regionEditSpinner);
        textLogin = findViewById(R.id.registrationInfoLogin);
        textPassword = findViewById(R.id.registrationInfoPassword);
        textDOB = findViewById(R.id.registrationInfoDOB);

        imageClose.setOnClickListener(v -> finish());
        imageSave.setOnClickListener(v -> SignUp());

        textDOB.setOnClickListener(v -> {
            textDOB.setTextColor(getResources().getColor(R.color.colorBottomNavigationUnChecked));
            DatePicker datePicker1 = new DatePicker();
            datePicker1.show(RegistrationUserActivity.this.getSupportFragmentManager(), "DatePickerDialogFragment");
        });
        textDOB.setOnClickListener(v -> {
            textDOB.setTextColor(getResources().getColor(R.color.colorBottomNavigationUnChecked));
            DatePickerRegistration datePickerRegistration = new DatePickerRegistration();
            datePickerRegistration.show(this.getSupportFragmentManager(), "DatePickerDialogFragment");
        });

        spinnerRegion = findViewById(R.id.regionEditSpinner);
        adapterRegion = new SpinnerRegionAdapter(this, R.layout.spinner_item, regions);
        adapterRegion.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerRegion.setAdapter(adapterRegion);


        Controller.getApi().getRegions().enqueue(new Callback<List<Region>>() {
            @Override
            public void onResponse(Call<List<Region>> call, Response<List<Region>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body() != null) {
                            regions.clear();
                            regions.addAll(response.body());
                            for(Region reg: regions){
                                regionsName.add(reg.getName());
                                regionsId.add(reg.getId());
                            }
                        }
                        if (regions.size() > 0)
                            spinnerRegion.setSelection(0);

                        adapterRegion.notifyDataSetChanged();
                        spinnerRegion.setSelection(0);
                    }
                } else {
                    Toast.makeText(RegistrationUserActivity.this, "Ошибка при загрузке регионов", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Region>> call, Throwable t) {
                Log.e("ERROR: ", t.getMessage());
                Toast.makeText(RegistrationUserActivity.this, "Ошибка при загрузке регионов", Toast.LENGTH_LONG).show();
            }
        });



    }

    private void SignUp() {
        imageSave.setEnabled(false);
        progressBarRegistration.setVisibility(View.VISIBLE);

        String name = textName.getText().toString();
        String surName = textSurname.getText().toString();
        String patronymic = textPatronymic.getText().toString();
        String login = textLogin.getText().toString();
        String password = textPassword.getText().toString();
        String DOB = textDOB.getText().toString();
        int idRegion = spinnerRegion.getSelectedItemPosition();

        if(idRegion == -1) {
            Toast.makeText(RegistrationUserActivity.this, "Регион - обязательное поле", Toast.LENGTH_SHORT).show();
            imageSave.setEnabled(true);
            progressBarRegistration.setVisibility(View.GONE);
            return;
        }
        if(name.length() < 1) {
            Toast.makeText(RegistrationUserActivity.this, "Имя - обязательное поле", Toast.LENGTH_SHORT).show();
            imageSave.setEnabled(true);
            progressBarRegistration.setVisibility(View.GONE);
            return;
        }
        if(surName.length() < 1) {
            Toast.makeText(RegistrationUserActivity.this, "Фамилия - обязательное поле", Toast.LENGTH_SHORT).show();
            imageSave.setEnabled(true);
            progressBarRegistration.setVisibility(View.GONE);
            return;
        }
        if(login.length() < 4) {
            Toast.makeText(RegistrationUserActivity.this, "Логин - обязательное поле(более 3-х символов)", Toast.LENGTH_SHORT).show();
            imageSave.setEnabled(true);
            progressBarRegistration.setVisibility(View.GONE);
            return;
        }
        if(password.length() < 6) {
            Toast.makeText(RegistrationUserActivity.this, "Пароль должен быть длинее 6-ти символов", Toast.LENGTH_SHORT).show();
            imageSave.setEnabled(true);
            progressBarRegistration.setVisibility(View.GONE);
            return;
        }
        if(DOB.length() != 12) {
            Toast.makeText(RegistrationUserActivity.this, "Дата рождения - обязательное поле", Toast.LENGTH_SHORT).show();
            imageSave.setEnabled(true);
            progressBarRegistration.setVisibility(View.GONE);
            System.out.println(DOB);
            return;
        }

        String region = regionsId.get(idRegion);

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

        if(photo == null) {
            photo = BitmapFactory.decodeResource(getResources(), R.drawable.ic_logo2);
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
                            Toast.makeText(RegistrationUserActivity.this, "Вы успешно зарегистрированы", Toast.LENGTH_SHORT).show();

                            finish();
                        }
                    }
                    else {
                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            String str = "Ошибка! ";
                            str += jsonObject.getString("message");
                            Toast.makeText(RegistrationUserActivity.this, str, Toast.LENGTH_LONG).show();
                            finish();
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    progressBarRegistration.setVisibility(View.GONE);
                    imageSave.setEnabled(true);

                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    progressBarRegistration.setVisibility(View.GONE);
                    imageSave.setEnabled(true);
                    log.error("ERROR: ", t);
                }
            });
        } catch (IOException e) {
            progressBarRegistration.setVisibility(View.GONE);
            imageSave.setEnabled(true);
            e.printStackTrace();
        }

    }



}
