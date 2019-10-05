package baikal.web.footballapp.players.activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.ImageButton;

import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.PersonTeams;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.players.adapter.RVPlayerInvAdapter;
import baikal.web.footballapp.user.activity.AuthoUser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class PlayerInv extends AppCompatActivity {
    final Logger log = LoggerFactory.getLogger(PlayerInv.class);
    Team itemTeam;
    List<PersonTeams> teams;
    public static String personId;
    Person person;
    public static RVPlayerInvAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        teams = new ArrayList<>();
        ImageButton imageClose;
        ImageButton imageSave;
        RecyclerView recyclerView;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_inv);
        imageClose = findViewById(R.id.playerInvClose);
        recyclerView = findViewById(R.id.recyclerViewPlayerInv);
//        imageSave = (ImageButton) findViewById(R.id.playerInvSave);
//        spinnerCommands = (Spinner) findViewById(R.id.playerInvSpinner);
        try{
            Intent intent = getIntent();
            person = (Person) intent.getExtras().getSerializable("PLAYERINV");
            personId = person.getId();
//            Person creator = AuthoUser.web.getUser();
//            for (PersonTeams personTeams : AuthoUser.personOngoingLeagues){
            for (PersonTeams personTeams : AuthoUser.personOwnCommand){
                League league = null;
                for (League league1 : PersonalActivity.tournaments){
                    if (league1.getId().equals(personTeams.getLeague())){
                        league = league1;
                        break;
                    }
                }
//                League league = personTeams.getLeague();
                for (Team team: league.getTeams()){
//                    if (team.getCreator().equals(creator.getId()) && team.getStatus().equals("Pending")
                    if (team.getStatus().equals("Pending")
                            && team.getCreator().equals(SaveSharedPreference.getObject().getUser().getId())){
                        PersonTeams personTeams1 = new PersonTeams();
                        personTeams1.setTeam(team.getId());
                        personTeams1.setLeague(league.getId());
//                        personTeams1.setId("000");
                        teams.add(personTeams1);
                    }
                }
            }
            adapter = new RVPlayerInvAdapter(this, teams);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
//            SpinnerCommandAdapter adapter = new SpinnerCommandAdapter(this,R.layout.spinner_item, teams);
//            spinnerCommands.setAdapter(adapter);
//            spinnerCommands.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//                    itemTeam = (Team) parent.getItemAtPosition(pos);
//                }
//                public void onNothingSelected(AdapterView<?> parent) {
//                }
//            });

        }catch (Exception e){ log.error("ERROR: ", e);}

        imageClose.setOnClickListener(v -> finish());
//        imageSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try{
//
//                        addPlayer(person.getId());
//
//
//                }catch (Exception e){ log.error("ERROR: ", e);}
//
//            }
//        });


    }


}
