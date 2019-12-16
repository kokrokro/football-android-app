package baikal.web.footballapp.user.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.MankindKeeper;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Event;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.user.activity.MatchEvents;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class RVEventsAdapter extends RecyclerView.Adapter<RVEventsAdapter.ViewHolder> {
    private static final String TAG = "RVEventsAdapter";
    Logger log = LoggerFactory.getLogger(MatchEvents.class);
    private final List<Event> events;
    private final List<Event> eventsForTime;
    private String eventTime;
    private String teamId1;
    private String teamId2;
    private HashMap<String, Integer> eventIconIds;
    private HashMap<String, String> eventTypeToShow;
    final private Set<Integer> eventsToDelete;
    private boolean isEditable;

    RVEventsAdapter(List<Event> events, String teamId1, String teamId2,
                    boolean isEditable, Set<Integer> eventsToDelete) {
        this.events = events;
        this.teamId1 = teamId1;
        this.teamId2 = teamId2;
        this.isEditable = isEditable;

        this.eventsToDelete = eventsToDelete;
        eventsForTime = new ArrayList<>();
        getEventCnt();

        String[] eventTypes = {"goal", "yellowCard", "redCard", "penalty",
                "autoGoal", "foul", "penaltySeriesSuccess", "penaltySeriesFailure"};
        this.eventIconIds = new HashMap<>();
        this.eventIconIds.put(eventTypes[0], R.drawable.ic_event_goal);
        this.eventIconIds.put(eventTypes[1], R.drawable.ic_event_yellow_card);
        this.eventIconIds.put(eventTypes[2], R.drawable.ic_event_red_card);
        this.eventIconIds.put(eventTypes[3], R.drawable.ic_event_penalty_success);
        this.eventIconIds.put(eventTypes[4], R.drawable.ic_event_autogoal);
        this.eventIconIds.put(eventTypes[5], R.drawable.ic_event_foul);
        this.eventIconIds.put(eventTypes[6], R.drawable.ic_event_penalty_series_success);
        this.eventIconIds.put(eventTypes[7], R.drawable.ic_event_penalty_series_failure);

        this.eventTypeToShow = new HashMap<>();
        this.eventTypeToShow.put(eventTypes[4], "Автогол");
        this.eventTypeToShow.put(eventTypes[5], "Фол");
    }

    @NonNull
    @Override
    public RVEventsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_protocol_event_cell, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVEventsAdapter.ViewHolder holder, int position) {
        Event event = eventsForTime.get(position);

        holder.playerNameLeft.setText("");
        holder.playerNameRight.setText("");
        holder.eventTypeImageBtn.setClickable(isEditable);

        holder.eventTypeImageBtn.setOnClickListener(v -> deleteEvent(position));

        if (event.getPerson() != null) {
            if (MankindKeeper.getInstance().allPerson.containsKey(event.getPerson())) {
                Person p = MankindKeeper.getInstance().allPerson.get(event.getPerson());

                if (event.getTeam().equals(teamId1) && event.getPerson() != null)
                    holder.playerNameLeft.setText(p.getSurnameAndName());
                if (event.getTeam().equals(teamId2) && event.getPerson() != null)
                    holder.playerNameRight.setText(p.getSurnameAndName());
            } else
                getPerson(event.getPerson());
        }
        else {
            if (event.getTeam().equals(teamId1) && event.getPerson() != null)
                holder.playerNameLeft.setText(eventTypeToShow.get(event.getEventType()));
            if (event.getTeam().equals(teamId2) && event.getPerson() != null)
                holder.playerNameRight.setText(eventTypeToShow.get(event.getEventType()));
        }
        holder.eventTypeImageBtn.setImageResource(eventIconIds.get(event.getEventType()));
    }

    private void deleteEvent(int position)
    {
        int k=0, i=0;
        for (; i<events.size(); i++) {
            if (events.get(i).getTime().equals(eventTime) && !eventsToDelete.contains(i))
                k++;
            if (k-1 == position)
                break;
        }

        eventsForTime.remove(position);
        eventsToDelete.add(i);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return getEventCnt();
    }

    private int getEventCnt()
    {
        eventsForTime.clear();
        for (int i=0; i<events.size(); i++) {
//            Log.e(TAG, events.get(i).getTime() + " " + eventTime);
            if (events.get(i).getTime().equals(eventTime) && !eventsToDelete.contains(i))
                eventsForTime.add(events.get(i));
        }

        Log.d(TAG, String.valueOf(eventsForTime.size()));
        return eventsForTime.size();
    }

    private void getPerson (String personId)
    {
        Controller.getApi().getPerson(personId).enqueue(new Callback<List<Person>>() {
            @Override
            public void onResponse(@NonNull Call<List<Person>> call, @NonNull Response<List<Person>> response) {
                if (response.body() != null && response.body().size() > 0) {
                    Person person = response.body().get(0);
                    MankindKeeper.getInstance().addPerson(person);
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Person>> call, @NonNull Throwable t) {

            }
        });
    }

    void setEventTime(String eventTime) {
        this.eventTime = eventTime;
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
