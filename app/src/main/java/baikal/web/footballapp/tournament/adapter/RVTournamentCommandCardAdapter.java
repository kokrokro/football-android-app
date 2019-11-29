package baikal.web.footballapp.tournament.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.LeagueInfo;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.tournament.activity.CommandInfoActivity;
import baikal.web.footballapp.tournament.activity.TournamentCommandFragment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;

public class RVTournamentCommandCardAdapter extends RecyclerView.Adapter<RVTournamentCommandCardAdapter.ViewHolder> {
    private final TournamentCommandFragment context;
    private final PersonalActivity activity;
    //    CommandInfoFragment commandInfoFragment = new CommandInfoFragment();
    Logger log = LoggerFactory.getLogger(PersonalActivity.class);

    private final List<Team> teams;
    private final String group;
    private final League leagueInfo;
    public RVTournamentCommandCardAdapter(Activity activity, TournamentCommandFragment context, List<Team> teams, String group,
                                          League leagueInfo) {
        this.activity = (PersonalActivity) activity;
        this.context = context;
        this.teams = teams;
        this.group = group;
        this.leagueInfo = leagueInfo;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.tournament_info_command, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        String str = String.valueOf(position+1);
        holder.textNum.setText(str);
        str = teams.get(position).getName();
        holder.btnTitle.setText(str);
        holder.btnTitle.setOnClickListener(v -> {
            Intent intent = new Intent(activity, CommandInfoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("TOURNAMENTMATCHCOMMANDINFO", teams.get(position));
            bundle.putSerializable("TOURNAMENTMATCHCOMMANDINFOMATCHES", (Serializable) leagueInfo.getMatches());
            intent.putExtras( bundle);
            activity.startActivity(intent);

        });
        int count;
        count = teams.get(position).getDraws() + teams.get(position).getWins() + teams.get(position).getLosses();
        str = String.valueOf(count);
        holder.textGame.setText(str);
        count = teams.get(position).getGoals() - teams.get(position).getGoalsReceived();
        str = String.valueOf(count);
        holder.textDifference.setText(str);
        str = String.valueOf(teams.get(position).getGroupScore());
        holder.textPoint.setText(str);
    }

    @Override
    public int getItemCount() {
        return teams.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textNum;
        //        TextView textTitle;
        final TextView btnTitle;
        final TextView textGame;
        final TextView textDifference;
        final TextView textPoint;
        ViewHolder(View item) {
            super(item);
            textNum = item.findViewById(R.id.commandNum);
            btnTitle = item.findViewById(R.id.commandTitle);
//            textTitle = (TextView) item.findViewById(R.id.commandTitle);
            textGame = item.findViewById(R.id.commandScoreGame);
            textDifference = item.findViewById(R.id.commandScoreDifference);
            textPoint = item.findViewById(R.id.commandScorePoint);
        }
    }
}
