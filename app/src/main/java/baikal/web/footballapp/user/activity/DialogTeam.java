package baikal.web.footballapp.user.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

import baikal.web.footballapp.R;

public class DialogTeam extends DialogFragment {
    private Listener mListener;
    private int pos = -1;

    public interface Listener{
        void onFinishEditDialog(int pos);
    }

    public DialogTeam(Listener mListener) {
        this.mListener = mListener;
    }

    private void sendBackResult() {
        mListener.onFinishEditDialog(pos);
        dismiss();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // setStyle(DialogRegion.STYLE_NORMAL, R.style.AppTheme);
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.DialogTheme);
        alertDialogBuilder.setTitle("Настройки команды");
        ArrayList <String> arrayList = new ArrayList<>();
        arrayList.add("Показать матчи");
        arrayList.add("Редактировать состав");
        alertDialogBuilder.setItems(arrayList.toArray(new CharSequence[arrayList.size()]), (dialogInterface, i) -> {
            pos = i;

            sendBackResult();
            dialogInterface.dismiss();
        });
        //alertDialogBuilder.setView(R.layout.dialog);
        return alertDialogBuilder.create();
    }
}
