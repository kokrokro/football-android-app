package baikal.web.footballapp.user.activity;

import android.app.ProgressDialog;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.model.EditCommand;
import baikal.web.footballapp.model.EditCommandResponse;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.PersonTeams;
import baikal.web.footballapp.model.Player;
import baikal.web.footballapp.model.Team;
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
    public static List<Player> playersInv;
    public static RVUserCommandPlayerAdapter adapter;
    public static RVUserCommandPlayerInvAdapter adapterInv;
    private Team team;
    private League league;
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
        try {


            mProgressDialog = new ProgressDialog(this, R.style.MyProgressDialogTheme);

            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Загрузка...");
            Intent intent = getIntent();
            team = (Team) intent.getExtras().getSerializable("COMMANDEDIT");
            league = (League) intent.getExtras().getSerializable("COMMANDEDITLEAGUE");
            for (Player player : team.getPlayers()){
                if (player.getInviteStatus().equals("Approved") || player.getInviteStatus().equals("Accepted")){
                    players.add(player);
                }
                if (player.getInviteStatus().equals("Pending")){
                    playersInv.add(player);
                }
            }
            if (playersInv.size()==0){
                TextView textView = findViewById(R.id.userCommandPlayersInvText);
                textView.setVisibility(View.GONE);
                textView = findViewById(R.id.userCommandPlayersStructureText);
                textView.setVisibility(View.GONE);
            }else {
                View line = findViewById(R.id.userCommandPlayersLine);
                line.setVisibility(View.VISIBLE);
            }
            toolbar = findViewById(R.id.toolbarUserCommandInfo);
            setSupportActionBar(toolbar);
            buttonAdd = findViewById(R.id.userCommandPlayerButton);
            buttonSave = findViewById(R.id.userCommandSave);
            buttonClose = findViewById(R.id.userCommandClose);
            recyclerViewPlayer = findViewById(R.id.recyclerViewUserCommandPlayers);
            adapter = new RVUserCommandPlayerAdapter(this, players);
            recyclerViewPlayer.setAdapter(adapter);
            recyclerViewPlayer.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewPlayerInv = findViewById(R.id.recyclerViewUserCommandPlayersInv);
            adapterInv = new RVUserCommandPlayerInvAdapter(this, playersInv);
            recyclerViewPlayerInv.setAdapter(adapterInv);
            recyclerViewPlayerInv.setLayoutManager(new LinearLayoutManager(this));
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
                editTeam();
                //post
                finish();
            });
            buttonClose.setOnClickListener(v -> finish());
        } catch (Exception e) {
            log.error("ERROR: ", e);
        }
    }
    private void editTeam() {
        List<Player> playerList = new ArrayList<>(players);
        if (playersInv.size()!=0) {
            playerList.addAll(playersInv);
        }
        PersonTeams personTeams2 = null;
        for (PersonTeams personTeams: AuthoUser.personOwnCommand){
            if (personTeams.getTeam().equals(team.getId())){
                personTeams2 = personTeams;
                break;
            }
        }
        EditCommand editCommand = new EditCommand();
        editCommand.setId(league.getId());
        editCommand.setTeamId(team.getId());
        editCommand.setPlayers(playerList);
        Call<EditCommandResponse> call2 = Controller.getApi().editTeam( SaveSharedPreference.getObject().getToken(), editCommand);
        final PersonTeams finalPersonTeams = personTeams2;
        call2.enqueue(new Callback<EditCommandResponse>() {
                @Override
                public void onResponse(Call<EditCommandResponse> call, Response<EditCommandResponse> response) {
                    log.info("INFO: check response");
                    if (response.isSuccessful()) {
                        log.info("INFO: response isSuccessful");
                        if (response.body() != null) {
                            List<Player> players = response.body().getPlayers();
                            team.setPlayers(players);
                            String teamId = team.getId();
                            Team teamLeague = null;
                            for (Team team: league.getTeams()){
                                if (team.getId().equals(teamId)){
                                    teamLeague = team;
                                    break;
                                }
                            }
                            league.getTeams().remove(teamLeague);
                            league.getTeams().add(team);

                            PersonTeams personTeams = new PersonTeams();
                            personTeams.setLeague(league.getId());
                            personTeams.setTeam(team.getId());

                            User user = SaveSharedPreference.getObject();
                            Person person = user.getUser();
                            List<PersonTeams> list = new ArrayList<>(person.getParticipation());
                            for (int i=0; i<list.size(); i++){
                                if (list.get(i).getLeague().equals(finalPersonTeams.getLeague())){
                                    list.set(i, personTeams);
                                }
                            }
                            person.setParticipation(list);
                            user.setUser(person);
                            SaveSharedPreference.editObject(user);



                            for (int i=0; i<AuthoUser.personOwnCommand.size(); i++){
                                if (AuthoUser.personOwnCommand.get(i).getLeague().equals(finalPersonTeams.getLeague())){
                                    AuthoUser.personOwnCommand.set(i, personTeams);
                                }
                            }
//                            AuthoUser.personOwnCommand.remove(finalPersonTeams);





                            for (int i = 0; i< PersonalActivity.tournaments.size(); i++){
                                if (league.getId().equals(PersonalActivity.tournaments.get(i).getId())){
                                    PersonalActivity.tournaments.set(i, league);
                                }
                            }



                            List<PersonTeams> result = new ArrayList<>(AuthoUser.personOwnCommand);
                            AuthoUser.adapterOwnCommand.dataChanged(result);
//                            AuthoUser.adapterOwnCommand.notifyDataSetChanged();
                            Toast.makeText(UserCommandInfo.this, "Изменения сохранены.", Toast.LENGTH_LONG).show();

                            //all is ok
                            finish();
//                        }
                        }
                    }
                    else {
                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            String str = "Ошибка! ";
                            str += jsonObject.getString("message");
                            Toast.makeText(UserCommandInfo.this, str, Toast.LENGTH_LONG).show();
                            finish();
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<EditCommandResponse> call, Throwable t) {
                    log.error("ERROR: ", t);
                }
            });

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
