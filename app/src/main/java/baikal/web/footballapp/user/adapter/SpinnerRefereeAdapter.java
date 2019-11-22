package baikal.web.footballapp.user.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import baikal.web.footballapp.CheckName;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.user.activity.NewCommand;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SpinnerRefereeAdapter extends ArrayAdapter<Person> {
    Logger log = LoggerFactory.getLogger(NewCommand.class);
    private final LayoutInflater inflater;
    private final Context context;
    private final List<Person> referees;
    private final int resource;
    public SpinnerRefereeAdapter(@NonNull Context context, int resource, List<Person> referees) {
        super(context, resource, referees);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.referees = referees;
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
        Person referee = referees.get(position);

//        View rowview = inflater.inflate(R.layout.spinner_item,null,true);

        TextView txtTitle = view.findViewById(R.id.text1);
        CheckName checkName = new CheckName();
        String str;
        try {
            str = checkName.check(referee.getSurname(), referee.getName(), referee.getLastname());
            txtTitle.setText(str);
        }catch (NullPointerException e){
            str = "Не выбрано";
            txtTitle.setText(str);
            txtTitle.setTextColor(ContextCompat.getColor(context, R.color.colorBadge));
        }



//        ImageView imageView = (ImageView) rowview.findViewById(R.id.icon);
//        imageView.setImageResource(rowItem.getLogo());

        return view;
    }
}