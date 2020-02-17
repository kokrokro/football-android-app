package baikal.web.footballapp.tournament.activity;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.util.List;

import baikal.web.footballapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DialogRegion extends DialogFragment {
    private List<String> regions;
    private int pos = -1;

    public interface mListener {
        void onFinishEditDialog(int pos);
    }
    DialogRegion(List<String> listItems) {
        this.regions = listItems;
    }
    private void sendBackResult() {
        mListener listener = (mListener) getParentFragment();
        listener.onFinishEditDialog(pos);
        dismiss();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(),R.style.DialogTheme);
        alertDialogBuilder.setTitle("Выберите регион");
        alertDialogBuilder.setItems(regions.toArray(new CharSequence[0]), (dialogInterface, i) -> {
            pos = i;
            sendBackResult();
            dialogInterface.dismiss();
        });
        return alertDialogBuilder.create();
    }
}
