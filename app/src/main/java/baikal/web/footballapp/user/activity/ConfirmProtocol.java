package baikal.web.footballapp.user.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import baikal.web.footballapp.CheckError;
import baikal.web.footballapp.Controller;
import baikal.web.footballapp.MankindKeeper;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.SetImage;
import baikal.web.footballapp.model.Club;
import baikal.web.footballapp.model.Event;
import baikal.web.footballapp.model.MatchPopulate;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.Player;
import baikal.web.footballapp.model.PlayerEvent;
import baikal.web.footballapp.model.Referee;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.model.TeamTitleClubLogoMatchEvents;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ConfirmProtocol extends AppCompatActivity {
    private final Logger log = LoggerFactory.getLogger(ConfirmProtocol.class);
    private List<PlayerEvent> playerEvents;
    private String clubOne;
    private String clubTwo;

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
        try {
            String str;
            MatchPopulate match = (MatchPopulate) getIntent().getExtras().getSerializable("CONFIRMPROTOCOL");
            HashMap<String, Team> teams = getTeams(match);
            fab.setOnClickListener(v -> {
                try {
                    List<Event> list = new ArrayList<>();
                    for (PlayerEvent playerEvent : playerEvents)
                        list.add(playerEvent.getEvent());

                    TeamTitleClubLogoMatchEvents playerEv = getPlayerEvent(list, match, teams.get("TeamOne"), teams.get("TeamTwo"));
                    Intent intent = new Intent(ConfirmProtocol.this, ProtocolScore.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("PROTOCOLMATCH", match);
                    bundle.putSerializable("PROTOCOLEVENTS", playerEv);
                    intent.putExtras(bundle);

                    Log.d("ConfirmProtocol.java", "trying to enter to work protocol...");

                    startActivity(intent);
                } catch (Exception e) {
                    log.error("ERROR", e);
                }
            });

            str = teams.get("TeamOne").getName();
            textTitle1.setText(str);
            str = teams.get("TeamTwo").getName();
            textTitle2.setText(str);
            TeamTitleClubLogoMatchEvents entry = getPlayerEvent(match.getEvents(), match, teams.get("TeamOne"), teams.get("TeamTwo"));
            try {
                playerEvents = new ArrayList<>(entry.getPlayerEvents());
            } catch (NullPointerException e) {
                playerEvents = new ArrayList<>();
            }
            imageSave.setOnClickListener(v -> confirmProtocol(match.getId()));
            buttonCommand1.setOnClickListener(v -> {
                Intent intent = new Intent(ConfirmProtocol.this, StructureCommand1.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("CONFIRMPROTOCOLMATCH", match);
                bundle.putSerializable("CONFIRMPROTOCOLCOMMAND", teams.get("TeamOne"));
                intent.putExtras(bundle);

                Log.d("ConfirmProtocol: ", "team1 ...");

                startActivity(intent);
            });
            buttonCommand2.setOnClickListener(v -> {
                Intent intent = new Intent(ConfirmProtocol.this, StructureCommand1.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("CONFIRMPROTOCOLMATCH", match);
                bundle.putSerializable("CONFIRMPROTOCOLCOMMAND", teams.get("TeamTwo"));
                intent.putExtras(bundle);

                Log.d("ConfirmProtocol: ", "team2 ...");

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
                Intent intent = new Intent(ConfirmProtocol.this, MatchEvents.class);
                startActivity(intent);
            });
        } catch (Exception e) {
            log.error("ERROR", e);
            Log.d("ConfirmProtocol.java", "132");
        }
    }

    @SuppressLint("CheckResult")
    private void confirmProtocol(String id) {
        CheckError checkError = new CheckError();
        Controller.getApi().confirmProtocol(SaveSharedPreference.getObject().getToken(), id)
                .map(responseBody -> {
                    if (!responseBody.isSuccessful()) {
                        String srt = responseBody.errorBody().string();
                        log.error(srt + " 141");
                        showToast(srt);
                    }
                    if (responseBody.errorBody() != null) {
                        checkError.checkHttpError(this, responseBody.errorBody().string());
                    }
                    return responseBody.body();
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                            String str = "Изменения сохранены";
                            showToastResult(str);
                            finish();
                        },
                        error -> checkError.checkError(this, error));

    }

    private HashMap<String, Team> getTeams(MatchPopulate match) {
        HashMap<String, Team> teams = new HashMap<>();
        ImageView image1 = findViewById(R.id.confirmProtocolCommand1Logo);
        ImageView image2 = findViewById(R.id.confirmProtocolCommand2Logo);
        SetImage setImage = new SetImage();
                            teams.put("TeamOne", match.getTeamOne());
                            teams.put("TeamTwo", match.getTeamTwo());
        //TODO
        //MAKE ACCESS THROUGH SINGLETON
                        for (Club club : MankindKeeper.getInstance().allClubs) {
                            if (match.getTeamOne().getClub().equals(club.getId())) {
                                try {
                                    clubOne = club.getLogo();
                                } catch (NullPointerException e) {
                                    clubOne = "";
                                }
                                setImage.setImage(image1.getContext(), image1, club.getLogo());
                            }
                            if (match.getTeamTwo().getClub().equals(club.getId())) {
                                try {
                                    clubTwo = club.getLogo();
                                } catch (NullPointerException e) {
                                    clubTwo = "";
                                }
                                setImage.setImage(image2.getContext(), image2, club.getLogo());
                            }
                        }
        return teams;
    }

    private TeamTitleClubLogoMatchEvents getPlayerEvent(List<Event> events, MatchPopulate match, Team team1, Team team2) {
        List<PlayerEvent> playerEvents1 = new ArrayList<>();
        String teamOne = team1.getName();
        String teamTwo = team2.getName();
        String clubEvent = "";
        String teamName = "";
        for (Event event : events) {
            Person person = MankindKeeper.getInstance().allPlayers.get(event.getPlayer());

            for (Player player : team1.getPlayers()) {
                if (player.getPlayerId().equals(person.getId())) {
                    clubEvent = clubOne;
                    teamName = team1.getName();
                }
            }
            for (Player player : team2.getPlayers()) {
                if (player.getPlayerId().equals(person.getId())) {
                    clubEvent = clubTwo;
                    teamName = team2.getName();
                }
            }

            PlayerEvent playerEvent = new PlayerEvent();
            playerEvent.setPerson(person);
            try {
                playerEvent.setClubLogo(clubEvent);
            } catch (Exception e) {
                playerEvent.setClubLogo(null);
            }
            playerEvent.setEvent(event);
            playerEvent.setNameTeam(teamName);
            playerEvents1.add(playerEvent);
        }
        if (match.getEvents().isEmpty()) {
            playerEvents1 = null;
        }

        TeamTitleClubLogoMatchEvents entry = new TeamTitleClubLogoMatchEvents();
        entry.setPlayerEvents(playerEvents1);
        entry.setClubLogo1(clubOne);
        entry.setClubLogo2(clubTwo);
        entry.setNameTeam1(teamOne);
        entry.setNameTeam2(teamTwo);

        return entry;
    }


    private void showToast(String str) {
        try {
            JSONObject jsonObject = new JSONObject(str);
            String str1 = jsonObject.getString("message");
            log.error(str + " 246");
            this.runOnUiThread(() -> Toast.makeText(ConfirmProtocol.this, str1, Toast.LENGTH_SHORT).show());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void showToastResult(String str) {
            this.runOnUiThread(() -> Toast.makeText(ConfirmProtocol.this, str, Toast.LENGTH_SHORT).show());
    }
}

