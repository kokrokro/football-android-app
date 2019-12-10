package baikal.web.footballapp.user.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import baikal.web.footballapp.R;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.Match;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.user.adapter.RVTeamMatchesAdapter;

public class TeamMatches extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RVTeamMatchesAdapter adapter;
    private List<Match> matches = new ArrayList<>();
    private Team team;
    private  League league;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_matches);
        Intent intent = getIntent();
        team = (Team) Objects.requireNonNull(intent.getExtras()).getSerializable("COMMANDEDIT");
        league = (League) intent.getExtras().getSerializable("COMMANDEDITLEAGUE");
        assert league != null;

        for(Match match : league.getMatches()){
            try {
                if(match.getTeamOne().equals(team.getId()) || match.getTeamTwo().equals(team.getId())){
                    matches.add(match);
                }
            }catch (NullPointerException ignored){}
        }

        recyclerView = findViewById(R.id.recyclerViewMyMatches);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RVTeamMatchesAdapter(matches, team, match -> {
            Intent intent1 = new Intent(this, ChangePlayersForMatch.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("MATCH", match);
            intent1.putExtras( bundle);
            Objects.requireNonNull(this).startActivity(intent1);
        });

        if(matches.size()==0){
            recyclerView.setVisibility(View.GONE);
        }
        else {
            LinearLayout linearLayoutEmpty = findViewById(R.id.emptyMatch);
            linearLayoutEmpty.setVisibility(View.GONE);
        }
        recyclerView.setAdapter(adapter);



    }
}
