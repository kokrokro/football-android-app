package baikal.web.footballapp.user.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.MankindKeeper;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.model.Match;
import baikal.web.footballapp.model.MatchPopulate;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.user.adapter.RVWorkProtocolEditTeamAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class StructureCommand1 extends AppCompatActivity {
    private static final String TAG = "StructureCommand1";
    private Logger log = LoggerFactory.getLogger(StructureCommand1.class);
    private Team team;
    private MatchPopulate match;
    private RVWorkProtocolEditTeamAdapter adapter;
    private LinearLayout emptyTeam;
    private RecyclerView recyclerView;
    private boolean isEditable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.structure_command1);

        emptyTeam = findViewById(R.id.SC_emptyEvents);
        ImageButton buttonBack = findViewById(R.id.editProtocolCommand1Back);
        ImageButton buttonSave = findViewById(R.id.SC_WorkProtocolTeamSave);
        TextView textCommandTitle = findViewById(R.id.command1Title);
        recyclerView = findViewById(R.id.recyclerViewConfirmProtocolCommand1);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        isEditable = getIntent().getExtras().getBoolean("IS_EDITABLE", false);
        if (isEditable)
            buttonSave.setVisibility(View.VISIBLE);

        try{
            match = (MatchPopulate) getIntent().getExtras().getSerializable("MATCH_EDIT_TEAM");
            team = (Team) getIntent().getExtras().getSerializable("TEAM");
            textCommandTitle.setText(team.getName());

            getTeamById(team.getId());
            Log.d(TAG, team.getPlayers().toString());
            adapter = new RVWorkProtocolEditTeamAdapter(this, team.getPlayers(), match, isEditable);
            recyclerView.setAdapter(adapter);
        } catch (NullPointerException ignored){}

        buttonSave.setOnClickListener(v -> {
            Match newMatch = new Match(match);
            String token = SaveSharedPreference.getObject().getToken();
            Controller.getApi().editMatch(match.getId(), token, newMatch).enqueue(new Callback<Match>() {
                @Override
                public void onResponse(@NonNull Call<Match> call, @NonNull Response<Match> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        match.onProtocolEdited(response.body());

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

                }
            });
        });
        buttonBack.setOnClickListener(v -> finish());
    }

    private void getTeamById (String id)
    {
        if (MankindKeeper.getInstance().getTeamById(id) == null ||
                (MankindKeeper.getInstance().getTeamById(id) != null &&
                 MankindKeeper.getInstance().getTeamById(id).getPlayers().size() == 0)) {
            Log.d(TAG, "getTeamById from server");
            Controller.getApi().getTeam(id).enqueue(new Callback<List<Team>>() {
                @Override
                public void onResponse(@NonNull Call<List<Team>> call, @NonNull Response<List<Team>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        MankindKeeper.getInstance().addTeam(response.body().get(0));
                        team.setPlayers(response.body().get(0).getPlayers());

                        if (team.getPlayers().size() == 0)
                            emptyTeam.setVisibility(View.VISIBLE);
                        else
                            emptyTeam.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                        Log.d(TAG, "getTeamById loaded");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Team>> call, @NonNull Throwable t) {
                    if (team.getPlayers().size() == 0)
                        emptyTeam.setVisibility(View.VISIBLE);
                    Toast.makeText(StructureCommand1.this, "не удалось сохранить, попробуйте снова", Toast.LENGTH_LONG).show();
                }
            });
        }
        else {
            team = MankindKeeper.getInstance().getTeamById(id);

            if (team.getPlayers().size() == 0)
                emptyTeam.setVisibility(View.VISIBLE);
            else
                emptyTeam.setVisibility(View.GONE);

//            Log.d(TAG, "getTeamById: " + MankindKeeper.getInstance().allTeams.toString());
//            Log.d(TAG, "id: " + id);
//            Log.d(TAG, "players: " + team.getPlayers().toString());
//            Log.d(TAG, "players cnt: " + team.getPlayers().size());
        }
    }
}

