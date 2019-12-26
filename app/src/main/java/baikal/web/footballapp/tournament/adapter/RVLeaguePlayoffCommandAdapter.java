package baikal.web.footballapp.tournament.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.List;

import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.model.TeamStats;
import baikal.web.footballapp.tournament.activity.CommandInfoActivity;

public class RVLeaguePlayoffCommandAdapter extends RecyclerView.Adapter<RVLeaguePlayoffCommandAdapter.ViewHolder> {
    private final PersonalActivity activity;
    //    CommandInfoFragment commandInfoFragment = new CommandInfoFragment();
//    private final Logger log = LoggerFactory.getLogger(PersonalActivity.class);

    private final List<Team> teams;
    private final League leagueInfo;
    private final List<TeamStats> teamStats;

    public RVLeaguePlayoffCommandAdapter(Activity activity, List<Team> teams,
                                         League leagueInfo, List<TeamStats> teamStats) {
        this.activity = (PersonalActivity) activity;
        this.teams = teams;
        this.leagueInfo = leagueInfo;
        this.teamStats = teamStats;
    }

    @NonNull
    @Override
    public RVLeaguePlayoffCommandAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.tournament_info_command, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVLeaguePlayoffCommandAdapter.ViewHolder holder, final int position) {
//        String str = String.valueOf(position+1);
        String str;
//        str = String.valueOf(teams.get(position).getGroupScore());
        try {
//            str = String.valueOf(teams.get(position).getPlace());
            str = String.valueOf(teams.get(position).getPlayoffPlace());
            if (str.equals("null")) {
                str = "-";
                try{
                    Boolean check = teams.get(position).getMadeToPlayoff();
                    if (!check ){
                        holder.layout.setBackgroundResource(R.color.colorBadgeScale);
                    }
                } catch (NullPointerException ignored){}



            }
        } catch (NullPointerException e) {
            str = "-";
//            holder.layout.setBackgroundResource(R.color.colorBadgeScale);
            try{
                Boolean check = teams.get(position).getMadeToPlayoff();
                if (!check){
                    holder.layout.setBackgroundResource(R.color.colorBadgeScale);
                }
            } catch (NullPointerException ignored){}
        }
        holder.textNum.setText(str);
//        holder.textTitle.setText("SomeTitle");
        str = teams.get(position).getName();
        holder.btnTitle.setText(str);
        holder.btnTitle.setOnClickListener(v -> {
            Intent intent = new Intent(activity, CommandInfoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("TOURNAMENTMATCHCOMMANDINFO", teams.get(position));
            bundle.putSerializable("TOURNAMENTMATCHCOMMANDINFOMATCHES", (Serializable) leagueInfo.getMatches());
            intent.putExtras(bundle);
            activity.startActivity(intent);

        });
        TeamStats teamStat = null;
        try {
            teamStat = teamStats.get(position);
        } catch (IndexOutOfBoundsException ignored){

        }
        try{
            holder.textGame.setText(String.valueOf(teamStat.getDraws()+teamStat.getDraws()+ teamStat.getLosses()));
            holder.textDifference.setText(String.valueOf(teamStat.getGoals()- teamStat.getGoalsReceived()));

        } catch (NullPointerException ignored){

        }


    }

    @Override
    public int getItemCount() {
        return teams.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final LinearLayout layout;
        final TextView textNum;
        //        TextView textTitle;
        final TextView btnTitle;
        final TextView textGame;
        final TextView textDifference;
        final TextView textPoint;

        ViewHolder(View item) {
            super(item);
            layout = item.findViewById(R.id.playoffLayout);
            textNum = item.findViewById(R.id.commandNum);
            btnTitle = item.findViewById(R.id.commandTitle);
//            textTitle = (TextView) item.findViewById(R.id.commandTitle);
            textGame = item.findViewById(R.id.commandScoreGame);
            textDifference = item.findViewById(R.id.commandScoreDifference);
            textPoint = item.findViewById(R.id.commandScorePoint);
        }
    }
}
