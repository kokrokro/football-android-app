package baikal.web.footballapp.Dialogs;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import baikal.web.footballapp.MankindKeeper;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Region;

/**
 * A simple {@link Fragment} subclass.
 */
public class DialogRegion extends DialogFragment {
    private List<Region> regions;
    private OnRegionListener mListener;

    public interface OnRegionListener {
        void onFinishEditDialog(Region region);
    }
    public DialogRegion(OnRegionListener mListener) {
        this.regions = MankindKeeper.getInstance().regions;
        this.mListener = mListener;
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
        List<String> regionNames = new ArrayList<>();
        for (Region r: regions)
            regionNames.add(r.getName());

        regionNames.add("Не выбирать");

        alertDialogBuilder.setItems(regionNames.toArray(new CharSequence[0]), (dialogInterface, i) -> {
            mListener.onFinishEditDialog(i==regions.size()?null:regions.get(i));
            dialogInterface.dismiss();
        });

        return alertDialogBuilder.create();
    }
}
