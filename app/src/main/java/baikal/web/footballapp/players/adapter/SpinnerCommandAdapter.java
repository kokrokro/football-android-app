package baikal.web.footballapp.players.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.players.activity.PlayerInv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SpinnerCommandAdapter extends ArrayAdapter<Team> {
    Logger log = LoggerFactory.getLogger(PlayerInv.class);
    private final LayoutInflater inflater;
    private final Context context;
    private final List<Team> clubs;
    private final int resource;
    public SpinnerCommandAdapter(@NonNull Context context, int resource, List<Team> clubs) {
        super(context, resource, clubs);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.clubs = clubs;
        this.resource = resource;
    }
    @Override
    public View getDropDownView(int position, @Nullable View convertView,
                                @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent){

        final View view = inflater.inflate(resource, parent, false);
        Team club = clubs.get(position);

//        View rowview = inflater.inflate(R.layout.spinner_item,null,true);

        TextView txtTitle = view.findViewById(R.id.text1);
        txtTitle.setText(club.getName());

//        ImageView imageView = (ImageView) rowview.findViewById(R.id.icon);
//        imageView.setImageResource(rowItem.getLogo());

        return view;
    }
}