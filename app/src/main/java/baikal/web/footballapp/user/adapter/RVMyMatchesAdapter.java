package baikal.web.footballapp.user.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import baikal.web.footballapp.DateToString;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.SetImage;
import baikal.web.footballapp.model.Club;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.Match;
import baikal.web.footballapp.model.Player;
import baikal.web.footballapp.model.Referee;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.user.activity.MyMatches;
import baikal.web.footballapp.user.activity.PlayerAddToTeam;
import baikal.web.footballapp.user.activity.ProtocolEdit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import q.rorbin.badgeview.QBadgeView;

public class RVMyMatchesAdapter extends RecyclerView.Adapter<RVMyMatchesAdapter.ViewHolder>{
    private final MyMatches context;
    private final List<Match> matches;
    Logger log = LoggerFactory.getLogger(PlayerAddToTeam.class);
    private final PersonalActivity activity;
    public RVMyMatchesAdapter(Activity activity, MyMatches context, List<Match> matches){
        this.context =  context;
        this.activity = (PersonalActivity) activity;
        this.matches = matches;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.match, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try{
        final Match match = matches.get(position);

        boolean check = false;

        String str;
        str = match.getDate();
        DateToString dateToString = new DateToString();
        holder.textDate.setText(dateToString.ChangeDate(str));
        try{
            holder.textTime.setText(TimeToString(str));
        }catch (NullPointerException e){
            holder.textTime.setText(str);
        }
        str = match.getPlace();
        try{
            String[] stadium;
            stadium = str.split(":", 1);
            holder.textStadium.setText(stadium[0]);
        }catch (NullPointerException e){
            holder.textStadium.setText(str);
        }

        str = match.getTour();
        holder.textTour.setText(str);
        int score1 = 0;
        int score2 = 0;
        List<String> teamPlayers1 = new ArrayList<>();
        List<String> teamPlayers2 = new ArrayList<>();

        League league = null;
        for (League league1 : PersonalActivity.tournaments){
            if (league1.getId().equals(match.getLeague())){
                league = league1;
                break;
            }
        }

            SetImage setImage = new SetImage();
        Team team1 = null;
        Team team2 = null;
        String club1 = "";
        String club2 = "";
        for (Team team: league.getTeams()){
            if (team.getId().equals(match.getTeamOne())){
                team1 = team;
                str = team.getName();
                holder.textCommand1.setText(str);
                for (Club club : PersonalActivity.allClubs){
                    if (club.getId().equals(team.getClub())){
                        setImage.setImage(activity, holder.image1, club.getLogo());
                    }
                }
                for (Player player: team.getPlayers()){
                    teamPlayers1.add(player.getId());
                    if (player.getActiveDisquals()!=0 ){
                        new QBadgeView(activity)
                                .bindTarget(holder.image1)
                                .setBadgeBackground(activity.getDrawable(R.drawable.ic_circle))
                                .setBadgeTextColor(activity.getResources().getColor(R.color.colorBadge))
                                .setBadgeTextSize(5, true)
                                .setBadgePadding(5,true)
                                .setBadgeGravity(Gravity.END|Gravity.BOTTOM)
                                .setGravityOffset(-3, 1, true)
                                .setBadgeNumber(3);
                    }
//                    score1+=player.getGoals();
                }
            }
            if (team.getId().equals(match.getTeamTwo())){
                team2 = team;
                str = team.getName();
                holder.textCommand2.setText(str);
                for (Club club : PersonalActivity.allClubs){
                    if (club.getId().equals(team.getClub())){
                        setImage.setImage(activity, holder.image2, club.getLogo());
                    }
                }
                for (Player player: team.getPlayers()){
                    teamPlayers2.add(player.getId());
                    if (player.getActiveDisquals()!=0 ){
                        new QBadgeView(activity)
                                .bindTarget(holder.image2)
                                .setBadgeBackground(activity.getDrawable(R.drawable.ic_circle))
                                .setBadgeTextColor(activity.getResources().getColor(R.color.colorBadge))
                                .setBadgeTextSize(5, true)
                                .setBadgePadding(5,true)
                                .setBadgeGravity(Gravity.END|Gravity.BOTTOM)
                                .setGravityOffset(-3, 1, true)
                                .setBadgeNumber(3);
                    }
                }
            }
        }
        try{
            if (!match.getScore().equals("")){
                str = match.getScore();
            }
            else {
                str = "-";
            }
        }catch (NullPointerException e){
            str = "-";
        }

        holder.textScore.setText(str);
        try{
            str = match.getPenalty();
            if (!str.equals("")){
                holder.textPenalty.setText(str);
                holder.textPenalty.setVisibility(View.VISIBLE);
            }
        }catch (NullPointerException e){}

        try{
            List<Referee> referees = new ArrayList<>(match.getReferees());
            for (Referee referee : referees){
                if (referee.getType().equals("3 судья") && referee.getPerson().equals(SaveSharedPreference.getObject().getUser().getId())){
                    check = true;
                    break;
                }
            }
        }catch (NullPointerException e){
            check=false;
        }

        if (check){
            holder.button.setVisibility(View.VISIBLE);
            final Team finalTeam = team1;
            final Team finalTeam1 = team2;
            final String finalClub = club1;
            final String finalClub1 = club2;
            holder.button.setOnClickListener(v -> {
                Intent intent = new Intent(activity, ProtocolEdit.class);
                Bundle bundle = new Bundle();
                int count = MyMatches.matches.indexOf(match);
                bundle.putSerializable("PROTOCOLMATCH", match);
                bundle.putSerializable("PROTOCOLTEAM1", finalTeam);
                bundle.putSerializable("PROTOCOLTEAM2", finalTeam1);
                bundle.putString("PROTOCOLCLUB1", finalClub);
                bundle.putString("PROTOCOLCLUB2", finalClub1);
                bundle.putInt("MATCHPOSITION", count);
                intent.putExtras(bundle);
                context.startActivity(intent);
            });
        }
        if (position==(matches.size()-1)){
            holder.line.setVisibility(View.INVISIBLE);
        }

        }catch (NullPointerException r){}
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
    private String TimeToString(String str)  {
        String dateDOB = "";
        try {
            SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
            Date date1;
            date1 = mdformat.parse(str);

            Calendar cal = Calendar.getInstance();
            cal.setTime(date1);
            if (String.valueOf(cal.get(Calendar.HOUR)).length()==1){
                dateDOB += "0" + cal.get(Calendar.HOUR) + ":";
            }
            else {
                dateDOB += cal.get(Calendar.HOUR) + ":";
            }
            if ((String.valueOf(cal.get(Calendar.MINUTE) + 1).length()==1)){
                dateDOB += "0" + (cal.get(Calendar.MINUTE) + 1);
            }
            else{
                dateDOB += String.valueOf(cal.get(Calendar.MINUTE) + 1);
            }
        } catch (ParseException e) {
            try{
                SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.US);
                Date date1;
                date1 = mdformat.parse(str);

                Calendar cal = Calendar.getInstance();
                cal.setTime(date1);
                if (String.valueOf(cal.get(Calendar.HOUR)).length()==1){
                    dateDOB += "0" + cal.get(Calendar.HOUR) + ":";
                }
                else {
                    dateDOB += cal.get(Calendar.HOUR) + ":";
                }
                if ((String.valueOf(cal.get(Calendar.MINUTE) + 1).length()==1)){
                    dateDOB += "0" + (cal.get(Calendar.MINUTE) + 1);
                }
                else{
                    dateDOB += String.valueOf(cal.get(Calendar.MINUTE) + 1);
                }
            }
            catch (ParseException t) {t.printStackTrace();}
        }
        return dateDOB;
    }

    public void dataChanged(List<Match> allPlayers1) {
        matches.clear();
        matches.addAll(allPlayers1);
        notifyDataSetChanged();
    }
}
