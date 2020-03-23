package baikal.web.footballapp.tournament.adapter;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import baikal.web.footballapp.App;
import baikal.web.footballapp.DateToString;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.Match;
import baikal.web.footballapp.model.Player;
import baikal.web.footballapp.model.Stadium;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.viewmodel.MainViewModel;
import q.rorbin.badgeview.QBadgeView;

public class RecyclerViewTournamentTimeTableAdapter extends RecyclerView.Adapter<RecyclerViewTournamentTimeTableAdapter.ViewHolder> {
    private final static String TAG = "RVTourneyTimeTableAd";
    Logger log = LoggerFactory.getLogger(PersonalActivity.class);

    private final List<Match> matches;
    private MainViewModel mainViewModel;

    public RecyclerViewTournamentTimeTableAdapter(League league, MainViewModel mainViewModel) {
        this.matches = league.getMatches();
        this.mainViewModel = mainViewModel;
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

        if (match != null)
            holder.bindTo(match);

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
        //        TextView textTournamentTitle;
        final TextView textScore;
//        final RelativeLayout layout;
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
//            textTournamentTitle = (TextView) item.findViewById(R.id.timetableLeagueTitle);
            textScore = item.findViewById(R.id.timetableGameScore);
            textPenalty = item.findViewById(R.id.timetablePenalty);
//            layout = item.findViewById(R.id.timetableLayout);
            line = item.findViewById(R.id.timetableLine);
        }

        void bindTo (Match match) {
            textDate.setText(DateToString.ChangeDate(match.getDate()));
            textTime.setText(DateToString.ChangeTime(match.getDate()));
            textTour.setText(match.getTour());

            Stadium s = mainViewModel.getStadiumById(
                    match.getPlace()==null?"":match.getPlace(),
                    (o) -> {
                        Log.d(TAG, o.toString());
                    }
            );

            textStadium.setText(s==null ? "Не назначен" : s.getName());
            textScore.setText(match.getScore()==null?"-":match.getScore());
            textPenalty.setText(match.getPenalty()==null?"":match.getPenalty());

            if (match.getPenalty()!=null && !match.getPenalty().equals(""))
                textPenalty.setVisibility(View.VISIBLE);
            else
                textPenalty.setVisibility(View.GONE);

            if (match.getTeamOne() != null) {
                Team teamOne = mainViewModel.getTeamById(match.getTeamOne(), team -> {
                    textCommandTitle1.setText(team.getName());
                    initBadge(team, imgCommandLogo1);
                });
                textCommandTitle1.setText(teamOne==null?"":teamOne.getName());
                initBadge(teamOne, imgCommandLogo1);
            } else
                textCommandTitle1.setText("Неизвестно");

            if (match.getTeamTwo() != null) {
                Team teamTwo = mainViewModel.getTeamById(match.getTeamTwo(), team -> {
                    textCommandTitle2.setText(team.getName());
                    initBadge(team, imgCommandLogo2);
                });
                textCommandTitle2.setText(teamTwo==null?"":teamTwo.getName());
                initBadge(teamTwo, imgCommandLogo2);
            } else
                textCommandTitle2.setText("Неизвестно");
        }

        private void initBadge (Team team, ImageView imageView) {
            if (team == null)
                return;
            List<Player> players = team.getPlayers();

            for (Player p: players)
                if (p.getActiveDisquals() > 0) {
                    new QBadgeView(App.getAppContext())
                            .bindTarget(imageView)
                            .setBadgeBackground(App.getAppContext().getDrawable(R.drawable.ic_circle))
                            .setBadgeTextColor(App.getAppContext().getResources().getColor(R.color.colorBadge))
                            .setBadgeTextSize(5, true)
                            .setBadgePadding(5, true)
                            .setBadgeGravity(Gravity.END | Gravity.BOTTOM)
                            .setGravityOffset(-3, 1, true)
                            .setBadgeNumber(3);
                    break;
                }
        }
    }
}
