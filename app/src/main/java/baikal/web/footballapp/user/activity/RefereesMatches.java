package baikal.web.footballapp.user.activity;

import android.annotation.SuppressLint;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import baikal.web.footballapp.CheckError;
import baikal.web.footballapp.Controller;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.model.ActiveMatch;
import baikal.web.footballapp.model.ActiveMatches;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.Referee;
import baikal.web.footballapp.model.RefereeRequest;
import baikal.web.footballapp.model.RefereeRequestList;
import baikal.web.footballapp.model.SetRefereeList;
import baikal.web.footballapp.user.adapter.RVRefereesMatchesAdapter;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RefereesMatches extends AppCompatActivity {
    private List<RefereeRequestList> refereeRequestLists;
    private RVRefereesMatchesAdapter adapter;
    private LinearLayout layout;
    private final Logger log = LoggerFactory.getLogger(RefereesMatches.class);
    private final List<ActiveMatch> matches = new ArrayList<>();
    private NestedScrollView scroller;
    private int count = 0;
    private final int limit = 5;
    private int offset = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        RecyclerView recyclerView;
        ImageButton buttonBack;
        ImageButton buttonSave;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.referees_matches);
        getActiveMatches("5", "0");
        scroller = findViewById(R.id.scrollerRefereesMatches);
        buttonBack = findViewById(R.id.refereesMatchesBack);
        buttonSave = findViewById(R.id.refereesMatchesSave);
        buttonBack.setOnClickListener(v -> finish());
        recyclerView = findViewById(R.id.recyclerViewRefereesMatches);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        layout = findViewById(R.id.emptyRefereesMatch);
        refereeRequestLists = new ArrayList<>();
        try{
            layout.setVisibility(View.GONE);
            Person person = (Person) getIntent().getExtras().getSerializable("REFEREESMATCHES");
//            List<Match> matches = person.getParticipationMatches();
            adapter = new RVRefereesMatchesAdapter(this, matches, person, (id, personId, check, type, position) -> {
                if (!check && refereeRequestLists.get(position).getId().equals(id)){
                    RefereeRequest referee = new RefereeRequest();
                    referee.setPerson(personId);
                    referee.setType(type);
                    List<RefereeRequest> list = refereeRequestLists.get(position).getRefereeRequest();
                    list.remove(referee);
                    refereeRequestLists.get(position).setRefereeRequest(list);
                }
                if (check && refereeRequestLists.get(position).getId().equals(id)){
                    RefereeRequest referee = new RefereeRequest();
                    referee.setPerson(personId);
                    referee.setType(type);
                    List<RefereeRequest> list = refereeRequestLists.get(position).getRefereeRequest();
                    list.add(referee);
                    refereeRequestLists.get(position).setRefereeRequest(list);
                }
            });
            recyclerView.setAdapter(adapter);
            scroller.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    offset++;
                    int temp = limit*offset;
                    if (temp<=count) {
                        String str = String.valueOf(temp);
                        getActiveMatches("5", str);
                    }
                }
            });
            buttonSave.setOnClickListener(v -> setReferees());
        }catch (NullPointerException e){
            layout.setVisibility(View.VISIBLE);
        }

    }
    @SuppressLint("CheckResult")
    private void getActiveMatches(String limit, String offset) {
//        Controller.getApi().getActiveMatches(limit, offset, false)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .repeatWhen(completed -> completed.delay(5, TimeUnit.MINUTES))
//                .subscribe(this::saveData
//                        ,
//                        error -> {
//                            layout.setVisibility(View.VISIBLE);
//                            log.error("ERROR: ", error);
//                            CheckError checkError = new CheckError();
//                            checkError.checkError(this, error);
//                        }
//                );
    }

    private void saveData(ActiveMatches matches1) {
        count = matches1.getCount();
        List<ActiveMatch> result;
        result = matches1.getMatches();
        if (result.size() != 0) {
            layout.setVisibility(View.GONE);
//            refereeRequestLists.clear();
            matches.addAll(matches.size(), result);
            List<RefereeRequestList> temp = new ArrayList<>();
            for (ActiveMatch activeMatch : result){
                RefereeRequestList count = new RefereeRequestList();
                count.setId(activeMatch.getId());
                List<RefereeRequest> requestList = new ArrayList<>();
                try {
                    for (Referee referee : activeMatch.getReferees()){
                        RefereeRequest refereeRequest = new RefereeRequest();
                        refereeRequest.setType(referee.getType());
                        refereeRequest.setPerson(referee.getPerson());
                        requestList.add(refereeRequest);
                    }
                }catch (NullPointerException e){
                    requestList.add(null);
                }
                count.setRefereeRequest(requestList);
                temp.add(count);
            }
            refereeRequestLists.addAll(refereeRequestLists.size(), temp);
            List<ActiveMatch> list = new ArrayList<>(matches);
            adapter.dataChanged(list);
        }
        if (matches.size()==0){
            layout.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("CheckResult")
    private void setReferees() {
        CheckError checkError = new CheckError();
        SetRefereeList setRefereeList = new SetRefereeList();
        setRefereeList.setRefereeRequestList(refereeRequestLists);
        Controller.getApi().setReferees(SaveSharedPreference.getObject().getToken(), setRefereeList)
                .map(responseBody -> {
                    if (!responseBody.isSuccessful()) {
                        String srt = responseBody.errorBody().string();
                        showToast(srt);
                    }
                    if (responseBody.errorBody() != null) {
                        checkError.checkHttpError(this, responseBody.errorBody().string());
                    }
                    return responseBody.body();
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                            String str = "Изменения сохранены";
                            showToastResult(str);
                            finish();
                        },
                        error -> checkError.checkError(this, error));

    }
    private void showToast(String str) {
        try {
            JSONObject jsonObject = new JSONObject(str);
            String str1 = jsonObject.getString("message");
            this.runOnUiThread(() -> Toast.makeText(RefereesMatches.this, str1, Toast.LENGTH_SHORT).show());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void showToastResult(String str) {
        this.runOnUiThread(() -> Toast.makeText(RefereesMatches.this, str, Toast.LENGTH_SHORT).show());
    }
}
