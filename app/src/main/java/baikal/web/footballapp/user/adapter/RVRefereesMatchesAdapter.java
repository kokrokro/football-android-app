package baikal.web.footballapp.user.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import baikal.web.footballapp.DateToString;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.MatchPopulate;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.Referee;
import baikal.web.footballapp.model.Stadium;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.viewmodel.PersonViewModel;

public class RVRefereesMatchesAdapter extends RecyclerView.Adapter<RVRefereesMatchesAdapter.ViewHolder> {
    private final Context context;
    private final List<MatchPopulate> matches;
    private final Person person;
    private final Activity activity;
    private final ListenerSendReferees listener;
    private PersonViewModel personViewModel;

    public RVRefereesMatchesAdapter(Context context, List<MatchPopulate> matches, Person person, Activity activity, PersonViewModel personViewModel, ListenerSendReferees listener){
        this.context =  context;
        this.matches =  matches;
        this.person =  person;
        this.activity = activity;
        this.listener = listener;
        this.personViewModel = personViewModel;
    }

    public interface ListAdapterListener {
        void onClickSwitch(String id, String personId, Boolean check, String type, int position);
    }

    public interface ListenerSendReferees{
        void onClick(List<Referee> referees, Referee referee, String matchId, Boolean isChecked);
    }
    @NonNull
    @Override
    public RVRefereesMatchesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.referees_match, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVRefereesMatchesAdapter.ViewHolder holder, int position) {
        MatchPopulate match = matches.get(position);
        String str = match.getDate();

        holder.textDate.setText(DateToString.ChangeDate(str));
        holder.textTime.setText(DateToString.ChangeTime(str));

        Stadium place = match.getPlace();
        try {
            holder.textStadium.setText(place.getName());
        } catch (NullPointerException e) {
            holder.textStadium.setText("Неизвестно");
        }
        try {
            str = match.getTour();
            holder.textLeague.setText(str);
        } catch (NullPointerException ignored){}

        Team team1 = match.getTeamOne();
        Team team2 = match.getTeamTwo();

        if(team1!=null) {
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
                } catch (Exception ignored) { }
            }
        }

        str = "Не назначен";
        holder.textReferee1.setText(str);
        holder.textReferee1.setTextColor(ContextCompat.getColor(activity, R.color.colorBadge));
        holder.textReferee2.setText(str);
        holder.textReferee2.setTextColor(ContextCompat.getColor(activity, R.color.colorBadge));
        holder.textReferee3.setText(str);
        holder.textReferee3.setTextColor(ContextCompat.getColor(activity, R.color.colorBadge));
        holder.textReferee4.setText(str);
        holder.textReferee4.setTextColor(ContextCompat.getColor(activity, R.color.colorBadge));

        setReferees(match, holder);



        for(Referee referee : match.getReferees()){
            SwitchCompat switchReferee = null;
            if(!referee.getPerson().equals(person.get_id())){
                continue;
            }
            switch (referee.getType()){
                case "firstReferee":
                    switchReferee = holder.switch1;
                    break;
                case "secondReferee":
                    switchReferee = holder.switch2;
                    break;
                case  "thirdReferee":
                    switchReferee = holder.switch3;
                    break;
                case  "timekeeper":
                    switchReferee = holder.switch4;
                    break;
            }
            switchReferee.setChecked(true);

        }

        holder.switch1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Referee newRef = new Referee();
            newRef.setPerson(person.get_id());
            newRef.setType("firstReferee");
            listener.onClick(match.getReferees(), newRef, match.getId(), isChecked);
        });
        holder.switch1.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                holder.switch1.getParent().requestDisallowInterceptTouchEvent(true);
            }
            return false;
        });

        holder.switch2.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    Referee newRef = new Referee();
                    newRef.setPerson(person.get_id());
                    newRef.setType("secondReferee");
                    listener.onClick(match.getReferees(), newRef, match.getId(), isChecked);
                });
        holder.switch2.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                holder.switch2.getParent().requestDisallowInterceptTouchEvent(true);
            }
            return false;
        });

        holder.switch3.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    Referee newRef = new Referee();
                    newRef.setPerson(person.get_id());
                    newRef.setType("thirdReferee");
                    listener.onClick(match.getReferees(), newRef, match.getId(), isChecked);
                });
        holder.switch3.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                holder.switch3.getParent().requestDisallowInterceptTouchEvent(true);
            }
            return false;
        });

        holder.switch4.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    Referee newRef = new Referee();
                    newRef.setPerson(person.get_id());
                    newRef.setType("timekeeper");
                    listener.onClick(match.getReferees(), newRef, match.getId(), isChecked);
                });
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

    private void setReferees (MatchPopulate match, RVRefereesMatchesAdapter.ViewHolder holder)
    {
        TreeMap<String, TextView> textViewRefs = new TreeMap<>();
        textViewRefs.put("firstReferee", holder.textReferee1);
        textViewRefs.put("secondReferee", holder.textReferee2);
        textViewRefs.put("thirdReferee", holder.textReferee3);
        textViewRefs.put("timekeeper", holder.textReferee4);

        List<Referee> referees = match.getReferees();
        for (Referee r: referees) {
            Person p = personViewModel.getPersonById(r.getPerson(),
                    (person) ->
                            setRefereeToTextView(textViewRefs.get(r.getType()), person));

            setRefereeToTextView(textViewRefs.get(r.getType()), p);
        }
    }

    private void setRefereeToTextView(TextView textView, Person referee) {
        try {
            textView.setText(referee.getSurnameWithInitials());
            textView.setTextColor(ContextCompat.getColor(activity, R.color.colorBottomNavigationUnChecked));
        } catch (Exception e) {
            textView.setText("Не назначен");
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
        final SwitchCompat switch1;
        final SwitchCompat switch2;
        final SwitchCompat switch3;
        final SwitchCompat switch4;

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
            switch1 = item.findViewById(R.id.refereesMatchSwitch);
            switch2 = item.findViewById(R.id.refereesMatchSwitch2);
            switch3 = item.findViewById(R.id.refereesMatchSwitch3);
            switch4 = item.findViewById(R.id.refereesMatchSwitch4);
        }


    }

    public void dataChanged(List<MatchPopulate> allPlayers1) {
        matches.clear();
        matches.addAll(allPlayers1);
        notifyDataSetChanged();
    }

}
