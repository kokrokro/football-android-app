package baikal.web.footballapp.user.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import baikal.web.footballapp.CheckError;
import baikal.web.footballapp.Controller;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.model.Match;
import baikal.web.footballapp.model.Matches;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.RefereeRequest;
import baikal.web.footballapp.model.RefereeRequestList;
import baikal.web.footballapp.user.adapter.SpinnerRefereeAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static baikal.web.footballapp.Controller.BASE_URL;

public class ResponsiblePersons extends AppCompatActivity {
    private final Logger log = LoggerFactory.getLogger(ResponsiblePersons.class);
    private List<String> countReferees;
    private Match match;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ImageView image;
        ImageButton imageClose;
        ImageButton imageSave;
        Spinner spinnerInspector;
        Spinner spinnerTimekeeper;
        Spinner spinnerFirstReferee;
        Spinner spinnerSecondReferee;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.responsible_persons);
        imageClose = findViewById(R.id.responsiblePersonsClose);
        imageSave = findViewById(R.id.responsiblePersonsSave);
        image = findViewById(R.id.responsiblePersonsPhoto);
        spinnerInspector = findViewById(R.id.responsiblePersonsSpinnerInspector);
        spinnerTimekeeper = findViewById(R.id.responsiblePersonsSpinnerReferee4);
        spinnerFirstReferee = findViewById(R.id.responsiblePersonsSpinnerReferee1);
        spinnerSecondReferee = findViewById(R.id.responsiblePersonsSpinnerReferee2);
        imageClose.setOnClickListener(v -> finish());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.optionalCircleCrop();
        requestOptions.format(DecodeFormat.PREFER_ARGB_8888);
        RequestOptions.errorOf(R.drawable.ic_logo2);
        requestOptions.override(500, 500);
        requestOptions.priority(Priority.HIGH);
        String uriPic = BASE_URL;
        try {

            Intent arguments = getIntent();
            countReferees = Objects.requireNonNull(arguments.getExtras()).getStringArrayList("PROTOCOLREFEREES");
            match = (Match) Objects.requireNonNull(arguments.getExtras()).getSerializable("PROTOCOLMATCH");
            List<String> allReferees = new ArrayList<>();
            for (Person person : AuthoUser.allReferees) {
                allReferees.add(person.getId());
            }


            SpinnerRefereeAdapter adapter = new SpinnerRefereeAdapter(this, R.layout.spinner_item, AuthoUser.allReferees);
            spinnerInspector.setAdapter(adapter);
            spinnerInspector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    Person person = (Person) parent.getItemAtPosition(pos);
                    countReferees.set(0, person.getId());
                }

                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            SpinnerRefereeAdapter adapter1 = new SpinnerRefereeAdapter(this, R.layout.spinner_item, AuthoUser.allReferees);
            spinnerFirstReferee.setAdapter(adapter1);
            spinnerFirstReferee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    Person person = (Person) parent.getItemAtPosition(pos);
                    countReferees.set(1, person.getId());
                }

                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            SpinnerRefereeAdapter adapter2 = new SpinnerRefereeAdapter(this, R.layout.spinner_item, AuthoUser.allReferees);
            spinnerSecondReferee.setAdapter(adapter2);
            spinnerSecondReferee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    Person person = (Person) parent.getItemAtPosition(pos);
                    countReferees.set(2, person.getId());
                }

                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            SpinnerRefereeAdapter adapter3 = new SpinnerRefereeAdapter(this, R.layout.spinner_item, AuthoUser.allReferees);
            spinnerTimekeeper.setAdapter(adapter3);
            spinnerTimekeeper.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    Person person = (Person) parent.getItemAtPosition(pos);
                    countReferees.set(3, person.getId());
                }

                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            for (int j = 0; j < countReferees.size(); j++) {
                if (countReferees.get(j) != null && !countReferees.get(j).equals("")) {
                    int count = allReferees.indexOf(countReferees.get(j));
                    switch (j) {
                        case 0:
                            spinnerInspector.setSelection(count);
                            break;
                        case 1:
                            spinnerFirstReferee.setSelection(count);
                            break;
                        case 2:
                            spinnerSecondReferee.setSelection(count);
                            break;
                        case 3:
                            spinnerTimekeeper.setSelection(count);
                            break;
                    }
                }
            }
            try {
                if (SaveSharedPreference.getObject().getUser().getPhoto() != null) {
                    uriPic += "/" + SaveSharedPreference.getObject().getUser().getPhoto();
                    URL url = new URL(uriPic);
                    Glide.with(this)
                            .asBitmap()
                            .load(url)
//                    .load(R.drawable.ic_fin)
                            .apply(requestOptions)
                            .into(image);
                } else {
                    Glide.with(this)
                            .asBitmap()
                            .load(R.drawable.ic_logo2)
                            .apply(requestOptions)
                            .into(image);
                }
            } catch (Exception e) {
                Glide.with(this)
                        .asBitmap()
                        .load(R.drawable.ic_logo2)
                        .apply(requestOptions)
                        .into(image);
            }

            imageSave.setOnClickListener(v -> refereeRequest());
        } catch (Exception e) {
            log.error("ERROR: ", e);
        }
    }

    @SuppressLint("CheckResult")
    private void refereeRequest() {
        List<RefereeRequest> list = new ArrayList<>();
        for (int i = 0; i < countReferees.size(); i++) {
            RefereeRequest refereeRequest = new RefereeRequest();
            String type = "";
            switch (i) {
                case 0:
                    type = "Инспектор";
                    break;
                case 1:
                    type = "1 судья";
                    break;
                case 2:
                    type = "2 судья";
                    break;
                case 3:
                    type = "хронометрист";
                    break;
                default:
                    break;
            }
            refereeRequest.setType(type);
            refereeRequest.setPerson(countReferees.get(i));
            list.add(refereeRequest);
        }
        RefereeRequest refereeRequest = new RefereeRequest();
        refereeRequest.setType("3 судья");
        refereeRequest.setPerson(SaveSharedPreference.getObject().getUser().getId());
        list.add(refereeRequest);
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
        Controller.getApi().editProtocolReferees(SaveSharedPreference.getObject().getToken(), requestList)
                .subscribeOn(Schedulers.io())
//                .doOnSubscribe(__ -> showDialog())
//                .doOnTerminate(this::hideDialog())
//                .map(matchesResponse -> {
//                    if (!matchesResponse.isSuccessful()){
//                        checkError.checkHttpError(this, matchesResponse.errorBody().string());
//                    }
//                    return "";
//                })
//                .map(input -> { throw new RuntimeException(); })
                .retryWhen(throwableObservable -> throwableObservable.take(3).delay(30, TimeUnit.SECONDS))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(matches -> saveData( matches.body())
                        ,
                        error -> checkError.checkError(this, error)
                );

    }

    private void saveData(Matches matches) {
        Match match = matches.getMatch();
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("PROTOCOLCOUNTREFEREES", (ArrayList<String>) countReferees);
        bundle.putSerializable("PROTOCOLMATCHAFTEREDITREFEREES", match);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        Toast.makeText(ResponsiblePersons.this, "Изменения сохранены.", Toast.LENGTH_SHORT).show();
        finish(); //post
    }
}
