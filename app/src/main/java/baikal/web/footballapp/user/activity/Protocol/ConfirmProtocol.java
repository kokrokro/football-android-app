package baikal.web.footballapp.user.activity.Protocol;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Objects;

import baikal.web.footballapp.R;
import baikal.web.footballapp.model.MatchPopulate;
import baikal.web.footballapp.model.Referee;
import baikal.web.footballapp.user.activity.MatchResponsiblePersons;
import baikal.web.footballapp.user.activity.StructureCommand1;

public class ConfirmProtocol extends AppCompatActivity {
    private static final String TAG = "ConfirmProtocol";
    private static final int EDIT_PROTOCOL_SUCCESS = 9741;
    private static final int EDIT_TEAM_ONE = 741;
    private static final int EDIT_TEAM_TWO = 742;
    private final Logger log = LoggerFactory.getLogger(ConfirmProtocol.class);
    private MatchPopulate match;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ImageButton imageClose;
        ImageButton imageSave;
        ImageButton buttonCommand1;
        ImageButton buttonCommand2;
        ImageButton buttonReferees;
        ImageButton buttonEvents;
        FloatingActionButton fab;
        TextView textTitle1;
        TextView textTitle2;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_protocol);
        fab = findViewById(R.id.confirmProtocolButtonShowScore);
        buttonCommand1 = findViewById(R.id.confirmProtocolCommand1ButtonShow);
        buttonCommand2 = findViewById(R.id.confirmProtocolCommand2ButtonShow);
        buttonReferees = findViewById(R.id.confirmProtocolRefereesButtonShow);
        buttonEvents = findViewById(R.id.confirmProtocolEventsButtonShow);
        imageClose = findViewById(R.id.confirmProtocolClose);
        imageSave = findViewById(R.id.confirmProtocolSave);
        textTitle1 = findViewById(R.id.confirmProtocolCommand1Title);
        textTitle2 = findViewById(R.id.confirmProtocolCommand2Title);
        imageClose.setOnClickListener(v -> finish());
        Intent initialIntent = getIntent();
        try{
            boolean status = Objects.requireNonNull(initialIntent.getExtras()).getBoolean("IS_EDITABLE");
            if(!status){
                fab.setVisibility(View.GONE);
            }
        }catch (Exception ignored){}
        try {
            match = (MatchPopulate) Objects.requireNonNull(initialIntent.getExtras()).getSerializable("CONFIRMPROTOCOL");
            if (match != null) {
                fab.setOnClickListener(v -> {
                    try {
                        Intent intent = new Intent(ConfirmProtocol.this, ProtocolScore.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("MATCH", match);
                        intent.putExtras(bundle);

                        Log.d(TAG, "trying to enter to work protocol...");

                        startActivityForResult(intent, EDIT_PROTOCOL_SUCCESS);
                    } catch (Exception e) {
                        log.error("ERROR", e);
                    }
                });
            }

            if (match.getTeamOne() != null)
                textTitle1.setText(match.getTeamOne().getName());
            if (match.getTeamTwo() != null)
                textTitle2.setText(match.getTeamTwo().getName());

            imageSave.setOnClickListener(v -> confirmProtocol(match != null ? match.getId() : null));
            buttonCommand1.setOnClickListener(v -> {
                if (match != null && match.getTeamOne() == null)
                    return;
                boolean status = Objects.requireNonNull(initialIntent.getExtras()).getBoolean("IS_EDITABLE");
                Intent intent = new Intent(ConfirmProtocol.this, StructureCommand1.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("MATCH_EDIT_TEAM", match);
                bundle.putSerializable("TEAM", match.getTeamOne());
                bundle.putBoolean("IS_EDITABLE", status);
                intent.putExtras(bundle);

                Log.d(TAG, "team1 ...");

                startActivityForResult(intent, EDIT_TEAM_ONE);
            });
            buttonCommand2.setOnClickListener(v -> {
                if (match != null && match.getTeamTwo() == null)
                    return;
                boolean status = Objects.requireNonNull(initialIntent.getExtras()).getBoolean("IS_EDITABLE");
                Intent intent = new Intent(ConfirmProtocol.this, StructureCommand1.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("MATCH_EDIT_TEAM", match);
                bundle.putSerializable("TEAM", match.getTeamTwo());
                bundle.putBoolean("IS_EDITABLE", status);
                intent.putExtras(bundle);

                Log.d(TAG, "team2 ...");

                startActivityForResult(intent, EDIT_TEAM_TWO);
            });
            buttonReferees.setOnClickListener(v -> {
                Intent intent = new Intent(ConfirmProtocol.this, MatchResponsiblePersons.class);
                Bundle bundle = new Bundle();

                ArrayList<CharSequence> refIds = new ArrayList<>();
                refIds.add("");
                refIds.add("");
                refIds.add("");
                refIds.add("");

                if (match != null)
                    for (Referee r : match.getReferees())
                        switch (r.getType()) {
                            case "firstReferee":
                                refIds.set(0, r.getPerson());
                                break;
                            case "secondReferee":
                                refIds.set(1, r.getPerson());
                                break;
                            case "thirdReferee":
                                refIds.set(2, r.getPerson());
                                break;
                            case "timekeeper":
                                refIds.set(3, r.getPerson());
                        }

                bundle.putCharSequenceArrayList("CONFIRMPROTOCOLREFEREES", refIds);
                intent.putExtras(bundle);
                startActivity(intent);
            });

            buttonEvents.setOnClickListener(v -> {
                boolean status = Objects.requireNonNull(initialIntent.getExtras()).getBoolean("IS_EDITABLE");
                Intent intent = new Intent(ConfirmProtocol.this, MatchEvents.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("MATCH", match);
                bundle.putBoolean("IS_EDITABLE", status);
                intent.putExtras(bundle);

                startActivity(intent);
            });
        } catch (Exception e) {
            log.error("ERROR", e);
            Log.d(TAG, "132");
        }
    }

    @SuppressLint("CheckResult")
    private void confirmProtocol(@Nullable String id) {
//        if (id == null)
//            return;

//        String token = SaveSharedPreference.getObject().getToken();
//        Event event = new Event();
////        noinspection ResultOfMethodCallIgnored
//        Controller.getApi().editProtocolMatch(id, token, event)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(newEvent -> {
//                            if (newEvent != null) {
//                                match.addEvent(newEvent);
//                                Toast.makeText(ConfirmProtocol.this, "Протокол сохранён", Toast.LENGTH_SHORT).show();
//                            }
//                        },
//                        error -> {
//                            log.debug("===================================");
//
//                            if (error.getMessage() != null)
//                                Log.e(TAG, error.getMessage());
//                        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_PROTOCOL_SUCCESS)
            if (data != null && data.getExtras() != null)
                match = (MatchPopulate) data.getExtras().getSerializable("MATCH");

        if ((requestCode == EDIT_TEAM_ONE || requestCode == EDIT_TEAM_TWO) && resultCode == RESULT_OK)
            if (data != null && data.getExtras() != null)
                match = (MatchPopulate) data.getExtras().getSerializable("MATCH_EDITED_TEAM");
    }
}

