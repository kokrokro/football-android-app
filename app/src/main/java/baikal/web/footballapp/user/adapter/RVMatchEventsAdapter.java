package baikal.web.footballapp.user.adapter;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Event;
import baikal.web.footballapp.model.PlayerEvent;
import baikal.web.footballapp.user.activity.MatchEvents;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RVMatchEventsAdapter extends RecyclerView.Adapter<RVMatchEventsAdapter.ViewHolder>{
    Logger log = LoggerFactory.getLogger(MatchEvents.class);
    private final MatchEvents context;
    private final HashMap<Integer, String> halves;
    private final List<Event> playerAllEvents;
    public RVMatchEventsAdapter(Activity context, HashMap<Integer, String> halves, List<Event> playerAllEvents){
        this.context = (MatchEvents) context;
        this.halves = halves;
        this.playerAllEvents = playerAllEvents;
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
        for (Event playerEvent : playerAllEvents){
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
