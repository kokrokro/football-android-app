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
        try {
            Intent intent = getIntent();
            person = (Person) intent.getExtras().getSerializable("PLAYERINV");
            personId = person.getId();
            for (PersonTeams personTeams : AuthoUser.personOwnCommand){
                League league = null;
                for (League league1 : PersonalActivity.tournaments){
                    if (league1.getId().equals(personTeams.getLeague())){
                        league = league1;
                        break;
                    }
                }
                for (Team team: league.getTeams()){
                    if (team.getStatus().equals("Pending")
                            && team.getCreator().equals(SaveSharedPreference.getObject().getUser().getId())){
                        PersonTeams personTeams1 = new PersonTeams();
                        personTeams1.setTeam(team.getId());
                        personTeams1.setLeague(league.getId());
                        teams.add(personTeams1);
                    }
                }
            }
            adapter = new RVPlayerInvAdapter(this, teams);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

        } catch(Exception e){ log.error("ERROR: ", e);}

        imageClose.setOnClickListener(v -> finish());


    }


}
