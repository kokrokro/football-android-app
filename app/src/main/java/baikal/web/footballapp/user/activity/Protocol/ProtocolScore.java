package baikal.web.footballapp.user.activity.Protocol;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.MankindKeeper;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.model.Event;
import baikal.web.footballapp.model.EventList;
import baikal.web.footballapp.model.MatchPopulate;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.user.activity.Protocol.Adapters.RVTeamEventListAdapter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProtocolScore extends AppCompatActivity{
    private static final String TAG = "ProtocolScore: ";
    private static final int EVENT_LIST_EDITED = 7341;
    private static final int MATCH_PENALTY = 7321;

    private final Logger log = LoggerFactory.getLogger(ProtocolScore.class);

    private final String[] eventTypes =  {"goal", "yellowCard", "redCard", "penalty",
                                    "autoGoal", "foul", "penaltySeriesSuccess", "penaltySeriesFailure"};

    private final String[] matchTimes       = {"firstHalf",     "secondHalf",  "extraTime", "penaltySeries"};
    private final String[] matchTimesToShow = {"Первый тайм",   "Второй тайм", "Дополнительное время"};
    private int currentMatchTime = 0;

    private RecyclerView firstTeamListRecyclerView;
    private RecyclerView secondTeamListRecyclerView;

    private RVTeamEventListAdapter adapter1;
    private RVTeamEventListAdapter adapter2;

    private TextView textViewTeam1;
    private TextView textViewTeam2;
    private TextView textViewMatchScore;
    private TextView textViewMatchTime;

    private Button textViewFoulsCntTeam1;
    private Button textViewFoulsCntTeam2;

    private MatchPopulate match;
    private Team team1;
    private Team team2;

    private LinkedList<Event> eventsToSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()...");
        setContentView(R.layout.protocol_match_score);

        firstTeamListRecyclerView = findViewById(R.id.PMS_firstTeamList);
        secondTeamListRecyclerView = findViewById(R.id.PMS_secondTeamList);

        textViewTeam1 = findViewById(R.id.PMS_teamName1);
        textViewTeam2 = findViewById(R.id.PMS_teamName2);
        textViewMatchScore = findViewById(R.id.PMS_matchScore);
        textViewFoulsCntTeam1 = findViewById(R.id.PMS_cnt_fouls1);
        textViewFoulsCntTeam2 = findViewById(R.id.PMS_cnt_fouls2);
        textViewMatchTime = findViewById(R.id.PMS_time);
        Button btnAutoGoal1 = findViewById(R.id.PMS_autoGoalBtn1);
        Button btnAutoGoal2 = findViewById(R.id.PMS_autoGoalBtn2);
        Button btnFirstTime = findViewById(R.id.firstTimeBtn);
        Button btnSecondTime = findViewById(R.id.secondTimeBtn);
        Button btnExtraTime = findViewById(R.id.extraTimeBtn);
        Button btnPenalty = findViewById(R.id.penaltyBtn);
        Button btnEndMatch = findViewById(R.id.endMatchBtn);
        ImageButton btnShowEvents = findViewById(R.id.PMS_show_events);
        ImageButton buttonBack = findViewById(R.id.protocolScoreBack);

        eventsToSend = new LinkedList<>();

        buttonBack.setOnClickListener(v -> {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("MATCH", match);
            intent.putExtras(bundle);
            setResult(RESULT_CANCELED, intent);
            finish();
        });
        btnFirstTime.setOnClickListener(v -> {
            currentMatchTime = 0;
            textViewMatchTime.setText(matchTimesToShow[currentMatchTime]);
            calculateScore();
        });
        btnSecondTime.setOnClickListener(v -> {
            currentMatchTime = 1;
            textViewMatchTime.setText(matchTimesToShow[currentMatchTime]);
            calculateScore();
        });
        btnExtraTime.setOnClickListener(v -> {
            currentMatchTime = 2;
            textViewMatchTime.setText(matchTimesToShow[currentMatchTime]);
            calculateScore();
        });
        btnPenalty.setOnClickListener(v -> {
            for (Event e: match.getEvents())
                if (e.getEventType().equals("penaltySeriesSuccess") ||
                        e.getEventType().equals("penaltySeriesFailure")) {
                    Log.d(TAG,"start penalty activity");

                    match.setScore(String.valueOf(textViewMatchScore.getText()));
                    Intent intent = new Intent(this, ProtocolPenalty.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("MATCH", match);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, MATCH_PENALTY);

                    return;
                }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            @SuppressLint("InflateParams")
            View view = LayoutInflater.from(this).inflate(R.layout.yes_no_dialog_view, null);
            builder.setView(view);

            AlertDialog dialog = builder.create();

            TextView textView = view.findViewById(R.id.YNDV_text);
            Button no = view.findViewById(R.id.YNDV_btn_no);
            Button yes = view.findViewById(R.id.YNDV_btn_yes);
            textView.setText("Начать серию пенальти ?");
            no.setOnClickListener(vv -> dialog.dismiss());
            yes.setOnClickListener(vv -> {
                Log.d(TAG,"start penalty activity");

                match.setScore(String.valueOf(textViewMatchScore.getText()));
                Intent intent = new Intent(this, ProtocolPenalty.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("MATCH", match);
                intent.putExtras(bundle);
                startActivityForResult(intent, MATCH_PENALTY);

                dialog.dismiss();
            });
            dialog.show();
            Log.d(TAG, "starting penalty series");
        });

        textViewFoulsCntTeam1.setOnClickListener(v -> {
            if (team1 != null)
                addEvent(team1.getId(), null, 5);
        });
        textViewFoulsCntTeam2.setOnClickListener(v -> {
            if (team2 != null)
                addEvent(team2.getId(), null, 5);
        });

        btnAutoGoal1.setOnClickListener(v -> {
            if (team1 != null)
                addEvent(team1.getId(), null, 4);
        });

        btnAutoGoal2.setOnClickListener(v -> {
            if (team2 != null)
                addEvent(team2.getId(), null, 4);
        });

        firstTeamListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        secondTeamListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnShowEvents.setOnClickListener(v-> {
            Intent intent = new Intent(this, MatchEvents.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("MATCH", match);
            bundle.putBoolean("IS_EDITABLE", true);
            intent.putExtras(bundle);

            startActivityForResult(intent, EVENT_LIST_EDITED);
        });

        btnEndMatch.setOnClickListener(v -> sendMatchEndEvent());

        try {
            match = (MatchPopulate) Objects.requireNonNull(getIntent().getExtras()).getSerializable("MATCH");
        } catch (Exception e) {
            log.error(TAG, e);
        }
        assignMatchData();
    }

    private void assignMatchData() {
        if (match != null) {
            if (match.getTeamOne() != null)
                assignTeam(match.getTeamOne().getId(), "teamOne");
            if (match.getTeamTwo() != null)
                assignTeam(match.getTeamTwo().getId(), "teamTwo");

            calculateScore();
        }
    }

    void setupAdapters() {
        if (team1!= null && team1.getPlayers() != null && adapter1 == null) {
            adapter1 = new RVTeamEventListAdapter(this, team1.getPlayers(), match.getPlayersList(),
                                                                team1.getTrainer(), match.getEvents(), team1.getId(), person -> {
                DialogProtocol dialog = new DialogProtocol((person.getId().equals(team1.getTrainer())
                                                            && !match.getPlayersList().contains(team1.getTrainer())),
                                                            i -> addEvent(team1.getId(), person.getId(), i));

                dialog.show(getSupportFragmentManager(), "choose_protocol_event1");
            });
            firstTeamListRecyclerView.setAdapter(adapter1);
            adapter1.dataChanged();
        }
        if (team2 != null && team2.getPlayers() != null && adapter2 == null) {
            adapter2 = new RVTeamEventListAdapter(this, team2.getPlayers(), match.getPlayersList(),
                                                                team2.getTrainer(), match.getEvents(), team2.getId(), person -> {
                DialogProtocol dialog = new DialogProtocol((person.getId().equals(team2.getTrainer())
                                                            && !match.getPlayersList().contains(team2.getTrainer())),
                                                            i -> addEvent(team2.getId(), person.getId(), i));

                dialog.show(getSupportFragmentManager(), "choose_protocol_event2");
            });
            secondTeamListRecyclerView.setAdapter(adapter2);
            adapter2.dataChanged();
        }
    }


    void addEvent (String teamId, String personId, int i) {
        Event event = new Event();
        event.setId(null);
        event.setEventType(eventTypes[i]);
        event.setTime(matchTimes[currentMatchTime]);
        event.setPerson(personId);
        event.setTeam(teamId);
        match.addEvent(event);
        eventsToSend.addLast(event);
        calculateScore();
        adapter2.dataChanged();
        adapter1.dataChanged();
        sendEvents();
    }

    @SuppressLint("CheckResult")
    void sendEvents() {
        if (eventsToSend.size() != 0) {
            Event event = eventsToSend.getFirst();
            eventsToSend.removeFirst();
            String token = SaveSharedPreference.getObject().getToken();
            //noinspection ResultOfMethodCallIgnored
            Controller.getApi().editProtocolMatch(match.getId(), token, event)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(newEvent -> {
                                if (newEvent != null) {
                                    Toast.makeText(ProtocolScore.this, "Синхронизированно с сервером", Toast.LENGTH_SHORT).show();
                                    event.setId(newEvent.getId());
                                    calculateScore();
                                    adapter2.dataChanged();
                                    adapter1.dataChanged();
                                    if (newEvent.getEventType().equals("matchEnd"))
                                        getFinishedMatch();
                                    sendEvents();
                                }
                            },
                            error ->
                                Log.d(TAG, error.toString())
                    );
        }
    }

    @SuppressLint("SetTextI18n")
    void calculateScore()
    {
        int goalCntTeam1 = 0;
        int goalCntTeam2 = 0;
        int foulsCntTeam1 = 0;
        int foulsCntTeam2 = 0;

        TreeSet<String> disabledEvents = new TreeSet<>();

        for (Event e: match.getEvents())
            if (e.getEventType().equals("disable"))
                disabledEvents.add(e.getEvent());
            else if (e.getEventType().equals("enable"))
                disabledEvents.remove(e.getEvent());

        for (Event e: match.getEvents()) {
            if (e.getEventType().equals("disable") ||
                    e.getEventType().equals("enable") ||
                    (e.getId() != null && disabledEvents.contains(e.getId())))
                continue;

            if (team1 != null && e.getTeam()!=null && e.getTeam().equals(team1.getId())) {
                if (e.getEventType().equals(eventTypes[0])     ||
                        e.getEventType().equals(eventTypes[3]) ||
                        e.getEventType().equals(eventTypes[6]))
                    goalCntTeam1++;

                if (e.getEventType().equals(eventTypes[4]))
                    goalCntTeam2++;

                if (e.getEventType().equals(eventTypes[5]))
                    if (currentMatchTime==0 && e.getTime().equals("firstHalf"))
                        foulsCntTeam1++;
                    else if (currentMatchTime > 0 && !e.getTime().equals("firstHalf"))
                        foulsCntTeam1++;
            }

            if (team2 != null && e.getTeam()!=null && e.getTeam().equals(team2.getId())) {
                if (e.getEventType().equals(eventTypes[0])     ||
                        e.getEventType().equals(eventTypes[3]) ||
                        e.getEventType().equals(eventTypes[6]))
                    goalCntTeam2++;

                if (e.getEventType().equals(eventTypes[4]))
                    goalCntTeam1++;

                if (e.getEventType().equals(eventTypes[5]))
                    if (currentMatchTime==0 && e.getTime().equals("firstHalf"))
                        foulsCntTeam2++;
                    else if (currentMatchTime > 0 && !e.getTime().equals("firstHalf"))
                        foulsCntTeam2++;
            }
        }

        textViewFoulsCntTeam1.setText("Фолы: " + foulsCntTeam1);
        textViewFoulsCntTeam2.setText("Фолы: " + foulsCntTeam2);

        String score = goalCntTeam1 + ":" + goalCntTeam2;
        textViewMatchScore.setText(score);
    }

    void assignTeam (String teamId, String team12)
    {
        if (MankindKeeper.getInstance().getTeamById(teamId) == null ||
                (MankindKeeper.getInstance().getTeamById(teamId) != null &&
                        MankindKeeper.getInstance().getTeamById(teamId).getPlayers().size() == 0))
            Controller.getApi().getTeamById(teamId).enqueue(new Callback<List<Team>>() {
                @Override
                public void onResponse(@NonNull Call<List<Team>> call, @NonNull Response<List<Team>> response) {
                    if (response.body() != null && response.body().size() > 0) {
                        if (team12.equals("teamOne")) {
                            MankindKeeper.getInstance().addTeam(team1 = response.body().get(0));
                            textViewTeam1.setText(team1.getName());
                            setupAdapters();
                            calculateScore();
                        }
                        if (team12.equals("teamTwo")) {
                            MankindKeeper.getInstance().addTeam(team2 = response.body().get(0));
                            textViewTeam2.setText(team2.getName());
                            setupAdapters();
                            calculateScore();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Team>> call, @NonNull Throwable t) {
                    log.error(TAG, t);
                }
            });
        else {
            if (team12.equals("teamOne")) {
                team1 = MankindKeeper.getInstance().getTeamById(teamId);
                textViewTeam1.setText(team1.getName());
                setupAdapters();
            }
            if (team12.equals("teamTwo")) {
                team2 = MankindKeeper.getInstance().getTeamById(teamId);
                textViewTeam2.setText(team2.getName());
                setupAdapters();
            }
        }
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EVENT_LIST_EDITED && resultCode == RESULT_OK) {
            Log.d(TAG, "event list has been changed");

            EventList newEvents = (EventList) data.getExtras().getSerializable("EVENTS");
            for (Event e: newEvents.getEvents())
                eventsToSend.addLast(e);

            sendEvents();
        }

        if (requestCode == MATCH_PENALTY) {
            Log.d(TAG, "from penalty series...");
            try {
                match = (MatchPopulate) data.getExtras().getSerializable("MATCH");
                boolean isFinished = data.getExtras().getBoolean("IS_FINISHED", false);

                if (isFinished) {
                    sendMatchEndEvent();
                }
            } catch (Exception ignored) {}
        }
    }

    private void sendMatchEndEvent() {
        TreeSet<String> disabledEvents = new TreeSet<>();

        for (Event e: match.getEvents())
            if (e.getEventType().equals("disable"))
                disabledEvents.add(e.getEvent());
            else if (e.getEventType().equals("enable"))
                disabledEvents.remove(e.getEvent());

        for (Event e: match.getEvents())
            if (e.getEventType().equals("matchEnd") && !disabledEvents.contains(e.getId())) {
                getFinishedMatch();
                return;
            }


        eventsToSend.clear();
        for (Event e: match.getEvents())
            if (e.getId() == null)
                eventsToSend.addLast(e);

        Event finishEvent = new Event();
        finishEvent.setId(null);
        finishEvent.setEvent(null);
        finishEvent.setId(null);
        finishEvent.setTime(null);
        finishEvent.setPerson(null);
        finishEvent.setTeam(null);
        finishEvent.setEventType("matchEnd");
        eventsToSend.addLast(finishEvent);
        sendEvents();
    }

    private void getFinishedMatch()
    {
        Controller.getApi().getMatchById(match.getId()).enqueue(new Callback<List<MatchPopulate>>() {
            @Override
            public void onResponse(@NonNull Call<List<MatchPopulate>> call, @NonNull Response<List<MatchPopulate>> response) {
                if (response.body() != null && response.isSuccessful() && response.body().size() > 0) {
                    MatchPopulate newMatch = response.body().get(0);
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("MATCH", newMatch);
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<MatchPopulate>> call, @NonNull Throwable t) { }
        });
    }
}
