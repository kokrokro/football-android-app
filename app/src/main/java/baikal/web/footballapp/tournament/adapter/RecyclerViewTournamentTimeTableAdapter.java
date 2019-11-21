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
import baikal.web.footballapp.SetImage;
import baikal.web.footballapp.model.Club;
import baikal.web.footballapp.model.LeagueInfo;
import baikal.web.footballapp.model.Match;
import baikal.web.footballapp.model.Player;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.tournament.activity.ShowProtocol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import q.rorbin.badgeview.QBadgeView;

public class RecyclerViewTournamentTimeTableAdapter extends RecyclerView.Adapter<RecyclerViewTournamentTimeTableAdapter.ViewHolder> {
    Logger log = LoggerFactory.getLogger(PersonalActivity.class);

    private final PersonalActivity activity;
    private final List<Match> matches;
    private final LeagueInfo league;

    public RecyclerViewTournamentTimeTableAdapter(Activity activity, List<Match> matches, LeagueInfo league) {
        this.activity = (PersonalActivity) activity;
        this.matches = matches;
        this.league = league;
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
        try {
            String[] stadium;
            stadium = str.split(":", 1);
            holder.textStadium.setText(stadium[0]);
        } catch (NullPointerException e) {
            holder.textStadium.setText(str);
        }

        str = match.getTour();
        holder.textTour.setText(str);
//        int score1 = 0;
//        int score2 = 0;
        Team team1 = null;
        Team team2 = null;
        List<String> teamPlayers1 = new ArrayList<>();
        List<String> teamPlayers2 = new ArrayList<>();

        SetImage setImage = new SetImage();
        for (Team team : league.getTeams()) {
            if (team.getId().equals(match.getTeamOne())) {
                str = team.getName();
                team1 = team;
                holder.textCommandTitle1.setText(str);
                for (Club club : PersonalActivity.allClubs) {
                    if (club.getId().equals(team.getClub())) {
                        setImage.setImage(activity, holder.imgCommandLogo1 ,club.getLogo());
                    }
                }
                for (Player player : team.getPlayers()) {
                    teamPlayers1.add(player.getId());
                    if (player.getActiveDisquals() != 0) {
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
                }
            }
            if (team.getId().equals(match.getTeamTwo())) {
                str = team.getName();
                team2 = team;
                holder.textCommandTitle2.setText(str);
                for (Club club : PersonalActivity.allClubs) {
                    if (club.getId().equals(team.getClub())) {
                        setImage.setImage(activity, holder.imgCommandLogo2 ,club.getLogo());
                    }
                }
                for (Player player : team.getPlayers()) {
                    teamPlayers2.add(player.getId());
                    if (player.getActiveDisquals() != 0) {
                        new QBadgeView(activity)
                                .bindTarget(holder.imgCommandLogo2)
                                .setBadgeBackground(activity.getDrawable(R.drawable.ic_circle))
                                .setBadgeTextColor(activity.getResources().getColor(R.color.colorBadge))
                                .setBadgeTextSize(5, true)
                                .setBadgePadding(5, true)
                                .setBadgeGravity(Gravity.END | Gravity.BOTTOM)
                                .setGravityOffset(-3, 1, true)
                                .setBadgeNumber(3);
//                        break;
                    }
//                    if (player.getGoals()!=0){
//                        score2+=player.getGoals();
//                    }
                }
//                break;
            }
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
                } catch (Exception e) {
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
        } catch (NullPointerException e) {
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
