package baikal.web.footballapp.user.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import baikal.web.footballapp.model.Event;

public class RVPlayerEventList extends RecyclerView.Adapter<RVPlayerEventList.ViewHolder> {

    private List<Event> events;
    private HashMap<String, Integer> eventsCnt;
    private String[] eventTypes = {"goal", "yellowCard", "redCard", "penalty"};

    RVPlayerEventList(List<Event> events) {
        this.events = events;
        this.eventsCnt = new HashMap<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return getEventTypeNumber();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View itemView) {
            super(itemView);
        }
    }

    private int getEventTypeNumber() {
        int ans=0;

        for (Event e: events) {

        }

        return ans;
    }
}
