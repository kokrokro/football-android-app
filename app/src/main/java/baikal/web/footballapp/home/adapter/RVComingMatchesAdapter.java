package baikal.web.footballapp.home.adapter;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
import baikal.web.footballapp.model.ActiveMatch;
import baikal.web.footballapp.model.Club;
import baikal.web.footballapp.model.Player;
import baikal.web.footballapp.model.Team;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import q.rorbin.badgeview.QBadgeView;

public class RVComingMatchesAdapter extends RecyclerView.Adapter<RVComingMatchesAdapter.ViewHolder> {
    private final Logger log = LoggerFactory.getLogger(PersonalActivity.class);
    private final PersonalActivity activity;
    private final List<ActiveMatch> matches;

    public RVComingMatchesAdapter(Activity activity, List<ActiveMatch> matches) {
        this.activity = (PersonalActivity) activity;
        this.matches = matches;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timetable_fragment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        log.error("++++++++++++++++++++++++++++++++++++++++++");
        ActiveMatch match = matches.get(position);
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
        Team team1 = match.getTeamOne();
        Team team2 = match.getTeamTwo();

        SetImage setImage = new SetImage();
        str = team1.getName();
        holder.textCommandTitle1.setText(str);
        for (Club club : PersonalActivity.allClubs) {
            if (club.getId().equals(team1.getClub())) {
                setImage.setImage(activity, holder.imgCommandLogo1, club.getLogo());
            }
        }
        Log.d("RVUpcomingMatches", str);
        Log.d("RVUpcomingMatches", "team1.getPlayers(): "+team1.getPlayers().size());

        for (Player player : team1.getPlayers()) {
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

        str = team2.getName();
        holder.textCommandTitle2.setText(str);
        for (Club club : PersonalActivity.allClubs) {
            if (club.getId().equals(team2.getClub())) {
                setImage.setImage(activity, holder.imgCommandLogo2, club.getLogo());
            }
        }
        for (Player player : team2.getPlayers()) {
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
            List<ActiveMatch> list = new ArrayList<>(matches);
            list.remove(match);
            for (ActiveMatch match1 : list) {
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
            textScore = item.findViewById(R.id.timetableGameScore);
            textPenalty = item.findViewById(R.id.timetablePenalty);
            layout = item.findViewById(R.id.timetableLayout);
            line = item.findViewById(R.id.timetableLine);
        }
    }

    public void dataChanged(List<ActiveMatch> allPlayers1) {
        log.error("*******************************************");
        matches.clear();
        matches.addAll(allPlayers1);
        notifyDataSetChanged();
    }
}
