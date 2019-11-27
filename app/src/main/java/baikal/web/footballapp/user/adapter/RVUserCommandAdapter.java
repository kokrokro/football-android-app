package baikal.web.footballapp.user.adapter;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import baikal.web.footballapp.DateToString;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.PersonTeams;
import baikal.web.footballapp.model.Player;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.model.Tourney;
import baikal.web.footballapp.user.activity.UserCommands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class RVUserCommandAdapter extends RecyclerView.Adapter<RVUserCommandAdapter.ViewHolder>{
    private final Logger log = LoggerFactory.getLogger(UserCommands.class);
    UserCommands context;
    private final PersonalActivity activity;
    private final List<Team> list;
    public RVUserCommandAdapter (Activity activity, List<Team> list){
        this.activity = (PersonalActivity) activity;
//        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_command, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Team personTeams = list.get(position);
//        League league = personTeams.getLeague();
        League league = null;
        for (League league1 : PersonalActivity.tournaments){
            if (league1.getId().equals(personTeams.getLeague())){
                league = league1;
                break;
            }
        }
        String teamId = personTeams.getId();
        Team teamLeague = personTeams;
//        Team team = personTeams.getTeam();
//        holder.textTournamentTitle.setText();
        String title = "Команда: ";
        String date = "Начало: ";
        String transfer = "Трансферные периоды: ";
        String playersNum = "Количество игроков: ";
        String str = title + teamLeague.getName();
        holder.textCommandTitle.setText(str);
//        if (teamLeague.getStatus().equals("Rejected")){
//            holder.textStatus.setText("Отклонена");
//            holder.textStatus.setTextColor(ContextCompat.getColor(activity, R.color.colorBadge));
//        }
//        if (teamLeague.getStatus().equals("Pending")){
//            holder.textStatus.setText("Ожидание");
//        }
//        if (teamLeague.getStatus().equals("Approved")){
//            holder.textStatus.setText("Утверждена");
//            holder.textStatus.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimary));
//        }
        Tourney tourney = null;
        for(Tourney tr: PersonalActivity.allTourneys ){
            if(tr.getId().equals(league.getTourney())){
                tourney = tr;
            }
        }
        str = tourney.getName() + ". " + league.getName();
        holder.textTournamentTitle.setText(str);

        DateToString dateToString = new DateToString();
        str = date + dateToString.ChangeDate(league.getBeginDate());
        holder.textTournamentDate.setText(str);
        log.error(str);
        str = transfer + dateToString.ChangeDate(league.getTransferBegin()) + "-" + dateToString.ChangeDate(league.getTransferEnd());
        holder.textTransfer.setText(str);
        try{
            str = playersNum + personTeams.getPlayers().size();

        }catch (NullPointerException e){
            str = playersNum;
        }
        holder.textPlayersNum.setText(str);
        if (position== (list.size() - 1)){
            holder.line.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        final TextView textTournamentTitle;
        final TextView textCommandTitle;
        final TextView textTournamentDate;
        final TextView textTransfer;
        final TextView textPlayersNum;
        final TextView textStatus;
        final View line;
        ViewHolder(View item) {
            super(item);
            textTournamentTitle = item.findViewById(R.id.userCommandTournamentTitle);
            textCommandTitle = item.findViewById(R.id.userCommandInfoTitle);
            textTournamentDate = item.findViewById(R.id.userCommandTournamentDate);
            textTransfer = item.findViewById(R.id.userCommandTournamentTransfer);
            textPlayersNum = item.findViewById(R.id.userCommandInfoPlayersNum);
            textStatus = item.findViewById(R.id.teamStatus);
           line = item.findViewById(R.id.userCommandLine);
        }
    }





}
