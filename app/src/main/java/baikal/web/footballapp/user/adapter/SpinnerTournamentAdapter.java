package baikal.web.footballapp.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import baikal.web.footballapp.R;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.user.activity.NewCommand;

public class SpinnerTournamentAdapter extends ArrayAdapter<League> {
    Logger log = LoggerFactory.getLogger(NewCommand.class);
    private final LayoutInflater inflater;
    private final List<League> tournaments;
    private final int resource;

    public SpinnerTournamentAdapter(@NonNull Context context, int resource, List<League> tournaments) {
        super(context, resource, tournaments);
        inflater = LayoutInflater.from(context);
        this.tournaments = tournaments;
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

//        View rowview = inflater.inflate(R.layout.spinner_item,null,true);

        TextView txtTitle = view.findViewById(R.id.text1);
        txtTitle.setText(tournaments.get(position).getName());
//        ImageView imageView = (ImageView) rowview.findViewById(R.id.icon);
//        imageView.setImageResource(rowItem.getLogo());

        return view;
    }
}
