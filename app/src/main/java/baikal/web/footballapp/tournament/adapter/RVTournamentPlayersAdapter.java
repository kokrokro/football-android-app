package baikal.web.footballapp.tournament.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import baikal.web.footballapp.App;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SetImage;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.PersonStats;
import baikal.web.footballapp.model.Player;
import baikal.web.footballapp.tournament.PlayerComparator;
import baikal.web.footballapp.viewmodel.PersonViewModel;


public class RVTournamentPlayersAdapter extends RecyclerView.Adapter<RVTournamentPlayersAdapter.ViewHolder>{
    private static final String TAG = "TournamentPlayersAdap";
    private final List<Player> players;
    private final HashMap<String, PersonStats> mapStats;
    private PersonViewModel personViewModel;

    public RVTournamentPlayersAdapter(List<Player> players, List<PersonStats> personStats, PersonViewModel personViewModel){
        this.players = players;
        this.mapStats = new HashMap<>();
        this.personViewModel = personViewModel;

        for (Player p: players)
            mapStats.put(p.getPerson(), new PersonStats());

        for (PersonStats p: personStats)
            if (mapStats.get(p.getPerson()) != null)
                mapStats.put(p.getPerson(), mapStats.get(p.getPerson()).addPersonStats(p));

        try {
            Collections.sort(players, new PlayerComparator());
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tournament_player, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Player player = players.get(position);

        if (player != null)
            holder.bindTo(player);
    }

    @Override
    public int getItemCount() {
        return players.size();
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

        void bindTo (Player player) {
            PersonStats personStat = mapStats.get(player.getPerson());
            Person person = personViewModel.getPersonById(player.getPerson(), (p) -> {
                textName.setText(p.getSurnameAndName());
                SetImage.setImage(App.getAppContext(), image, p.getPhoto());
            });

            textName.setText(person==null?"":person.getSurnameAndName());
            SetImage.setImage(App.getAppContext(), image, person==null?"":person.getPhoto());

            textPoint1.setText(personStat==null?"":String.valueOf(personStat.getMatches()));
            textPoint2.setText(personStat==null?"":String.valueOf(personStat.getGoals()));
            textPoint3.setText(personStat==null?"":String.valueOf(personStat.getYellowCards()));
            textPoint4.setText(personStat==null?"":String.valueOf(personStat.getRedCards()));
        }
    }
    public void dataChanged(List<Player> allPlayers1){
        players.clear();
        players.addAll(allPlayers1);
        notifyDataSetChanged();
    }
}
