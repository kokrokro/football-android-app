package baikal.web.footballapp.user.adapter;

import android.app.Activity;
import android.content.Intent;
import android.database.ContentObservable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.DateToString;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.ParticipationRequest;
import baikal.web.footballapp.model.PersonTeams;
import baikal.web.footballapp.model.Player;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.model.Tourney;
import baikal.web.footballapp.user.activity.UserCommandInfo;
import baikal.web.footballapp.user.activity.UserCommands;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RVOwnCommandAdapter extends RecyclerView.Adapter<RVOwnCommandAdapter.ViewHolder>{
    private final Logger log = LoggerFactory.getLogger(UserCommands.class);
    UserCommands context;
    private final PersonalActivity activity;
    private final List<Team> list;
    public RVOwnCommandAdapter (Activity activity, List<Team> list){
        this.activity = (PersonalActivity) activity;
//        this.context = context;

        this.list = list;
    }
    @NonNull
    @Override
    public RVOwnCommandAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_command, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVOwnCommandAdapter.ViewHolder holder, int position) {
        Team personTeams = list.get(position);
//        final League league = personTeams.getLeague();
        League league = null;
        for (League league1 : activity.tournaments){
            if (league1.getId().equals(personTeams.getLeague())){
                league = league1;
                break;
            }
        }
        String nameTourney = league.getTourney();
        for(Tourney tr : activity.allTourneys){
            if(tr.getId().equals(nameTourney)){
                nameTourney = tr.getName();
                break;
            }
        }
        String teamId = personTeams.getId();
//        Team teamLeague = null;
//        for (Team team: league.getTeams()){
//            if (team.getId().equals(teamId)){
//                teamLeague = team;
//            }
//        }
//        Team team = personTeams.getTeam();
//        holder.textTournamentTitle.setText();
        String title = "Команда: ";
        String date = "Начало: ";
        String transfer = "Трансферные периоды: ";
        String playersNum = "Количество игроков: ";
        String str = title + personTeams.getName();
        holder.textCommandTitle.setText(str);


        str = nameTourney + ". " + league.getName();
        holder.textTournamentTitle.setText(str);


        DateToString dateToString = new DateToString();
        str = date + dateToString.ChangeDate(league.getBeginDate());
        holder.textTournamentDate.setText(str);

        log.error(str);

        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Calendar calendar = Calendar.getInstance();
        String transferBeginStr = dateToString.ChangeDate(league.getTransferBegin());
        String transferEndStr = dateToString.ChangeDate(league.getTransferEnd());

        str = transfer + transferBeginStr + "-" + transferEndStr;
        holder.textTransfer.setText(str);
//        List<Player> players = personTeams.getPlayers();
//        List<Player> playerList = new ArrayList<>();
//        for (Player player: players){
//            if (player.getInviteStatus().equals("Approved") || player.getInviteStatus().equals("Accepted")){
////            if (player.getInviteStatus().equals("Accepted")){
//                playerList.add(player);
//            }
//        }
        str = playersNum + personTeams.getPlayers().size();
        holder.textPlayersNum.setText(str);
        if (position== (list.size() - 1)){
            holder.line.setVisibility(View.INVISIBLE);
        }
        Controller.getApi().getParticipation(teamId).enqueue(new Callback<List<ParticipationRequest>>() {
            @Override
            public void onResponse(Call<List<ParticipationRequest>> call, Response<List<ParticipationRequest>> response) {
                if(response.isSuccessful()){
                    if(response.body()!=null && response.body().size()>0){
                        holder.textStatus.setText(response.body().get(0).getStatus());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ParticipationRequest>> call, Throwable t) {

            }
        });
        setListener(holder,personTeams,league);

//        if (teamLeague.getStatus().equals("Rejected")){
//            holder.textStatus.setText("Отклонена");
//            holder.textStatus.setTextColor(ContextCompat.getColor(activity, R.color.colorBadge));
//            setListener(holder, finalTeamLeague, finalLeague);
//        }
//        if (teamLeague.getStatus().equals("Pending")){
//            holder.textStatus.setText("Ожидание");
//            setListener(holder, finalTeamLeague, finalLeague);
//        }
//        if (teamLeague.getStatus().equals("Approved")){
//            holder.textStatus.setText("Утверждена");
//            holder.textStatus.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimary));
////            holder.buttonShow.setVisibility(View.INVISIBLE);
//        }

    }

    private void setListener(RVOwnCommandAdapter.ViewHolder holder, Team finalTeamLeague, League finalLeague) {
        holder.buttonShow.setOnClickListener(v -> {
            Intent intent = new Intent(activity, UserCommandInfo.class);
            Bundle bundle = new Bundle();
            Bundle bundle1 = new Bundle();
            bundle.putSerializable("COMMANDEDIT", finalTeamLeague);
            bundle1.putSerializable("COMMANDEDITLEAGUE", finalLeague);
            intent.putExtras( bundle);
            intent.putExtras( bundle1);
            activity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        final LinearLayout buttonShow;
        final TextView textTournamentTitle;
        final TextView textCommandTitle;
        final TextView textTournamentDate;
        final TextView textTransfer;
        final TextView textPlayersNum;
        final TextView textStatus;
        final View line;
        ViewHolder(View item) {
            super(item);
            buttonShow = item.findViewById(R.id.userCommandShow);
            textTournamentTitle = item.findViewById(R.id.userCommandTournamentTitle);
            textCommandTitle = item.findViewById(R.id.userCommandInfoTitle);
            textTournamentDate = item.findViewById(R.id.userCommandTournamentDate);
            textTransfer = item.findViewById(R.id.userCommandTournamentTransfer);
            textPlayersNum = item.findViewById(R.id.userCommandInfoPlayersNum);
            textStatus = item.findViewById(R.id.teamStatus);
            line = item.findViewById(R.id.userCommandLine);
        }
    }




    public void dataChanged(List<PersonTeams> allPlayers1){
//        list.clear();
//        list.addAll(allPlayers1);
        notifyDataSetChanged();
    }
}
