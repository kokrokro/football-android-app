package baikal.web.footballapp.user.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Event;

public class RVPlayerEventList extends RecyclerView.Adapter<RVPlayerEventList.ViewHolder> {

    private Set<Event> events;
    private HashMap<String, Integer> eventsCnt;
    private HashMap<String, Integer> eventsCntToShow;
    private HashMap<String, Integer> eventIconIds;
    private HashMap<String, Float> iconsAlpha;
    private HashMap<String, Typeface> bgFont;

    private String[] eventTypes = {"goal", "yellowCard", "redCard", "penalty"};
    private List<String> eventTypesToShow;

    RVPlayerEventList(Activity activity, Set<Event> events) {
        this.events = events;
        this.eventsCnt = new HashMap<>();
        this.eventsCntToShow = new HashMap<>();
        this.eventIconIds = new HashMap<>();
        this.iconsAlpha= new HashMap<>();
        this.bgFont = new HashMap<>();
        this.eventTypesToShow = new ArrayList<>();

        this.eventIconIds.put(eventTypes[0], R.drawable.ic_event_goal);
        this.eventIconIds.put(eventTypes[1], R.drawable.ic_event_yellow_card);
        this.eventIconIds.put(eventTypes[2], R.drawable.ic_event_red_card);
        this.eventIconIds.put(eventTypes[3], R.drawable.ic_event_penalty_success);

        this.iconsAlpha.put(eventTypes[0], 0.95f);
        this.iconsAlpha.put(eventTypes[1], 1f);
        this.iconsAlpha.put(eventTypes[2], 1f);
        this.iconsAlpha.put(eventTypes[3], 0.95f);

        this.bgFont.put(eventTypes[0], Typeface.createFromAsset(activity.getAssets(),"fonts/manrope_bold.otf"));
        this.bgFont.put(eventTypes[1], Typeface.createFromAsset(activity.getAssets(),"fonts/manrope_regular.otf"));
        this.bgFont.put(eventTypes[2], Typeface.createFromAsset(activity.getAssets(),"fonts/manrope_regular.otf"));
        this.bgFont.put(eventTypes[3], Typeface.createFromAsset(activity.getAssets(),"fonts/manrope_bold.otf"));

        for (String eventType : eventTypes) this.eventsCnt.put(eventType, 0);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        try {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_protocol_event_item, parent, false);
            return new ViewHolder(view);
        } catch (Exception ignored) { }

        return new ViewHolder(new View(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String key = eventTypesToShow.get(position);

        Log.d("RVPLAYEREVENTLIST", key);
        Log.d("RVPLAYEREVENTLIST", eventIconIds.get(key).toString());

        holder.eventTypeIcon.setImageResource(eventIconIds.get(key));
        holder.eventTypeIcon.setAlpha(iconsAlpha.get(key));
        holder.eventCnt.setText(String.valueOf(eventsCntToShow.get(key)));
        holder.bgEventCnt.setTypeface(bgFont.get(key));

        holder.bgEventCnt.setText(String.valueOf(eventsCntToShow.get(key)));
        holder.eventCnt.setText(String.valueOf(eventsCntToShow.get(key)));
    }

    @Override
    public int getItemCount() {
        int sz = getEventTypeNumber();
        if (sz > 4) {
            Log.d("RVPLAYEREVENTLIST", String.valueOf(sz));
            return 0;
        }

        return sz;
    }

    private int getEventTypeNumber() {
        eventsCntToShow.clear();
        eventTypesToShow.clear();
        for (String eventType : eventTypes) this.eventsCnt.put(eventType, 0);

        for (Event e: events) {
            Integer incr = eventsCnt.get(e.getEventType());
            eventsCnt.put(e.getEventType(), (incr == null ? 0 : incr) + 1);
        }

        Set<String> keys = eventsCnt.keySet();

        for (String key: keys)
            if (eventsCnt.get(key) != null && eventsCnt.get(key) != 0)
                eventsCntToShow.put(key, eventsCnt.get(key));

        for (String key: eventTypes)
            if (eventsCntToShow.containsKey(key))
                eventTypesToShow.add(key);

        return eventTypesToShow.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView eventTypeIcon;
        TextView bgEventCnt;
        TextView eventCnt;

        ViewHolder(View itemView) {
            super(itemView);
            eventTypeIcon = itemView.findViewById(R.id.EPEI_editProtocolEventIcon);
            bgEventCnt = itemView.findViewById(R.id.EPEI_editProtocolEventCntBG);
            eventCnt = itemView.findViewById(R.id.EPEI_editProtocolEventCnt);
        }
    }
}
