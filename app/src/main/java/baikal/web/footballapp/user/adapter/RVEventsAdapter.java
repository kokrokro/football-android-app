package baikal.web.footballapp.user.adapter;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import baikal.web.footballapp.CheckName;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SetImage;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.PlayerEvent;
import baikal.web.footballapp.user.activity.MatchEvents;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RVEventsAdapter extends RecyclerView.Adapter<RVEventsAdapter.ViewHolder>{
    private final MatchEvents context;
    Logger log = LoggerFactory.getLogger(MatchEvents.class);
    private final List<PlayerEvent> playerEvents;
    public RVEventsAdapter(Activity context, List<PlayerEvent> playerEvents){
        this.context = (MatchEvents) context;
        this.playerEvents = playerEvents;
    }
    @NonNull
    @Override
    public RVEventsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.events, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVEventsAdapter.ViewHolder holder, int position) {
        String str = "";
        PlayerEvent playerEvent = playerEvents.get(position);
        switch (playerEvent.getEvent().getEventType()){
            case "goal":
                str = "Г";
                break;
            case "yellowCard":
                str = "ЖК";
                break;
            case "redCard":
                str = "КК";
                break;
            case "foul":
                str = "Ф";
                break;
            case "autoGoal":
                str = "А";
                break;
            case "penalty":
                str = "П";
                break;
        }
//        holder.textTime.setText(str);
        holder.textEvent.setText(str);
        Person person;
        try {
            person = playerEvent.getPerson();
        }catch (NullPointerException e){
            person = new Person();
            person.setSurname("Удален");
            person.setName("");
            person.setLastname("");
        }
        CheckName checkName = new CheckName();
        str = checkName.check(person.getSurname(), person.getName(), person.getLastname());
        holder.textName.setText(str);
        SetImage setImage = new SetImage();
        setImage.setImage(context, holder.image, person.getPhoto());
        if (position==(playerEvents.size()-1)){
            holder.line.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return playerEvents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textTime;
        final TextView textName;
        final TextView textEvent;
        final ImageView image;
        final View line;
        ViewHolder(View item) {
            super(item);
            textTime = item.findViewById(R.id.eventTime);
            textName = item.findViewById(R.id.eventPlayerName);
            textEvent = item.findViewById(R.id.eventPlayerEvent);
            image = item.findViewById(R.id.eventCommandLogo);
            line = item.findViewById(R.id.eventLine);
        }
    }
}
