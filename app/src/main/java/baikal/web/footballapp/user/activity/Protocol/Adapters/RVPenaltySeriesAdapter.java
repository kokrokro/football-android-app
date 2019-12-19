package baikal.web.footballapp.user.activity.Protocol.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Event;

public class RVPenaltySeriesAdapter extends RecyclerView.Adapter<RVPenaltySeriesAdapter.ViewHolder> {

    private final List<Event> events;
    private final List<List<Event>> eventsToShow;
    private String teamId;
    private HashMap<String, Drawable> iconHashMap;

    public RVPenaltySeriesAdapter (Context context, List<Event> events, String teamId) {
        this.events = events;
        this.teamId = teamId;
        eventsToShow = new ArrayList<>();
        countEventsToShow();

        iconHashMap = new HashMap<>();
        iconHashMap.put("penaltySeriesSuccess", context.getDrawable(R.drawable.ic_event_goal));
        iconHashMap.put("penaltySeriesFailure", context.getDrawable(R.drawable.ic_event_goal_failure));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.protocol_penalty_series_cell, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        List<Event> eventsToShowRaw = eventsToShow.get(position);

        holder.series1.setImageDrawable(null);
        holder.series2.setImageDrawable(null);
        holder.series3.setImageDrawable(null);

        holder.series1.setImageDrawable(iconHashMap.get(eventsToShowRaw.get(0).getEventType()));
        if (eventsToShowRaw.size() > 1)
        holder.series2.setImageDrawable(iconHashMap.get(eventsToShowRaw.get(1).getEventType()));
        if (eventsToShowRaw.size() > 2)
        holder.series3.setImageDrawable(iconHashMap.get(eventsToShowRaw.get(2).getEventType()));
    }

    @Override
    public int getItemCount() {
        return countEventsToShow();
    }

    private int countEventsToShow()
    {
        eventsToShow.clear();
        TreeSet<String> disabledEvents = new TreeSet<>();

        List<Event> seriesCell = new ArrayList<>();
        for (Event e: events)
            if (e.getEventType().equals("disable"))
                disabledEvents.add(e.getEvent());
            else if (e.getEventType().equals("enable"))
                disabledEvents.remove(e.getEvent());

        for (Event e: events) {
            if (e.getEventType().equals("disable") ||
                    e.getEventType().equals("enable") ||
                    (e.getId() != null && disabledEvents.contains(e.getId())))
                continue;

            if ((e.getEventType().equals("penaltySeriesSuccess") ||
                    e.getEventType().equals("penaltySeriesFailure")) &&
                    e.getTeam().equals(teamId)){
                seriesCell.add(e);

                if (seriesCell.size() == 3) {
                    eventsToShow.add(new ArrayList<>());
                    eventsToShow.get(eventsToShow.size()-1).addAll(seriesCell);
                    seriesCell.clear();
                }
            }
        }

        if (seriesCell.size() != 0) {
            eventsToShow.add(new ArrayList<>());
            eventsToShow.get(eventsToShow.size()-1).addAll(seriesCell);
            seriesCell.clear();
        }

        Log.d("RVPSAdapter", String.valueOf(eventsToShow.size()));
        return eventsToShow.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView series1;
        final ImageView series2;
        final ImageView series3;

        ViewHolder(View item) {
            super(item);
            series1 = item.findViewById(R.id.PPSC_series1);
            series2 = item.findViewById(R.id.PPSC_series2);
            series3 = item.findViewById(R.id.PPSC_series3);

            series1.setImageDrawable(null);
            series2.setImageDrawable(null);
            series3.setImageDrawable(null);
        }
    }
}
