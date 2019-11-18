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
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import baikal.web.footballapp.Controller;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.model.EditProfile;
import baikal.web.footballapp.model.Person;

import baikal.web.footballapp.R;
import baikal.web.footballapp.model.User;
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
import java.util.Calendar;
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
    private String token;
    private User user;
    //    public static Button buttonDOB;
    private String uriPic;
    private URL url;
    public static TextView buttonDOB;
    private ImageButton buttonPhoto;
    private ImageButton buttonClose;
    private ImageButton buttonSave;
    private EditText textName;
    private EditText textSurname;
    private EditText textPatronymic;
    private EditText textLogin;
    TextView textDOB;
    private Bitmap myBitmap;
    private Uri picUri;
    private Bitmap photo;
    private Person person;
    private Person person1;
    private ArrayList permissionsToRequest;
    private final ArrayList permissionsRejected = new ArrayList();
    private final ArrayList permissions = new ArrayList();

    private final static int ALL_PERMISSIONS_RESULT = 107;

    private boolean press;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info);
        final Logger log = LoggerFactory.getLogger(UserInfo.class);
        press = false;
        textName = findViewById(R.id.userInfoName);
        textName.clearFocus();
        textSurname = findViewById(R.id.userInfoSurname);
        textPatronymic = findViewById(R.id.userInfoPatronymic);
        textLogin = findViewById(R.id.userInfoLogin);
        buttonPhoto = findViewById(R.id.userInfoPhoto);
        buttonDOB = findViewById(R.id.userInfoDOB);
        buttonClose = findViewById(R.id.userProfileClose);
        buttonSave = findViewById(R.id.userProfileSave);
        textName.getBackground().setColorFilter(getResources().getColor(R.color.colorLightGray), PorterDuff.Mode.SRC_IN);
        textName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                textName.getBackground().clearColorFilter();
            } else {
                textName.getBackground().setColorFilter(getResources().getColor(R.color.colorLightGray), PorterDuff.Mode.SRC_IN);
            }
        });
        textSurname.getBackground().setColorFilter(getResources().getColor(R.color.colorLightGray), PorterDuff.Mode.SRC_IN);
        textSurname.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                textSurname.getBackground().clearColorFilter();
            } else {
                textSurname.getBackground().setColorFilter(getResources().getColor(R.color.colorLightGray), PorterDuff.Mode.SRC_IN);
            }
        });

        textPatronymic.getBackground().setColorFilter(getResources().getColor(R.color.colorLightGray), PorterDuff.Mode.SRC_IN);
        textPatronymic.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                textPatronymic.getBackground().clearColorFilter();
            } else {
                textPatronymic.getBackground().setColorFilter(getResources().getColor(R.color.colorLightGray), PorterDuff.Mode.SRC_IN);
            }
        });
        textLogin.getBackground().setColorFilter(getResources().getColor(R.color.colorLightGray), PorterDuff.Mode.SRC_IN);
        textLogin.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                textLogin.getBackground().clearColorFilter();
            } else {
                textLogin.getBackground().setColorFilter(getResources().getColor(R.color.colorLightGray), PorterDuff.Mode.SRC_IN);
            }
        });
        try {
            Intent intent = getIntent();
//            web = (User) intent.getExtras().getSerializable("ONLYPERSONINFO");
            user = SaveSharedPreference.getObject();
            token = user.getToken();
            person = user.getUser();
            textName.setText(person.getName());
            textSurname.setText(person.getSurname());
//            textSurname.setText(UserPage.person.getSurname());
            textPatronymic.setText(person.getLastname());
            textLogin.setText(person.getLogin());
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.optionalCircleCrop();
            requestOptions.format(DecodeFormat.PREFER_ARGB_8888);
            RequestOptions.errorOf(R.drawable.ic_logo2);
            requestOptions.override(500, 500);
            requestOptions.priority(Priority.HIGH);
            if (person.getPhoto().equals(null)){

                Glide.with(this)
                        .asBitmap()
                        .load(R.drawable.ic_logo2)
                        .apply(requestOptions)
                        .into(buttonPhoto);
            }
            uriPic = BASE_URL;
            uriPic += "/" + person.getPhoto();
            try {
                url = new URL(uriPic);
                Glide.with(this)
                        .asBitmap()
                        .load(url)
                        .apply(requestOptions)
                        .into(buttonPhoto);
            } catch (Exception e) {
                log.error("ERROR: don't load img");
            }


            String DOB = person.getBirthdate();


            try {
                HashMap<Integer, String> months = new HashMap<>();
                months.put(0, "янв.");
                months.put(1, "февр.");
                months.put(2, "марта");
                months.put(3, "апр.");
                months.put(4, "мая");
                months.put(5, "июня");
                months.put(6, "июля");
                months.put(7, "авг.");
                months.put(8, "сент.");
                months.put(9, "окт.");
                months.put(10, "нояб.");
                months.put(11, "дек.");
                DateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy", Locale.US);
                Date date = dateFormat.parse(DOB);

                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                String dateDOB = cal.get(Calendar.DAY_OF_MONTH) + " " + months.get(cal.get(Calendar.MONTH)) + " " + cal.get(Calendar.YEAR);
                buttonDOB.setText(dateDOB);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        buttonClose.setOnClickListener(v -> finish());
        buttonSave.setOnClickListener(v -> {
            //post data
            String name;
            String surname;
            String patronymic;
            String login;
            String DOB;

            try {
                name = textName.getText().toString();
                surname = textSurname.getText().toString();
                patronymic = textPatronymic.getText().toString();
                login = textLogin.getText().toString();
                DOB = (String) buttonDOB.getText();
                photo = myBitmap;
                Map<String, RequestBody> map = new HashMap<>();
                RequestBody request = RequestBody.create(MediaType.parse("text/plain"), name);
                map.put("name", request);
                request = RequestBody.create(MediaType.parse("text/plain"), surname);
                map.put("surname", request);
                request = RequestBody.create(MediaType.parse("text/plain"), patronymic);
                map.put("lastname", request);
                request = RequestBody.create(MediaType.parse("text/plain"), login);
                map.put("login", request);
                request = RequestBody.create(MediaType.parse("text/plain"), person.getId());
                map.put("_id", request);

                DateFormat format = new SimpleDateFormat("dd MMMM yyyy", new Locale("ru"));
                try {
                    Date date = format.parse(DOB);
                    RequestBody requestDOB = RequestBody.create(MediaType.parse("text/plain"), date.toString());
                    map.put("birthdate", requestDOB);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                try {

                    if (photo != null) {
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
                        Call<EditProfile> call = Controller.getApi().editProfile(token, map, body);
                        call.enqueue(new Callback<EditProfile>() {
                            @Override
                            public void onResponse(Call<EditProfile> call, Response<EditProfile> response) {
                                log.info("INFO: check response");
                                if (response.isSuccessful()) {
                                    log.info("INFO: response isSuccessful");
                                    if (response.body() == null) {
                                        log.error("ERROR: body is null");
                                    } else {
//                                            Person person1;
                                        person1 = response.body().getPerson();
//                                            person = person1;
                                        user.setUser(person1);
//                                            AuthoUser.web.setUser(person1);
                                        SaveSharedPreference.getObject().setUser(person1);
                                        SaveSharedPreference.saveObject(user);
                                        RequestOptions requestOptions = new RequestOptions();
                                        requestOptions.optionalCircleCrop();
                                        requestOptions.format(DecodeFormat.PREFER_ARGB_8888);
                                        RequestOptions.errorOf(R.drawable.ic_logo2);
                                        requestOptions.override(500, 500);
                                        requestOptions.priority(Priority.HIGH);
//                                                Glide.with(UserInfo.this)
                                        Glide.with(AuthoUser.buttonOpenProfile.getContext())
                                                .asBitmap()
                                                .load(myBitmap)
                                                .apply(requestOptions)
                                                .into(AuthoUser.buttonOpenProfile);
                                        AuthoUser.textName.setText(person1.getName());
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
                                        Toast.makeText(UserInfo.this, str, Toast.LENGTH_LONG).show();
                                        finish();
//                        }
                                    }
                                } else {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                        String str = "Ошибка! ";
                                        str += jsonObject.getString("message");
                                        Toast.makeText(UserInfo.this, str, Toast.LENGTH_LONG).show();
                                    } catch (IOException | JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<EditProfile> call, Throwable t) {
                                log.error("ERROR: ", t);
                                Toast.makeText(UserInfo.this, "Ошибка сервера. Изменения не сохранены.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Call<EditProfile> call = Controller.getApi().editProfile(token, map, null);
                        call.enqueue(new Callback<EditProfile>() {
                            @Override
                            public void onResponse(Call<EditProfile> call, Response<EditProfile> response) {
                                log.info("INFO: check response");
                                if (response.isSuccessful()) {
                                    log.info("INFO: response isSuccessful");
                                    if (response.body() == null) {
                                        log.error("ERROR: body is null");
                                    } else {
                                        person1 = response.body().getPerson();
                                        user.setUser(person1);
//                                            AuthoUser.web.setUser(person1);
                                        SaveSharedPreference.getObject().setUser(person1);
                                        log.error(SaveSharedPreference.getObject().getUser().getName());
                                        log.error("777777777777777777777777777777777777777777777777777777777777777");
                                        SaveSharedPreference.editObject(user);
                                        log.error("777777777777777777777777777777777777777777777777777777777777777");

//                                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                                            ft.detach(PersonalActivity.authoUser).attach(PersonalActivity.authoUser).commit();
//                                            person = person1;
                                        AuthoUser.textName.setText(person1.getName());
                                        Person man1 = new Person();
                                        if (person.getType().equals("player")) {
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
                                        Toast.makeText(UserInfo.this, str, Toast.LENGTH_LONG).show();
                                        //all is ok
                                        finish();
                                    }
                                } else {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                        String str = "Ошибка! ";
                                        str += jsonObject.getString("message");
                                        Toast.makeText(UserInfo.this, str, Toast.LENGTH_LONG).show();
                                    } catch (IOException | JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<EditProfile> call, Throwable t) {
                                log.error("ERROR: ", t);
                                Toast.makeText(UserInfo.this, "Ошибка сервера. Изменения не сохранены.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } catch (IOException e) {
                    log.error("ERROR");
                }
            } catch (Exception e) {
            }

            finish();
        });
        buttonPhoto.setOnClickListener(v -> {
            press = true;
            startActivityForResult(getPickImageChooserIntent(), 200);
        });
        permissions.add(CAMERA);
        permissionsToRequest = findUnAskedPermissions(permissions);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions((String[]) permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }


        buttonDOB.setOnClickListener(v -> {
            buttonDOB.setTextColor(getResources().getColor(R.color.colorBottomNavigationUnChecked));
            DatePicker datePicker1 = new DatePicker();
            datePicker1.show(getSupportFragmentManager(), "DatePickerDialogFragment");
        });
    }


    private Intent getPickImageChooserIntent() {

        // Determine Uri of camera image to save.
        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

        // collect all camera intents
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                log.info("INFO: outputFileUri != null");
            } else {
                log.error("ERROR: outputFileUri == null");
            }
            allIntents.add(intent);
        }

        // collect all gallery intents
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        // the main intent is the last in the list (fucking android) so pickup the useless one
        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        // Create a chooser from the main intent
        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }

    /**
     * Get URI to image received from capture by camera.
     */
    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
        }
        return outputFileUri;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Bitmap bitmap;
        if (resultCode == Activity.RESULT_OK) {
//            picUri = getPickImageResultUri(data);
            if (getPickImageResultUri(data) != null) {
                picUri = getPickImageResultUri(data);
                try {
                    myBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), picUri);
//                    myBitmap = rotateImageIfRequired(myBitmap, picUri);
                    Bitmap newBitmap = getResizedBitmap(myBitmap, 500);
                    Glide.with(this)
                            .load(newBitmap)
                            .apply(RequestOptions
                                    .circleCropTransform()
                                    .format(DecodeFormat.PREFER_ARGB_8888)
                                    .priority(Priority.HIGH))
//                            .load(picUri)
                            .into(buttonPhoto);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                log.info("INFO: CAMERA");
                if (picUri == null) {
                    log.info("INFO: picUri==null");
                }
                if (data != null && data.getExtras() != null) {
//                    bitmap = (Bitmap) data.getExtras().get("data");
                    log.info("INFO: data != null");

                    bitmap = (Bitmap) data.getExtras().get("data");
                    myBitmap = bitmap;
                    Bitmap newBitmap = getResizedBitmap(myBitmap, 500);
                    Glide.with(this)
                            .load(newBitmap)
                            .apply(RequestOptions
                                    .circleCropTransform()
                                    .format(DecodeFormat.PREFER_ARGB_8888)
                                    .priority(Priority.HIGH))
//                        .load(picUri)
                            .into(buttonPhoto);
                }


            }

        }

    }

    private Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private Uri getPickImageResultUri(Intent data) {
        boolean isCamera = true;
//        if (data != null) {
//            String action = data.getAction();
//            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
//        }
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            isCamera = data == null || data.getClipData() == null;
        } else {
            if (data != null) {
                String action = data.getAction();
                isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
            }
        }
//        if (data != null && data.getData() != null) {
//            String action = data.getAction();
//            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
//        }
        return isCamera ? getCaptureImageOutputUri() : data.getData();


//        return isCamera ? getCaptureImageOutputUri() : data.getData();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        log.info("INFO: onSaveInstanceState");
        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("pic_uri", picUri);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        log.info("INFO: onRestoreInstanceState");
        // get the file url
        picUri = savedInstanceState.getParcelable("pic_uri");

    }

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        //for android 6
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
//            if (checkSelfPermission(Manifest.permission.CAMERA)
//                    != PackageManager.PERMISSION_GRANTED) {
//
//                requestPermissions(new String[]{Manifest.permission.CAMERA , Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                        200);
//            }
//        }

        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == ALL_PERMISSIONS_RESULT) {
            for (Object perms : permissionsToRequest) {
                if (hasPermission(perms.toString())) {

                } else {

                    permissionsRejected.add(perms);
                }
            }

            if (permissionsRejected.size() > 0) {


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (shouldShowRequestPermissionRationale((String) permissionsRejected.get(0))) {
                        showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                                            //Log.d("API123", "permisionrejected " + permissionsRejected.size());

                                            requestPermissions((String[]) permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                        }
                                    }
                                });
                        return;
                    }
                }

            }
        }

    }

}
