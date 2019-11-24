package baikal.web.footballapp.user.adapter;

import android.widget.ArrayAdapter;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Region;
import baikal.web.footballapp.user.activity.NewCommand;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SpinnerRegionAdapter extends ArrayAdapter<Region> {
    Logger log = LoggerFactory.getLogger(NewCommand.class);
    private final LayoutInflater inflater;
    private final List<Region> regions;
    private final int resource;

    public SpinnerRegionAdapter (@NonNull Context context, int resource, List<Region> regions) {
        super(context, resource, regions);
        this.resource = resource;
        this.regions = regions;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, parent);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, parent);
    }

    private View createItemView(int position, ViewGroup parent) {
        final View view = inflater.inflate(resource, parent, false);
        Region region = regions.get(position);
        TextView txtTitle = view.findViewById(R.id.text1);
        txtTitle.setText(region.getName());

        return view;
    }

}
