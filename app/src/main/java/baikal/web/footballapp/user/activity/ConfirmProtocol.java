package baikal.web.footballapp.user.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.MankindKeeper;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.SetImage;
import baikal.web.footballapp.model.Club;
import baikal.web.footballapp.model.Match;
import baikal.web.footballapp.model.MatchPopulate;
import baikal.web.footballapp.model.Referee;
import baikal.web.footballapp.model.Team;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmProtocol extends AppCompatActivity {
    private static final String TAG = "ConfirmProtocol";
    private static final int EDIT_PROTOCOL_SUCCESS = 9741;
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
            boolean status = Objects.requireNonNull(initialIntent.getExtras()).getBoolean("STATUS");
            if(!status){
                fab.setVisibility(View.GONE);
            }
        }catch (Exception ignored){}
        try {
            match = (MatchPopulate) Objects.requireNonNull(initialIntent.getExtras()).getSerializable("CONFIRMPROTOCOL");
            HashMap<String, Team> teams = null;
            if (match != null) {
                teams = getTeams(match);
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


            if (teams != null && teams.get("TeamOne") != null)
                textTitle1.setText(Objects.requireNonNull(teams.get("TeamOne")).getName());
            if (teams != null && teams.get("TeamTwo") != null)
                textTitle2.setText(Objects.requireNonNull(teams.get("TeamTwo")).getName());

            imageSave.setOnClickListener(v -> confirmProtocol(match != null ? match.getId() : null));
            HashMap<String, Team> finalTeams1 = teams;
            buttonCommand1.setOnClickListener(v -> {
                if (match != null && match.getTeamOne() == null)
                    return;
                Intent intent = new Intent(ConfirmProtocol.this, StructureCommand1.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("CONFIRMPROTOCOLMATCH", match);
                bundle.putSerializable("CONFIRMPROTOCOLCOMMAND", finalTeams1 != null ? finalTeams1.get("TeamOne") : null);
                intent.putExtras(bundle);

                Log.d(TAG, "team1 ...");

                startActivity(intent);
            });
            buttonCommand2.setOnClickListener(v -> {
                if (match != null && match.getTeamTwo() == null)
                    return;
                Intent intent = new Intent(ConfirmProtocol.this, StructureCommand1.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("CONFIRMPROTOCOLMATCH", match);
                bundle.putSerializable("CONFIRMPROTOCOLCOMMAND", finalTeams1 != null ? finalTeams1.get("TeamTwo") : null);
                intent.putExtras(bundle);

                Log.d(TAG, "team2 ...");

                startActivity(intent);
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
                boolean status = Objects.requireNonNull(initialIntent.getExtras()).getBoolean("STATUS");
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
        if (id == null)
            return;

        match.setPlayed(true);

        String token = SaveSharedPreference.getObject().getToken();
        Match newMatch = new Match(match);
        //noinspection ResultOfMethodCallIgnored
        Controller.getApi().editProtocolMatch(id, token, newMatch)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newMatch2 -> {
                            if (newMatch2 != null) {
                                match.onProtocolEdited(newMatch2);
                                Toast.makeText(ConfirmProtocol.this, "Протокол сохранён", Toast.LENGTH_SHORT).show();
                            }
                        },
                        error -> {
                            log.debug("===================================");

                            if (error.getMessage() != null) {
                                Log.e(TAG, error.getMessage());

                                if (error.getMessage().contains("HTTP 500"))
                                    syncProtocolOneMoreTime();
                            }
                        });
    }

    private HashMap<String, Team> getTeams(MatchPopulate match) {
        HashMap<String, Team> teams = new HashMap<>();
        ImageView image1 = findViewById(R.id.confirmProtocolCommand1Logo);
        ImageView image2 = findViewById(R.id.confirmProtocolCommand2Logo);
        SetImage setImage = new SetImage();
                            teams.put("TeamOne", match.getTeamOne());
                            teams.put("TeamTwo", match.getTeamTwo());

        for (Club club : MankindKeeper.getInstance().allClubs) {
            if (match.getTeamOne().getClub().equals(club.getId()))
                setImage.setImage(image1.getContext(), image1, club.getLogo());
            if (match.getTeamTwo().getClub().equals(club.getId()))
                setImage.setImage(image2.getContext(), image2, club.getLogo());
        }
        return teams;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_PROTOCOL_SUCCESS) {
            if (data != null && data.getExtras() != null)
                match = (MatchPopulate) data.getExtras().getSerializable("FINISHED_MATCH");
        }
    }

    @SuppressLint("CheckResult")
    void syncProtocolOneMoreTime() {
        Controller.getApi().getMatchById(match.getId()).enqueue(new Callback<List<MatchPopulate>>() {
            @Override
            public void onResponse(@NonNull Call<List<MatchPopulate>> call, @NonNull Response<List<MatchPopulate>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    match.setV(response.body().get(0).getV());
                    String token = SaveSharedPreference.getObject().getToken();
                    Match newMatch = new Match(match);

                    //noinspection ResultOfMethodCallIgnored
                    Controller.getApi().editProtocolMatch(match.getId(), token, newMatch)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(newMatch2 -> {
                                        if (newMatch2 != null) {
                                            match.onProtocolEdited(newMatch2);
                                            Toast.makeText(ConfirmProtocol.this, "Протокол сохранён", Toast.LENGTH_SHORT).show();
                                        }
                                    },
                                    error -> {
                                        log.debug("===================================");

                                        if (error.getMessage() != null) {
                                            Log.e(TAG, error.getMessage());

                                            if (error.getMessage().contains("HTTP 500"))
                                                syncProtocolOneMoreTime();
                                        }
                                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<MatchPopulate>> call, @NonNull Throwable t) {

            }
        });
    }
}

