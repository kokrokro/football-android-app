package baikal.web.footballapp.user.activity;

import android.app.ProgressDialog;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.MankindKeeper;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.model.EditCommand;
import baikal.web.footballapp.model.EditCommandResponse;
import baikal.web.footballapp.model.Invite;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.PersonTeams;
import baikal.web.footballapp.model.Player;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.model.Tourney;
import baikal.web.footballapp.model.User;
import baikal.web.footballapp.user.adapter.RVUserCommandPlayerAdapter;
import baikal.web.footballapp.user.adapter.RVUserCommandPlayerInvAdapter;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import baikal.web.footballapp.user.adapter.TrainerAdapter;
import baikal.web.footballapp.viewmodel.MainViewModel;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserCommandInfo extends AppCompatActivity {
    private final Logger log = LoggerFactory.getLogger(UserCommandInfo.class);
    private static ProgressDialog mProgressDialog;
    public static List<Player> players;
    public static List<String> playersInv;
    public static RVUserCommandPlayerAdapter adapter;
    public static RVUserCommandPlayerInvAdapter adapterInv;
    public static List<Invite> allInvites = new ArrayList<>();
    public static List<Invite> accepted = new ArrayList<>();
    public static List<String> canceled = new ArrayList<>();
    private Team team;
    private League league;
    private EditText teamName;
    private Button teamTrainer;
    private EditText teamNumber;
    private String newTrainerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        RecyclerView recyclerViewPlayer;
        RecyclerView recyclerViewPlayerInv;
        players = new ArrayList<>();
        playersInv = new ArrayList<>();
        Toolbar toolbar;
        ImageButton buttonClose;
        ImageButton buttonSave;
        Button buttonAdd;


        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_command_info);
        teamName = findViewById(R.id.editTeamTitle);
        teamTrainer = findViewById(R.id.newCommandTrainer);
        teamNumber = findViewById(R.id.newCommandNumber);
        try {


            mProgressDialog = new ProgressDialog(this, R.style.MyProgressDialogTheme);

            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Загрузка...");
            Intent intent = getIntent();
            team = (Team) intent.getExtras().getSerializable("COMMANDEDIT");
            league = (League) intent.getExtras().getSerializable("COMMANDEDITLEAGUE");
            teamName.setText(team.getName());
            String str = team.getTrainer();
            if (MankindKeeper.getInstance().allPlayers.containsKey(team.getTrainer())) {
                Person p = MankindKeeper.getInstance().allPlayers.get(team.getTrainer());
                str = p.getSurname()+" "+p.getName();
            }

            teamTrainer.setOnClickListener(v -> {
                Intent intent1 = new Intent(this, ChooseTrainer.class);
                startActivityForResult(intent1, 1);
            });
            teamTrainer.setText(str);
            players.addAll(team.getPlayers());
            teamNumber.setText(team.getCreatorPhone());
//            if (playersInv.size()==0){
//                TextView textView = findViewById(R.id.userCommandPlayersInvText);
//                textView.setVisibility(View.GONE);
//                textView = findViewById(R.id.userCommandPlayersStructureText);
//                textView.setVisibility(View.GONE);
//            }else {
//                View line = findViewById(R.id.userCommandPlayersLine);
//                line.setVisibility(View.VISIBLE);
//            }
            toolbar = findViewById(R.id.toolbarUserCommandInfo);
            setSupportActionBar(toolbar);
            buttonAdd = findViewById(R.id.userCommandPlayerButton);
            buttonSave = findViewById(R.id.userCommandSave);
            buttonClose = findViewById(R.id.userCommandClose);
            recyclerViewPlayer = findViewById(R.id.recyclerViewUserCommandPlayers);
            adapter = new RVUserCommandPlayerAdapter(this, players);
            recyclerViewPlayer.setAdapter(adapter);
            recyclerViewPlayer.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewPlayer.setVisibility(View.GONE);
            recyclerViewPlayerInv = findViewById(R.id.recyclerViewUserCommandPlayersInv);
            adapterInv = new RVUserCommandPlayerInvAdapter(this, playersInv);
            recyclerViewPlayerInv.setAdapter(adapterInv);
            recyclerViewPlayerInv.setLayoutManager(new LinearLayoutManager(this));
            MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
            mainViewModel.getAllInvites().observe(this, invites -> {
                playersInv.clear();
                allInvites.clear();
                accepted.clear();
                for(Invite invite : invites){
                    if(invite.getTeam().getId().equals(team.getId())){
                        if(invite.getStatus().equals("pending")){
                            playersInv.add(invite.getPerson());
                            allInvites.add(invite);
                            continue;
                        }
                        if(invite.getStatus().equals("accepted")){
                            accepted.add(invite);
                            continue;
                        }
                        if(invite.getStatus().equals("canceled")){
                            canceled.add(invite.getPerson());
                        }
                    }

                }
                adapterInv.notifyDataSetChanged();
                for(Player player : players){
                    for(String personId : canceled){
                        if(player.getPerson().equals(personId)){
                            players.remove(player);

                            break;
                        }
                    }
                }
                adapter.notifyDataSetChanged();
                recyclerViewPlayer.setVisibility(View.VISIBLE);
            });



            buttonAdd.setOnClickListener(v -> {
                Observable.just("input_parameter")
                        .subscribeOn(Schedulers.io())//creation of secondary thread
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(s -> showDialog());//now this runs in main thread
                Intent intent1 = new Intent(UserCommandInfo.this, PlayerAddToTeam.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("ADDPLAYERTOUSERTEAM", team);
                intent1.putExtras(bundle);
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable("ADDPLAYERTOUSERTEAMLEAGUE", league);
                intent1.putExtras(bundle2);
                startActivity(intent1);
            });
            buttonSave.setOnClickListener(v -> {
                editTeam(team.getId());
                //post
                finish();
            });
            buttonClose.setOnClickListener(v -> finish());
        } catch (Exception e) {
            log.error("ERROR: ", e);
        }
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
           public void onResponse(Call<Team> call, Response<Team> response) {
               if(response.isSuccessful()){
                   if(response.body()!=null){
                       log.debug("success");
                   }
               }
           }

           @Override
           public void onFailure(Call<Team> call, Throwable t) {
                Toast.makeText(getBaseContext(),"Failed to edit",Toast.LENGTH_SHORT).show();
           }
       });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                newTrainerId = data.getData().toString();
                String str  = data.getSerializableExtra("surname") +" "+data.getSerializableExtra("name");
                teamTrainer.setText(str);
            }
        }
    }
    private void showDialog() {

        if (mProgressDialog != null && !mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    public static void hideDialog() {

        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

}
