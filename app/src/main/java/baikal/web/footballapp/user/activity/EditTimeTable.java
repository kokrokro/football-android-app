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
import baikal.web.footballapp.MankindKeeper;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
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
import java.util.TreeMap;

import baikal.web.footballapp.user.activity.Protocol.ConfirmProtocol;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditTimeTable extends AppCompatActivity {
    private static final String TAG = "EditTimeTable";
    private final Logger log = LoggerFactory.getLogger(EditTimeTable.class);
    private MatchPopulate match;
    private Button ref1;
    private Button ref2;
    private Button ref3;
    private Button ref4;
    private List<String> refIds;
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

        refIds = new ArrayList<>();
        refIds.add(null);
        refIds.add(null);
        refIds.add(null);
        refIds.add(null);

        imageClose.setOnClickListener(v -> finish());

        match = (MatchPopulate) Objects.requireNonNull(getIntent().getExtras()).getSerializable("MatchConfirmProtocolRefereesToEdit");

        if (match != null && match.getPlayed())
            fab.hide();

        TreeMap<String, Integer> refTypeId = new TreeMap<>();
        refTypeId.put("firstReferee" , 0);
        refTypeId.put("secondReferee", 1);
        refTypeId.put("thirdReferee" , 2);
        refTypeId.put("timekeeper"   , 3);

        TreeMap<String, TextView> textViewRefs = new TreeMap<>();
        textViewRefs.put("firstReferee" , ref1);
        textViewRefs.put("secondReferee", ref2);
        textViewRefs.put("thirdReferee" , ref3);
        textViewRefs.put("timekeeper"   , ref4);

        if (match != null) {
            for(Referee ref : match.getReferees()) {
                refIds.set(Objects.requireNonNull(refTypeId.get(ref.getType())), ref.getPerson());
                Person p;

                try {
                    p = MankindKeeper.getInstance().getPersonById(ref.getPerson());
                } catch (Exception e) {
                    log.error(TAG, e);
                    p = null;
                }

                if (p == null) {
                    Controller.getApi().getPerson(ref.getPerson()).enqueue(new Callback<List<Person>>() {
                        @Override
                        public void onResponse(@NonNull Call<List<Person>> call, @NonNull Response<List<Person>> response) {
                            if (response.isSuccessful())
                                if (response.body() != null && response.body().size()>0) {
                                    Person pp = response.body().get(0);
                                    MankindKeeper.getInstance().addPerson(pp);
                                    setPersonInitialsToTextView(pp, Objects.requireNonNull(textViewRefs.get(ref.getType())));
                                }
                        }
                        @Override
                        public void onFailure(@NonNull Call<List<Person>> call, @NonNull Throwable t) { }
                    });
                } else
                    setPersonInitialsToTextView(p, Objects.requireNonNull(textViewRefs.get(ref.getType())));
            }
        }

        try {
            imageSave.setOnClickListener(v -> refereeRequest());
            fab.setOnClickListener(v -> {
                Intent intent = new Intent(EditTimeTable.this, ConfirmProtocol.class);
                Bundle bundle = new Bundle();
//                boolean isProtocolAvailable = getIntent().getExtras().getBoolean("STATUS");
                bundle.putBoolean("IS_EDITABLE", true);
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
        textView.setTextColor(getResources().getColor(R.color.colorBottomNavigationUnChecked));
    }

    @SuppressLint("CheckResult")
    private void refereeRequest() {
        Log.d(TAG, "trying to save referees...");
        List<Referee> refereeRequests = new ArrayList<>();

        if(refIds.get(0)!=null){
            Referee ref = new Referee();
            ref.setPerson(refIds.get(0));
            ref.setType("firstReferee");
            refereeRequests.add(ref);
        }
        if(refIds.get(1)!=null){
            Referee ref = new Referee();
            ref.setPerson(refIds.get(1));
            ref.setType("secondReferee");
            refereeRequests.add(ref);
        }
        if(refIds.get(2)!=null){
            Referee ref = new Referee();
            ref.setPerson(refIds.get(2));
            ref.setType("thirdReferee");
            refereeRequests.add(ref);
        }
        if(refIds.get(3)!=null){
            Referee ref = new Referee();
            ref.setPerson(refIds.get(3));
            ref.setType("timekeeper");
            refereeRequests.add(ref);
        }

        Match newMatch = new Match();
        newMatch.setReferees(refereeRequests);
        Controller.getApi().
                editMatch(match.getId(), SaveSharedPreference.getObject().getToken(), newMatch)
               .enqueue(new Callback<Match>() {
           @Override
           public void onResponse(@NonNull Call<Match> call, @NonNull Response<Match> response) {
               showToast("Судьи назначены успешно");

               Intent intent = new Intent();
               Bundle bundle = new Bundle();
               bundle.putSerializable("MatchWithNewRefs", response.body());
               intent.putExtra("MatchIndex", getIntent().getIntExtra("MatchIndex", -1));
               intent.putExtras(bundle);

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

        if (resultCode == RESULT_OK) {
            String id = Objects.requireNonNull(data.getData()).toString();

            List<TextView> refs = new ArrayList<>();
            refs.add(ref1);
            refs.add(ref2);
            refs.add(ref3);
            refs.add(ref4);

            try {
                refIds.set(requestCode - 1, id);
                setPersonInitialsToTextView(Objects.requireNonNull(MankindKeeper.getInstance().getPersonById(id)), refs.get(requestCode - 1));
            } catch (Exception ignored) {}
        }
    }
}
