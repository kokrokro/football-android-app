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
import android.widget.Toast;

import baikal.web.footballapp.CheckError;
import baikal.web.footballapp.Controller;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.model.ActiveMatch;
import baikal.web.footballapp.model.Match;
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
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class EditTimeTable extends AppCompatActivity {
    private List<String> countReferees;
    private final Logger log = LoggerFactory.getLogger(EditTimeTable.class);
    private ActiveMatch match;

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

            List<Person> referees = new ArrayList<>();
            referees.add(null);
            referees.addAll(1, AuthoUser.allReferees);
            match = (ActiveMatch) getIntent().getExtras().getSerializable("MATCHCONFIRMPROTOCOLREFEREES");

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
                        case "Хронометрист":
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
            SpinnerRefereeAdapter adapter = new SpinnerRefereeAdapter(this, R.layout.spinner_item, referees);
            spinnerReferee1.setAdapter(adapter);
            spinnerReferee1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    Person person = (Person) parent.getItemAtPosition(pos);
                    if (person != null) {
                        countReferees.set(0, person.getId());
                    } else {
                        countReferees.set(0, null);
                    }
                }

                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            SpinnerRefereeAdapter adapter1 = new SpinnerRefereeAdapter(this, R.layout.spinner_item, referees);
            spinnerReferee2.setAdapter(adapter1);
            spinnerReferee2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    Person person = (Person) parent.getItemAtPosition(pos);
                    if (person != null) {
                        countReferees.set(1, person.getId());
                    } else {
                        countReferees.set(1, null);
                    }
                }

                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            SpinnerRefereeAdapter adapter2 = new SpinnerRefereeAdapter(this, R.layout.spinner_item, referees);
            spinnerReferee3.setAdapter(adapter2);
            spinnerReferee3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    Person person = (Person) parent.getItemAtPosition(pos);
                    if (person != null) {
                        countReferees.set(2, person.getId());
                    } else {
                        countReferees.set(2, null);
                    }
                }

                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            SpinnerRefereeAdapter adapter3 = new SpinnerRefereeAdapter(this, R.layout.spinner_item, referees);
            spinnerReferee4.setAdapter(adapter3);
            spinnerReferee4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    Person person = (Person) parent.getItemAtPosition(pos);
                    if (person != null) {
                        countReferees.set(3, person.getId());
                    } else {
                        countReferees.set(3, null);
                    }
                }

                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            for (int j = 0; j < countReferees.size(); j++) {
                if (countReferees.get(j) != null && !countReferees.get(j).equals("")) {
                    // count +1
                    int count = allReferees.indexOf(countReferees.get(j)) + 1;
                    switch (j) {
                        case 0:
                            spinnerReferee1.setSelection(count);
                            break;
                        case 1:
                            spinnerReferee2.setSelection(count);
                            break;
                        case 2:
                            spinnerReferee3.setSelection(count);
                            break;
                        case 3:
                            spinnerReferee4.setSelection(count);
                            break;
                    }
                }
            }
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
        List<RefereeRequest> list = new ArrayList<>();
        for (int i = 0; i < countReferees.size(); i++) {
            if (countReferees.get(i) != null) {
                RefereeRequest refereeRequest = new RefereeRequest();
                String type = "";
                switch (i) {
                    case 0:
                        type = "1 судья";
                        break;
                    case 1:
                        type = "2 судья";
                        break;
                    case 2:
                        type = "3 судья";
                        break;
                    case 3:
                        type = "Хронометрист";
                        break;
                    default:
                        break;
                }
                refereeRequest.setType(type);
                refereeRequest.setPerson(countReferees.get(i));
                list.add(refereeRequest);
            }
        }
        RefereeRequestList requestList = new RefereeRequestList();
        requestList.setRefereeRequest(list);
        requestList.setId(match.getId());
//        Call<Matches> call = Controller.getApi().editProtocolReferees(AuthoUser.web.getToken(), requestList);
//        call.enqueue(new Callback<Matches>() {
//            @Override
//            public void onResponse(Call<Matches> call, Response<Matches> response) {
//                if (response.isSuccessful()) {
//                    if (response.body() != null) {
//                        Match match = response.body().getMatch();
//
//                        Intent intent = new Intent();
//                        Bundle bundle = new Bundle();
//                        bundle.putStringArrayList("PROTOCOLCOUNTREFEREES", (ArrayList<String>) countReferees);
//                        bundle.putSerializable("PROTOCOLMATCHAFTEREDITREFEREES", match);
//                        intent.putExtras(bundle);
//                        setResult(RESULT_OK, intent);
//                        Toast.makeText(ResponsiblePersons.this, "Изменения сохранены.", Toast.LENGTH_SHORT).show();
//                        finish(); //post
//                    }
//                }else {
//                    try {
//                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
//                        String str = "Ошибка! ";
//                        str += jsonObject.getString("message");
//                        Toast.makeText(ResponsiblePersons.this, str, Toast.LENGTH_LONG).show();
//                        finish();
//                    } catch (IOException | JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Matches> call, Throwable t) {
//                Toast.makeText(ResponsiblePersons.this, "Ошибка сервера.", Toast.LENGTH_SHORT).show();
//            }
//        });

        CheckError checkError = new CheckError();

//                .map(matchesResponse -> {
//                    if (!matchesResponse.isSuccessful()){
//                        log.error("QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ");
//                        checkError.checkHttpError(this, matchesResponse.errorBody().string());
//                    }
//                    else {
//                        log.error("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
//                    }
//                    return "";
//                })
//                .map(input -> { throw new RuntimeException(); })
//                .onErrorReturn(error -> "Uh oh")

//                .error(new IOException())
//                .onErrorResumeNext((Throwable e) -> Observable.error(new IllegalArgumentException()))
//                .onErrorResumeNext((Throwable e) -> Observable.error(new IllegalArgumentException()))
//                .map(b -> {
//                    try {
//                        Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
//                        throw new IOException("something went wrong");
//                    } catch (Exception e) {
////                        throw new RXIOException(e);
//                        Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
//                        // Or you can use
//                        throw Exceptions.propagate(e);
//                    }
//                })
//                .map(r -> r.success(r.response()))
//                .onErrorReturn(Model::error)
//                .retryWhen(throwableObservable -> throwableObservable.take(3).delay(30, TimeUnit.SECONDS))
//                .onErrorReturn((Throwable ex) -> {
//                    checkError.checkError(this, ex); //examine error here
//                    return null; //empty object of the datatype
//                })

//                .onErrorResumeNext(e -> {
//                    if (e instanceof HttpException && ((HttpException) e).code() == 401) {
//                        HttpException error = (HttpException) e;
//                        String errorBody = error.response().errorBody().string();
//                        JSONObject jsonObject = new JSONObject(errorBody);
//                        String str;
//                        str = jsonObject.getString("message");
//                        Toast.makeText(EditTimeTable.this, str, Toast.LENGTH_SHORT).show();
//                        return Observable.error(e);
//                    }
//                    else
//                    return Observable.error(e);
//                })
//                .subscribe(
//                        matches ->
//                                saveData(matches)
//                        ,
//                        error -> checkError.checkError(this, error)
////                        new Consumer<Object>() {
////                            @Override
////                            public void accept(Object o) throws Exception {
////                                if (o instanceof HttpException) {
////                                    HttpException e = (HttpException) o;
////                                    String errorBody = e.response().errorBody().string();
////                                    JSONObject jsonObject = new JSONObject(errorBody);
////                                    String str;
////                                    str = jsonObject.getString("message");
////                                    Toast.makeText(EditTimeTable.this, str, Toast.LENGTH_SHORT).show();
////                                } else {
////                                    saveData((Matches) o);
////                                }
////                            }
////                        }
//                );

//        Observable<Response<Matches>> observable =
                Controller.getApi().editProtocolReferees(SaveSharedPreference.getObject().getToken(), requestList)
                        .map(responseBody -> {
//                            try {
//                                return responseBody.body();
//                            } catch (Exception e) {
//                                responseBody.errorBody();
//                                e.printStackTrace();
//                            }
                            if (!responseBody.isSuccessful()){
                                String srt = responseBody.errorBody().string();
                                log.error(srt);
                                showToast(srt);
//                                log.error(responseBody.message());
                            }
                            if (responseBody.errorBody()!=null){
                                checkError.checkHttpError(this, responseBody.errorBody().string());
                            }
                            return responseBody.body();
                        })
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::saveData,
                                error -> checkError.checkError(this, error));

//                .subscribe(
////                        matches -> {
//                public void accept(Matches matches) throws Exception {
//                    try {
//                        if (matches.errorBody() != null){
//                            log.error("dggggggggggggggggggggggggggggggggggggggggggggggggf");
//                        }
//                        saveData(matches.body());
//
//                    }catch (Exception ex){
//                        try {
//                            checkError.checkError(EditTimeTable.this, ex);
//                        } catch (IOException | JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                }
//
//
//
//        public void onError(Throwable e){
//            HttpException error = (HttpException)e;
//            String errorBody = error.response().errorBody().string();
//            // now,you can do what you want to do ,like parse ....
//        }
////                        error->{
////
////                    log.error("hjnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn");
////
////                    checkError.checkError(EditTimeTable.this, error);}
//                );


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
