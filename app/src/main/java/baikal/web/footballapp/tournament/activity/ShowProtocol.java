package baikal.web.footballapp.tournament.activity;


import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import baikal.web.footballapp.MankindKeeper;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SetImage;
import baikal.web.footballapp.model.Club;
import baikal.web.footballapp.model.Event;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.Match;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.Player;
import baikal.web.footballapp.model.PlayerEvent;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.model.TeamTitleClubLogoMatchEvents;
import baikal.web.footballapp.user.activity.MatchEvents;
import baikal.web.footballapp.user.activity.MatchResponsiblePersons;
import baikal.web.footballapp.user.activity.ProtocolScore;
import baikal.web.footballapp.user.activity.StructureCommand1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShowProtocol extends AppCompatActivity {
    private final Logger log = LoggerFactory.getLogger(baikal.web.footballapp.user.activity.ConfirmProtocol.class);
    private List<PlayerEvent> playerEvents;
    private String clubOne;
    private String clubTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ImageButton imageClose;
        ImageButton buttonCommand1;
        ImageButton buttonCommand2;
        ImageButton buttonReferees;
        ImageButton buttonEvents;
        FloatingActionButton fab;
        TextView textTitle1;
        TextView textTitle2;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_protocol);
        fab = findViewById(R.id.confirmProtocolButtonShowScore);
        buttonCommand1 = findViewById(R.id.confirmProtocolCommand1ButtonShow);
        buttonCommand2 = findViewById(R.id.confirmProtocolCommand2ButtonShow);
        buttonReferees = findViewById(R.id.confirmProtocolRefereesButtonShow);
        buttonEvents = findViewById(R.id.confirmProtocolEventsButtonShow);
        imageClose = findViewById(R.id.confirmProtocolClose);
        textTitle1 = findViewById(R.id.confirmProtocolCommand1Title);
        textTitle2 = findViewById(R.id.confirmProtocolCommand2Title);
        imageClose.setOnClickListener(v -> finish());
        try {
            String str;
            Match match = (Match) getIntent().getExtras().getSerializable("SHOWPROTOCOL");
            HashMap<String, Team> teams = getTeams(match);
            str = teams.get("TeamOne").getName();
            textTitle1.setText(str);
            str = teams.get("TeamTwo").getName();
            textTitle2.setText(str);

            TeamTitleClubLogoMatchEvents entry = null;
            if (match != null)
                entry = getPlayerEvent(match.getEvents(), match, teams.get("TeamOne"), teams.get("TeamTwo"));

            try {
                if (entry != null)
                    playerEvents = new ArrayList<>(entry.getPlayerEvents());
            } catch (NullPointerException e) {
                playerEvents = new ArrayList<>();
            }

            fab.setOnClickListener(v -> {
                List<Event> list = new ArrayList<>();
                for (PlayerEvent playerEvent : playerEvents) {
                    list.add(playerEvent.getEvent());
                }
                TeamTitleClubLogoMatchEvents playerEv = getPlayerEvent(list, match, teams.get("TeamOne"), teams.get("TeamTwo"));
//                log.error("-----------------------------------------------");
//                log.error("ERROR: ", playerEv.getPlayerEvents().toString());
//                log.error("-----------------------------------------------");
                Intent intent = new Intent(ShowProtocol.this, ProtocolScore.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("PROTOCOLMATCH", match);
                bundle.putSerializable("PROTOCOLEVENTS", playerEv);
                intent.putExtras(bundle);
                startActivity(intent);
            });
            buttonCommand1.setOnClickListener(v -> {
                Intent intent = new Intent(ShowProtocol.this, StructureCommand1.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("CONFIRMPROTOCOLMATCH", match);
                bundle.putSerializable("CONFIRMPROTOCOLCOMMAND", teams.get("TeamOne"));
                intent.putExtras(bundle);
                startActivity(intent);
            });
            buttonCommand2.setOnClickListener(v -> {
                Intent intent = new Intent(ShowProtocol.this, StructureCommand1.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("CONFIRMPROTOCOLMATCH", match);
                bundle.putSerializable("CONFIRMPROTOCOLCOMMAND", teams.get("TeamTwo"));
                intent.putExtras(bundle);
                startActivity(intent);
            });
            buttonReferees.setOnClickListener(v -> {
                Intent intent = new Intent(ShowProtocol.this, MatchResponsiblePersons.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("CONFIRMPROTOCOLREFEREES", (Serializable) match.getReferees());
                intent.putExtras(bundle);
                startActivity(intent);
            });

            buttonEvents.setOnClickListener(v -> {
                List<Event> list = new ArrayList<>();
                for (PlayerEvent playerEvent : playerEvents) {
                    list.add(playerEvent.getEvent());
                }
                TeamTitleClubLogoMatchEvents playerEv = getPlayerEvent(list, match, teams.get("TeamOne"), teams.get("TeamTwo"));
                log.error("-----------------------------------------------");
                log.error("ERROR: ", playerEv.getPlayerEvents().toString());
                log.error(String.valueOf(playerEv.getPlayerEvents().size()));
                log.error("-----------------------------------------------");
                Intent intent = new Intent(ShowProtocol.this, MatchEvents.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("PROTOCOLEVENTS", playerEv);
                intent.putExtras(bundle);
                startActivity(intent);
            });
        } catch (NullPointerException e) {
        }
    }



    private HashMap<String, Team> getTeams(Match match) {
        HashMap<String, Team> teams = new HashMap<>();
        ImageView image1 = findViewById(R.id.confirmProtocolCommand1Logo);
        ImageView image2 = findViewById(R.id.confirmProtocolCommand2Logo);
        SetImage setImage = new SetImage();
        for (League league : PersonalActivity.tournaments) {
            if (teams.size() != 2 && match.getLeague().equals(league.getId())) {
                for (Team team : league.getTeams()) {
                    if (team.getId().equals(match.getTeamOne())
                            || team.getId().equals(match.getTeamTwo())) {
                        if (team.getId().equals(match.getTeamOne())) {
                            teams.put("TeamOne", team);
                        }
                        if (team.getId().equals(match.getTeamTwo())) {
                            teams.put("TeamTwo", team);
                        }
                        for (Club club : PersonalActivity.allClubs) {
                            if (team.getId().equals(match.getTeamOne())
                                    && team.getClub().equals(club.getId())) {
                                try {
                                    clubOne = club.getLogo();
                                } catch (NullPointerException e) {
                                    clubOne = "";
                                }
                                setImage.setImage(image1.getContext(), image1, club.getLogo());
                            }
                            if (team.getId().equals(match.getTeamTwo())
                                    && team.getClub().equals(club.getId())) {
                                try {
                                    clubTwo = club.getLogo();
                                } catch (NullPointerException e) {
                                    clubTwo = "";
                                }
                                setImage.setImage(image2.getContext(), image2, club.getLogo());
                            }
                        }
                    }
                }
            }
        }

        return teams;
    }

    private TeamTitleClubLogoMatchEvents getPlayerEvent(List<Event> events, Match match, @Nullable Team team1, @Nullable Team team2) {
        log.error(match.getId());
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
            log.error("-----------------------------------------");
            log.error("EVENT");
            log.error(event.getEventType());
            log.error("-----------------------------------------");
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

        log.error("===========================================");
        log.error("EVENT");
        log.error(String.valueOf(playerEvents1.size()));
        log.error(String.valueOf(entry.getPlayerEvents().size()));
        log.error("===========================================");
        entry.setClubLogo1(clubOne);
        entry.setClubLogo2(clubTwo);
        entry.setNameTeam1(teamOne);
        entry.setNameTeam2(teamTwo);
//        return playerEvents;
        return entry;
    }


}

