package baikal.web.footballapp.user.activity;

import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.SetImage;
import baikal.web.footballapp.model.Club;
import baikal.web.footballapp.model.EditProtocolBody;
import baikal.web.footballapp.model.Event;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.Match;
import baikal.web.footballapp.model.Matches;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.Player;
import baikal.web.footballapp.model.PlayerEvent;
import baikal.web.footballapp.model.Referee;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.model.TeamTitleClubLogoMatchEvents;
import baikal.web.footballapp.model.User;


import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProtocolEdit extends AppCompatActivity {
    private static List<Integer> items;
    private NestedScrollView scroller;
    private final Logger log = LoggerFactory.getLogger(ProtocolEdit.class);
    private final int REQUEST_CODE_PROTOCOLTEAM = 286;
    private final int REQUEST_CODE_PROTOCOLTREFEREE = 296;
    private final int REQUEST_CODE_PROTOCOLEVENTS = 276;
    private List<String> players;
    private List<String> referees;
    private List<Event> events;
    private List<PlayerEvent> playerEvents;
    private Match match;
    private int matchOldPosition;
    private String clubOne;
    private String clubTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        items = new ArrayList<>();
        items.add(1);
        TextView textTitle1;
        TextView textTitle2;
        ImageButton buttonReferees;
        ImageButton buttonShowCommand1;
        ImageButton buttonShowCommand2;
        ImageButton buttonEvents;
        ImageButton imageClose;
        ImageButton imageSave;
        FloatingActionButton fab;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.protocol_edit);
        textTitle1 = findViewById(R.id.editProtocolCommand1Title);
        textTitle2 = findViewById(R.id.editProtocolCommand2Title);
        buttonShowCommand1 = findViewById(R.id.editProtocolCommand1ButtonShow);
        buttonShowCommand2 = findViewById(R.id.editProtocolCommand2ButtonShow);
        buttonReferees = findViewById(R.id.editProtocolRefereesButtonShow);
        buttonEvents = findViewById(R.id.editProtocolEventsButtonShow);
        fab = findViewById(R.id.editProtocolButtonShowScore);
        imageClose = findViewById(R.id.editProtocolClose);
        imageSave = findViewById(R.id.editProtocolSave);
        scroller = findViewById(R.id.scrollProtocolEdit);


        try {
            Intent arguments = getIntent();
            match = (Match) arguments.getExtras().getSerializable("PROTOCOLMATCH");
            matchOldPosition = arguments.getExtras().getInt("MATCHPOSITION");
            final Team team1 = (Team) arguments.getExtras().getSerializable("PROTOCOLTEAM1");
            final Team team2 = (Team) arguments.getExtras().getSerializable("PROTOCOLTEAM2");
            String club1 = arguments.getExtras().getString("PROTOCOLCLUB1");
            String club2 = arguments.getExtras().getString("PROTOCOLCLUB2");
//            textTitle1.setText(team1.getName());
//            textTitle2.setText(team2.getName());
            String str;
            HashMap<String, Team> teams = getTeams(match);
            str = teams.get("TeamOne").getName();
            textTitle1.setText(str);
            str = teams.get("TeamTwo").getName();
            textTitle2.setText(str);
            TeamTitleClubLogoMatchEvents entry = getPlayerEvent(match.getEvents(), match, teams.get("TeamOne"), teams.get("TeamTwo"));
            try {
                playerEvents = new ArrayList<>(entry.getPlayerEvents());
            }
            catch (NullPointerException e){
                playerEvents = new ArrayList<>();
            }

            players = new ArrayList<>(match.getPlayersList());
            events = new ArrayList<>(match.getEvents());
            referees = new ArrayList<>();
            referees.add("0");
            referees.add("0");
            referees.add("0");
            referees.add("0");
            List<Referee> refereesMatch = new ArrayList<>(match.getReferees());
            for (Referee referee : refereesMatch) {
                switch (referee.getType()) {
                    case "Инспектор":
                        referees.set(0, referee.getPerson());
                        break;
                    case "1 судья":
                        referees.set(1, referee.getPerson());
                        break;
                    case "2 судья":
                        referees.set(2, referee.getPerson());
                        break;
                    case "хронометрист":
                        referees.set(3, referee.getPerson());
                        break;
                    default:
                        break;
                }
            }
            buttonShowCommand2.setOnClickListener(v -> {
                Intent intent = new Intent(ProtocolEdit.this, ProtocolCommand1.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("PROTOCOLTEAMPLAYERS", team2);
                bundle.putStringArrayList("PROTOCOLTEAMMATCH", (ArrayList<String>) players);
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_CODE_PROTOCOLTEAM);
            });
            buttonShowCommand1.setOnClickListener(v -> {
                Intent intent = new Intent(ProtocolEdit.this, ProtocolCommand1.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("PROTOCOLTEAMPLAYERS", team1);
                bundle.putStringArrayList("PROTOCOLTEAMMATCH", (ArrayList<String>) players);
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_CODE_PROTOCOLTEAM);
            });

            buttonEvents.setOnClickListener(v -> {
                if (players.size() != 0) {
//                        TeamTitleClubLogoMatchEvents entry = getPlayerEvent(match.getEvents(), match);
//                        List<PlayerEvent> playerEvents;
//                        try {
//                            playerEvents = new ArrayList<>(entry.getPlayerEvents());
//                        }
//                        catch (NullPointerException e){
//                            playerEvents = new ArrayList<>();
//                        }
                    Intent intent = new Intent(ProtocolEdit.this, ProtocolEventsEdit.class);
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("PROTOCOLTEAMMATCH", (ArrayList<String>) players);
                    bundle.putSerializable("PROTOCOLEVENTS", (Serializable) playerEvents);
                    bundle.putSerializable("PROTOCOLMATCH", match);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, REQUEST_CODE_PROTOCOLEVENTS);
                } else {
                    Toast.makeText(ProtocolEdit.this, "Выберите игроков обеих команд!", Toast.LENGTH_SHORT).show();
                }
            });
            buttonReferees.setOnClickListener(v -> {
                Intent intent = new Intent(ProtocolEdit.this, ResponsiblePersons.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("PROTOCOLREFEREES", (ArrayList<String>) referees);
                bundle.putSerializable("PROTOCOLMATCH", match);
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_CODE_PROTOCOLTREFEREE);
            });
            fab.setOnClickListener(v -> {
//                    List<PlayerEvent> playerEvents = new ArrayList<>(getPlayerEvent(match.getEvents(), match));
//                TeamTitleClubLogoMatchEvents playerEv= getPlayerEvent(match.getEvents(), match);
                List<Event> list = new ArrayList<>();
                for (PlayerEvent playerEvent : playerEvents){
                    list.add(playerEvent.getEvent());
                }
                TeamTitleClubLogoMatchEvents playerEv = getPlayerEvent(list, match, teams.get("TeamOne"), teams.get("TeamTwo"));
                Intent intent = new Intent(ProtocolEdit.this, ProtocolMatchScore.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("PROTOCOLMATCH", match);
                bundle.putSerializable("PROTOCOLEVENTS", playerEv);
                intent.putExtras(bundle);
                startActivity(intent);
            });
            imageSave.setOnClickListener(v -> {
                if (checkProtocol()){
                    editProtocolRequest();
                }else {
                    String str1;
                    str1 = "Ошибка. Список событий не совпадает со списком выбранных игроков.";
                    Toast.makeText(ProtocolEdit.this, str1, Toast.LENGTH_SHORT).show();
                }
            });

            imageClose.setOnClickListener(v -> {
                try {
                    if (MyMatches.matches.size()==1){
                        MyMatches.matches.clear();
                        MyMatches.matches.add(match);

                    }else {
                        MyMatches.matches.set(matchOldPosition, match);
                    }
                    List<Match> matches = new ArrayList<>(MyMatches.matches);
                    MyMatches.adapter.dataChanged(matches);
                    finish();
                }catch (NullPointerException e){
                    finish();
                }


            });
        } catch (Exception e) {
            log.error("ERROR: ", e);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_PROTOCOLTEAM) {
                List<String> result = data.getExtras().getStringArrayList("TEAMCOUNTPLAYERS");
                try {
                    players.clear();
                    players.addAll(result);
                }catch (NullPointerException e){
                    players.clear();
                }
            }
            if (requestCode == REQUEST_CODE_PROTOCOLTREFEREE) {
                List<String> result = data.getExtras().getStringArrayList("PROTOCOLCOUNTREFEREES");
                referees.clear();
                referees.addAll(result);
                Match newMatch = (Match) data.getExtras().getSerializable("PROTOCOLMATCHAFTEREDITREFEREES");
                match.setReferees(newMatch.getReferees());


//                match.setReferees(result);
            }
            if (requestCode == REQUEST_CODE_PROTOCOLEVENTS) {
                List<PlayerEvent> result = (List<PlayerEvent>) data.getExtras().getSerializable("EDITEVENTRESULT");
                try {
                    playerEvents.clear();
                    playerEvents.addAll(result);
                }catch (NullPointerException e){
                    playerEvents.clear();
                }
            }
        } else {
            log.error("ERROR: onActivityResult");
        }
    }

    private Boolean checkProtocol(){
        boolean check = false;
        for (PlayerEvent playerEvent : playerEvents){
            if (players.contains(playerEvent.getPerson().getId())){
                check = true;
            }
            else {
                check=false;
                break;
            }
        }
        return check;
    }

    private void editProtocolRequest(){
        List<Event> list = new ArrayList<>();
        for (PlayerEvent playerEvent : playerEvents){
            list.add(playerEvent.getEvent());
        }
        EditProtocolBody editProtocolBody = new EditProtocolBody();
        editProtocolBody.setId(match.getId());
        editProtocolBody.setPlayerList(players);
        editProtocolBody.setEvents(list);
        User user = SaveSharedPreference.getObject();
        Call<Matches> call = Controller.getApi().editProtocol(user.getToken(),editProtocolBody);
        call.enqueue(new Callback<Matches>() {
            @Override
            public void onResponse(Call<Matches> call, Response<Matches> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Match result = response.body().getMatch();
                        int position = MyMatches.matches.indexOf(match);
                        if (MyMatches.matches.size()==1){
                            MyMatches.matches.clear();
                            MyMatches.matches.add(result);
                        }else {
                            MyMatches.matches.set(matchOldPosition, result);
                        }
                        List<Match> matches = new ArrayList<>(MyMatches.matches);
                        MyMatches.adapter.dataChanged(matches);
                        Toast.makeText(ProtocolEdit.this, "Изменения сохранены", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        String str = "Ошибка! ";
                        str += jsonObject.getString("message");
                        Toast.makeText(ProtocolEdit.this, str, Toast.LENGTH_LONG).show();
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Matches> call, Throwable t) {
                Toast.makeText(ProtocolEdit.this, "Ошибка сервера.", Toast.LENGTH_SHORT).show();
            }
        });
         }




    private HashMap<String, Team> getTeams(Match match){
        HashMap<String, Team> teams = new HashMap<>();
        ImageView image1 = findViewById(R.id.editProtocolCommand1Logo);
        ImageView image2 = findViewById(R.id.editProtocolCommand2Logo);
        SetImage setImage = new SetImage();
        for (League league : PersonalActivity.tournaments) {
            if (teams.size()!=2 && match.getLeague().equals(league.getId())) {
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
                                }catch (NullPointerException e){
                                    clubOne = "";
                                }
                                setImage.setImage(image1.getContext(), image1, club.getLogo());
                            }
                            if (team.getId().equals(match.getTeamTwo())
                                    && team.getClub().equals(club.getId())) {
                                try {
                                    clubTwo = club.getLogo();
                                }catch (NullPointerException e){
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
    private TeamTitleClubLogoMatchEvents getPlayerEvent(List<Event> events, Match match, Team team1, Team team2) {

        log.error(match.getId());
        List<PlayerEvent> playerEvents1 = new ArrayList<>();
        String teamOne = team1.getName();
        String teamTwo = team2.getName();
        String clubEvent = "";
        String teamName = "";
        for (Event event : events) {
            Person person = null;
            for (Person person1 : PersonalActivity.AllPeople) {
                if (person1.getId().equals(event.getPlayer())) {
                    person = person1;
                }
            }
            for (Player player : team1.getPlayers()){
                if (player.getPlayerId().equals(person.getId())){
                    clubEvent = clubOne;
                    teamName = team1.getName();
                }
            }
            for (Player player : team2.getPlayers()){
                if (player.getPlayerId().equals(person.getId())){
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
        if (match.getEvents().isEmpty()){
            playerEvents1 = null;
        }

        TeamTitleClubLogoMatchEvents entry = new TeamTitleClubLogoMatchEvents();
        entry.setPlayerEvents(playerEvents1);
        entry.setClubLogo1(clubOne);
        entry.setClubLogo2(clubTwo);
        entry.setNameTeam1(teamOne);
        entry.setNameTeam2(teamTwo);
//        return playerEvents;
        return entry;
    }
}
