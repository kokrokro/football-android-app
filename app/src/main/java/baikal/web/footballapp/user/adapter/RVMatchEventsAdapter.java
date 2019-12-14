package baikal.web.footballapp.user.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
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
import java.util.List;

public class RVMatchEventsAdapter extends RecyclerView.Adapter<RVMatchEventsAdapter.ViewHolder>{
    Logger log = LoggerFactory.getLogger(MatchEvents.class);
    private Context context;
    private final List<Event> events;
    public RVMatchEventsAdapter(Context context, List<Event> events){
        this.context = context;
        this.events = events;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.half_events, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textHalf;
        final RecyclerView recyclerView;
        final List<Event> events;
        ViewHolder(View item) {
            super(item);
            textHalf = item.findViewById(R.id.matchEventsHalf);
            recyclerView = item.findViewById(R.id.recyclerViewHalfEvents);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            events = new ArrayList<>();
            RVEventsAdapter adapter = new RVEventsAdapter(context, events);
            recyclerView.setAdapter(adapter);
        }
    }
}
