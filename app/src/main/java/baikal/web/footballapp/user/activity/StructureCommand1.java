package baikal.web.footballapp.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import baikal.web.footballapp.App;
import baikal.web.footballapp.Controller;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.model.Match;
import baikal.web.footballapp.model.MatchPopulate;
import baikal.web.footballapp.model.Player;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.user.adapter.RVWorkProtocolEditTeamAdapter;
import baikal.web.footballapp.viewmodel.MainViewModel;
import baikal.web.footballapp.viewmodel.PersonViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StructureCommand1 extends AppCompatActivity {
    private static final String TAG = "StructureCommand1";
    private Logger log = LoggerFactory.getLogger(StructureCommand1.class);
    private Team team;
    private MatchPopulate match;
    private RVWorkProtocolEditTeamAdapter adapter;
    private LinearLayout emptyTeam;
    private final List<Player> players = new ArrayList<>();

    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.structure_command1);
        emptyTeam = findViewById(R.id.SC_emptyEvents);
        ImageButton buttonBack = findViewById(R.id.editProtocolCommand1Back);
        ImageButton buttonSave = findViewById(R.id.SC_WorkProtocolTeamSave);
        TextView textCommandTitle = findViewById(R.id.command1Title);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewConfirmProtocolCommand1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        String status = getIntent().getExtras().getString("STATUS", "");
        boolean isEditable = status.equals("thirdReferee") || status.equals("mainReferee");// || status.equals("trainer");

        Log.d(TAG, status + " " + isEditable);

        if (isEditable)
            buttonSave.setVisibility(View.VISIBLE);

        PersonViewModel personViewModel = ViewModelProviders.of(this).get(PersonViewModel.class);

        try{
            match = (MatchPopulate) getIntent().getExtras().getSerializable("MATCH_EDIT_TEAM");
            team = (Team) getIntent().getExtras().getSerializable("TEAM");

            textCommandTitle.setText(team.getName());
            getPlayersByTeamId(team.getId());

            players.clear();
            players.addAll(team.getPlayers());
            adapter = new RVWorkProtocolEditTeamAdapter(personViewModel, players, match, isEditable);
            recyclerView.setAdapter(adapter);
        } catch (NullPointerException ignored){ }

        buttonSave.setOnClickListener(v -> {
            Match newMatch = new Match(match);
            String token = SaveSharedPreference.getObject().getToken();
            Controller.getApi().editMatch(match.getId(), token, newMatch).enqueue(new Callback<Match>() {
                @Override
                public void onResponse(@NonNull Call<Match> call, @NonNull Response<Match> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        match.assignNewMatchData(response.body());

                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("MATCH_EDITED_TEAM", match);
                        intent.putExtras(bundle);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Match> call, @NonNull Throwable t) {
                    Toast.makeText(App.getAppContext(), "Что-то пошло не так ...", Toast.LENGTH_LONG).show();
                }
            });
        });
        buttonBack.setOnClickListener(v -> finish());
    }

    private void getPlayersByTeamId(String id)
    {
        mainViewModel.getTeamById(id, team -> {
            this.team = team;
            players.clear();
            players.addAll(team.getPlayers());
            if (team.getPlayers().size() == 0)
                emptyTeam.setVisibility(View.VISIBLE);
            else
                emptyTeam.setVisibility(View.GONE);

            if (adapter != null)
                adapter.notifyDataSetChanged();
        });
    }
}

