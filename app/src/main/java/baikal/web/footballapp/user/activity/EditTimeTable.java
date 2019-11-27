package baikal.web.footballapp.user.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Match;
import baikal.web.footballapp.model.MatchPopulate;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.Referee;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditTimeTable extends AppCompatActivity {
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

        match = (MatchPopulate) Objects.requireNonNull(getIntent().getExtras()).getSerializable("MatchConfirmProtocolRefereesToEdit");

        if (match != null && match.getPlayed())
            fab.hide();

        if (match != null) {
            for(Referee ref : match.getReferees()) {
                switch (ref.getType()) {
                    case "firstReferee":
                        ref1Id = ref.getPerson();
                        Controller.getApi().getPerson(ref.getPerson()).enqueue(new Callback<List<Person>>() {
                            @Override
                            public void onResponse(@NonNull Call<List<Person>> call, @NonNull Response<List<Person>> response) {
                                if (response.isSuccessful())
                                    if (response.body() != null && response.body().size()>0)
                                        setPersonInitialsToTextView(response.body().get(0), ref1);
                            }
                            @Override
                            public void onFailure(@NonNull Call<List<Person>> call, @NonNull Throwable t) { }
                        });
                        break;
                    case "secondReferee":
                        ref2Id = ref.getPerson();
                        Controller.getApi().getPerson(ref.getPerson()).enqueue(new Callback<List<Person>>() {
                            @Override
                            public void onResponse(@NonNull Call<List<Person>> call, @NonNull Response<List<Person>> response) {
                                if (response.isSuccessful())
                                    if (response.body() != null && response.body().size()>0)
                                        setPersonInitialsToTextView(response.body().get(0), ref2);
                            }
                            @Override
                            public void onFailure(@NonNull Call<List<Person>> call, @NonNull Throwable t) { }
                        });
                        break;
                    case "thirdReferee":
                        ref3Id = ref.getPerson();
                        Controller.getApi().getPerson(ref.getPerson()).enqueue(new Callback<List<Person>>() {
                            @Override
                            public void onResponse(@NonNull Call<List<Person>> call, @NonNull Response<List<Person>> response) {
                                if (response.isSuccessful())
                                    if (response.body() != null && response.body().size()>0)
                                        setPersonInitialsToTextView(response.body().get(0), ref3);
                            }
                            @Override
                            public void onFailure(@NonNull Call<List<Person>> call, @NonNull Throwable t) { }
                        });
                        break;
                    case "timekeeper":
                        ref4Id = ref.getPerson();
                        Controller.getApi().getPerson(ref.getPerson()).enqueue(new Callback<List<Person>>() {
                            @Override
                            public void onResponse(@NonNull Call<List<Person>> call, @NonNull Response<List<Person>> response) {
                                if (response.isSuccessful())
                                    if (response.body() != null && response.body().size()>0)
                                        setPersonInitialsToTextView(response.body().get(0), ref4);
                            }
                            @Override
                            public void onFailure(@NonNull Call<List<Person>> call, @NonNull Throwable t) { }
                        });
                }
            }
        }

        try {
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

    @SuppressLint("SetTextI18n")
    private void setPersonInitialsToTextView (Person p, TextView textView) {
        textView.setText(p.getSurname() + " " + p.getName() + " " + p.getLastname());
    }

    @SuppressLint("CheckResult")
    private void refereeRequest() {
        Log.d("EDITTIMETABLE: ", "trying to save referees...");
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
                editMatch(match.getId(), PersonalActivity.token, newMatch)
               .enqueue(new Callback<Match>() {
           @Override
           public void onResponse(@NonNull Call<Match> call, @NonNull Response<Match> response) {
               showToast("Судьи назначены успешно");

               Intent intent = new Intent();
               intent.putExtra("MatchIndex", getIntent().getIntExtra("MatchIndex", -1));
               setResult(RESULT_OK, intent);

               finish();
           }

           @Override
           public void onFailure(@NonNull Call<Match> call, @NonNull Throwable t) {
               showToast("Ошибка!");
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
            this.runOnUiThread(() -> Toast.makeText(EditTimeTable.this, str, Toast.LENGTH_SHORT).show());
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String name;
        String id;

        if (resultCode == RESULT_OK) {
            name = data.getStringExtra("surname") + " " + data.getStringExtra("name");
            id = Objects.requireNonNull(data.getData()).toString();
        } else {
            return;
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
}
