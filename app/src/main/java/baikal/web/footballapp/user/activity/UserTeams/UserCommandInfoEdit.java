package baikal.web.footballapp.user.activity.UserTeams;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.MankindKeeper;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.model.Invite;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.Player;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.user.activity.ChooseTrainer;
import baikal.web.footballapp.user.activity.PlayerAddToTeam;
import baikal.web.footballapp.user.adapter.RVUserCommandPlayerAdapter;
import baikal.web.footballapp.user.adapter.RVUserCommandPlayerInvAdapter;
import baikal.web.footballapp.viewmodel.MainViewModel;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserCommandInfoEdit extends AppCompatActivity {
    private final Logger log = LoggerFactory.getLogger(UserCommandInfoEdit.class);
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
    private Boolean isCreator;

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
            isCreator = team.getCreator().equals(SaveSharedPreference.getObject().getUser().get_id());
            if(!isCreator){
                LinearLayout linearLayout = findViewById(R.id.linLayoutInformationTeam);
                linearLayout.setVisibility(View.GONE);
            }
            Person trainer = MankindKeeper.getInstance().getPersonById(team.getTrainer());
            if (trainer != null)
                str = trainer.getSurname()+" "+trainer.getName();


            teamTrainer.setOnClickListener(v -> {
                Intent intent1 = new Intent(this, ChooseTrainer.class);
                startActivityForResult(intent1, 1);
            });
            if( isCreator){
                teamTrainer.setOnClickListener(v -> {
                    Intent intent1 = new Intent(this, ChooseTrainer.class);
                    startActivityForResult(intent1, 1);
                });
            }
            else {
                teamName.getFreezesText();
                teamNumber.getFreezesText();
            }
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
            adapter = new RVUserCommandPlayerAdapter(this, players, position -> {
                UserCommandInfoEdit.players.remove(players.get(position));
                UserCommandInfoEdit.adapter.notifyDataSetChanged();
                Log.d("cancel invite id ", ""+ UserCommandInfoEdit.accepted.get(position).get_id());
                Controller.getApi()
                        .cancelInv(UserCommandInfoEdit.accepted.get(position).get_id(), SaveSharedPreference.getObject().getToken())
                        .enqueue(new Callback<Invite>() {
                            @Override
                            public void onResponse(@NonNull Call<Invite> call, @NonNull Response<Invite> response) {
                                if(response.isSuccessful()){
                                    if(response.body()!=null){
                                        Log.d("cancel invite", "__SUCCCESS");
                                    }
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<Invite> call, @NonNull Throwable t) {
                                Log.d("cancel invite", "__FAIL");
                            }
                        });
            });
            recyclerViewPlayer.setAdapter(adapter);
            recyclerViewPlayer.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewPlayer.setVisibility(View.GONE);
            recyclerViewPlayerInv = findViewById(R.id.recyclerViewUserCommandPlayersInv);
            adapterInv = new RVUserCommandPlayerInvAdapter(this, playersInv, position -> {
                Controller.getApi().cancelInv(UserCommandInfoEdit.allInvites.get(position).get_id(), SaveSharedPreference.getObject().getToken()).enqueue(new Callback<Invite>() {
                    @Override
                    public void onResponse(@NonNull Call<Invite> call, @NonNull Response<Invite> response) {
                        if(response.isSuccessful()){
//                            Toast.makeText(context,"Приглашение отменено", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<Invite> call, @NonNull Throwable t) {
//                        Toast.makeText(context,"Не удалось", Toast.LENGTH_SHORT).show();
                    }
                });

                UserCommandInfoEdit.playersInv.remove(players.get(position));
//            List<String> players = new ArrayList<>(UserCommandInfoEdit.playersInv);
                UserCommandInfoEdit.adapterInv.notifyDataSetChanged();
            }, "UserCommandInfoEdit");
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
//                for(Player player : players){
//                    for(String personId : canceled){
//                        if(player.getPerson().equals(personId)){
//                            players.remove(player);
//
//                            break;
//                        }
//                    }
//                }
                adapter.notifyDataSetChanged();
                recyclerViewPlayer.setVisibility(View.VISIBLE);
            });



            buttonAdd.setOnClickListener(v -> {
                Observable.just("input_parameter")
                        .subscribeOn(Schedulers.io())//creation of secondary thread
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(s -> showDialog());//now this runs in main thread
                Intent intent1 = new Intent(UserCommandInfoEdit.this, PlayerAddToTeam.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("ADDPLAYERTOUSERTEAM", team);
                intent1.putExtras(bundle);
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable("ADDPLAYERTOUSERTEAMLEAGUE", league);
                intent1.putExtras(bundle2);
                startActivity(intent1);
            });
            if(isCreator){
                buttonSave.setOnClickListener(v -> {
                    editTeam(team.getId());
                    //post
                    finish();
                });
            }
            else {
                buttonSave.setVisibility(View.INVISIBLE);
            }

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
                newTrainerId = Objects.requireNonNull(data.getData()).toString();
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
