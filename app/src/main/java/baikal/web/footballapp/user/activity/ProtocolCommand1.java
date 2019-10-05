package baikal.web.footballapp.user.activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.ImageButton;
import android.widget.TextView;

import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Player;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.user.adapter.RVProtocolCommand1Adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProtocolCommand1 extends AppCompatActivity {

    Logger log = LoggerFactory.getLogger(ProtocolCommand1.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        RecyclerView recyclerView;
        TextView textCommandTitle;
        ImageButton imageSave;
        ImageButton imageClose;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.protocol_command1);
        textCommandTitle = findViewById(R.id.command1);
        imageClose = findViewById(R.id.editProtocolCommand1Close);
        imageSave = findViewById(R.id.editProtocolCommand1Save);
        imageClose.setOnClickListener(v -> finish());
        recyclerView = findViewById(R.id.recyclerViewProtocolCommand1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        try {
            Intent arguments = getIntent();
            Team team = (Team) Objects.requireNonNull(arguments.getExtras()).getSerializable("PROTOCOLTEAMPLAYERS");
//            Match match = (Match) Objects.requireNonNull(arguments.getExtras()).getSerializable("PROTOCOLTEAMMATCH");
            final List<String> countPlayers = Objects.requireNonNull(arguments.getExtras()).getStringArrayList("PROTOCOLTEAMMATCH");
            textCommandTitle.setText(team.getName());
//            final List<String> playerList = new ArrayList<>(match.getPlayersList());
//            final List<String> countPlayers = new ArrayList<>(match.getPlayersList());
            List<Player> players = new ArrayList<>(team.getPlayers());
//            RVProtocolCommand1Adapter adapter = new RVProtocolCommand1Adapter(this, players, countPlayers);
            final RVProtocolCommand1Adapter adapter = new RVProtocolCommand1Adapter(this, players, countPlayers, (position, personId, check) -> {
                if (!check){
                    countPlayers.remove(personId);
                }
                else{
                    countPlayers.add(personId);
                }
            });
            recyclerView.setAdapter(adapter);
            imageSave.setOnClickListener(v -> {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("TEAMCOUNTPLAYERS", (ArrayList<String>) countPlayers);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
//                    adapter.dataChanged(countPlayers);
                finish(); //post
            });
        } catch (Exception e) {
        }
    }
}
