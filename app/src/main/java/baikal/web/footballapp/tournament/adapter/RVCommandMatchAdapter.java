package baikal.web.footballapp.tournament.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import baikal.web.footballapp.DateToString;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Match;
import baikal.web.footballapp.model.Stadium;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.tournament.activity.CommandInfoActivity;

public class RVCommandMatchAdapter extends RecyclerView.Adapter<RVCommandMatchAdapter.ViewHolder> {
    private static final String TAG = "RVCommandMatchAdapter";
    Logger log = LoggerFactory.getLogger(CommandInfoActivity.class);
    private final List<Match> matches;
    private final List<Stadium> allStadiums;
    private final List<Team> allTeams;

    public RVCommandMatchAdapter(List<Match> matches, List<Stadium> allStadiums, List<Team> allTeams) {
        this.matches = matches;
        this.allStadiums = allStadiums;
        this.allTeams = allTeams;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timetable_fragment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Match match = matches.get(position);
        String str = match.getDate();
        DateToString dateToString = new DateToString();
        holder.textDate.setText(dateToString.ChangeDate(str));
        try{
            holder.textTime.setText(TimeToString(str));
        }catch (Exception e){
            holder.textTime.setText(str);
        }

        if (match.getPlace() != null)
            for (Stadium s: allStadiums)
                if (s.get_id().equals(match.getPlace()))
                    holder.textStadium.setText(s.getName());

        holder.textTour.setText(match.getTour()==null ? "" : match.getTour());

        if (match.getTeamOne() != null)
            for (Team t: allTeams)
                if (t.getId().equals(match.getTeamOne())) {
                    holder.textCommandTitle1.setText(t.getName());
                    break;
                }

        if (match.getTeamTwo() != null)
            for (Team t: allTeams)
                if (t.getId().equals(match.getTeamTwo())) {
                    holder.textCommandTitle2.setText(t.getName());
                    break;
                }

        if (match.getScore() == null || match.getScore().equals(""))
             str = "-";
        else str = match.getScore();
        holder.textScore.setText(str);

        try{
            str = match.getPenalty();
            if (!str.equals("")) {
                holder.textPenalty.setText(str);
                holder.textPenalty.setVisibility(View.VISIBLE);
            }
        } catch (NullPointerException ignored){}

        if (position == (matches.size() - 1))
            holder.line.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return matches.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textCommandTitle1;
        final TextView textCommandTitle2;
        final ImageView imgCommandLogo1;
        final ImageView imgCommandLogo2;
        final TextView textStadium;
        final TextView textDate;
        final TextView textTime;
        final TextView textTour;
        final TextView textLastScore;
        final TextView textPenalty;
        final TextView textScore;
        final View line;

        ViewHolder(View item) {
            super(item);
            textCommandTitle1 = item.findViewById(R.id.timetableCommandTitle1);
            textCommandTitle2 = item.findViewById(R.id.timetableCommandTitle2);
            imgCommandLogo1 = item.findViewById(R.id.timetableCommandLogo1);
            imgCommandLogo2 = item.findViewById(R.id.timetableCommandLogo2);
            textStadium = item.findViewById(R.id.timetableStadium);
            textDate = item.findViewById(R.id.timetableDate);
            textTime = item.findViewById(R.id.timetableTime);
            textTour = item.findViewById(R.id.timetableTour);
            textLastScore = item.findViewById(R.id.timetableLastScore);
            textScore = item.findViewById(R.id.timetableGameScore);
            textPenalty = item.findViewById(R.id.timetablePenalty);
            line = item.findViewById(R.id.timetableLine);
        }
    }


    private String TimeToString(String str) {
        String dateDOB = "";
        try {
            SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
            Date date1;
            date1 = mdformat.parse(str);

            Calendar cal = Calendar.getInstance();
            cal.setTime(date1);
            if (String.valueOf(cal.get(Calendar.HOUR)).length() == 1) {
                dateDOB += "0" + cal.get(Calendar.HOUR) + ":";
            } else {
                dateDOB += cal.get(Calendar.HOUR) + ":";
            }
            if ((String.valueOf(cal.get(Calendar.MINUTE) + 1).length() == 1)) {
                dateDOB += "0" + (cal.get(Calendar.MINUTE) + 1);
            } else {
                dateDOB += String.valueOf(cal.get(Calendar.MINUTE) + 1);
            }
        } catch (ParseException e) {
            try {
                SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.US);
                Date date1;
                date1 = mdformat.parse(str);

                Calendar cal = Calendar.getInstance();
                cal.setTime(date1);
                if (String.valueOf(cal.get(Calendar.HOUR)).length() == 1) {
                    dateDOB += "0" + cal.get(Calendar.HOUR) + ":";
                } else {
                    dateDOB += cal.get(Calendar.HOUR) + ":";
                }
                if ((String.valueOf(cal.get(Calendar.MINUTE) + 1).length() == 1)) {
                    dateDOB += "0" + (cal.get(Calendar.MINUTE) + 1);
                } else {
                    dateDOB += String.valueOf(cal.get(Calendar.MINUTE) + 1);
                }
            } catch (ParseException t) {
                t.printStackTrace();
            }
        }
        return dateDOB;
    }
}
