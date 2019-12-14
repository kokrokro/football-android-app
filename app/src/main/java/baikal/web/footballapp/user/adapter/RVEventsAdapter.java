package baikal.web.footballapp.user.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Event;
import baikal.web.footballapp.user.activity.MatchEvents;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RVEventsAdapter extends RecyclerView.Adapter<RVEventsAdapter.ViewHolder>{
    private Context context;
    Logger log = LoggerFactory.getLogger(MatchEvents.class);
    private final List<Event> events;
    RVEventsAdapter(Context context, List<Event> events){
        this.context = context;
        this.events = events;
    }
    @NonNull
    @Override
    public RVEventsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_protocol_event_cell, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVEventsAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView playerNameLeft;
        final TextView playerNameRight;
        final ImageButton eventTypeImageBtn;
        ViewHolder(View item) {
            super(item);
            playerNameLeft = item.findViewById(R.id.PPEC_player_name1);
            playerNameRight = item.findViewById(R.id.PPEC_player_name2);
            eventTypeImageBtn = item.findViewById(R.id.PPEC_event_type);
        }
    }
}
