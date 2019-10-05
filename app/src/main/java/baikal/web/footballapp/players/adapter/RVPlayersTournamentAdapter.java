package baikal.web.footballapp.players.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import baikal.web.footballapp.R;
import baikal.web.footballapp.model.PastLeague;
import baikal.web.footballapp.players.activity.Player;

import java.util.List;


public class RVPlayersTournamentAdapter extends RecyclerView.Adapter<RVPlayersTournamentAdapter.ViewHolder>{
    private final Player context;
    private final List<PastLeague> pastLeagues;
    public RVPlayersTournamentAdapter(Player context, List<PastLeague> pastLeagues){
        this.context = context;
        this.pastLeagues = pastLeagues;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_tournament, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String str;
        if (pastLeagues.get(position).getPlace().equals("0")){
            str = pastLeagues.get(position).getTourney() + ". " + pastLeagues.get(position).getName() + ". Команда: "
                    + pastLeagues.get(position).getTeamName();
        }
        else {
            str = pastLeagues.get(position).getTourney() + ". " + pastLeagues.get(position).getName() + ". Команда: "
                    + pastLeagues.get(position).getTeamName() + ". " + pastLeagues.get(position).getPlace() + " место";
        }

        holder.textTournament.setText(str);
        if (position==(pastLeagues.size()-1)){
            holder.view.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return pastLeagues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View view;
        final TextView textTournament;
        public ViewHolder(View item) {
            super(item);
            textTournament = item.findViewById(R.id.tournamentPlayersTitle);
            view = item.findViewById(R.id.playerTournamentLine);
        }
    }
}
