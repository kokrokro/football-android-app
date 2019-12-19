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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.model.Event;
import baikal.web.footballapp.model.MatchPopulate;
import baikal.web.footballapp.user.activity.Protocol.Adapters.RVPenaltySeriesAdapter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class ProtocolPenalty extends AppCompatActivity {
    private static final String TAG = "ProtocolPenalty";

    private TextView scoreTeam1;
    private TextView scoreTeam2;
    private Button undo;
    private Button setTeamOrderBtn1;
    private Button setTeamOrderBtn2;
    private ImageButton penaltySuccessBtn;
    private ImageButton penaltyFailureBtn;

    private RVPenaltySeriesAdapter adapter1;
    private RVPenaltySeriesAdapter adapter2;

    private MatchPopulate match;

    int order=0;
    private int[] bgBtnColor;
    private final String[] teamIds = {null, null};
    private final String[] eventTypes =  {"goal", "yellowCard", "redCard", "penalty",
            "autoGoal", "foul", "penaltySeriesSuccess", "penaltySeriesFailure"};

    private LinkedList<Event> eventsToSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate ...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.protocol_penalty_series);
        ImageButton buttonBack = findViewById(R.id.PPS_protocolPenaltyBack);
        buttonBack.setOnClickListener(v -> {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("MATCH", match);
            intent.putExtras(bundle);
            setResult(RESULT_CANCELED, intent);
            finish();
        });

        TextView teamName1 = findViewById(R.id.PPS_team_name1);
        TextView teamName2 = findViewById(R.id.PPS_team_name2);
        RecyclerView recyclerViewTeam1 = findViewById(R.id.PPS_RV_team1);
        RecyclerView recyclerViewTeam2 = findViewById(R.id.PPS_RV_team2);
        scoreTeam1      = findViewById(R.id.PPS_team_score1);
        scoreTeam2      = findViewById(R.id.PPS_team_score2);
        undo             = findViewById(R.id.PPS_undo);
        setTeamOrderBtn1 = findViewById(R.id.PPS_set_penalty_order_btn1);
        setTeamOrderBtn2 = findViewById(R.id.PPS_set_penalty_order_btn2);
        penaltySuccessBtn = findViewById(R.id.PPS_penalty_successful);
        penaltyFailureBtn = findViewById(R.id.PPS_penalty_failure);

        bgBtnColor = new int[]{getResources().getColor(R.color.blue), getResources().getColor(R.color.red)};

        match = (MatchPopulate) getIntent().getExtras().getSerializable("MATCH");
        teamIds[0] = match.getTeamOne().getId();
        teamIds[1] = match.getTeamTwo().getId();

        teamName1.setText(match.getTeamOne().getName());
        teamName2.setText(match.getTeamTwo().getName());

        recyclerViewTeam1.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTeam2.setLayoutManager(new LinearLayoutManager(this));
        adapter1 = new RVPenaltySeriesAdapter(this, match.getEvents(), match.getTeamOne().getId());
        adapter2 = new RVPenaltySeriesAdapter(this, match.getEvents(), match.getTeamTwo().getId());
        recyclerViewTeam1.setAdapter(adapter1);
        recyclerViewTeam2.setAdapter(adapter2);

        setButtonsListeners();
        calculateScore();

        eventsToSend = new LinkedList<>();
        for (Event e: match.getEvents())
            if (e.getId() == null)
                eventsToSend.addLast(e);
    }

    private void setButtonsListeners() {
        setTeamOrderBtn1.setOnClickListener(v->changeOrder(2));
        setTeamOrderBtn2.setOnClickListener(v->changeOrder(1));

        penaltySuccessBtn.setOnClickListener(v->{
            if (order != 0) {
                addEvent(6);
                changeOrder(++order);
            }
        });

        penaltyFailureBtn.setOnClickListener(v->{
            if (order != 0) {
                addEvent(7);
                changeOrder(++order);
            }
        });

        undo.setOnClickListener(v->{
            if (eventsToSend.size() != 0) {
                eventsToSend.removeLast();

                for (int i=match.getEvents().size()-1; i>=0; i--) {
                    Event event = match.getEvents().get(i);
                    if (event.getEventType().equals("penaltySeriesSuccess") ||
                            event.getEventType().equals("penaltySeriesFailure"))
                        if (event.getId() == null) {
                            match.getEvents().remove(i);
                            break;
                        }
                }

                adapter1.notifyDataSetChanged();
                adapter2.notifyDataSetChanged();
                calculateScore();

                return;
            }

            for (int i=match.getEvents().size()-1; i>=0; i--) {
                Event event = match.getEvents().get(i);
                if (event.getEventType().equals("penaltySeriesSuccess") ||
                        event.getEventType().equals("penaltySeriesFailure")) {
                    if (event.getId() == null) {
                        match.getEvents().remove(i);
                        adapter1.notifyDataSetChanged();
                        adapter2.notifyDataSetChanged();
                        calculateScore();
                        return;
                    }

                    Event newEvent = new Event();
                    newEvent.setId(null);
                    newEvent.setEvent(event.getId());
                    newEvent.setEventType("disable");
                    match.addEvent(newEvent);
                    eventsToSend.addLast(event);
                    adapter1.notifyDataSetChanged();
                    adapter2.notifyDataSetChanged();
                    calculateScore();
                    sendEvents();
                    return;
                }
            }

            adapter1.notifyDataSetChanged();
            adapter2.notifyDataSetChanged();
            calculateScore();
        });
    }

    private void changeOrder (int order) {
        this.order = order;

        Log.d(TAG, String.valueOf(order));

        penaltySuccessBtn.setBackgroundColor(bgBtnColor[(this.order&1)]);
        penaltyFailureBtn.setBackgroundColor(bgBtnColor[(this.order&1)]);
    }

    private void addEvent(int i) {
        Event event = new Event();
        event.setId(null);
        event.setEventType(eventTypes[i]);
        event.setTime("penaltySeries");
        event.setTeam(teamIds[(order&1)]);
        match.addEvent(event);
        eventsToSend.addLast(event);
        adapter1.notifyDataSetChanged();
        adapter2.notifyDataSetChanged();
        calculateScore();
        sendEvents();

        if (checkEventsForEnd() != 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            @SuppressLint("InflateParams")
            View view = LayoutInflater.from(this).inflate(R.layout.yes_no_dialog_view, null);
            builder.setView(view);

            AlertDialog dialog = builder.create();

            TextView textView = view.findViewById(R.id.YNDV_text);
            Button no = view.findViewById(R.id.YNDV_btn_no);
            Button yes = view.findViewById(R.id.YNDV_btn_yes);
            textView.setText("Серия пенальти окончена.\nЗавершить матч ?");
            no.setOnClickListener(vv -> dialog.dismiss());
            yes.setOnClickListener(vv -> {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("MATCH", match);
                bundle.putBoolean("IS_FINISHED", true);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                dialog.dismiss();
                finish();
            });
            dialog.show();
        }
    }

    private int checkEventsForEnd()
    {
        List<List<Event> > eventsTeam1 = new ArrayList<>();
        List<List<Event> > eventsTeam2 = new ArrayList<>();
        TreeSet<String> disabledEvents = new TreeSet<>();

        List<Event> seriesCellT1 = new ArrayList<>();
        List<Event> seriesCellT2 = new ArrayList<>();
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

            if (e.getEventType().equals("penaltySeriesSuccess") ||
                    e.getEventType().equals("penaltySeriesFailure")) {
                if (e.getTeam().equals(match.getTeamOne().getId())) {
                    seriesCellT1.add(e);

                    if (seriesCellT1.size() == 3) {
                        eventsTeam1.add(new ArrayList<>());
                        eventsTeam1.get(eventsTeam1.size() - 1).addAll(seriesCellT1);
                        seriesCellT1.clear();
                    }
                }

                if (e.getTeam().equals(match.getTeamTwo().getId())) {
                    seriesCellT2.add(e);

                    if (seriesCellT2.size() == 3) {
                        eventsTeam1.add(new ArrayList<>());
                        eventsTeam1.get(eventsTeam1.size() - 1).addAll(seriesCellT2);
                        seriesCellT2.clear();
                    }
                }
            }
        }

        if (seriesCellT1.size() != 0) {
            eventsTeam1.add(new ArrayList<>());
            eventsTeam1.get(eventsTeam1.size()-1).addAll(seriesCellT1);
            seriesCellT1.clear();
        }

        if (seriesCellT2.size() != 0) {
            eventsTeam2.add(new ArrayList<>());
            eventsTeam2.get(eventsTeam2.size()-1).addAll(seriesCellT2);
            seriesCellT2.clear();
        }

        if (eventsTeam1.size() == eventsTeam2.size()) {
            int freeSlots1 = 3 - eventsTeam1.get(eventsTeam1.size()-1).size();
            int freeSlots2 = 3 - eventsTeam2.get(eventsTeam2.size()-1).size();
            int goals1 = getGoals(eventsTeam1.get(eventsTeam1.size()-1));
            int goals2 = getGoals(eventsTeam2.get(eventsTeam2.size()-1));

            if (freeSlots2 + goals2 < goals1)
                return 1;

            if (freeSlots1 + goals1 < goals2)
                return -1;
        }

        return 0;
    }

    private int getGoals (List<Event> events) {
        int ans=0;
        for (Event e: events)
            if (e.getEventType().equals("penaltySeriesSuccess"))
                ans++;

        return ans;
    }

    @SuppressLint("SetTextI18n")
    void calculateScore()
    {
        int goalCntTeam1 = 0;
        int goalCntTeam2 = 0;

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

            if (teamIds[0] != null && e.getTeam().equals(teamIds[0])) {
                if (e.getEventType().equals(eventTypes[0])     ||
                        e.getEventType().equals(eventTypes[3]) ||
                        e.getEventType().equals(eventTypes[6]))
                    goalCntTeam1++;

                if (e.getEventType().equals(eventTypes[4]))
                    goalCntTeam2++;
            }

            if (teamIds[1] != null && e.getTeam().equals(teamIds[1])) {
                if (e.getEventType().equals(eventTypes[0])     ||
                        e.getEventType().equals(eventTypes[3]) ||
                        e.getEventType().equals(eventTypes[6]))
                    goalCntTeam2++;

                if (e.getEventType().equals(eventTypes[4]))
                    goalCntTeam1++;
            }
        }

        scoreTeam1.setText(String.valueOf(goalCntTeam1));
        scoreTeam2.setText(String.valueOf(goalCntTeam2));
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
                                    Toast.makeText(ProtocolPenalty.this, "Синхронизированно с сервером", Toast.LENGTH_SHORT).show();
                                    match.addEventWithId(newEvent);
                                    adapter1.notifyDataSetChanged();
                                    adapter2.notifyDataSetChanged();
                                    calculateScore();
                                    sendEvents();
                                }
                            },
                            error ->
                                    Log.d(TAG, error.toString())
                    );
        }
    }
}
