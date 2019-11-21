package baikal.web.footballapp.user.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.ImageButton;
import android.widget.TextView;

import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Match;
import baikal.web.footballapp.model.MatchPopulate;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.Player;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.user.adapter.RVProtocolFirstCommandAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class StructureCommand1 extends AppCompatActivity {
    Logger log = LoggerFactory.getLogger(StructureCommand1.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        RecyclerView recyclerView;
        TextView textCommandTitle;
        ImageButton buttonBack;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.structure_command1);
        buttonBack = findViewById(R.id.editProtocolCommand1Back);
        textCommandTitle = findViewById(R.id.command1Title);
        recyclerView = findViewById(R.id.recyclerViewConfirmProtocolCommand1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        recyclerView.setNestedScrollingEnabled(false);
        try{
            String str;
//            ActiveMatch match = (ActiveMatch) getIntent().getExtras().getSerializable("CONFIRMPROTOCOLMATCH");
            MatchPopulate match = (MatchPopulate) getIntent().getExtras().getSerializable("CONFIRMPROTOCOLMATCH");
            Team team = (Team) getIntent().getExtras().getSerializable("CONFIRMPROTOCOLCOMMAND");
            str = team.getName();
            textCommandTitle.setText(str);
            List<String> playersTeam = new ArrayList<>();
            for (Player player: team.getPlayers()){
                playersTeam.add(player.getPlayerId());
            }
            List<String> list = new ArrayList<>(playersTeam);
            List<String> count = new ArrayList<>();
            for (String id : list){
                if (!match.getPlayersList().contains(id)){
                    playersTeam.remove(id);
                    count.add(id);
                }
            }
            playersTeam.addAll(playersTeam.size(), count);
            List<Person> personList = new ArrayList<>();
            for (String id : playersTeam) {
                for (Person person : PersonalActivity.people) {
                    if (person.getId().equals(id)){
                        personList.add(person);
                    }
                }
            }
            RVProtocolFirstCommandAdapter adapter = new RVProtocolFirstCommandAdapter(this, playersTeam, team.getPlayers(), personList);
            recyclerView.setAdapter(adapter);
        }catch (NullPointerException e){}
        buttonBack.setOnClickListener(v -> finish());
    }
}
