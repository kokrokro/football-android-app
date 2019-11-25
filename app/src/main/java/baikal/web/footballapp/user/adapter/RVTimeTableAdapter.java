package baikal.web.footballapp.user.adapter;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.Toast;

import baikal.web.footballapp.CheckName;
import baikal.web.footballapp.DateToString;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.MatchPopulate;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.Referee;
import baikal.web.footballapp.model.Stadium;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.user.activity.EditTimeTable;
import baikal.web.footballapp.user.activity.TimeTableFragment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class RVTimeTableAdapter extends RecyclerView.Adapter<RVTimeTableAdapter.ViewHolder> {
    private static final String TAG = "RVTimeTableAdapter: ";
    private final TimeTableFragment context;
    private final PersonalActivity activity;
    private final List<MatchPopulate> matches;

    private final Logger log = LoggerFactory.getLogger(TimeTableFragment.class);
    public RVTimeTableAdapter(Activity activity, TimeTableFragment context, List<MatchPopulate> matches) {
        this.matches = matches;
        this.context = context;
        this.activity = (PersonalActivity) activity;
    }

    @NonNull
    @Override
    public RVTimeTableAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timetable, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVTimeTableAdapter.ViewHolder holder, int position) {
        MatchPopulate match = matches.get(position);
        String str = match.getDate();
        DateToString dateToString = new DateToString();
        holder.textDate.setText(dateToString.ChangeDate(str));
        try {
            holder.textTime.setText(dateToString.ChangeTime(str));
        } catch (NullPointerException e) {
            holder.textTime.setText(str);
        }
        Stadium place = match.getPlace();
        try {
            holder.textStadium.setText(place.getName());
        } catch (NullPointerException e) {
            holder.textStadium.setText("Неизвестно");
        }

        str = match.getTour();
        holder.textLeague.setText(str);
        Team team1 = match.getTeamOne();
        Team team2 = match.getTeamTwo();

        if(team1!=null){
            str = team1.getName();
            holder.textCommandTitle1.setText(str);
        }
        else
            holder.textCommandTitle1.setText("Не назначено");

        if(team2!=null){
            str = team2.getName();
            holder.textCommandTitle2.setText(str);
        }
        else
            holder.textCommandTitle2.setText("Не назначено");

        try {
            str = match.getScore();
            if (str.equals(""))
                str = "-";
        } catch (NullPointerException e) {
            str = "-";
        }

        holder.textScore.setText(str);
        if (!str.equals("-")) {
            List<MatchPopulate> list = new ArrayList<>(matches);
            list.remove(match);
            for (MatchPopulate match1 : list) {
                try {
                    str = match1.getScore();
                    if (!str.equals("")
                            && match1.getTeamOne().equals(match.getTeamOne())
                            && match1.getTeamOne().equals(match.getTeamTwo())) {
                        str = match1.getScore();
                        holder.textLastScore.setVisibility(View.VISIBLE);
                        holder.textLastScore.setText(str);
                    }
                    if (!str.equals("")
                            && match1.getTeamOne().equals(match.getTeamTwo())
                            && match1.getTeamOne().equals(match.getTeamOne())) {
                        str = match1.getScore();
                        String[] strArray = str.split(":");
                        str = strArray[1] + ":" + strArray[0];
                        holder.textLastScore.setVisibility(View.VISIBLE);
                        holder.textLastScore.setText(str);
                    }
                } catch (Exception e) {
                    log.error(TAG, e);
                }

            }
        }

        List<Referee> referees = match.getReferees();
        CheckName checkName = new CheckName();

        str = "Не назначен";
        holder.textReferee1.setText(str);
        holder.textReferee1.setTextColor(ContextCompat.getColor(activity, R.color.colorBadge));
        holder.textReferee2.setText(str);
        holder.textReferee2.setTextColor(ContextCompat.getColor(activity, R.color.colorBadge));
        holder.textReferee3.setText(str);
        holder.textReferee3.setTextColor(ContextCompat.getColor(activity, R.color.colorBadge));
        holder.textReferee4.setText(str);
        holder.textReferee4.setTextColor(ContextCompat.getColor(activity, R.color.colorBadge));

        holder.buttonEdit.setOnClickListener(v -> {
            Intent intent = new Intent(activity, EditTimeTable.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("MatchConfirmProtocolRefereesToEdit", match);
            bundle.putInt("MatchIndex", position);
            intent.putExtras(bundle);
            context.startActivity(intent);
        });

        if (position == (matches.size() - 1))
            holder.line.setVisibility(View.INVISIBLE);
    }

    private void setRefereeToTextView (TextView textView, Person referee) {
        try {
            textView.setText(referee.getSurnameWithInitials());
        } catch (Exception e) {
            textView.setText("Не назначен");
//            Toast.makeText(context, "Не удалось загрузить список судий...", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return matches.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textDate;
        final TextView textTime;
        final TextView textLeague;
        final TextView textLastScore;
        final TextView textPenalty;
        final TextView textStadium;
        final TextView textCommandTitle1;
        final TextView textCommandTitle2;
        final TextView textScore;
        final ImageView image1;
        final ImageView image2;
        final TextView textReferee1;
        final TextView textReferee2;
        final TextView textReferee3;
        final TextView textReferee4;
        final LinearLayout buttonEdit;
        final View line;

        ViewHolder(View item) {
            super(item);
            textDate = item.findViewById(R.id.timetableMatchDate);
            textTime = item.findViewById(R.id.timetableMatchTime);
            textLeague = item.findViewById(R.id.timetableMatchLeague);
            textStadium = item.findViewById(R.id.timetableMatchStadium);
            textLastScore = item.findViewById(R.id.timetableMatchLastScore);
            textPenalty = item.findViewById(R.id.timetableMatchPenalty);
            textCommandTitle1 = item.findViewById(R.id.timetableMatchCommandTitle1);
            textCommandTitle2 = item.findViewById(R.id.timetableMatchCommandTitle2);
            image1 = item.findViewById(R.id.timetableMatchCommandLogo1);
            image2 = item.findViewById(R.id.timetableMatchCommandLogo2);
            textReferee1 = item.findViewById(R.id.timetableMatchReferee1);
            textReferee2 = item.findViewById(R.id.timetableMatchReferee2);
            textReferee3 = item.findViewById(R.id.timetableMatchReferee3);
            textReferee4 = item.findViewById(R.id.timetableMatchReferee4);
            buttonEdit = item.findViewById(R.id.timetableMatchEdit);
            //may be null
            textScore = item.findViewById(R.id.timetableMatchScore);
            line = item.findViewById(R.id.timetableMatchLine);
        }
    }

    public void dataChanged(List<MatchPopulate> allPlayers1) {
        matches.clear();
        matches.addAll(allPlayers1);
        notifyDataSetChanged();
    }
}
