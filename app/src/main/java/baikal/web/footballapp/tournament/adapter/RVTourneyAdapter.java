package baikal.web.footballapp.tournament.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.DateToString;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.EditProfile;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.Tourney;
import baikal.web.footballapp.tournament.activity.Tournament;
import baikal.web.footballapp.tournament.activity.TournamentPage;
import baikal.web.footballapp.user.activity.UserInfo;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RVTourneyAdapter extends RecyclerView.Adapter<RVTourneyAdapter.ViewHolder> {
    private List<Tourney> tourneys ;
    private PersonalActivity activity;
    private  List<String> favTourneys;
    final Logger log = LoggerFactory.getLogger(TournamentPage.class);
    private MyListener mListener;

    public RVTourneyAdapter(List<Tourney> tourneys, Activity activity, List<String> favTourneys, MyListener mListener){
        this.tourneys = tourneys;
        this.activity = (PersonalActivity) activity;
        this.favTourneys = favTourneys;
        this.mListener = mListener;

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
        final String id = tourney.getId();
        DateToString dateToString = new DateToString();
        String str = dateToString.ChangeDate(tourney.getBeginDate()) + "-" + dateToString.ChangeDate(tourney.getEndDate());
        holder.textDate.setText(str);
        str = tourney.getName();
        holder.textTitle.setText(str);
        str = activity.getString(R.string.tournamentFilterCommandNum) + ": " + tourney.getMaxTeams();

        holder.textCommandNum.setText(str);
        holder.favBtn.setChecked(favTourneys.contains(id));

        holder.favBtn.setOnCheckedChangeListener((v,c)-> {
            mListener.onClick(id,c);
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
        final CheckBox favBtn;
        ViewHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.tourneyLine);
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override public void onClick(View v) {
//                    // item clicked
//                }
//            });
            favBtn = itemView.findViewById(R.id.like_button_cb);
            textCommandNum = itemView.findViewById(R.id.tourneyTeamNumber);
            textDate = itemView.findViewById(R.id.tourneyDate);
            textTitle = itemView.findViewById(R.id.tourneyTitle);
            textStatusFinish = itemView.findViewById(R.id.tourneyFinish);


//            mProgressDialog.setIndeterminate(true);
        }
    }
    public void dataChanged(List<Tourney> tourneys){
        this.tourneys.clear();
        this.tourneys.addAll(tourneys);
        notifyDataSetChanged();
    }
    public interface MyListener {

        void onClick(String id,Boolean isChecked);
    }
}
