package baikal.web.footballapp.user.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.model.Match;
import baikal.web.footballapp.model.Player;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.user.adapter.RVUserCommandPlayerAdapter;
import baikal.web.footballapp.user.adapter.RVUserCommandPlayerInvAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePlayersForMatch extends AppCompatActivity {
    private List<Player> players = new ArrayList<>();
    private RVUserCommandPlayerAdapter adapter;
    private RVUserCommandPlayerInvAdapter adapterInv;
    private List<String> notPlayers = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_command_info);
        Intent intent = getIntent();
        LinearLayout editTeam = findViewById(R.id.linLayoutInformationTeam);
        Button button = findViewById(R.id.userCommandPlayerButton);
        button.setVisibility(View.GONE);
        editTeam.setVisibility(View.GONE);

        TextView textView = findViewById(R.id.userCommandPlayersInvText);
        textView.setText("Не участвующие:");
        textView = findViewById(R.id.editTeamTitleToolbar);
        textView.setText("Редактировать состав");

        Match match = (Match) intent.getExtras().getSerializable("MATCH");
        Team team = (Team) intent.getExtras().getSerializable("TEAM");

        try {
            if(match.getPlayersList().size()==0)
                players.addAll(team.getPlayers());
            else{
                for(Player player : team.getPlayers()){
                    if(match.getPlayersList().contains(player.getPerson())){
                        players.add(player);
                    }
                    else {
                        notPlayers.add(player.getPerson());
                    }
                }
            }
        }catch (NullPointerException ignored){}

        RecyclerView recyclerViewTeam = findViewById(R.id.recyclerViewUserCommandPlayers);
        RecyclerView recyclerViewSpare = findViewById(R.id.recyclerViewUserCommandPlayersInv);

        adapter = new RVUserCommandPlayerAdapter(this, players, position -> {
            notPlayers.add(players.get(position).getPerson());
            players.remove(position);
            adapter.notifyDataSetChanged();
            adapterInv.notifyDataSetChanged();


        });
        recyclerViewTeam.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTeam.setAdapter(adapter);

        adapterInv = new RVUserCommandPlayerInvAdapter(this, notPlayers, position -> {
            Player player = new Player();
            player.setPerson(notPlayers.get(position));
            players.add(player);
            notPlayers.remove(position);
            adapterInv.notifyDataSetChanged();
            adapter.notifyDataSetChanged();
        }, "ChangePlayersForMatch");
        recyclerViewSpare.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSpare.setAdapter(adapterInv);

        ImageButton btnClose = findViewById(R.id.userCommandClose);
        btnClose.setOnClickListener(v -> {
            finish();
        });

        ImageButton btnSave = findViewById(R.id.userCommandSave);
        btnSave.setOnClickListener(v -> {
            List<String> playersRequestBody = new ArrayList<>();
            for(Player player : players){
                playersRequestBody.add(player.getPerson());
            }

            Controller.getApi().changePlayersForMatch(match.getId(), SaveSharedPreference.getObject().getToken(),playersRequestBody)
                    .enqueue(new Callback<Match>() {
                        @Override
                        public void onResponse(Call<Match> call, Response<Match> response) {
                            if(response.isSuccessful()){
                                if(response.body()!=null){
                                    try {
                                        Log.d("Response", response.body().getPlayersList().size()+"");
                                        finish();
                                    }catch (NullPointerException ignored){}

                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Match> call, Throwable t) {

                        }
                    });

        });
    }
}
