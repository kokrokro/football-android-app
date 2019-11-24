package baikal.web.footballapp;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.File;
import java.util.Objects;

public class SelectImageDialog extends DialogFragment {

    private static final String TAG = "SelectImageDialog: ";
    private static final int PICKIMAGE_REQUEST_CODE = 5234; //magic number
    private static final int CAMERA_REQUEST_CODE = 7491;    //magic number

    public interface OnImageSelectedListener {
        void getImagePath(Uri ImagePath);
        void getImageBitmap(Bitmap bitmap);
    }

    private OnImageSelectedListener mOnImageSelectedListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_select_image, container, false);

        TextView selectImage = view.findViewById(R.id.dialogChoosePhoto);
        selectImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, PICKIMAGE_REQUEST_CODE);
        });
        TextView takePhoto = view.findViewById(R.id.dialogTakePhoto);
        takePhoto.setOnClickListener(v -> {
            Uri outputFileUri = getCaptureImageOutputUri();
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            try {
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getContext(), "Камера не доступна", Toast.LENGTH_SHORT).show();
                Log.e(TAG, e.toString());
            } catch (Exception e) {
                Log.e(TAG , e.toString());
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == PICKIMAGE_REQUEST_CODE) {
            Uri selectedImageUri = data.getData();

            mOnImageSelectedListener.getImagePath(selectedImageUri);
            getDialog().dismiss();
        }

        if (resultCode == Activity.RESULT_OK && requestCode == CAMERA_REQUEST_CODE) {
            Bitmap bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            mOnImageSelectedListener.getImageBitmap(bitmap);
            getDialog().dismiss();
        }
    }

    @Override
    public void onAttach(Context context) {
        try {
            mOnImageSelectedListener = (OnImageSelectedListener) getTargetFragment();
        } catch (ClassCastException e) {
            Log.e(TAG, e.toString());
        }
        super.onAttach(context);
    }

    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = Objects.requireNonNull(getActivity()).getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
        }
        return outputFileUri;
    }
}
