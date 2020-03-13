package baikal.web.footballapp.user.activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.MankindKeeper;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.model.Club;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.user.adapter.SpinnerTournamentAdapter;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewCommand extends AppCompatActivity {
    private final Logger log = LoggerFactory.getLogger(NewCommand.class);
    private final List<Club> allClubs = new ArrayList<>();
    private final List<League> allTournaments = new ArrayList<>();
    private Spinner spinnerTournament;
    private League itemTournament;
    private Team team;
    private EditText textTitle;
    private EditText number;
    private Button trainer;
    private static String newTrainerId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageButton imageClose;
        ImageButton imageSave;
        setContentView(R.layout.user_new_command);
        allTournaments.clear();
        allTournaments.addAll(MankindKeeper.getInstance().allLeagues);

        log.error(String.valueOf(allClubs.size()));
        imageClose = findViewById(R.id.newCommandClose);
        imageSave = findViewById(R.id.newCommandSave);
        textTitle = findViewById(R.id.newCommandTitle);
        spinnerTournament = findViewById(R.id.newCommandTournamentSpinner);
        number = findViewById(R.id.newCommandNumber);
//        spinnerLeague = (Spinner) findViewById(R.id.newCommandLeagueSpinner);
        //spinnerClubs = findViewById(R.id.newCommandClubSpinner);
        trainer = findViewById(R.id.newCommandTrainer);

        try {
            spinnerTournament.setPopupBackgroundResource(R.color.colorWhite);
        } catch (Exception e) {}

        trainer.setOnClickListener(v->{
            Intent intent = new Intent(this, ChooseTrainer.class);
            startActivityForResult(intent, 24128);
        });

       // Drawable spinnerDrawable = spinnerClubs.getBackground().getConstantState().newDrawable();


        //spinnerDrawable.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);


       // spinnerClubs.setBackground(spinnerDrawable);


       // spinnerDrawable = spinnerTournament.getBackground().getConstantState().newDrawable();


        //spinnerDrawable.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);


        //spinnerTournament.setBackground(spinnerDrawable);
        SpinnerTournamentAdapter adapterTournament = new SpinnerTournamentAdapter(this, R.layout.spinner_item, allTournaments);

        adapterTournament.setDropDownViewResource(R.layout.spinner_dropdown);
        Drawable spinnerDrawable = spinnerTournament.getBackground().getConstantState().newDrawable();


        spinnerDrawable.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

        spinnerTournament.setBackground(spinnerDrawable);
        spinnerTournament.setSelection(0);
        spinnerTournament.setAdapter(adapterTournament);
        spinnerTournament.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                itemTournament = (League) parent.getItemAtPosition(pos);
            }


            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
//        adapterTournament.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        adapterTournament.setDropDownViewResource(R.layout.spinner_item);
//        SpinnerClubAdapter adapterClub = new SpinnerClubAdapter(this, R.layout.spinner_item, allClubs);
//
//
//
//
//        adapterClub.setDropDownViewResource(R.layout.spinner_dropdown);
//        spinnerClubs.setAdapter(adapterClub);
//        spinnerClubs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//                itemClub = (Club) parent.getItemAtPosition(pos);
//            }
//
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
        textTitle.getBackground().setColorFilter(getResources().getColor(R.color.colorLightGray), PorterDuff.Mode.SRC_IN);
        textTitle.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                textTitle.getBackground().clearColorFilter();
            } else {
                textTitle.getBackground().setColorFilter(getResources().getColor(R.color.colorLightGray), PorterDuff.Mode.SRC_IN);
            }
        });
        imageClose.setOnClickListener(v -> finish());
        imageSave.setOnClickListener(v -> CreateNewCommand());
    }

    private void CreateNewCommand() {
        League league = new League();
        String name = String.valueOf(textTitle.getText());
        Map<String, RequestBody> map = new HashMap<>();
        String token = SaveSharedPreference.getObject().getToken();
        String tournament = itemTournament.getId();
//        for (PersonTeams league1: AuthoUser.personOngoingLeagues){
        for (League league1 : MankindKeeper.getInstance().allLeagues) {
//            League tournament1 = league1.getLeague();
            if (league1.getId().equals(tournament)) {
                league = league1;
            }
        }
        String numberTrainer  = number.getText().toString();
        //String club = itemClub.getId();
        String creator = SaveSharedPreference.getObject().getUser().getId();
        RequestBody request = RequestBody.create(MediaType.parse("text/plain"), tournament);
        map.put("league", request);
       // request = RequestBody.create(MediaType.parse("text/plain"), club);
        //map.put("club", request);
        request = RequestBody.create(MediaType.parse("text/plain"), creator);
        map.put("creator", request);
        request = RequestBody.create(MediaType.parse("text/plain"), name);
        map.put("name", request);
        request = RequestBody.create(MediaType.parse("text/plain"), numberTrainer);
        map.put("creatorPhone", request);
        request = RequestBody.create(MediaType.parse("text/plain"), newTrainerId);
        map.put("trainer", request);


        Call<Team> call = Controller.getApi().addTeam(token, map);
        log.info("INFO: load and parse json-file");
        final League finalLeague = league;
        call.enqueue(new Callback<Team>() {
            @Override
            public void onResponse(Call<Team> call, Response<Team> response) {
                log.info("INFO: check response");
                if (response.isSuccessful()) {
                    log.info("INFO: response isSuccessful");
                    if (response.body() == null) {
                        log.error("ERROR: body is null");
                    } else {
                        team = response.body();
                        Map<String, RequestBody> map = new HashMap<>();
                        RequestBody request = RequestBody.create(MediaType.parse("text/plain"), tournament);
                        map.put("league", request);
                        request = RequestBody.create(MediaType.parse("text/plain"), team.getId());
                        map.put("team",request);
                        Controller.getApi().addTeamToLeague(token,map).enqueue(new Callback<Team>() {
                            @Override
                            public void onResponse(Call<Team> call, Response<Team> response) {
                                if(response.isSuccessful()){
                                    {
                                        if(response.body()!=null){
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<Team> call, Throwable t) {
                                log.error(t.getMessage());
                            }
                        });
//                        PersonTeams personTeams = new PersonTeams();
//                        personTeams.setTeam(team.getId());
//                        finalLeague.getTeams().add(team);
//                        personTeams.setLeague(finalLeague.getId());
//                        personTeams.setId("000");
//                        AuthoUser.personOwnCommand.add(personTeams);
//                        AuthoUser.personOngoingLeagues.add(personTeams);


//                        User user = SaveSharedPreference.getObject();
//                        Person person = user.getUser();
//                        List<PersonTeams> list = new ArrayList<>(person.getParticipation());
//                        list.add(personTeams);
//                        person.setParticipation(list);
//                        SaveSharedPreference.editObject(user);


//                        UserCommands.adapterCommand.notifyDataSetChanged();
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish(); //post
                    }
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        String str = "Ошибка! ";
                        str += jsonObject.getString("message");
                        Toast.makeText(NewCommand.this, str, Toast.LENGTH_LONG).show();
                        finish();
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Team> call, Throwable t) {
                log.error("ERROR: onResponse", t);
                Toast.makeText(NewCommand.this, "Ошибка сервера.", Toast.LENGTH_LONG).show();
//                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 24128) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Person person = (Person) data.getExtras().getSerializable("PERSON");
                newTrainerId = person.getId();
                String str  = person.getNameWithSurname();
                trainer.setText(str);
            }
        }
    }

}
