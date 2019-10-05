package baikal.web.footballapp.tournament.adapter;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import baikal.web.footballapp.DateToString;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Club;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.Match;
import baikal.web.footballapp.model.Player;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.tournament.activity.CommandInfoActivity;
import baikal.web.footballapp.tournament.activity.CommandMatchFragment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import q.rorbin.badgeview.QBadgeView;

import static baikal.web.footballapp.Controller.BASE_URL;

public class RVCommandMatchAdapter extends RecyclerView.Adapter<RVCommandMatchAdapter.ViewHolder> {
    Logger log = LoggerFactory.getLogger(CommandInfoActivity.class);
    private final CommandMatchFragment context;
    private final CommandInfoActivity activity;
    private final List<Match> matches;

    public RVCommandMatchAdapter(Activity activity, CommandMatchFragment context, List<Match> matches) {
        this.activity = (CommandInfoActivity) activity;
        this.context = context;
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
        Match match = matches.get(position);
        String str = match.getDate();
        DateToString dateToString = new DateToString();
        holder.textDate.setText(dateToString.ChangeDate(str));
        try{
            holder.textTime.setText(TimeToString(str));
        }catch (Exception e){
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
        int score1 = 0;
        int score2 = 0;
        Team team1 = null;
        Team team2 = null;
        List<String> teamPlayers1 = new ArrayList<>();
        List<String> teamPlayers2 = new ArrayList<>();
        League league = null;
        for (League league1 : PersonalActivity.tournaments) {
            if (league1.getId().equals(match.getLeague())) {
                league = league1;
            }
        }

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.optionalCircleCrop();
        requestOptions.format(DecodeFormat.PREFER_ARGB_8888);
        requestOptions.error(R.drawable.ic_logo2);
        requestOptions.override(500, 500);
        requestOptions.priority(Priority.HIGH);

        for (Team team : league.getTeams()) {
            if (team.getId().equals(match.getTeamOne())) {
                str = team.getName();
                team1 = team;
                holder.textCommandTitle1.setText(str);
                for (Club club : PersonalActivity.allClubs) {
                    if (club.getId().equals(team.getClub())) {
                        String uriPic = BASE_URL;
                        try {
                            uriPic += "/" + club.getLogo();
                            URL url = new URL(uriPic);
                            Glide.with(activity)
                                    .asBitmap()
                                    .load(url)
                                    .apply(requestOptions)
                                    .into(holder.imgCommandLogo1);
                        } catch (MalformedURLException e) {
                            Glide.with(activity)
                                    .asBitmap()
                                    .load(R.drawable.ic_logo2)
                                    .apply(requestOptions)
                                    .into(holder.imgCommandLogo1);
                        }
//                        break;
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
//                        break;
                    }
//                    if (player.getGoals()!=0){
                    score1 += player.getGoals();
//                    }
                }
            }
            if (team.getId().equals(match.getTeamTwo())) {
                str = team.getName();
                team2 = team;
                holder.textCommandTitle2.setText(str);
                for (Club club : PersonalActivity.allClubs) {
                    if (club.getId().equals(team.getClub())) {
                        String uriPic = BASE_URL;
                        try {
                            uriPic += "/" + club.getLogo();
                            URL url = new URL(uriPic);
                            Glide.with(activity)
                                    .asBitmap()
                                    .load(url)
                                    .apply(requestOptions)
                                    .into(holder.imgCommandLogo2);
                        } catch (MalformedURLException e) {
                            Glide.with(activity)
                                    .asBitmap()
                                    .load(R.drawable.ic_logo2)
                                    .apply(requestOptions)
                                    .into(holder.imgCommandLogo1);
                        }
//                        break;
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
                    }
                    score2 += player.getGoals();
                }
            }
        }
        try{
            str = match.getScore();
            if (str.equals("")){
                str = "-";
            }
        }catch (NullPointerException e){
            str = "-";
        }
        holder.textScore.setText(str);
        if (!str.equals("-")) {
            List<Match> list = new ArrayList<>(matches);
            list.remove(match);
            for (Match match1 : list) {
                try{
                    str = match1.getScore();
                    if ( !str.equals("")
                            && match1.getTeamOne().equals(match.getTeamOne())
                            && match1.getTeamOne().equals(match.getTeamTwo())){
                        str = match1.getScore();
                        holder.textLastScore.setVisibility(View.VISIBLE);
                        holder.textLastScore.setText(str);
                    }
                    if ( !str.equals("")
                            && match1.getTeamOne().equals(match.getTeamTwo())
                            && match1.getTeamOne().equals(match.getTeamOne())){
                        str = match1.getScore();
                        String[] strArray = str.split(":");
                        str = strArray[1]+":"+ strArray[0];
                        holder.textLastScore.setVisibility(View.VISIBLE);
                        holder.textLastScore.setText(str);
                    }
                }catch (Exception e){}

            }
        }



        try{
            str = match.getPenalty();
            if (!str.equals("")) {
                holder.textPenalty.setText(str);
                holder.textPenalty.setVisibility(View.VISIBLE);
            }
        }catch (NullPointerException e){}

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
        final TextView textScore;
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
            line = item.findViewById(R.id.timetableLine);
        }
    }


    private String TimeToString(String str) {
        String dateDOB = "";
        try {
            SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
            Date date1;
            date1 = mdformat.parse(str);

            Calendar cal = Calendar.getInstance();
            cal.setTime(date1);
            if (String.valueOf(cal.get(Calendar.HOUR)).length() == 1) {
                dateDOB += "0" + cal.get(Calendar.HOUR) + ":";
            } else {
                dateDOB += cal.get(Calendar.HOUR) + ":";
            }
            if ((String.valueOf(cal.get(Calendar.MINUTE) + 1).length() == 1)) {
                dateDOB += "0" + (cal.get(Calendar.MINUTE) + 1);
            } else {
                dateDOB += String.valueOf(cal.get(Calendar.MINUTE) + 1);
            }
        } catch (ParseException e) {
            try {
                SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.US);
                Date date1;
                date1 = mdformat.parse(str);

                Calendar cal = Calendar.getInstance();
                cal.setTime(date1);
                if (String.valueOf(cal.get(Calendar.HOUR)).length() == 1) {
                    dateDOB += "0" + cal.get(Calendar.HOUR) + ":";
                } else {
                    dateDOB += cal.get(Calendar.HOUR) + ":";
                }
                if ((String.valueOf(cal.get(Calendar.MINUTE) + 1).length() == 1)) {
                    dateDOB += "0" + (cal.get(Calendar.MINUTE) + 1);
                } else {
                    dateDOB += String.valueOf(cal.get(Calendar.MINUTE) + 1);
                }
            } catch (ParseException t) {
                t.printStackTrace();
            }
        }
        return dateDOB;
    }
}
