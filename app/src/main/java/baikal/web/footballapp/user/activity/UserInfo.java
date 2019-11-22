package baikal.web.footballapp.user.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
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
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.model.EditProfile;
import baikal.web.footballapp.model.Person;

import baikal.web.footballapp.R;
import baikal.web.footballapp.players.activity.PlayersPage;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;
import static baikal.web.footballapp.Controller.BASE_URL;

public class UserInfo extends AppCompatActivity {
    private final Logger log = LoggerFactory.getLogger(UserInfo.class);
    private final PersonalInfo personalInfo = new PersonalInfo(this);
    private final FragmentManager fragmentManager = this.getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info);
        ImageButton buttonClose = findViewById(R.id.userProfileClose);
        ImageButton buttonSave = findViewById(R.id.userProfileSave);

        buttonClose.setOnClickListener(v -> finish());
        buttonSave.setOnClickListener(v -> saveData());

        personalInfo.textPassword.setVisibility(View.INVISIBLE);
        fragmentManager.beginTransaction().add(R.id.registrationViewPager, personalInfo, "personalInfo").show(personalInfo).commit();
    }

    private void saveData ()
    {
        String name = personalInfo.textName.getText().toString();
        String surName = personalInfo.textSurname.getText().toString();
        String patronymic = personalInfo.textPatronymic.getText().toString();
        String login = personalInfo.textLogin.getText().toString();
        String password = personalInfo.textPassword.getText().toString();
        String DOB = (new DateToString()).TimeForServer(personalInfo.textDOB.getText().toString(), "dd.MM.yyyy", "ru");
        Bitmap photo = personalInfo.myBitmap;

        Map<String, RequestBody> map = new HashMap<>();
        RequestBody request = RequestBody.create(MediaType.parse("text/plain"), name);
        request = RequestBody.create(MediaType.parse("text/plain"), surName);
        request = RequestBody.create(MediaType.parse("text/plain"), patronymic);
        request = RequestBody.create(MediaType.parse("text/plain"), login);
        request = RequestBody.create(MediaType.parse("text/plain"), PersonalActivity.id);

        map.put("name", request);
        map.put("surname", request);
        map.put("lastname", request);
        map.put("login", request);
        map.put("_id", request);

        finish();
    }

}
