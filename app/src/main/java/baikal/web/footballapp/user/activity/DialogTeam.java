package baikal.web.footballapp.user.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import baikal.web.footballapp.R;
import baikal.web.footballapp.tournament.activity.DialogRegion;

public class DialogTeam extends DialogFragment {
    private Listener mListener;
    private int pos = -1;
    private Fragment fragment = null;
    public interface Listener{
        void onFinishEditDialog(int pos);
    }
    public DialogTeam(Listener mListener ) {
        this.mListener = mListener;
    }
    public void sendBackResult() {
        mListener.onFinishEditDialog(pos);
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
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.DialogTheme);
        alertDialogBuilder.setTitle("Настройки команды");
        ArrayList <String> arrayList = new ArrayList<>();
        arrayList.add("Показать матчи");
        arrayList.add("Редактировать состав");
        alertDialogBuilder.setItems(arrayList.toArray(new CharSequence[arrayList.size()]), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                pos = i;

                sendBackResult();
                dialogInterface.dismiss();
            }
        });
        //alertDialogBuilder.setView(R.layout.dialog);
        return alertDialogBuilder.create();
    }
}
