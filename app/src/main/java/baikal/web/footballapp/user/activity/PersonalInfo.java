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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.DateToString;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.SelectImageDialog;
import baikal.web.footballapp.SetImage;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.Region;
import baikal.web.footballapp.model.User;
import baikal.web.footballapp.user.adapter.SpinnerRegionAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalInfo extends Fragment implements SelectImageDialog.OnImageSelectedListener {
    private static final String TAG = "Personal Info: ";
    private static final int REQUEST_CODE = 6424;           //magic number which doesn't mater

    private Uri picUri;


    private List<Region> regions = new ArrayList<>();
    List<String> regionsId = new ArrayList<>();
    int selectedRegionIndex = 0;

    private ImageButton buttonPhoto;
    Bitmap myBitmap;
    EditText textSurname;
    EditText textName;
    EditText textPatronymic;
    Spinner spinnerRegion;
    EditText textLogin;
    EditText textPassword;
    TextView textDOB;

    private Context context;
    private SpinnerRegionAdapter adapterRegion;
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
        spinnerRegion = view.findViewById(R.id.regionEditSpinner);
        textLogin = view.findViewById(R.id.registrationInfoLogin);
        TextView titlePassword = view.findViewById(R.id.registrationInfoPasswordTitle);
        textPassword = view.findViewById(R.id.registrationInfoPassword);

        Region r = new Region();
        r.setName("Регион");
        r.setId("-1");
        regions.add(r);
        regionsId.add("-1");

        adapterRegion = new SpinnerRegionAdapter (Objects.requireNonNull(this.getContext()), R.layout.spinner_item, regions);
        adapterRegion.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerRegion.setBackgroundColor(Color.rgb(245,245,245));
        spinnerRegion.setAdapter(adapterRegion);

        if (isPasswordHidden) {
            textPassword.setVisibility(View.INVISIBLE);
            titlePassword.setVisibility(View.INVISIBLE);
            fillPersonData();
        }

        Controller.getApi().getRegions().enqueue(new Callback<List<Region>>() {
            @Override
            public void onResponse(@NonNull Call<List<Region>> call, @NonNull Response<List<Region>> response) {
                if (response.isSuccessful())
                    if (response.body() != null) {
                        regions.clear();
                        regions.addAll(response.body());
                        for (Region rr: regions)
                            regionsId.add(rr.getId());
                        adapterRegion.notifyDataSetChanged();

                        if (isPasswordHidden)
                            setRegionData();
                    }
            }

            @Override
            public void onFailure(@NonNull Call<List<Region>> call, @NonNull Throwable t) {
                Log.e("ERROR: ", Objects.requireNonNull(t.getMessage()));
                Toast.makeText(context, "Ошибка при загрузке регионов", Toast.LENGTH_LONG).show();
            }
        });

        spinnerRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRegionIndex = position;
                parent.getItemAtPosition(position);
             }

             @Override
             public void onNothingSelected(AdapterView<?> parent) {
             }
        });


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

    private void setRegionData ()
    {
        for (int i=0; i<regionsId.size(); i++)
            if (regionsId.get(i).equals(person.getRegion())) {
                spinnerRegion.setSelection(i);
                Log.d(TAG, "position = " + spinnerRegion.getSelectedItemPosition());
            }
    }

    private void fillPersonData ()
    {
        try {
            textName.setText(person.getName());
            textSurname.setText(person.getSurname());
            textPatronymic.setText(person.getLastname());
            textLogin.setText(person.getLogin());
            textDOB.setText((new DateToString()).ChangeDate(person.getBirthdate()));

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
