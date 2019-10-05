package baikal.web.footballapp.user.activity;

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
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import baikal.web.footballapp.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.CAMERA;

public class PersonalInfo extends Fragment {
    public static TextView textDOB;
    private static ImageButton buttonPhoto;
    public static Bitmap myBitmap;
    private static Uri picUri;
    private ArrayList permissionsToRequest;
    private final ArrayList permissionsRejected = new ArrayList();
    private final ArrayList permissions = new ArrayList();
    private final static int ALL_PERMISSIONS_RESULT = 107;

    public static EditText textLogin;
    public static EditText textPassword;
    public static EditText textName;
    public static EditText textSurname;
    public static EditText textPatronymic;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view;
        view = inflater.inflate(R.layout.personal_info, container, false);
        buttonPhoto = view.findViewById(R.id.registrationInfoPhoto);
        textName = view.findViewById(R.id.registrationInfoName);
        textSurname = view.findViewById(R.id.registrationInfoSurname);
        textPatronymic = view.findViewById(R.id.registrationInfoPatronymic);
        textLogin = view.findViewById(R.id.registrationInfoLogin);
        textPassword = view.findViewById(R.id.registrationInfoPassword);
        textDOB = view.findViewById(R.id.registrationInfoDOB);
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
        textPassword.getBackground().setColorFilter(getResources().getColor(R.color.colorLightGray), PorterDuff.Mode.SRC_IN);
        textPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                textPassword.getBackground().clearColorFilter();
            } else {
                textPassword.getBackground().setColorFilter(getResources().getColor(R.color.colorLightGray), PorterDuff.Mode.SRC_IN);
            }
        });
        textDOB.setOnClickListener(v -> {
            textDOB.setTextColor(getResources().getColor(R.color.colorBottomNavigationUnChecked));
            DatePicker datePicker1 = new DatePicker();
            datePicker1.show(getActivity().getSupportFragmentManager(), "DatePickerDialogFragment");
        });

        buttonPhoto.setOnClickListener(v -> startActivityForResult(getPickImageChooserIntent(), 200));
        permissions.add(CAMERA);
        permissionsToRequest = findUnAskedPermissions(permissions);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions((String[]) permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }

        return view;
    }


    private Intent getPickImageChooserIntent() {

        // Determine Uri of camera image to save.
        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getActivity().getPackageManager();

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
        File getImage = getActivity().getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
        }
        return outputFileUri;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Bitmap bitmap;
        if (resultCode == Activity.RESULT_OK) {
//            picUri = getPickImageResultUri(data);
            if (getPickImageResultUri(data) != null) {
                picUri = getPickImageResultUri(data);
                try {
                    myBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), picUri);

//                    MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), myBitmap ,"nameofimage" , "description");
//                    myBitmap = rotateImageIfRequired(myBitmap, picUri);
//                    myBitmap = getResizedBitmap(myBitmap, 500);

                    Bitmap newBitmap = getResizedBitmap(myBitmap, 500);
//                    CircleImageView croppedImageView = (CircleImageView) findViewById(R.id.img_profile);
//                    croppedImageView.setImageBitmap(myBitmap);
//                    buttonPhoto.setImageBitmap(myBitmap);
                    Glide.with(this)
//                            .load(myBitmap)
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
                if (data != null && data.getExtras() != null) {
//                    bitmap = (Bitmap) data.getExtras().get("data");

                    bitmap = (Bitmap) data.getExtras().get("data");
                    myBitmap = bitmap;
//                    MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), myBitmap ,"nameofimage" , "description");
//                    String root = Environment.getExternalStorageDirectory().toString();
//                    File myDir = new File(root);
//                    myDir.mkdirs();
//                    String fname = "Image-" + "profile" + ".jpg";
//                    File file = new File(myDir, fname);
//                    if (file.exists()) file.delete();
//                    try {
//                        FileOutputStream out = new FileOutputStream(file);
//                        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
//                        out.flush();
//                        out.close();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                    Bitmap newBitmap = getResizedBitmap(myBitmap, 500);
                    Glide.with(this)
                            .load(newBitmap)
//                            .load(myBitmap)
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

        if (android.os.Build.VERSION.SDK_INT >= 23) {
            isCamera = data == null || data.getClipData() == null;
        } else {
            if (data != null) {
                String action = data.getAction();
                isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
            }
        }

        return isCamera ? getCaptureImageOutputUri() : data.getData();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("pic_uri", picUri);

    }


//    @Override
//    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
//        super.onViewStateRestored(savedInstanceState);
//        picUri = savedInstanceState.getParcelable("pic_uri");
//    }

    //    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        picUri = savedInstanceState.getParcelable("pic_uri");
//    }

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
                return (getActivity().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
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
        new AlertDialog.Builder(getActivity())
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
