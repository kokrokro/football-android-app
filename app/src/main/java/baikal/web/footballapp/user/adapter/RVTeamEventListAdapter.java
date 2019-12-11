package baikal.web.footballapp.user.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.MankindKeeper;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.Player;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RVTeamEventListAdapter extends RecyclerView.Adapter<RVTeamEventListAdapter.ViewHolder> {
    private List<Player> players;
    private List<Person> persons;
    private TeamEventListListener listener;

    public interface TeamEventListListener {
        void onClick (Person person);
    }

    public RVTeamEventListAdapter(List<Player> players, TeamEventListListener listener)
    {
        this.listener = listener;
        this.players = players;

        persons = new ArrayList<>();
        for (int i=0; i<players.size(); i++)
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
        }

        if (persons.get(position) != null)
            holder.linearLayout.setOnClickListener(v -> listener.onClick(persons.get(position)));

    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder  {
        final TextView playerName;
        final TextView playerNumber;
        final RecyclerView playerEventList;
        final RVPlayerEventList adapter;
        final LinearLayout linearLayout;

        ViewHolder(View itemView) {
            super(itemView);

            playerName = itemView.findViewById(R.id.PLC_playerName);
            playerNumber = itemView.findViewById(R.id.PLC_playerNumber);
            playerEventList = itemView.findViewById(R.id.PLC_playerEvents);
            linearLayout = itemView.findViewById(R.id.plc_Linear_Layout);

            adapter = new RVPlayerEventList();
            if (playerEventList != null)
                playerEventList.setAdapter(adapter);
        }
    }
}
