package baikal.web.footballapp.user.activity.UserTeams;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.model.Invite;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.Player;
import baikal.web.footballapp.model.ResponseInvite;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.user.activity.ChooseTrainer;
import baikal.web.footballapp.user.activity.PlayerAddToTeam;
import baikal.web.footballapp.user.adapter.EditTeamPlayersAdapter;
import baikal.web.footballapp.user.adapter.EditTeamPlayersInvAdapter;
import baikal.web.footballapp.viewmodel.PersonViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserCommandInfoEdit extends AppCompatActivity {
    private final static String TAG = "UserCommandInfoEdit";
    private final static int SEND_INVITATIONS_REQUEST_CODE = 17214;
    private final Logger log = LoggerFactory.getLogger(UserCommandInfoEdit.class);

    public static List<Player> players = new ArrayList<>();

    public EditTeamPlayersAdapter adapter;
    public EditTeamPlayersInvAdapter adapterInv;

    private ArrayList<String> teamIdsOfThatLeague = new ArrayList<>();
    public static List<Invite> pending = new ArrayList<>();
    public static List<Invite> accepted = new ArrayList<>();

    private EditText teamName;
    private Button teamTrainer;
    private EditText teamNumber;
    private ImageButton buttonSave;

    private Team team;
    private League league;
    private String newTrainerId;
    private String status;

    private SwipeRefreshLayout swipeRefreshLayout;

    private PersonViewModel personViewModel;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_command_info);
        setSupportActionBar(findViewById(R.id.toolbarUserCommandInfo));

        teamName = findViewById(R.id.editTeamTitle);
        teamTrainer = findViewById(R.id.newCommandTrainer);
        teamNumber = findViewById(R.id.newCommandNumber);

        buttonSave = findViewById(R.id.userCommandSave);

        swipeRefreshLayout = findViewById(R.id.UCI_swipe_to_refresh);
        swipeRefreshLayout.setOnRefreshListener(this::loadData);

        personViewModel = ViewModelProviders.of(this).get(PersonViewModel.class);

        team = (Team) getIntent().getExtras().getSerializable("COMMANDEDIT");
        league = (League) getIntent().getExtras().getSerializable("COMMANDEDITLEAGUE");
        status = getIntent().getExtras().getString("STATUS");

        initViewsAndData();

        try {
            players.clear();
            players.addAll(team.getPlayers());

            RecyclerView recyclerViewPlayer = findViewById(R.id.recyclerViewUserCommandPlayers);
            adapter = new EditTeamPlayersAdapter(personViewModel, players, this::removePlayerFromTeam);
            recyclerViewPlayer.setAdapter(adapter);
            recyclerViewPlayer.setLayoutManager(new LinearLayoutManager(this));

            RecyclerView recyclerViewPlayerInv = findViewById(R.id.recyclerViewUserCommandPlayersInv);
            adapterInv = new EditTeamPlayersInvAdapter(pending, this::cancelInvite);
            recyclerViewPlayerInv.setAdapter(adapterInv);
            recyclerViewPlayerInv.setLayoutManager(new LinearLayoutManager(this));

        } catch (Exception e) {
            log.error("ERROR: ", e);
        }

        findViewById(R.id.userCommandClose).setOnClickListener(v -> finish());
    }

    private void setupViewForTrainer() {
        findViewById(R.id.linLayoutInformationTeam).setVisibility(View.GONE);

        findViewById(R.id.userCommandPlayerButton).setOnClickListener(v -> {
            Intent intent = new Intent(UserCommandInfoEdit.this, PlayerAddToTeam.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("ADD_PLAYER_TO_USER_TEAM", team);
            bundle.putStringArrayList("TEAM_IDS_FOR_THAT_LEAGUE", teamIdsOfThatLeague);
            intent.putExtras(bundle);
//            startActivity(intent);
            startActivityForResult(intent, SEND_INVITATIONS_REQUEST_CODE);
        });

        buttonSave.setVisibility(View.INVISIBLE);
    }

    private void setupViewForCreator () {
        findViewById(R.id.UCI_command_structure_edit).setVisibility(View.GONE);
        teamNumber.setText(team.getCreatorPhone());

        teamTrainer.setOnClickListener(v -> {
            Intent intent1 = new Intent(this, ChooseTrainer.class);
            startActivityForResult(intent1,  1);
        });
        Person trainer = personViewModel.getPersonById(team.getTrainer(), p->
                teamTrainer.setText(p.getSurnameAndName())
        );
        if (trainer != null)
            teamTrainer.setText(trainer.getSurnameAndName());
        teamTrainer.setOnClickListener(v -> {
            Intent intent1 = new Intent(this, ChooseTrainer.class);
            startActivityForResult(intent1, 1);
        });
        buttonSave.setOnClickListener(v -> {
            editTeam(team.getId());
            finish();
        });
    }

    private void initViewsAndData() {
        teamName.setText(team.getName());

        if(status.equals("train"))
            setupViewForTrainer();

        if(status.equals("creator"))
            setupViewForCreator();

        Callback<List<Team>> responseCallback = new Callback<List<Team>>() {
            @Override
            public void onResponse(@NonNull Call<List<Team>> call, @NonNull Response<List<Team>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    teamIdsOfThatLeague.clear();
                    for (Team t: response.body())
                        teamIdsOfThatLeague.add(t.getId());
                }

                if (swipeRefreshLayout != null)
                    swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(@NonNull Call<List<Team>> call, @NonNull Throwable t) {
                if (swipeRefreshLayout != null)
                    swipeRefreshLayout.setRefreshing(false);
            }
        };

        Controller.getApi().getTeamsIdsByLeague(league.getId()).enqueue(responseCallback);

        loadInvites();
    }

    private void loadInvites ()
    {
        Callback<List<Invite>> responseCallback_1 = new Callback<List<Invite>>() {
            @Override
            public void onResponse(@NonNull Call<List<Invite>> call, @NonNull Response<List<Invite>> response) {
                if (response.isSuccessful() && response.body()!=null) {
                    accepted.clear();
                    pending.clear();

                    for (Invite invite: response.body())
                        if (invite.getStatus().equals("accepted"))
                            accepted.add(invite);
                        else
                            pending.add(invite);

                    adapterInv.notifyDataSetChanged();
                }

                if (swipeRefreshLayout != null)
                    swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(@NonNull Call<List<Invite>> call, @NonNull Throwable t) {
                if (swipeRefreshLayout != null)
                    swipeRefreshLayout.setRefreshing(false);
            }
        };

        Controller.getApi().getInvites(null, team.getId(), ",accepted,pending").enqueue(responseCallback_1);
    }

    private void loadData() {
        Callback<List<Team>> responseCallback = new Callback<List<Team>>() {
            @Override
            public void onResponse(@NonNull Call<List<Team>> call, @NonNull Response<List<Team>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    team = response.body().get(0);
                    players.clear();
                    players.addAll(team.getPlayers());
                    adapter.notifyDataSetChanged();
                    initViewsAndData();
                    return;
                }
                initViewsAndData();
            }

            @Override
            public void onFailure(@NonNull Call<List<Team>> call, @NonNull Throwable t) {
                if (swipeRefreshLayout != null)
                    swipeRefreshLayout.setRefreshing(false);
                initViewsAndData();
            }
        };
        Controller.getApi().getTeamById(team.getId()).enqueue(responseCallback);
    }

    private void cancelInvite (Invite invite) {
        Controller.getApi().cancelInv(invite.get_id(), SaveSharedPreference.getObject().getToken()).enqueue(new Callback<ResponseInvite>() {
            @Override
            public void onResponse(@NonNull Call<ResponseInvite> call, @NonNull Response<ResponseInvite> response) {
                if(response.isSuccessful() && response.body()!=null) {
                    int indexI = -1;

                    for (int i=0; i<pending.size(); i++)
                        if (pending.get(i).get_id().equals(invite.get_id()))
                            indexI = i;

                    if (indexI == -1) {
//                        Log.e(TAG, "invite to delete: " + invite.toString() + "\n$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
//                        for(Invite invite1: pending) {
//                            Log.e(TAG, invite1.toString() + "\n**************************************************************");
//                        }
                        Log.e(TAG, "Can't find object in pending invites...");
                        return;
                    }

                    pending.remove(indexI);
                    adapterInv.notifyDataSetChanged();

                    Toast.makeText(getApplicationContext(), "Приглашение отменено", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseInvite> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(),"Не удалось", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removePlayerFromTeam (Player player) {
        String inviteId = null;
        for (Invite invite: accepted)
            if (invite.getPerson().get_id().equals(player.getPerson()))
                inviteId = invite.get_id();


        if (inviteId != null)
            Controller.getApi()
                    .cancelInv(inviteId, SaveSharedPreference.getObject().getToken())
                    .enqueue(new Callback<ResponseInvite>() {
                        @Override
                        public void onResponse(@NonNull Call<ResponseInvite> call, @NonNull Response<ResponseInvite> response) {
                            if(response.isSuccessful() && response.body()!=null) {
                                int indexP = players.indexOf(player);

                                if (indexP == -1) {
                                    Log.e(TAG, "Can't find object in players...");
                                    return;
                                }

                                players.remove(indexP);
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ResponseInvite> call, @NonNull Throwable t) {
                            Log.d("cancel invite", "__FAIL");
                        }
                    });
    }

    private void editTeam(String id) {
       Team editTeam = new Team();
       editTeam.setName(teamName.getText().toString());
       if(newTrainerId!=null){
           editTeam.setTrainer(newTrainerId);
       }
       editTeam.setCreatorPhone(teamNumber.getText().toString());
       Controller.getApi().editTeam(id, SaveSharedPreference.getObject().getToken(),editTeam).enqueue(new Callback<Team>() {
           @Override
           public void onResponse(@NonNull Call<Team> call, @NonNull Response<Team> response) {
               if(response.isSuccessful())
                   if(response.body()!=null)
                       log.debug("success");
           }

           @Override
           public void onFailure(@NonNull Call<Team> call, @NonNull Throwable t) {
                Toast.makeText(getBaseContext(),"Failed to edit",Toast.LENGTH_SHORT).show();
           }
       });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Person person = (Person) data.getExtras().getSerializable("PERSON");
                newTrainerId = person.getId();
                teamTrainer.setText(person.getSurnameAndName());
            }
        }

        if (requestCode == SEND_INVITATIONS_REQUEST_CODE) {
            loadData();
        }
    }
}
