package baikal.web.footballapp.tournament.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import baikal.web.footballapp.DateToString;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.model.Tourney;
import baikal.web.footballapp.tournament.activity.TournamentPage;

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

    public interface MyListener {
        void onClick(String id,Boolean isChecked);
    }

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

        holder.bind(position);
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

        void bind(int position) {
            if (favTourneys.contains(tourneys.get(position).getId()))
                favBtn.setChecked(true);

            else
                favBtn.setChecked(false);

            favBtn.setOnClickListener((v) -> {
                if (SaveSharedPreference.getObject() == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Внимание")
                            .setMessage("Войдите или зарегистрируйтесь")
                            .setCancelable(true)
                            .setNegativeButton("Позже", (dialog, idd) -> dialog.cancel())
                            .setPositiveButton("Войти", (dialog, which) -> activity.moveToLogin());
                    AlertDialog alert = builder.create();
                    alert.show();
                    favBtn.setChecked(false);
                } else {
                    mListener.onClick(tourneys.get(position).getId(), favBtn.isChecked());
                }
            });
        }
    }
}
