package baikal.web.footballapp.user.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import baikal.web.footballapp.CheckError;
import baikal.web.footballapp.Controller;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.PersonTeams;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.players.activity.PlayerInv;
import baikal.web.footballapp.players.activity.PlayersPage;
import baikal.web.footballapp.players.adapter.RVPlayerInvAdapter;
import baikal.web.footballapp.players.adapter.RecyclerViewPlayersAdapter;
import baikal.web.footballapp.user.adapter.TrainerAdapter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ChooseTrainer extends AppCompatActivity {
    private RecyclerView recyclerView;
    private int count = 0;
    private int offset = 0;
    private final int limit = 10;
    public static TrainerAdapter adapter;
    //    RecyclerViewPlayersAdapter adapter;
    private final Logger log = LoggerFactory.getLogger(PlayersPage.class);
    private SearchView searchView;
    private ProgressBar progressBar;
    private final List<Person> result = new ArrayList<>();
    private ProgressDialog mProgressDialog;
    private final List<Person> people = new ArrayList<>();
    private final List<Person> allPeople = new ArrayList<>();
    private NestedScrollView scroller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_players);
        mProgressDialog = new ProgressDialog(this, R.style.MyProgressDialogTheme);
        mProgressDialog.setIndeterminate(true);
//        mProgressDialog.setMessage("Загрузка...");
        final View view;
        getAllPlayers("10", "0");
        scroller = findViewById(R.id.scrollerPlayersPage);
        searchView =findViewById(R.id.searchView);
        progressBar =findViewById(R.id.progressSearch);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/manrope_regular.otf");
        SearchView.SearchAutoComplete theTextArea = searchView.findViewById(R.id.search_src_text);
        theTextArea.setTextColor(getResources().getColor(R.color.colorBottomNavigationUnChecked));
        theTextArea.setTypeface(tf);
        theTextArea.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        searchView.setQueryHint(Html.fromHtml("<font color = #63666F>" + getResources().getString(R.string.search) + "</font>"));
        ImageView icon = searchView.findViewById(androidx.appcompat.R.id.search_button);
        icon.setColorFilter(getResources().getColor(R.color.colorLightGrayForText), PorterDuff.Mode.SRC_ATOP);
        ImageView searchViewClose = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        searchViewClose.setColorFilter(getResources().getColor(R.color.colorLightGrayForText), PorterDuff.Mode.SRC_ATOP);
        scroller.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                offset++;
                int temp = limit*offset;
                if (temp<=count && result.size()== 0) {
                    String str = String.valueOf(temp);
                    getAllPlayers("10", str);
                }
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchUsers(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnCloseListener(() -> {
            result.clear();
            people.clear();
            people.addAll(allPeople);
            List<Person> list = new ArrayList<>(people);
            adapter.dataChanged(list);
            return false;
        });

        try{
            recyclerView = findViewById(R.id.recyclerViewPlayers);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new TrainerAdapter(this,  PersonalActivity.allPlayers,(id , name, surname) -> {
                Intent data = new Intent();
//---set the data to pass back---
                data.setData(Uri.parse(id));
                data.putExtra("name",name );
                data.putExtra("surname", surname);
                setResult(RESULT_OK, data);
//---close the activity---
                finish();
            });
            recyclerView.setAdapter(adapter);
        }catch (Exception e){log.error("ERROR: ", e);}

    }

    @SuppressLint("CheckResult")
    private void SearchUsers(String search){
//        PersonalActivity.people.clear();
        Controller.getApi().getAllPersons( search, "32575", "0")
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(__ -> showDialog())
                .doOnTerminate(this::hideDialog)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::savePlayers,
                        error -> {
                            CheckError checkError = new CheckError();
                            checkError.checkError(this, error);
                        }
                );

    }

    private void savePlayers(List<Person> people) {
        result.clear();
        result.addAll(people);
        adapter.dataChanged(result);
    }

    @Override
    public void onPause() {
        PersonalActivity.people.clear();
        PersonalActivity.people.addAll(PersonalActivity.AllPeople);
        super.onPause();
    }

    private void showDialog() {

        if (mProgressDialog != null && !mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    private void hideDialog() {

        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }


    @SuppressLint("CheckResult")
    private void getAllPlayers(String limit, String offset) {
        CheckError checkError = new CheckError();
        String type = "player";
        Controller.getApi().getAllPersons( null, limit, offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::saveAllPlayers,
                        error -> checkError.checkError(this, error)
                );
    }

    private void saveAllPlayers(List<Person> peopleList) {
        count += peopleList.size();
        people.addAll(people.size(), peopleList);
        List<Person> list = new ArrayList<>(people);
        allPeople.clear();
        allPeople.addAll(people);
        adapter.dataChanged(list);
    }
}



