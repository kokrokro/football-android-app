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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import baikal.web.footballapp.Controller;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.SetImage;
import baikal.web.footballapp.model.Club;
import baikal.web.footballapp.model.DataClub;

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

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;

public class UserEditClub extends AppCompatActivity {
    private final Logger log = LoggerFactory.getLogger(UserEditClub.class);
    private ImageButton buttonLogo;
    private Bitmap myBitmap;
    Bitmap photo;
    private Uri picUri;
    private Club club1;
    private ArrayList permissionsToRequest;
    private final ArrayList permissionsRejected = new ArrayList();
    private final ArrayList permissions = new ArrayList();

    private final static int ALL_PERMISSIONS_RESULT = 107;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        RecyclerView recyclerView;
        ImageButton buttonSave;
        ImageButton buttonClose;
        Toolbar toolbar;
        final EditText editTitle;
        final EditText editDesc;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_edit_club);
        try {
            Intent intent = getIntent();
            club1 = (Club) intent.getExtras().getSerializable("AUTHOUSERCLUBINFO");
            toolbar = findViewById(R.id.toolbarUserCommandInfo);
            setSupportActionBar(toolbar);
            buttonClose = findViewById(R.id.userEditClubClose);
            buttonSave = findViewById(R.id.userEditClubSave);
            buttonLogo = findViewById(R.id.userEditClubLogo);
            editTitle = findViewById(R.id.userEditClubTitle);
            editDesc = findViewById(R.id.userEditClubDesc);
            editTitle.getBackground().setColorFilter(getResources().getColor(R.color.colorLightGray), PorterDuff.Mode.SRC_IN);
            editTitle.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    editTitle.getBackground().clearColorFilter();
                } else {
                    editTitle.getBackground().setColorFilter(getResources().getColor(R.color.colorLightGray), PorterDuff.Mode.SRC_IN);
                }
            });
            editTitle.setText(club1.getName());
//            editDesc.getBackground().setColorFilter(getResources().getColor(R.color.colorLightGray), PorterDuff.Mode.SRC_IN);
            editDesc.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    editDesc.getBackground().clearColorFilter();
                }
            });
            editDesc.setText(club1.getInfo());
            editDesc.setOnTouchListener((v, event) -> {
                if (v.getId() == R.id.userEditClubDesc) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                    }
                }
                return false;
            });


            SetImage.setImage(this, buttonLogo, club1.getLogo());
            buttonClose.setOnClickListener(v -> finish());
            buttonSave.setOnClickListener(v -> {
                String title = editTitle.getText().toString();
                String desc = editDesc.getText().toString();
                String id = SaveSharedPreference.getObject().getUser().getId();
                Map<String, RequestBody> map = new HashMap<>();
                Bitmap photo = myBitmap;
                RequestBody request = RequestBody.create(MediaType.parse("text/plain"), title);
                map.put("name", request);
                request = RequestBody.create(MediaType.parse("text/plain"), desc);
                map.put("info", request);
//                    request = RequestBody.create(MediaType.parse("text/plain"), id);
                request = RequestBody.create(MediaType.parse("text/plain"), club1.getId());
                map.put("_id", request);
                if (photo != null) {
                    try {
                        File file = new File(getCacheDir(), "photo");
                        file.createNewFile();
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        photo.compress(Bitmap.CompressFormat.JPEG, 80 /*ignored for PNG*/, bos);
                        byte[] bitmapdata = bos.toByteArray();
                        FileOutputStream fos = new FileOutputStream(file);
                        fos.write(bitmapdata);
                        fos.flush();
                        fos.close();
                        RequestBody requestFile =
                                RequestBody.create(MediaType.parse("multipart/form-data"), file);
                        MultipartBody.Part body =
                                MultipartBody.Part.createFormData("logo", file.getName(), requestFile);
                        editClub(SaveSharedPreference.getObject().getToken(), map, body);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    editClub(SaveSharedPreference.getObject().getToken(), map, null);
                }
            });
            buttonLogo.setOnClickListener(v -> startActivityForResult(getPickImageChooserIntent(), 200));
            permissions.add(CAMERA);
            permissionsToRequest = findUnAskedPermissions(permissions);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (permissionsToRequest.size() > 0)
                    requestPermissions((String[]) permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
            }
        } catch (Exception e) {
            log.error("ERROR: ", e);
        }
    }


    private void editClub(String token, Map<String, RequestBody> map, final MultipartBody.Part body) {
        Call<DataClub> call = Controller.getApi().editClub(token, map, body);
        call.enqueue(new Callback<DataClub>() {
            @Override
            public void onResponse(Call<DataClub> call, Response<DataClub> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try{
                            Club club =  response.body().getResultClub();
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("CREATECLUBRESULT", club);
                            intent.putExtras(bundle);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                        catch (Exception t) {
                            log.error("ERROR: ", t);
                        }
                    }
                }
                else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        String str = "Ошибка! ";
                        str += jsonObject.getString("message");
                        Toast.makeText(UserEditClub.this, str, Toast.LENGTH_LONG).show();
                        finish();
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<DataClub> call, Throwable t) {
                log.error("ERROR: ", t);
            }
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
                    myBitmap = getResizedBitmap(myBitmap, 500);

//                    CircleImageView croppedImageView = (CircleImageView) findViewById(R.id.img_profile);
//                    croppedImageView.setImageBitmap(myBitmap);
//                    buttonPhoto.setImageBitmap(myBitmap);
                    Glide.with(this)
                            .load(myBitmap)
                            .apply(RequestOptions
                                    .circleCropTransform()
                                    .format(DecodeFormat.PREFER_ARGB_8888)
                                    .priority(Priority.HIGH))
//                            .load(picUri)
                            .into(buttonLogo);
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

                    bitmap = (Bitmap) data.getExtras().get("data");
                    myBitmap = bitmap;
                    myBitmap = getResizedBitmap(myBitmap, 500);
                    Glide.with(this)
                            .load(myBitmap)
                            .apply(RequestOptions
                                    .circleCropTransform()
                                    .format(DecodeFormat.PREFER_ARGB_8888)
                                    .priority(Priority.HIGH))
//                        .load(picUri)
                            .into(buttonLogo);
                }
//                else {
//                    try {
//                        myBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), picUri);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
////                    myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_con);
//                }

//                if(data.getData()!=null){
//                    bitmap = (Bitmap)data.getExtras().get("data");
//                    myBitmap = bitmap;
//                }else{
//                    try {
//                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
//                        myBitmap = bitmap;
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }


//                CircleImageView croppedImageView = (CircleImageView) findViewById(R.id.img_profile);
//                if (croppedImageView != null) {
//                    croppedImageView.setImageBitmap(myBitmap);
//                }


//                buttonPhoto.setImageBitmap(myBitmap);

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

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener
            okListener) {
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {

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
