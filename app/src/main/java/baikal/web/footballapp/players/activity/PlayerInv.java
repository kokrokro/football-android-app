package baikal.web.footballapp.players.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.PersonTeams;
import baikal.web.footballapp.players.adapter.RVPlayerInvAdapter;

public class PlayerInv extends AppCompatActivity {
    final Logger log = LoggerFactory.getLogger(PlayerInv.class);
    List<PersonTeams> teams;
    public static String personId;
    Person person;
    public static RVPlayerInvAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        teams = new ArrayList<>();
        ImageButton imageClose;
        RecyclerView recyclerView;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_inv);
        imageClose = findViewById(R.id.playerInvClose);
        recyclerView = findViewById(R.id.recyclerViewPlayerInv);
        try {
            Intent intent = getIntent();
            person = (Person) Objects.requireNonNull(intent.getExtras()).getSerializable("PLAYERINV");
            if (person != null)
                personId = person.getId();

//            for (PersonTeams personTeams : AuthoUser.personOwnCommand){
//                League league = null;
//                for (League league1 : MankindKeeper.getInstance().allLeagues){
//                    if (league1.getId().equals(personTeams.getLeague())){
//                        league = league1;
//                        break;
//                    }
//                }
//                if (league != null) {
//                    for (Team team: league.getTeams()){
//                        if (team.getStatus().equals("Pending")
//                                && team.getCreator().equals(SaveSharedPreference.getObject().getUser().getId())){
//                            PersonTeams personTeams1 = new PersonTeams();
//                            personTeams1.setTeam(team.getId());
//                            personTeams1.setLeague(league.getId());
//                            teams.add(personTeams1);
//                        }
//                    }
//                }
//            }
            adapter = new RVPlayerInvAdapter(this, teams);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

        } catch(Exception e){ log.error("ERROR: ", e);}

        imageClose.setOnClickListener(v -> finish());


    }


}