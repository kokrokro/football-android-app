package baikal.web.footballapp.user.activity;

import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.widget.Button;

import baikal.web.footballapp.R;

public class Abbrev extends DialogFragment {
    public static Abbrev newInstance() {
        return new Abbrev();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.abbrev, null);
        Button button = view.findViewById(R.id.abbrevCloseButton);
        button.setOnClickListener(v -> getDialog().dismiss());
        builder.setView(view);
        return builder.create();
    }
}