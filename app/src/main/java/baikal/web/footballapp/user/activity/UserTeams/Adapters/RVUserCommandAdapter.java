package baikal.web.footballapp.user.activity.UserTeams.Adapters;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import baikal.web.footballapp.DateToString;
import baikal.web.footballapp.MankindKeeper;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.model.Tourney;

public class RVUserCommandAdapter extends RecyclerView.Adapter<RVUserCommandAdapter.ViewHolder> {
    private final List<Team> list = new ArrayList<>();
    private final Listener listener;

    public RVUserCommandAdapter(List<Team> list, String status, Listener listener) {
        Log.d("RVUC_adapter", "in the constructor => " + status);
        setData(list);
        this.listener = listener;
    }
    public interface Listener {
         void onClick(Team team, League league);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_command, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Team personTeams = list.get(position);
        League league = null;
        for (League league1 : MankindKeeper.getInstance().allLeagues)
            if (league1.getId().equals(personTeams.getLeague())){
                league = league1;
                break;
            }

        final League currentLeague = league;

        String title = "Команда: ";
        String date = "Начало: ";
        String transfer = "Трансферные периоды: ";
        String playersNum = "Количество игроков: ";
        String str = title + personTeams.getName();
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
        for(Tourney tr: MankindKeeper.getInstance().allTourneys )
            if(tr.getId().equals(league.getTourney()))
                tourney = tr;

        holder.textTournamentTitle.setText(tourney.getName() + ". " + league.getName());

        DateToString dateToString = new DateToString();
        holder.textTournamentDate.setText(date + dateToString.ChangeDate(league.getBeginDate()));

        str = transfer + dateToString.ChangeDate(league.getTransferBegin()) + "-" + dateToString.ChangeDate(league.getTransferEnd());
        holder.textTransfer.setText(str);

        try {
            str = playersNum + personTeams.getPlayers().size();
        } catch (NullPointerException e){ str = playersNum; }

        holder.textPlayersNum.setText(str);
        if (position == (list.size() - 1))
            holder.line.setVisibility(View.INVISIBLE);

        switch (league.getStatus()) {
            case "started":
                holder.textStatus.setText("Началось");
                break;
            case "pending":
                holder.textStatus.setText("В ожидании");
                break;
        }

        holder.linearLayout.setOnClickListener(v -> listener.onClick(personTeams, currentLeague));
    }

    @Override
    public int getItemCount() {
        Log.d("RVUC_adapter", "" + list.size());
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textTournamentTitle;
        final TextView textCommandTitle;
        final TextView textTournamentDate;
        final TextView textTransfer;
        final TextView textPlayersNum;
        final TextView textStatus;
        final View line;
        final LinearLayout linearLayout;

        ViewHolder(View item) {
            super(item);
            textTournamentTitle = item.findViewById(R.id.userCommandTournamentTitle);
            textCommandTitle = item.findViewById(R.id.userCommandInfoTitle);
            textTournamentDate = item.findViewById(R.id.userCommandTournamentDate);
            textTransfer = item.findViewById(R.id.userCommandTournamentTransfer);
            textPlayersNum = item.findViewById(R.id.userCommandInfoPlayersNum);
            textStatus = item.findViewById(R.id.teamStatus);
            line = item.findViewById(R.id.userCommandLine);
            linearLayout = item.findViewById(R.id.userCommandShow);
        }
    }

    public void setData (List<Team> newTeams) {
        list.clear();
        list.addAll(newTeams);
        notifyDataSetChanged();
    }
}
