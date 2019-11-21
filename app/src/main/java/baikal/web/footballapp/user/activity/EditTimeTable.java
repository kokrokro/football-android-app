package baikal.web.footballapp.user.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import baikal.web.footballapp.CheckError;
import baikal.web.footballapp.Controller;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.model.ActiveMatch;
import baikal.web.footballapp.model.Match;
import baikal.web.footballapp.model.MatchPopulate;
import baikal.web.footballapp.model.Matches;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.Referee;
import baikal.web.footballapp.model.RefereeRequest;
import baikal.web.footballapp.model.RefereeRequestList;
import baikal.web.footballapp.user.adapter.SpinnerRefereeAdapter;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditTimeTable extends AppCompatActivity {
    private List<String> countReferees;
    private final Logger log = LoggerFactory.getLogger(EditTimeTable.class);
    private MatchPopulate match;
    private Button ref1;
    private Button ref2;
    private Button ref3;
    private Button ref4;
    private String ref1Id = null;
    private String ref2Id = null;
    private String ref3Id = null;
    private String ref4Id = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ImageButton imageClose;
        ImageButton imageSave;

        FloatingActionButton fab;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_timetable);
        fab = findViewById(R.id.buttonMainRefereeShowProtocol);
        imageClose = findViewById(R.id.refereeEditMatchClose);
        imageSave = findViewById(R.id.refereeEditMatchSave);
        ref1 = findViewById(R.id.refereeEditMatchSpinnerReferee1);
        ref2 = findViewById(R.id.refereeEditMatchSpinnerReferee2);
        ref3 = findViewById(R.id.refereeEditMatchSpinnerReferee3);
        ref4 = findViewById(R.id.refereeEditMatchSpinnerReferee4);

        imageClose.setOnClickListener(v -> finish());
        countReferees = new ArrayList<>();
        countReferees.add("0");
        countReferees.add("0");
        countReferees.add("0");
        countReferees.add("0");

            List<Person> referees = new ArrayList<>();
            referees.add(null);
            referees.addAll(1, AuthoUser.allReferees);
            match = (MatchPopulate) getIntent().getExtras().getSerializable("MATCHCONFIRMPROTOCOLREFEREES");

        if (match.getPlayed()){
            fab.hide();
        }
            for(Referee ref : match.getReferees()) {
                switch (ref.getType()) {
                    case "firstReferee":
                        Controller.getApi().getPerson(ref.getPerson()).enqueue(new Callback<List<Person>>() {
                            @Override
                            public void onResponse(Call<List<Person>> call, Response<List<Person>> response) {
                                if (response.isSuccessful()) {
                                    if (response.body() != null && response.body().size()>0) {
                                        Person p = response.body().get(0);
                                        ref1.setText(p.getSurname()+" "+ p.getName());
                                        ref1Id = p.getId();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<List<Person>> call, Throwable t) {

                            }
                        });
                    case "secondReferee":
                        Controller.getApi().getPerson(ref.getPerson()).enqueue(new Callback<List<Person>>() {
                            @Override
                            public void onResponse(Call<List<Person>> call, Response<List<Person>> response) {
                                if (response.isSuccessful()) {
                                    if (response.body() != null && response.body().size()>0) {
                                        Person p = response.body().get(0);
                                        ref2.setText(p.getSurname()+" "+ p.getName());
                                        ref2Id = p.getId();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<List<Person>> call, Throwable t) {

                            }
                        });
                    case "thirdReferee":
                        Controller.getApi().getPerson(ref.getPerson()).enqueue(new Callback<List<Person>>() {
                            @Override
                            public void onResponse(Call<List<Person>> call, Response<List<Person>> response) {
                                if (response.isSuccessful()) {
                                    if (response.body() != null && response.body().size()>0) {
                                        Person p = response.body().get(0);
                                        ref3.setText(p.getSurname()+" "+ p.getName());
                                        ref3Id = p.getId();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<List<Person>> call, Throwable t) {

                            }
                        });
                    case "timekeeper":
                        Controller.getApi().getPerson(ref.getPerson()).enqueue(new Callback<List<Person>>() {
                            @Override
                            public void onResponse(Call<List<Person>> call, Response<List<Person>> response) {
                                if (response.isSuccessful()) {
                                    if (response.body() != null && response.body().size()>0) {
                                        Person p = response.body().get(0);
                                        ref4.setText(p.getSurname()+" "+ p.getName());
                                        ref4Id = p.getId();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<List<Person>> call, Throwable t) {

                            }
                        });

                }
            }

            try{
            imageSave.setOnClickListener(v -> refereeRequest());
            fab.setOnClickListener(v -> {
                Intent intent = new Intent(EditTimeTable.this, ConfirmProtocol.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("CONFIRMPROTOCOL", match);
                intent.putExtras(bundle);
                startActivity(intent);
            });
        } catch (NullPointerException e) {
            log.error("ERROR: ", e);
        }
    }

    @SuppressLint("CheckResult")
    private void refereeRequest() {
        List<Referee> refereeRequests = new ArrayList<>();


        if(ref1Id!=null){
            Referee ref = new Referee();
            ref.setPerson(ref1Id);
            ref.setType("firstReferee");
            refereeRequests.add(ref);
        }
        if(ref2Id!=null){
            Referee ref = new Referee();
            ref.setPerson(ref2Id);
            ref.setType("secondReferee");
            refereeRequests.add(ref);
        }
        if(ref3Id!=null){
            Referee ref = new Referee();
            ref.setPerson(ref3Id);
            ref.setType("thirdReferee");
            refereeRequests.add(ref);
        }
        if(ref4Id!=null){
            Referee ref = new Referee();
            ref.setPerson(ref4Id);
            ref.setType("timekeeper");
            refereeRequests.add(ref);
        }


        Match newMatch = new Match();
        newMatch.setReferees(refereeRequests);
        Controller.getApi().
               setReferees(match.getId(), PersonalActivity.token, newMatch)
               .enqueue(new Callback<Match>() {
           @Override
           public void onResponse(Call<Match> call, Response<Match> response) {

           }

           @Override
           public void onFailure(Call<Match> call, Throwable t) {
                log.error(t.getLocalizedMessage());
                log.error(t.getMessage());
           }
       });

    }
    public void getReferee(View v){
        if(v.getId() == ref1.getId()){
            Intent intent = new Intent(this, ChooseTrainer.class);
            startActivityForResult(intent, 1);
        }
        if(v.getId() == ref2.getId()){
            Intent intent = new Intent(this, ChooseTrainer.class);
            startActivityForResult(intent, 2);
        }
        if(v.getId() == ref3.getId()){
            Intent intent = new Intent(this, ChooseTrainer.class);
            startActivityForResult(intent, 3);
        }
        if(v.getId() == ref4.getId()){
            Intent intent = new Intent(this, ChooseTrainer.class);
            startActivityForResult(intent, 4);
        }
    }


    private void showToast (String str){
        try {
            JSONObject jsonObject  = new JSONObject(str);
            String str1 = jsonObject.getString("message");
            log.error(str);
            this.runOnUiThread(() -> Toast.makeText(EditTimeTable.this, str1, Toast.LENGTH_SHORT).show());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String name = "";
        String id = "";

        if (resultCode == RESULT_OK) {
              name =data.getStringExtra("surname")+" "+data.getStringExtra("name") ;
              id = data.getData().toString();
        }
        if (requestCode == 1) {
            ref1Id = id;
            ref1.setText(name);
        }
        if (requestCode == 2) {
            ref2.setText(name);
            ref2Id = id;
        }
        if (requestCode == 3) {
            ref3.setText(name);
            ref3Id = id;
        }
        if (requestCode == 4) {
            ref4.setText(name);
            ref4Id = id;
        }
    }

    private void saveData(Matches matches) {
        String str;
        str = "Изменения сохранены";
        Toast.makeText(EditTimeTable.this, str, Toast.LENGTH_SHORT).show();
        Match match = matches.getMatch();
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("PROTOCOLMATCHAFTEREDITREFEREES", match);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
//
    }
}
