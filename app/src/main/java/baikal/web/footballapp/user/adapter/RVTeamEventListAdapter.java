package baikal.web.footballapp.user.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.MankindKeeper;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Event;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.Player;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RVTeamEventListAdapter extends RecyclerView.Adapter<RVTeamEventListAdapter.ViewHolder> {
    private static final String TAG = "RV_TeamEvent_LA";
    private List<String> allowedPlayersId;
    private final List<Player> allowedPlayers;
    private List<Player> players;
    private List<Person> persons;
    private HashMap<String, List<Event>> events;
    final private List<Event> eventList;
    private String trainerId;
    private TeamEventListListener listener;
    private Activity context;
    private String teamId;


    public interface TeamEventListListener {
        void onClick (Person person);
    }

    public RVTeamEventListAdapter(Activity context, List<Player> players, List<String> allowedPlayersId, String trainerId,
                                  List<Event> events, String teamId, TeamEventListListener listener) {
        this.listener = listener;
        this.players = players;
        this.trainerId = trainerId;
        this.context = context;
        this.events = new HashMap<>();
        this.teamId = teamId;
        this.allowedPlayersId = allowedPlayersId;
        eventList = events;

        this.allowedPlayers = new ArrayList<>();
        getAllowedPlayersCnt();

        Log.d(TAG, teamId + ": " + this.allowedPlayers.toString() + " \n " + players.toString());

        if (trainerId != null)
            for (Player p: this.allowedPlayers)
                if (p.getPerson().equals(trainerId)) {
                    Log.d(TAG, teamId + ": " + trainerId + " " + this.allowedPlayers.toString());
                    this.trainerId = null;
                }

        persons = new ArrayList<>();
        for (int i=0; i<players.size() + (this.trainerId == null ? 0 : 1); i++)
            persons.add(null);

        fillEvents(eventList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        try {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.players_list_cell, parent, false);
            return new ViewHolder(view);
        } catch (Exception ignored) { }

        return new ViewHolder(new View(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (allowedPlayers.size() == position) {
            setupTrainersViewHolder(holder, position);
            return;
        }

        Player player = allowedPlayers.get(position);

        if (MankindKeeper.getInstance().getPersonById(player.getPerson()) == null)
            getPerson(player.getPerson());
        else {
            if (persons.get(position) == null)
                persons.set(position, MankindKeeper.getInstance().getPersonById(player.getPerson()));
            holder.playerName.setText(persons.get(position).getSurnameAndName());
            holder.playerNumber.setText(String.valueOf(player.getNumber()));
            holder.linearLayout.setOnClickListener(v -> listener.onClick(persons.get(position)));

            holder.events.clear();
            if (events.containsKey(players.get(position).getPerson()))
                holder.events.addAll(events.get(players.get(position).getPerson()));
            holder.adapter.notifyDataSetChanged();
        }
    }

    private void setupTrainersViewHolder(ViewHolder holder, int position) {
        if (MankindKeeper.getInstance().getPersonById(trainerId) == null)
            getPerson(trainerId);
        else {
            if (persons.get(position) == null)
                persons.set(position, MankindKeeper.getInstance().getPersonById(trainerId));
            holder.playerName.setText(persons.get(position).getSurnameAndName());
            holder.playerNumber.setText("ТР");
            holder.linearLayout.setOnClickListener(v -> listener.onClick(persons.get(position)));

            holder.events.clear();
            holder.events.addAll(events.get(trainerId)==null ? new LinkedHashSet<>() : events.get(trainerId));
            holder.adapter.notifyDataSetChanged();
        }
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

    private void fillEvents(List<Event> events) {
        this.events.clear();
        for (Event e: events) {
            if (e.getTeam().equals(teamId)) {
                if (!this.events.containsKey(e.getPerson()))
                    this.events.put(e.getPerson(), new ArrayList<>());
                this.events.get(e.getPerson()).add(e);
            }
        }
    }

    public void dataChanged()
    {
        fillEvents(eventList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return getAllowedPlayersCnt();
    }

    private int getAllowedPlayersCnt() {
        allowedPlayers.clear();
        for (Player p: players)
            if (allowedPlayersId.contains(p.getPerson()))
                allowedPlayers.add(p);

        if (trainerId != null)
            for (Player p: allowedPlayers)
                if (p.getPerson().equals(trainerId))
                    trainerId = null;

        return allowedPlayers.size() + (trainerId == null ? 0 : 1);
    }

    class ViewHolder extends RecyclerView.ViewHolder  {
        final TextView playerName;
        final TextView playerNumber;
        final RecyclerView playerEventList;
        final RVPlayerEventList adapter;
        final LinearLayout linearLayout;
        final List<Event> events;

        ViewHolder(View itemView) {
            super(itemView);

            playerName = itemView.findViewById(R.id.PLC_playerName);
            playerNumber = itemView.findViewById(R.id.PLC_playerNumber);
            playerEventList = itemView.findViewById(R.id.PLC_playerEvents);
            playerEventList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            linearLayout = itemView.findViewById(R.id.plc_Linear_Layout);
            events = new ArrayList<>();
            adapter = new RVPlayerEventList(context, events);
            playerEventList.setAdapter(adapter);
        }
    }
}
