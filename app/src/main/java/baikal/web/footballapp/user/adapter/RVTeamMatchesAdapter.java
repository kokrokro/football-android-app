package baikal.web.footballapp.user.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Match;
import baikal.web.footballapp.model.Team;

public class RVTeamMatchesAdapter extends RecyclerView.Adapter<RVTeamMatchesAdapter.ViewHolder> {
    final List<Match> matches;
    final Team team;
    final Listener listener;

    public RVTeamMatchesAdapter(List<Match> matches, Team team, Listener listener) {
        this.matches = matches;
        this.team = team;
        this.listener = listener;
    }
    public interface Listener{
        void onClick(Match match);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.match, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.layout.setOnClickListener(V->{
            listener.onClick(matches.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return matches.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textDate;
        final TextView textTime;
        final TextView textTour;
        final TextView textStadium;
        final TextView textScore;
        final TextView textCommand1;
        final TextView textCommand2;
        final ImageView image1;
        final ImageView image2;
        final Button button;
        final RelativeLayout layout;
        final View line;
        final TextView textPenalty;
        ViewHolder(View item) {
            super(item);
            button = item.findViewById(R.id.myMatchEdit);
            textDate = item.findViewById(R.id.myMatchDate);
            textTime = item.findViewById(R.id.myMatchTime);
            textTour = item.findViewById(R.id.myMatchLeague);
            textStadium = item.findViewById(R.id.myMatchStadium);
            textScore = item.findViewById(R.id.myMatchScore);
            textCommand1 = item.findViewById(R.id.myMatchCommandTitle1);
            textCommand2 = item.findViewById(R.id.myMatchCommandTitle2);
            image1 = item.findViewById(R.id.myMatchCommandLogo1);
            image2 = item.findViewById(R.id.myMatchCommandLogo2);
            layout = item.findViewById(R.id.myMatchShowProtocol);
            line = item.findViewById(R.id.myMatchLine);
            textPenalty = item.findViewById(R.id.myMatchPenalty);
        }
    }
}
