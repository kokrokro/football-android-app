package baikal.web.footballapp.tournament.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import baikal.web.footballapp.MankindKeeper;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.PersonStats;
import baikal.web.footballapp.model.Player;


public class RVTournamentPlayersAdapter extends RecyclerView.Adapter<RVTournamentPlayersAdapter.ViewHolder>{
    private static final String TAG = "TournamentPlayersAdap";
    private final List<Player> players;
    private final List<PersonStats> personStats;

    public RVTournamentPlayersAdapter(List<Player> players, List<PersonStats> personStats){
        this.players = players;
        this.personStats = personStats;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tournament_player, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Person player = null;
        try {
            Log.d(TAG, personStats.get(position).getPerson());
//            Log.d(TAG, String.valueOf(MankindKeeper.getInstance().allPerson.containsKey(players.get(position).getId())));
            player = MankindKeeper.getInstance().getPersonById(personStats.get(position).getPerson());
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        String str;

        try {
            if (player==null){
                player = new Person();
                player.setName("Удален");
                player.setSurname("");
                player.setLastname("");
            }
            str = player.getSurnameAndName();
            holder.textName.setText(str);
            int count = personStats.get(position).getMatches();
            str = String.valueOf(count);
            holder.textPoint1.setText(str);
            holder.textPoint2.setText(personStats.get(position).getGoals());
            count = personStats.get(position).getYellowCards();// % league.getYellowCardsToDisqual();
            str = String.valueOf(count);
            holder.textPoint3.setText(str);
            count = personStats.get(position).getRedCards();// + personStats.get(position).getYellowCards() / league.getYellowCardsToDisqual();
            str = String.valueOf(count);
            holder.textPoint4.setText(str);
        } catch (Exception ignored) { }
    }

    @Override
    public int getItemCount() {
        return personStats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        final TextView textName;
        final TextView textPoint1;
        final TextView textPoint2;
        final TextView textPoint3;
        final TextView textPoint4;
        final ImageView image;

        ViewHolder(View item) {
            super(item);
            textName = item.findViewById(R.id.tournamentPlayer);
            textPoint1 = item.findViewById(R.id.tournamentPlayerPoint1);
            textPoint2 = item.findViewById(R.id.tournamentPlayerPoint2);
            textPoint3 = item.findViewById(R.id.tournamentPlayerPoint3);
            textPoint4 = item.findViewById(R.id.tournamentPlayerPoint4);
            image = item.findViewById(R.id.tournamentPlayerCommandLogo);
        }
    }
    public void dataChanged(List<Player> allPlayers1){
        players.clear();
        players.addAll(allPlayers1);
        notifyDataSetChanged();
    }
}
