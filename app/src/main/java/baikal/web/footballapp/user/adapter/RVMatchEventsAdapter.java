package baikal.web.footballapp.user.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Event;
import baikal.web.footballapp.user.activity.MatchEvents;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RVMatchEventsAdapter extends RecyclerView.Adapter<RVMatchEventsAdapter.ViewHolder>{
    Logger log = LoggerFactory.getLogger(MatchEvents.class);
    private final HashMap<Integer, String> halves;
    private final List<Event> events;
    public RVMatchEventsAdapter(HashMap<Integer, String> halves, List<Event> playerAllEvents){
        this.halves = halves;
        this.events = playerAllEvents;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.half_events, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String half = halves.get(position);
        holder.textHalf.setText(half);
        List<Event> playerEvents = new ArrayList<>();
        for (Event playerEvent : events){
            if (playerEvent.getTime().equals(half)){
                playerEvents.add(playerEvent);
            }
        }
    }

    @Override
    public int getItemCount() {
        return halves.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textHalf;
        final RecyclerView recyclerView;
        ViewHolder(View item) {
            super(item);
            textHalf = item.findViewById(R.id.matchEventsHalf);
            recyclerView = item.findViewById(R.id.recyclerViewHalfEvents);
        }
    }
}
