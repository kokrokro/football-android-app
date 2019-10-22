package baikal.web.footballapp.user.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SwitchCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import baikal.web.footballapp.CheckName;
import baikal.web.footballapp.DateToString;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SetImage;
import baikal.web.footballapp.TimeToString;
import baikal.web.footballapp.model.ActiveMatch;
import baikal.web.footballapp.model.Club;
import baikal.web.footballapp.model.LeagueInfo;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.Player;
import baikal.web.footballapp.model.Referee;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.user.activity.AuthoUser;
import baikal.web.footballapp.user.activity.RefereesMatches;

import java.util.List;

import q.rorbin.badgeview.QBadgeView;

public class RVRefereesMatchesAdapter extends RecyclerView.Adapter<RVRefereesMatchesAdapter.ViewHolder> {
    private final RefereesMatches context;
    private final List<ActiveMatch> matches;
    private LeagueInfo leagueInfo;
    private final Person person;
    public RVRefereesMatchesAdapter(RefereesMatches context, List<ActiveMatch> matches, Person person, ListAdapterListener mListener){
        this.context =  context;
        this.matches =  matches;
        this.person =  person;
        this.mListener = mListener;
    }
    private final ListAdapterListener mListener;

    public interface ListAdapterListener {
        void onClickSwitch(String id, String personId, Boolean check, String type, int position);
    }
    @NonNull
    @Override
    public RVRefereesMatchesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.referees_match, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVRefereesMatchesAdapter.ViewHolder holder, int position) {
        ActiveMatch match = matches.get(position);
        String str;
        str = match.getDate();
        DateToString dateToString = new DateToString();
        holder.textDate.setText(dateToString.ChangeDate(str));
        try {
            TimeToString timeToString = new TimeToString();
            holder.textTime.setText(timeToString.ChangeTime(str));
        } catch (NullPointerException e) {
            holder.textTime.setText(str);
        }
        str = match.getPlace();
        try {
            String[] stadium;
            stadium = str.split(":", 1);
            holder.textStadium.setText(stadium[0]);
        } catch (NullPointerException e) {
            holder.textStadium.setText(str);
        }

        str = match.getTour();
        holder.textTour.setText(str);
        str = match.getLeague();
        holder.textTournamentTitle.setText(str);
        SetImage setImage = new SetImage();
        Team team1 = match.getTeamOne();
        Team team2 = match.getTeamTwo();
        str = team1.getName();
        holder.textCommandTitle1.setText(str);
        for (Club club : PersonalActivity.allClubs) {
            if (club.getId().equals(team1.getClub())) {
                setImage.setImage(context, holder.image1, club.getLogo());
            }
        }
        for (Player player : team1.getPlayers()) {
            if (player.getActiveDisquals() != 0) {
                new QBadgeView(context)
                        .bindTarget(holder.image1)
                        .setBadgeBackground(context.getDrawable(R.drawable.ic_circle))
                        .setBadgeTextColor(context.getResources().getColor(R.color.colorBadge))
                        .setBadgeTextSize(5, true)
                        .setBadgePadding(5, true)
                        .setBadgeGravity(Gravity.END | Gravity.BOTTOM)
                        .setGravityOffset(-3, 1, true)
                        .setBadgeNumber(3);
            }
        }

        str = team2.getName();
        holder.textCommandTitle2.setText(str);
        for (Club club : PersonalActivity.allClubs) {
            if (club.getId().equals(team2.getClub())) {
                setImage.setImage(context, holder.image2, club.getLogo());
            }
        }
        for (Player player : team2.getPlayers()) {
            if (player.getActiveDisquals() != 0) {
                new QBadgeView(context)
                        .bindTarget(holder.image2)
                        .setBadgeBackground(context.getDrawable(R.drawable.ic_circle))
                        .setBadgeTextColor(context.getResources().getColor(R.color.colorBadge))
                        .setBadgeTextSize(5, true)
                        .setBadgePadding(5, true)
                        .setBadgeGravity(Gravity.END | Gravity.BOTTOM)
                        .setGravityOffset(-3, 1, true)
                        .setBadgeNumber(3);
            }
        }

        try {
            str = match.getScore();
            if (str.equals("")) {
                str = "-";
            }
        } catch (NullPointerException e) {
            str = "-";
        }
        holder.textScore.setText(str);
        List<Referee> referees = match.getReferees();
        CheckName checkName = new CheckName();


        try{
            for (Referee referee : referees) {
                for (Person person : AuthoUser.allReferees) {
                    if (referee.getPerson().equals(person.getId())) {
                        switch (referee.getType()) {
                            case "1 судья":
//                                str = checkName.check(person.getSurname(), person.getName(), person.getLastname());
                                str = checkName.check(person.getSurname(), person.getName(), person.getLastname());
                                holder.referee1.setText(str);
                                holder.switch1.setVisibility(View.GONE);
                                break;
                            case "2 судья":
                                str = checkName.check(person.getSurname(), person.getName(), person.getLastname());
                                holder.referee2.setText(str);
                                holder.switch2.setVisibility(View.GONE);
                                break;
                            case "3 судья":
                                str = checkName.check(person.getSurname(), person.getName(), person.getLastname());
                                holder.referee3.setText(str);
                                holder.switch3.setVisibility(View.GONE);
                                break;
                            case "хронометрист":
                                str = checkName.check(person.getSurname(), person.getName(), person.getLastname());
                                holder.referee4.setText(str);
                                holder.switch4.setVisibility(View.GONE);
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }catch (Exception e){
        }
        holder.switch1.setOnCheckedChangeListener(
                (buttonView, isChecked) -> mListener.onClickSwitch(match.getId(), person.getId(), isChecked, "1 судья", position));
        holder.switch1.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                holder.switch1.getParent().requestDisallowInterceptTouchEvent(true);
            }
            return false;
        });

        holder.switch2.setOnCheckedChangeListener(
                (buttonView, isChecked) -> mListener.onClickSwitch(match.getId(), person.getId(), isChecked, "2 судья", position));
        holder.switch2.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                holder.switch2.getParent().requestDisallowInterceptTouchEvent(true);
            }
            return false;
        });

        holder.switch3.setOnCheckedChangeListener(
                (buttonView, isChecked) -> mListener.onClickSwitch(match.getId(), person.getId(), isChecked, "3 судья", position));
        holder.switch3.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                holder.switch3.getParent().requestDisallowInterceptTouchEvent(true);
            }
            return false;
        });

        holder.switch4.setOnCheckedChangeListener(
                (buttonView, isChecked) -> mListener.onClickSwitch(match.getId(), person.getId(), isChecked, "хронометрист", position));
        holder.switch4.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                holder.switch4.getParent().requestDisallowInterceptTouchEvent(true);
            }
            return false;
        });
        if (position == (matches.size() - 1)) {
            holder.line.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return matches.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textTournamentTitle;
        final TextView textDate;
        final TextView textTime;
        final TextView textScore;
        final TextView textTour;
        final TextView textStadium;
        final TextView textCommandTitle1;
        final TextView textCommandTitle2;
        final ImageView image1;
        final ImageView image2;
        final TextView referee1;
        final TextView referee2;
        final TextView referee3;
        final TextView referee4;
        final SwitchCompat switch1;
        final SwitchCompat switch2;
        final SwitchCompat switch3;
        final SwitchCompat switch4;
        final View line;
        ViewHolder(View item) {
            super(item);
            textTournamentTitle = item.findViewById(R.id.refereesMatchLeagueTitle);
            textDate = item.findViewById(R.id.refereesMatchDate);
            textTime = item.findViewById(R.id.refereesMatchTime);
            textScore = item.findViewById(R.id.refereesMatchScore);
            textTour = item.findViewById(R.id.timetableMatchRefereeLeague);
            textStadium = item.findViewById(R.id.refereesMatchStadium);
            textCommandTitle1 = item.findViewById(R.id.refereesMatchCommandTitle1);
            textCommandTitle2 = item.findViewById(R.id.refereesMatchCommandTitle2);
            image1 = item.findViewById(R.id.refereesMatchCommandLogo1);
            image2 = item.findViewById(R.id.refereesMatchCommandLogo2);
//            refereeType = item.findViewById(R.id.refereesMatchType);
            referee1 = item.findViewById(R.id.refereesMatchReferee1);
            referee2 = item.findViewById(R.id.refereesMatchReferee2);
            referee3 = item.findViewById(R.id.refereesMatchReferee3);
            referee4 = item.findViewById(R.id.refereesMatchReferee4);
            switch1 = item.findViewById(R.id.refereesMatchSwitch1);
            switch2 = item.findViewById(R.id.refereesMatchSwitch2);
            switch3 = item.findViewById(R.id.refereesMatchSwitch3);
            switch4 = item.findViewById(R.id.refereesMatchSwitch4);
            line = item.findViewById(R.id.timetableLine);
        }
    }

    public void dataChanged(List<ActiveMatch> allPlayers1) {
        matches.clear();
        matches.addAll(allPlayers1);
        notifyDataSetChanged();
    }

}
