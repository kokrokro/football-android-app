package baikal.web.footballapp.tournament.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import baikal.web.footballapp.DateToString;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.Tourney;
import baikal.web.footballapp.tournament.activity.TournamentPage;

public class RVFavTourneyAdapter extends RecyclerView.Adapter<RVFavTourneyAdapter.ViewHolder> {
    private List<Tourney> tourneys;
    private PersonalActivity activity;
    private List<League> favLeagues;
    private RecyclerViewTournamentAdapter.Listener mListener;
    final Logger log = LoggerFactory.getLogger(TournamentPage.class);

    public RVFavTourneyAdapter(List<Tourney> tourneys, Activity activity, List<League> favLeagues, RecyclerViewTournamentAdapter.Listener mListener){
        this.tourneys = tourneys;
        this.activity = (PersonalActivity) activity;
        this.favLeagues = favLeagues;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public RVFavTourneyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_tourney, parent, false);
        return new RVFavTourneyAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVFavTourneyAdapter.ViewHolder holder, int position) {
        final Tourney tourney = tourneys.get(position);
        //final String id = tourney.getId();
        DateToString dateToString = new DateToString();
        String str = dateToString.ChangeDate(tourney.getBeginDate()) + "-" + dateToString.ChangeDate(tourney.getEndDate());
        holder.textDate.setText(str);
        str = tourney.getName();
        holder.textTitle.setText(str);
        str = activity.getString(R.string.tournamentFilterCommandNum) + ": " + tourney.getMaxTeams();
        holder.textCommandNum.setText(str);


        holder.leagueList.clear();
        for (League l : favLeagues)
            if(l.getTourney().equals(tourney.getId()))
                holder.leagueList.add(l);

        holder.adapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return tourneys.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textTitle;
        final TextView textDate;
        final TextView textCommandNum;
        final TextView textStatusFinish;
        final View view;
        final RecyclerView rvLeagues;
        final RelativeLayout linearLayout;
        final RecyclerViewTournamentAdapter adapter;
        final List<League> leagueList;

        ViewHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.tourneyLine);
            textCommandNum = itemView.findViewById(R.id.tourneyTeamNumber);
            textDate = itemView.findViewById(R.id.tourneyDate);
            textTitle = itemView.findViewById(R.id.tourneyTitle);
            textStatusFinish = itemView.findViewById(R.id.tourneyFinish);
            rvLeagues = itemView.findViewById(R.id.recyclerViewFavLeagues);
            linearLayout = itemView.findViewById(R.id.tourneyShow);

            leagueList = new ArrayList<>();
            rvLeagues.setLayoutManager(new LinearLayoutManager(activity));
            adapter = new RecyclerViewTournamentAdapter(activity, leagueList, mListener);
            rvLeagues.setAdapter(adapter);
        }
    }
    public void dataChanged(List<Tourney> tourneys){
        this.tourneys.clear();
        this.tourneys.addAll(tourneys);
        notifyDataSetChanged();
    }
}
