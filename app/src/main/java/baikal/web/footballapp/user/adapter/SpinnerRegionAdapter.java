package baikal.web.footballapp.user.adapter;

import android.graphics.Color;
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
import java.util.Objects;

public class SpinnerRegionAdapter extends ArrayAdapter<Region> {
    public static final String TAG = "SpinnerRegionAdapter: ";
    Logger log = LoggerFactory.getLogger(SpinnerRegionAdapter.class);
    private final List<Region> regions;
    private final int resource;

    public SpinnerRegionAdapter (@NonNull Context context, int resource, List<Region> regions) {
        super(context, resource, regions);
        this.resource = resource;
        this.regions = regions;
    }

    @Override
    public void notifyDataSetChanged() {
        log.debug(TAG, "notifyDataSetChanged ..........");
        super.notifyDataSetChanged();
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        log.debug(TAG, "getDropDownView at position = " + position);
        return createItemView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        log.debug(TAG, "getView at position = " + position);
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent) {
//        View view = new View(getContext());
//        return view;

        try {
            if (convertView == null)
                convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);

            if (getItem(position) != null) {
                Region region = getItem(position);
                TextView txtTitle = convertView.findViewById(R.id.text1);
                txtTitle.setText(Objects.requireNonNull(region).getName());

                if (position == 0)
                    txtTitle.setTextColor(Color.LTGRAY);
            }
        } catch (Exception e) {
            log.error(TAG, e);
        }
        return convertView;
    }

}
