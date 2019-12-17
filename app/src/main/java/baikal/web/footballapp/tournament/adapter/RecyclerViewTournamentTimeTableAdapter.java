package baikal.web.footballapp.tournament.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import baikal.web.footballapp.DateToString;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.Match;
import baikal.web.footballapp.model.PersonStats;
import baikal.web.footballapp.model.PersonStatus;
import baikal.web.footballapp.model.Stadium;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.tournament.activity.ShowProtocol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import baikal.web.footballapp.tournament.activity.TournamentPage;
import baikal.web.footballapp.tournament.activity.TournamentsFragment;
import q.rorbin.badgeview.QBadgeView;

public class RecyclerViewTournamentTimeTableAdapter extends RecyclerView.Adapter<RecyclerViewTournamentTimeTableAdapter.ViewHolder> {
    Logger log = LoggerFactory.getLogger(PersonalActivity.class);

    private final PersonalActivity activity;
    private final List<Match> matches;
    private TournamentsFragment tournamentsFragment;

    public RecyclerViewTournamentTimeTableAdapter(Activity activity, League league, TournamentsFragment tournamentsFragment) {
        this.activity = (PersonalActivity) activity;
        this.matches = league.getMatches();
        this.tournamentsFragment = tournamentsFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timetable_fragment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        new QBadgeView(activity).bindTarget(holder.imgCommandLogo1).setBadgeBackground(activity.getDrawable(R.drawable.ic_circle)).setBadgeNumber(3);
        Match match = matches.get(position);
        String str;
        str = match.getDate();
        DateToString dateToString = new DateToString();
        holder.textDate.setText(dateToString.ChangeDate(str));
        try {
            holder.textTime.setText(dateToString.ChangeTime(str));
        } catch (NullPointerException e) {
            holder.textTime.setText(str);
        }
        str = match.getPlace();
        if(str!=null){
            for(Stadium stadium : tournamentsFragment.allStadiums){
                if(stadium.get_id().equals(str)){
                    holder.textStadium.setText(stadium.getName());
                    break;
                }
            }
        }
        else {
            holder.textStadium.setText("Не назначен");
        }


        str = match.getTour();
        holder.textTour.setText(str);
//        int score1 = 0;
//        int score2 = 0;
        String team1 = match.getTeamOne();
        String team2 = match.getTeamTwo();
        Team teamOne = null, teamTwo = null;
        try{
            for(Team team: tournamentsFragment.allTeams){
                if(team1.equals(team.getId())){
                    teamOne = team;
                    continue;
                }
                if(team2.equals(team.getId())){
                    teamTwo = team;
                }
            }
        }catch (NullPointerException ignored){

        }
        int i = 0;
        for(PersonStatus personStatus : TournamentPage.personStatus){
            if(i==2) break;
            if(personStatus.getTeam().equals(team1) && personStatus.getActiveDisquals()>0){
                i++;
                new QBadgeView(activity)
                        .bindTarget(holder.imgCommandLogo1)
                        .setBadgeBackground(activity.getDrawable(R.drawable.ic_circle))
                        .setBadgeTextColor(activity.getResources().getColor(R.color.colorBadge))
                        .setBadgeTextSize(5, true)
                        .setBadgePadding(5, true)
                        .setBadgeGravity(Gravity.END | Gravity.BOTTOM)
                        .setGravityOffset(-3, 1, true)
                        .setBadgeNumber(3);
            }
            else if(personStatus.getTeam().equals(team2) && personStatus.getActiveDisquals()>0){
                i++;
                new QBadgeView(activity)
                        .bindTarget(holder.imgCommandLogo2)
                        .setBadgeBackground(activity.getDrawable(R.drawable.ic_circle))
                        .setBadgeTextColor(activity.getResources().getColor(R.color.colorBadge))
                        .setBadgeTextSize(5, true)
                        .setBadgePadding(5, true)
                        .setBadgeGravity(Gravity.END | Gravity.BOTTOM)
                        .setGravityOffset(-3, 1, true)
                        .setBadgeNumber(3);
            }
        }

        if( teamOne!=null){
            holder.textCommandTitle1.setText(teamOne.getName());

        }
        else {
            holder.textCommandTitle1.setText("Неизвестно");
        }
        if( teamTwo!=null){
            holder.textCommandTitle2.setText(teamTwo.getName());
        }
        else {
            holder.textCommandTitle2.setText("Неизвестно");
        }

        try {
            str = match.getScore();
            if (str.equals("")) {
                str = "-";
            }
        } catch (NullPointerException e) {
            str = "-";
        }
        holder.textScore.setText(str);

        if (!str.equals("-")) {
            List<Match> list = new ArrayList<>(matches);
            list.remove(match);
            for (Match match1 : list) {
                try {
                    str = match1.getScore();
                    if (!str.equals("")
                            && match1.getTeamOne().equals(match.getTeamOne())
                            && match1.getTeamOne().equals(match.getTeamTwo())) {
                        str = match1.getScore();
                        holder.textLastScore.setVisibility(View.VISIBLE);
                        holder.textLastScore.setText(str);
                    }
                    if (!str.equals("")
                            && match1.getTeamOne().equals(match.getTeamTwo())
                            && match1.getTeamOne().equals(match.getTeamOne())) {
                        str = match1.getScore();
                        String[] strArray = str.split(":");
                        str = strArray[1] + ":" + strArray[0];
                        holder.textLastScore.setVisibility(View.VISIBLE);
                        holder.textLastScore.setText(str);
                    }
                } catch (Exception ignored) {
                }

            }
        }
        if (match.getPlayed()){
            holder.layout.setBackgroundResource(R.color.colorBadgeScale);
            holder.layout.setOnClickListener(v -> {
                Intent intent = new Intent(activity, ShowProtocol.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("SHOWPROTOCOL", match);
                intent.putExtras(bundle);
                activity.startActivity(intent);
            });
        }
        try {
            str = match.getPenalty();
            if (!str.equals("")) {
                holder.textPenalty.setText(str);
                holder.textPenalty.setVisibility(View.VISIBLE);
            }
        } catch (NullPointerException ignored) {
        }
        if (position == (matches.size() - 1)) {
            holder.line.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return matches.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textCommandTitle1;
        final TextView textCommandTitle2;
        final ImageView imgCommandLogo1;
        final ImageView imgCommandLogo2;
        final TextView textStadium;
        final TextView textDate;
        final TextView textTime;
        final TextView textTour;
        final TextView textLastScore;
        final TextView textPenalty;
        //        TextView textTournamentTitle;
        final TextView textScore;
        final RelativeLayout layout;
        final View line;

        ViewHolder(View item) {
            super(item);
            textCommandTitle1 = item.findViewById(R.id.timetableCommandTitle1);
            textCommandTitle2 = item.findViewById(R.id.timetableCommandTitle2);
            imgCommandLogo1 = item.findViewById(R.id.timetableCommandLogo1);
            imgCommandLogo2 = item.findViewById(R.id.timetableCommandLogo2);
            textStadium = item.findViewById(R.id.timetableStadium);
            textDate = item.findViewById(R.id.timetableDate);
            textTime = item.findViewById(R.id.timetableTime);
            textTour = item.findViewById(R.id.timetableTour);
            textLastScore = item.findViewById(R.id.timetableLastScore);
//            textTournamentTitle = (TextView) item.findViewById(R.id.timetableLeagueTitle);
            textScore = item.findViewById(R.id.timetableGameScore);
            textPenalty = item.findViewById(R.id.timetablePenalty);
            layout = item.findViewById(R.id.timetableLayout);
            line = item.findViewById(R.id.timetableLine);
        }
    }
}
