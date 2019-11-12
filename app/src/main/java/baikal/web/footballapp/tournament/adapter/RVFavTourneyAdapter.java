package baikal.web.footballapp.tournament.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.DateToString;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.EditProfile;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.Tourney;
//import baikal.web.footballapp.tournament.CustomLinearLayoutManager;
import baikal.web.footballapp.tournament.CustomLinearLayoutManager;
import baikal.web.footballapp.tournament.activity.TournamentPage;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RVFavTourneyAdapter extends RecyclerView.Adapter<RVFavTourneyAdapter.ViewHolder> {
    private List<Tourney> tourneys;
    private PersonalActivity activity;
    private List<List<League>> favLeagues;
    final Logger log = LoggerFactory.getLogger(TournamentPage.class);
    private Listener mListener;

    public RVFavTourneyAdapter(List<Tourney> tourneys, Activity activity, List<List<League>> favLeagues,  Listener mListener){
        this.tourneys = tourneys;
        this.activity = (PersonalActivity) activity;
        this.favLeagues = favLeagues;
        this.mListener = mListener;

    }
     public interface Listener {
        void onClick(String id);
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
        holder.rvLeagues.setLayoutManager(new CustomLinearLayoutManager(activity));
        holder.rvLeagues.setAdapter(new RecyclerViewTournamentAdapter(activity, favLeagues.get(position), tourneys ));
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null){
                    if(favLeagues.get(position).size()>0)
                    mListener.onClick(favLeagues.get(position).get(0).getId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {

        return tourneys.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        final TextView textTitle;
        final TextView textDate;
        final TextView textCommandNum;
        final TextView textStatusFinish;
        final View view;
        final RecyclerView rvLeagues;
        final LinearLayout linearLayout;
        ViewHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.tourneyLine);
            textCommandNum = itemView.findViewById(R.id.tourneyTeamNumber);
            textDate = itemView.findViewById(R.id.tourneyDate);
            textTitle = itemView.findViewById(R.id.tourneyTitle);
            textStatusFinish = itemView.findViewById(R.id.tourneyFinish);
            rvLeagues = itemView.findViewById(R.id.recyclerViewFavLeagues);
            linearLayout = itemView.findViewById(R.id.tourneyShow);
        }
    }
    public void dataChanged(List<Tourney> tourneys){
        this.tourneys.clear();
        this.tourneys.addAll(tourneys);
        notifyDataSetChanged();
    }
}
