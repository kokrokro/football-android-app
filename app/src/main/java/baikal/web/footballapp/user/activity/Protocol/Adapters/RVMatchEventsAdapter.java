package baikal.web.footballapp.user.activity.Protocol.Adapters;

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
import baikal.web.footballapp.user.activity.Protocol.MatchEvents;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class RVMatchEventsAdapter extends RecyclerView.Adapter<RVMatchEventsAdapter.ViewHolder>{
    Logger log = LoggerFactory.getLogger(MatchEvents.class);
    private Context context;
    private final List<Event> events;

    private String[] matchTimesToShow = {"Первый тайм", "Второй тайм", "Дополнительное время", "Серия пенальти"};
    private String[] matchTimes = {"firstHalf", "secondHalf", "extraTime", "penaltySeries"};
    private LinkedHashMap<String, Integer> matchTimesMap;
    private String teamId1;
    private String teamId2;
    private boolean isEditable;
    final private Set<Integer> eventsToDelete;

    public RVMatchEventsAdapter(Context context, List<Event> events,
                                String teamId1, String teamId2,
                                boolean isEditable, Set<Integer> eventsToDelete){
        this.context = context;
        this.events = events;
        this.teamId1 = teamId1;
        this.teamId2 = teamId2;
        this.isEditable = isEditable;
        this.eventsToDelete = eventsToDelete;

        matchTimesMap = new LinkedHashMap<>();
        matchTimesMap.put("firstHalf",     1);
        matchTimesMap.put("secondHalf",    2);
        matchTimesMap.put("extraTime",     3);
        matchTimesMap.put("penaltySeries", 4);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.half_events, parent, false);
        ViewHolder holder = new ViewHolder(view);

        holder.events.clear();
        holder.events.addAll(events);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textHalf.setText(matchTimesToShow[position]);
        holder.adapter.setEventTime(matchTimes[position]);
        holder.adapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return getTimeCount();
    }

    private int getTimeCount()
    {
        int ans=0;

        for (Event e: events)
            if (e.getTime() != null)
                ans = Math.max(ans, matchTimesMap.get(e.getTime()));

        return ans;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textHalf;
        final RecyclerView recyclerView;
        final List<Event> events;
        final RVEventsAdapter adapter;
        ViewHolder(View item) {
            super(item);
            textHalf = item.findViewById(R.id.matchEventsHalf);
            recyclerView = item.findViewById(R.id.recyclerViewHalfEvents);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            events = new ArrayList<>();
            adapter = new RVEventsAdapter(events, teamId1, teamId2, isEditable, eventsToDelete);
            recyclerView.setAdapter(adapter);
        }
    }
}
