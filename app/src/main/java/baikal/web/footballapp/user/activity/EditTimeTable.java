package baikal.web.footballapp.user.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ImageButton imageClose;
        ImageButton imageSave;
        TextView spinnerReferee1;
        TextView spinnerReferee2;
        TextView spinnerReferee3;
        TextView spinnerReferee4;
        FloatingActionButton fab;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_timetable);
        fab = findViewById(R.id.buttonMainRefereeShowProtocol);
        imageClose = findViewById(R.id.refereeEditMatchClose);
        imageSave = findViewById(R.id.refereeEditMatchSave);
        spinnerReferee1 = findViewById(R.id.refereeEditMatchSpinnerReferee1);
        spinnerReferee2 = findViewById(R.id.refereeEditMatchSpinnerReferee2);
        spinnerReferee3 = findViewById(R.id.refereeEditMatchSpinnerReferee3);
        spinnerReferee4 = findViewById(R.id.refereeEditMatchSpinnerReferee4);

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
            match.getLeague();
        try {
            try{
                for (Referee referee : match.getReferees()) {
                    switch (referee.getType()) {
                        case "1 судья":
                            countReferees.set(0, referee.getPerson());
                            break;
                        case "2 судья":
                            countReferees.set(1, referee.getPerson());
                            break;
                        case "3 судья":
                            countReferees.set(2, referee.getPerson());
                            break;
                        case "хронометрист":
                            countReferees.set(3, referee.getPerson());
                            break;
                        default:
                            break;
                    }
                }
            }catch(NullPointerException e){}
            List<String> allReferees = new ArrayList<>();
            for (Person person1 : AuthoUser.allReferees) {
                allReferees.add(person1.getId());
            }
            spinnerReferee1.setOnClickListener(v->{
                spinnerReferee1.setText("5dd222d98701b2471e018bd3");
            });

//            SpinnerRefereeAdapter adapter = new SpinnerRefereeAdapter(this, R.layout.spinner_item, referees);
//            spinnerReferee1.setAdapter(adapter);
//            spinnerReferee1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//                    Person person = (Person) parent.getItemAtPosition(pos);
//                    if (person != null) {
//                        countReferees.set(0, person.getId());
//                    } else {
//                        countReferees.set(0, null);
//                    }
//                }
//
//                public void onNothingSelected(AdapterView<?> parent) {
//                }
//            });
//            SpinnerRefereeAdapter adapter1 = new SpinnerRefereeAdapter(this, R.layout.spinner_item, referees);
//            spinnerReferee2.setAdapter(adapter1);
//            spinnerReferee2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//                    Person person = (Person) parent.getItemAtPosition(pos);
//                    if (person != null) {
//                        countReferees.set(1, person.getId());
//                    } else {
//                        countReferees.set(1, null);
//                    }
//                }
//
//                public void onNothingSelected(AdapterView<?> parent) {
//                }
//            });
//            SpinnerRefereeAdapter adapter2 = new SpinnerRefereeAdapter(this, R.layout.spinner_item, referees);
//            spinnerReferee3.setAdapter(adapter2);
//            spinnerReferee3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//                    Person person = (Person) parent.getItemAtPosition(pos);
//                    if (person != null) {
//                        countReferees.set(2, person.getId());
//                    } else {
//                        countReferees.set(2, null);
//                    }
//                }
//
//                public void onNothingSelected(AdapterView<?> parent) {
//                }
//            });
//            SpinnerRefereeAdapter adapter3 = new SpinnerRefereeAdapter(this, R.layout.spinner_item, referees);
//            spinnerReferee4.setAdapter(adapter3);
//            spinnerReferee4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//                    Person person = (Person) parent.getItemAtPosition(pos);
//                    if (person != null) {
//                        countReferees.set(3, person.getId());
//                    } else {
//                        countReferees.set(3, null);
//                    }
//                }
//
//                public void onNothingSelected(AdapterView<?> parent) {
//                }
//            });
//            for (int j = 0; j < countReferees.size(); j++) {
//                if (countReferees.get(j) != null && !countReferees.get(j).equals("")) {
//                    // count +1
//                    int count = allReferees.indexOf(countReferees.get(j)) + 1;
//                    switch (j) {
//                        case 0:
//                            spinnerReferee1.setSelection(count);
//                            break;
//                        case 1:
//                            spinnerReferee2.setSelection(count);
//                            break;
//                        case 2:
//                            spinnerReferee3.setSelection(count);
//                            break;
//                        case 3:
//                            spinnerReferee4.setSelection(count);
//                            break;
//                    }
//                }
//            }
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
        Referee ref = new Referee();
        ref.setPerson(PersonalActivity.id);
        ref.setType("firstReferee");
        List<Referee> refereeRequests = new ArrayList<>();
        refereeRequests.add(ref);
        ref = new Referee();
        ref.setPerson(PersonalActivity.id);
        ref.setType("secondReferee");
        ref.setPerson(PersonalActivity.id);
        refereeRequests.add(ref);
        ref = new Referee();
        ref.setType("thirdReferee");
        ref.setPerson(PersonalActivity.id);
        refereeRequests.add(ref);
        ref = new Referee();
        ref.setType("timekeeper");
        ref.setPerson(PersonalActivity.id);
        refereeRequests.add(ref);
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
//        List<RefereeRequest> list = new ArrayList<>();
//        for (int i = 0; i < countReferees.size(); i++) {
//            if (countReferees.get(i) != null) {
//                RefereeRequest refereeRequest = new RefereeRequest();
//                String type = "";
//                switch (i) {
//                    case 0:
//                        type = "1 судья";
//                        break;
//                    case 1:
//                        type = "2 судья";
//                        break;
//                    case 2:
//                        type = "3 судья";
//                        break;
//                    case 3:
//                        type = "хронометрист";
//                        break;
//                    default:
//                        break;
//                }
//                refereeRequest.setType(type);
//                refereeRequest.setPerson(countReferees.get(i));
//                list.add(refereeRequest);
//            }
//        }
//        RefereeRequestList requestList = new RefereeRequestList();
//        requestList.setRefereeRequest(list);
//        requestList.setId(match.getId());
//
//
//        CheckError checkError = new CheckError();
//
//                Controller.getApi().editProtocolReferees(SaveSharedPreference.getObject().getToken(), requestList)
//                        .map(responseBody -> {
////                            try {
////                                return responseBody.body();
////                            } catch (Exception e) {
////                                responseBody.errorBody();
////                                e.printStackTrace();
////                            }
//                            if (!responseBody.isSuccessful()){
//                                String srt = responseBody.errorBody().string();
//                                log.error(srt);
//                                showToast(srt);
////                                log.error(responseBody.message());
//                            }
//                            if (responseBody.errorBody()!=null){
//                                checkError.checkHttpError(this, responseBody.errorBody().string());
//                            }
//                            return responseBody.body();
//                        })
//                        .subscribeOn(Schedulers.newThread())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(this::saveData,
//                                error -> checkError.checkError(this, error));

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
