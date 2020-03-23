package baikal.web.footballapp.user.activity.Protocol.Adapters;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import baikal.web.footballapp.App;
import baikal.web.footballapp.Controller;
import baikal.web.footballapp.MankindKeeper;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Event;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.Team;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RVEventsAdapter extends RecyclerView.Adapter<RVEventsAdapter.ViewHolder> {
    private final List<Event> events;
    private final List<Event> eventsForTime;
    private String eventTime;
    private String teamId1;
    private String teamId2;
    private Team team1;
    private Team team2;
    private HashMap<String, Integer> eventIconIds;
    final private Set<Integer> eventsToDelete;
    final private TreeSet<String> disabledEvents;
    private boolean isEditable;

    RVEventsAdapter(List<Event> events, Team team1, Team team2,
                    boolean isEditable, Set<Integer> eventsToDelete) {
        this.events = events;
        this.teamId1 = team1.getId();
        this.teamId2 = team2.getId();
        this.team1 = team1;
        this.team2 = team2;
        this.isEditable = isEditable;
        this.eventsToDelete = eventsToDelete;

        disabledEvents = new TreeSet<>();
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

//        HashMap<String, String> eventTypeToShow = new HashMap<>();
//        eventTypeToShow.put(eventTypes[4], "Автогол");
//        eventTypeToShow.put(eventTypes[5], "Фол");
//        eventTypeToShow.put(eventTypes[6], "Гол");
//        eventTypeToShow.put(eventTypes[7], "Не гол");
    }

    @NonNull
    @Override
    public RVEventsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_protocol_event_cell, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RVEventsAdapter.ViewHolder holder, int position) {
        Event event = eventsForTime.get(position);

        holder.setRegularFont();
        holder.playerNameLeft.setText("");
        holder.playerNameRight.setText("");
        holder.eventTypeImageBtn.setClickable(isEditable);

        holder.eventTypeImageBtn.setOnClickListener(v -> deleteEvent(position));

        if (event.getPerson() != null && !event.getPerson().equals("")) {
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
            holder.setBoldFont();
            if (event.getEventType().equals("autoGoal")) {
                if (event.getTeam().equals(teamId1))
                    holder.playerNameRight.setText("[ " + team1.getName() + " ]");
                if (event.getTeam().equals(teamId2))
                    holder.playerNameLeft.setText("[ " + team2.getName() + " ]");
            }
            else {
                if (event.getTeam().equals(teamId1))
                    holder.playerNameLeft.setText(team1.getName());
                if (event.getTeam().equals(teamId2))
                    holder.playerNameRight.setText(team2.getName());
            }
        }
        holder.eventTypeImageBtn.setImageResource(eventIconIds.get(event.getEventType()));
    }

    private void deleteEvent(int position)
    {
        int k=0, i=0;
        for (; i<events.size(); i++) {
            if (!eventsToDelete.contains(i) &&
                    !events.get(i).getEventType().equals("disable") &&
                    !events.get(i).getEventType().equals("enable") &&
                    !disabledEvents.contains(events.get(i).getId()) &&
                    !events.get(i).getEventType().equals("matchEnd") &&
                    !events.get(i).getEventType().equals("matchStart") &&
                    events.get(i).getTime().equals(eventTime))
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
        disabledEvents.clear();

        for (Event e: events)
            if (e.getEventType().equals("disable"))
                disabledEvents.add(e.getEvent());
            else if (e.getEventType().equals("enable"))
                disabledEvents.remove(e.getEvent());

        for (int i=0; i<events.size(); i++)
            if (events.get(i).getTime() != null &&
                    events.get(i).getTime().equals(eventTime) &&
                    !eventsToDelete.contains(i) &&
                    !events.get(i).getEventType().equals("disable") &&
                    !events.get(i).getEventType().equals("enable") &&
                    !events.get(i).getEventType().equals("matchEnd") &&
                    !events.get(i).getEventType().equals("matchStart") &&
                    (events.get(i).getId()==null || !disabledEvents.contains(events.get(i).getId())))
                eventsForTime.add(events.get(i));

//        Log.d(TAG, String.valueOf(eventsForTime.size()));
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

        void setBoldFont() {
            Typeface font = ResourcesCompat.getFont(App.getAppContext(), R.font.manrope_semibold);
            playerNameLeft.setTypeface(font);
            playerNameRight.setTypeface(font);
        }

        void setRegularFont() {
            Typeface font = ResourcesCompat.getFont(App.getAppContext(), R.font.manrope_regular);
            playerNameLeft.setTypeface(font);
            playerNameRight.setTypeface(font);
        }
    }
}
