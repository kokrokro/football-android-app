package baikal.web.footballapp.user.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
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
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditTimeTable extends AppCompatActivity {
    private List<String> countReferees;
    List<Person> referees = new ArrayList<>();
    private final Logger log = LoggerFactory.getLogger(EditTimeTable.class);
    private MatchPopulate match;

    private SpinnerRefereeAdapter adapter1;
    private SpinnerRefereeAdapter adapter2;
    private SpinnerRefereeAdapter adapter3;
    private SpinnerRefereeAdapter adapter4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ImageButton imageClose;
        ImageButton imageSave;

        Spinner spinnerReferee1;
        Spinner spinnerReferee2;
        Spinner spinnerReferee3;
        Spinner spinnerReferee4;

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

        referees.add(null);
        referees.addAll(1, AuthoUser.allReferees);
        match = (MatchPopulate) getIntent().getExtras().getSerializable("MATCHCONFIRMPROTOCOLREFEREES");

        if (match.getPlayed())
            fab.hide();

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

            adapter1 = new SpinnerRefereeAdapter(this, R.layout.spinner_item, referees);
            adapter2 = new SpinnerRefereeAdapter(this, R.layout.spinner_item, referees);
            adapter3 = new SpinnerRefereeAdapter(this, R.layout.spinner_item, referees);
            adapter4 = new SpinnerRefereeAdapter(this, R.layout.spinner_item, referees);

            setUpSpinner(spinnerReferee1, adapter1);
            setUpSpinner(spinnerReferee2, adapter2);
            setUpSpinner(spinnerReferee3, adapter3);
            setUpSpinner(spinnerReferee4, adapter4);

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

        getAllPersonsForRef("20", "0");
    }

    private void setUpSpinner (Spinner spinnerRef, SpinnerRefereeAdapter adapter)
    {
        spinnerRef.setAdapter(adapter);
        spinnerRef.setSelection(0);
        spinnerRef.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Person person = (Person) parent.getItemAtPosition(pos);
                if (person != null)
                    countReferees.set(0, person.getId());
                else
                    countReferees.set(0, null);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinnerRef.getBackground().setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_IN);
        spinnerRef.setOnFocusChangeListener( (v, hasFocus) -> {
                    if (hasFocus) {
                        spinnerRef.getBackground().clearColorFilter();
                    } else {
                        spinnerRef.getBackground()
                                .setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_IN);
                    }
                }
        );
    }

    private void updateReferees (List<Person> referees) {
        this.referees.addAll(referees);
        adapter1.notifyDataSetChanged();
        adapter2.notifyDataSetChanged();
        adapter3.notifyDataSetChanged();
        adapter4.notifyDataSetChanged();
    }

    @SuppressLint("CheckResult")
    private void getAllPersonsForRef(String limit, String offset) {
        Controller.getApi().getAllPersons( null, limit, offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateReferees,
                        error -> {
                            log.error("EditTimeTable", error);
                        }
                );
    }

    @SuppressLint("CheckResult")
    private void refereeRequest() {
        Log.d("Edit Time Table: ", "button save is clicked...");
        RefereeRequest ref = new RefereeRequest();
        ref.setPerson(PersonalActivity.id);
        ref.setType("firstReferee");
        List<RefereeRequest> refereeRequests = new ArrayList<>(4);
        refereeRequests.add(ref);
        ref.setType("secondReferee");
        refereeRequests.add(ref);
        ref.setType("thirdReferee");
        refereeRequests.add(ref);
        ref.setType("timekeeper");
        Controller.getApi().setReferees(match.getId(), PersonalActivity.token, refereeRequests).enqueue(new Callback<Match>() {
           @Override
           public void onResponse(Call<Match> call, Response<Match> response) {
               try {
                   Log.d("EDITTIMETABLE: ", response.toString());
                   Log.d("EDITTIMETABLE: ", response.body().toString());
               } catch (Exception e) {
                   log.error("EDITTIMETABLE: ", e);
               }
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
