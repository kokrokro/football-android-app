package baikal.web.footballapp.user.adapter;

import android.app.Activity;
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
    private List<Player> players;
    private List<Person> persons;
    private HashMap<String, List<Event>> events;
    private List<Event> eventList;
    private String trainerId;
    private TeamEventListListener listener;
    private Activity context;

    public interface TeamEventListListener {
        void onClick (Person person);
    }

    public RVTeamEventListAdapter(Activity context, List<Player> players, String trainerId,
                                  List<Event> events, TeamEventListListener listener) {
        this.listener = listener;
        this.players = players;
        this.trainerId = trainerId;
        this.context = context;
        this.events = new HashMap<>();
        eventList = events;

        fillEvents(eventList);

        for (Player p: players)
            if (p.getPerson().equals(trainerId))
                trainerId = null;

        persons = new ArrayList<>();
        for (int i=0; i<players.size() + (trainerId == null ? 0 : 1); i++)
            persons.add(null);
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

        if (players.size() == position) {
            setupTrainersViewHolder(holder, position);
            return;
        }

        Player player = players.get(position);

        if (MankindKeeper.getInstance().getPersonById(player.getPerson()) == null)
            Controller.getApi().getPerson(player.getPerson()).enqueue(new Callback<List<Person>>() {
                @Override
                public void onResponse(@NonNull Call<List<Person>> call, @NonNull Response<List<Person>> response) {
                    if (response.body() != null && response.body().size() > 0) {
                        Person person = response.body().get(0);
                        persons.set(position, person);
                        MankindKeeper.getInstance().addPerson(person);
                        notifyDataSetChanged();

                        holder.linearLayout.setOnClickListener(v -> listener.onClick(persons.get(position)));
                        holder.playerName.setText(persons.get(position).getSurnameAndName());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Person>> call, @NonNull Throwable t) {

                }
            });
        else {
            if (persons.get(position) == null)
                persons.set(position, MankindKeeper.getInstance().getPersonById(player.getPerson()));
            holder.playerName.setText(persons.get(position).getSurnameAndName());
            holder.playerNumber.setText(String.valueOf(player.getNumber()));
            holder.linearLayout.setOnClickListener(v -> listener.onClick(persons.get(position)));

            holder.events.clear();
            holder.events.addAll(events.get(players.get(position).getPerson()));
        }

    }

    private void setupTrainersViewHolder(ViewHolder holder, int position) {
        if (MankindKeeper.getInstance().getPersonById(trainerId) == null)
            Controller.getApi().getPerson(trainerId).enqueue(new Callback<List<Person>>() {
                @Override
                public void onResponse(@NonNull Call<List<Person>> call, @NonNull Response<List<Person>> response) {
                    if (response.body() != null && response.body().size() > 0) {
                        Person person = response.body().get(0);
                        persons.set(position, person);
                        MankindKeeper.getInstance().addPerson(person);
                        notifyDataSetChanged();

                        holder.linearLayout.setOnClickListener(v -> listener.onClick(persons.get(position)));
                        holder.playerName.setText(persons.get(position).getSurnameAndName());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Person>> call, @NonNull Throwable t) {

                }
            });
        else {
            if (persons.get(position) == null)
                persons.set(position, MankindKeeper.getInstance().getPersonById(trainerId));
            holder.playerName.setText(persons.get(position).getSurnameAndName());
            holder.playerNumber.setText("ТР");
            holder.linearLayout.setOnClickListener(v -> listener.onClick(persons.get(position)));

            holder.events.clear();
            holder.events.addAll(events.get(trainerId)==null ? new ArrayList<>() : events.get(trainerId));
        }
    }

    private void fillEvents(List<Event> events) {
        for (Event e: events) {
            if (!this.events.containsKey(e.getPerson()))
                this.events.put(e.getPerson(), new ArrayList<>());
            this.events.get(e.getPerson()).add(e);
        }
    }

    public void dataChanged()
    {
        fillEvents(eventList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return players.size() + (trainerId == null ? 0 : 1);
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
            playerEventList.setLayoutManager(new LinearLayoutManager(context));
            linearLayout = itemView.findViewById(R.id.plc_Linear_Layout);
            events = new ArrayList<>();
            adapter = new RVPlayerEventList(context, events);
            if (playerEventList != null)
                playerEventList.setAdapter(adapter);
        }
    }
}
