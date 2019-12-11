package baikal.web.footballapp.user.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import baikal.web.footballapp.DateToString;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.model.MatchPopulate;
import baikal.web.footballapp.model.Referee;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.user.activity.ConfirmProtocol;
import baikal.web.footballapp.user.activity.PlayerAddToTeam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RVMyMatchesAdapter extends RecyclerView.Adapter<RVMyMatchesAdapter.ViewHolder>{
    private final List<MatchPopulate> matches;
    Logger log = LoggerFactory.getLogger(PlayerAddToTeam.class);
    private final PersonalActivity activity;
    public RVMyMatchesAdapter(Activity activity, List<MatchPopulate> matches){
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
        final MatchPopulate match = matches.get(position);

        String str;
        str = match.getDate();
        DateToString dateToString = new DateToString();
        holder.textDate.setText(dateToString.ChangeDate(str));
        try{
            holder.textTime.setText(TimeToString(str));
        }catch (NullPointerException e){
            holder.textTime.setText(str);
        }

        try{
            str = match.getPlace().getName();
            holder.textStadium.setText(str);
        }catch (NullPointerException e){
            holder.textStadium.setText("Неизвестно");
        }
        try{
            str = match.getTour();
            holder.textTour.setText(str);
        }catch (NullPointerException ignored){}

//        League league = null;
//        for (League league1 : MankindKeeper.getInstance().allLeagues){
//            if (league1.getId().equals(match.getLeague())){
//                league = league1;
//                break;
//            }
//        }


        Team team1 = match.getTeamOne();
        Team team2 = match.getTeamTwo();
        try {
            holder.textCommand1.setText(team1.getName());
            holder.textCommand2.setText(team2.getName());
        }catch (NullPointerException ignored){

        }
        str = "Ваш статус: ";
        boolean isProtocolAvailable = false;
        for(Referee referee : match.getReferees()){
            if(referee.getPerson().equals(SaveSharedPreference.getObject().getUser().get_id())){
                switch (referee.getType()){
                    case "firstReferee":
                        str += "1 судья";
                        break;
                    case "secondReferee":
                        str += "2 судья";
                        break;
                    case "thirdReferee":
                        str += "3 судья";
                        isProtocolAvailable = true;
                        break;
                    case "timekeeper":
                        str += "хронометрист";
                        break;
                }
                break;
            }
        }

        holder.matchStatus.setText(str);
//        for (Team team: league.getTeams()){
//            if (team.getId().equals(match.getTeamOne())){
//                team1 = team;
//                str = team.getName();
//                holder.textCommand1.setText(str);
//                for (Club club : PersonalActivity.allClubs){
//                    if (club.getId().equals(team.getClub())){
//                        setImage.setImage(activity, holder.image1, club.getLogo());
//                    }
//                }
//                for (Player player: team.getPlayers()){
//                    teamPlayers1.add(player.getId());
//                    if (player.getActiveDisquals()!=0 ){
//                        new QBadgeView(activity)
//                                .bindTarget(holder.image1)
//                                .setBadgeBackground(activity.getDrawable(R.drawable.ic_circle))
//                                .setBadgeTextColor(activity.getResources().getColor(R.color.colorBadge))
//                                .setBadgeTextSize(5, true)
//                                .setBadgePadding(5,true)
//                                .setBadgeGravity(Gravity.END|Gravity.BOTTOM)
//                                .setGravityOffset(-3, 1, true)
//                                .setBadgeNumber(3);
//                    }
////                    score1+=player.getGoals();
//                }
//            }
//            if (team.getId().equals(match.getTeamTwo())){
//                team2 = team;
//                str = team.getName();
//                holder.textCommand2.setText(str);
//                for (Club club : PersonalActivity.allClubs){
//                    if (club.getId().equals(team.getClub())){
//                        setImage.setImage(activity, holder.image2, club.getLogo());
//                    }
//                }
//                for (Player player: team.getPlayers()){
//                    teamPlayers2.add(player.getId());
//                    if (player.getActiveDisquals()!=0 ){
//                        new QBadgeView(activity)
//                                .bindTarget(holder.image2)
//                                .setBadgeBackground(activity.getDrawable(R.drawable.ic_circle))
//                                .setBadgeTextColor(activity.getResources().getColor(R.color.colorBadge))
//                                .setBadgeTextSize(5, true)
//                                .setBadgePadding(5,true)
//                                .setBadgeGravity(Gravity.END|Gravity.BOTTOM)
//                                .setGravityOffset(-3, 1, true)
//                                .setBadgeNumber(3);
//                    }
//                }
//            }
//        }


//        try{
//            if (!match.getScore().equals("")){
//                str = match.getScore();
//            }
//            else {
//                str = "-";
//            }
//        }catch (NullPointerException e){
//            str = "-";
//        }

//        holder.textScore.setText(str);
        try{
            str = match.getPenalty();
            if (!str.equals("")){
                holder.textPenalty.setText(str);
                holder.textPenalty.setVisibility(View.VISIBLE);
            }
        }catch (NullPointerException ignored){}

//        if (check){
//            holder.showProtocol.setVisibility(View.VISIBLE);
            final boolean status = isProtocolAvailable;

            holder.layout.setOnClickListener(v -> {
                Intent intent = new Intent(activity, ConfirmProtocol.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("CONFIRMPROTOCOL", match);
                bundle.putBoolean("STATUS", status);
                intent.putExtras(bundle);
                activity.startActivity(intent);
            });
//        }
        if (position==(matches.size()-1)){
            holder.line.setVisibility(View.INVISIBLE);
        }

        } catch (NullPointerException ignored){}
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
        final TextView matchStatus;
        final RelativeLayout layout;
        final View line;
        final TextView textPenalty;
        ViewHolder(View item) {
            super(item);
            matchStatus = item.findViewById(R.id.matchStatus);
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
            if (date1 != null)
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
                if (date1 != null)
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

    public void dataChanged(List<MatchPopulate> allPlayers1) {
        matches.clear();
        matches.addAll(allPlayers1);
        notifyDataSetChanged();
    }
}
