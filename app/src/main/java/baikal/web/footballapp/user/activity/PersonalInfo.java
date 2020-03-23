package baikal.web.footballapp.user.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import baikal.web.footballapp.DateToString;
import baikal.web.footballapp.Dialogs.DialogRegion;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.SelectImageDialog;
import baikal.web.footballapp.SetImage;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.Region;
import baikal.web.footballapp.model.User;

public class PersonalInfo extends Fragment implements SelectImageDialog.OnImageSelectedListener {
    private static final String TAG = "Personal Info: ";
    private static final int REQUEST_CODE = 6424;           //magic number which doesn't mater

    private Uri picUri;

    private ImageButton buttonPhoto;
    private TextView regionDialogSummoner;

    Region region = null;
    Bitmap myBitmap;
    EditText textSurname;
    EditText textName;
    EditText textPatronymic;
    EditText textLogin;
    EditText textPassword;
    TextView textDOB;

    private Context context;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Person person;

    private boolean isPasswordHidden;

    PersonalInfo(Context context, boolean isPasswordHidden)  {
        this.isPasswordHidden = isPasswordHidden;
        this.context = context;

        if (isPasswordHidden) {
            User user = SaveSharedPreference.getObject();
            person = user.getUser();
            Log.d(TAG, person.getRegion());
        }
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.personal_info, container, false);
        buttonPhoto = view.findViewById(R.id.registrationInfoPhoto);
        textName = view.findViewById(R.id.registrationInfoName);
        textSurname = view.findViewById(R.id.registrationInfoSurname);
        textPatronymic = view.findViewById(R.id.registrationInfoPatronymic);
        textDOB = view.findViewById(R.id.registrationInfoDOB);
        regionDialogSummoner = view.findViewById(R.id.PI_regionEdit);
        textLogin = view.findViewById(R.id.registrationInfoLogin);
        TextView titlePassword = view.findViewById(R.id.registrationInfoPasswordTitle);
        textPassword = view.findViewById(R.id.registrationInfoPassword);

        regionDialogSummoner.setBackgroundColor(Color.rgb(245,245,245));
        regionDialogSummoner.setOnClickListener(v -> {
            FragmentManager fm = getChildFragmentManager();
            DialogRegion dialogRegion =  new DialogRegion(r -> {
                region = r;
                regionDialogSummoner.setText(r==null?"Не выбрано":r.getName());
            });
            dialogRegion.show(fm, "personal_info");
        });

        if (isPasswordHidden) {
            textPassword.setVisibility(View.GONE);
            titlePassword.setVisibility(View.GONE);
            fillPersonData();
        }


        textDOB.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();

            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            Locale locale = getResources().getConfiguration().locale;
            Locale.setDefault(locale);

            DatePickerDialog dialog = new DatePickerDialog(context, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year, month, day);

            dialog.getDatePicker().setMaxDate(c.getTimeInMillis());
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });

        mDateSetListener = (view1, year, month, dayOfMonth) -> {
            DateFormat format = new SimpleDateFormat("dd.MM.yyyy", new Locale("ru"));
            Calendar c = Calendar.getInstance();
            c.set(year, month, dayOfMonth);
            Date d = c.getTime();
            textDOB.setText(format.format(d));
            textDOB.setTextColor(getResources().getColor(R.color.colorBottomNavigationUnChecked));
        };

        buttonPhoto.setOnClickListener(v -> {
            verifyPermissions();
            SelectImageDialog dialog = new SelectImageDialog();
            dialog.show(Objects.requireNonNull(getFragmentManager()), getString(R.string.dialog_select_image));
            dialog.setTargetFragment(PersonalInfo.this, 1); //request code doesn't mater
        });

        return view;
    }

    private void fillPersonData ()
    {
        try {
            textName.setText(person.getName());
            textSurname.setText(person.getSurname());
            textPatronymic.setText(person.getLastname());
            textLogin.setText(person.getLogin());
            textDOB.setText(DateToString.ChangeDate(person.getBirthdate()));

            SetImage.setImage(getContext(), buttonPhoto, person.getPhoto());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap getResizedBitmap(Bitmap image) {
        int maxSize = 500;
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("pic_uri", picUri);
    }

    @Override
    public void getImagePath(Uri ImagePath) {
        picUri = ImagePath;
        Log.d(TAG, "is from galary !!!");
        try {
            myBitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getActivity()).getContentResolver(), picUri);
            Bitmap newBitmap = getResizedBitmap(myBitmap);
            Glide.with(this)
                    .load(newBitmap)
                    .apply(RequestOptions
                            .circleCropTransform()
                            .format(DecodeFormat.PREFER_ARGB_8888)
                            .priority(Priority.HIGH))
                    .into(buttonPhoto);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getImageBitmap(Bitmap bitmap) {
        myBitmap = bitmap;
        Bitmap newBitmap = getResizedBitmap(myBitmap);
        Glide.with(this)
                .load(newBitmap)
                .apply(RequestOptions
                        .circleCropTransform()
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .priority(Priority.HIGH))
                .into(buttonPhoto);
    }




    private void verifyPermissions() {
        Log.d(TAG, " verifyPermissions: asking for permission");
        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        };

        for (String permission : permissions)
            if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()).getApplicationContext(), permission) == PackageManager.PERMISSION_DENIED)
                ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), permissions, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        verifyPermissions();
    }
}
