package baikal.web.footballapp.tournament.activity;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import baikal.web.footballapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DialogRegion extends DialogFragment {
    private List<String> regions = new ArrayList<>();
    private int pos = -1;
    private Fragment fragment = null;
    public interface mListener{
        void onFinishEditDialog(int pos);
    }
    public DialogRegion(List<String> listItems) {
        this.regions = listItems;
        // Empty constructor required for DialogFragment
    }
    public void sendBackResult() {
        // Notice the use of `getTargetFragment` which will be set when the dialog is displayed
        mListener listener = (mListener) getParentFragment();
//        if(listener==null)
//            Log.d("nullllllllllllll","dssfsdfsdfsd");
        listener.onFinishEditDialog(pos);
        dismiss();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
       // setStyle(DialogRegion.STYLE_NORMAL, R.style.AppTheme);
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Выберите регион");
        alertDialogBuilder.setSingleChoiceItems(regions.toArray(new CharSequence[regions.size()]), -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                pos = i;

                sendBackResult();
                dialogInterface.dismiss();
            }
        });
        return alertDialogBuilder.create();
    }
}
