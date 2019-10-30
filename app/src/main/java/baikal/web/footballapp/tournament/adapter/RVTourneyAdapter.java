package baikal.web.footballapp.tournament.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import baikal.web.footballapp.DateToString;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Tourney;

public class RVTourneyAdapter extends RecyclerView.Adapter<RVTourneyAdapter.ViewHolder> {
    private List<Tourney> tourneys = new ArrayList<>();
    private PersonalActivity activity;
    //private ListAdapterListener mListener;

    public RVTourneyAdapter(List<Tourney> tourneys, Activity activity){
        this.tourneys = tourneys;
        this.activity = (PersonalActivity) activity;
        //this.mListener = mListener;

    }
//    public interface ListAdapterListener {
//        void onClickSwitch(String leagueId);
//    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tourney, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Tourney tourney = tourneys.get(position);
        DateToString dateToString = new DateToString();
        String str = dateToString.ChangeDate(tourney.getBeginDate()) + "-" + dateToString.ChangeDate(tourney.getEndDate());
        holder.textDate.setText(str);
        str = tourney.getName();
        holder.textTitle.setText(str);
        str = activity.getString(R.string.tournamentFilterCommandNum) + ": " + tourney.getMaxTeams();
        holder.textCommandNum.setText(str);
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
        final RelativeLayout imageButton;
        ViewHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.tourneyLine);
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override public void onClick(View v) {
//                    // item clicked
//                }
//            });
            textCommandNum = itemView.findViewById(R.id.tourneyTeamNumber);
            textDate = itemView.findViewById(R.id.tourneyDate);
            textTitle = itemView.findViewById(R.id.tourneyTitle);
            textStatusFinish = itemView.findViewById(R.id.tourneyFinish);
            imageButton = itemView.findViewById(R.id.tournamentShow);


//            mProgressDialog.setIndeterminate(true);
        }
    }
    public void dataChanged(List<Tourney> tourneys){
//        this.tourneys.clear();
//        this.tourneys.addAll(tourneys);
        notifyDataSetChanged();
    }
}
