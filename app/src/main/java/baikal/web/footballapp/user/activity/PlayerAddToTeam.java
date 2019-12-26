package baikal.web.footballapp.user.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.text.Html;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.ProgressBar;

import baikal.web.footballapp.CheckError;
import baikal.web.footballapp.Controller;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.user.activity.UserTeams.UserCommandInfoEdit;
import baikal.web.footballapp.user.adapter.RVPlayerAddToTeamAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PlayerAddToTeam extends AppCompatActivity {
    Logger log = LoggerFactory.getLogger(PlayerAddToTeam.class);
    private RVPlayerAddToTeamAdapter adapter;
    private ProgressDialog mProgressDialog;
    private ProgressBar progressBar;
    private NestedScrollView scroller;
    private final List<Person> people = new ArrayList<>();
    private final List<Person> allPeople = new ArrayList<>();
    private final List<Person> result = new ArrayList<>();
    private int count = 0;
    private int offset = 0;
    private final int limit = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mProgressDialog = new ProgressDialog(this, R.style.MyProgressDialogTheme);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Загрузка...");
        RecyclerView recyclerView;
        SearchView searchView;
        super.onCreate(savedInstanceState);
        getAllPlayers("10", "0");
        setContentView(R.layout.player_add_to_team);
        recyclerView = findViewById(R.id.recyclerViewUserCommandAddPlayers);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar = findViewById(R.id.progressSearchPlayerToTeam);
        scroller = findViewById(R.id.scrollerPlayerAddToTeam);
        try {
            Team team = (Team) getIntent().getExtras().getSerializable("ADDPLAYERTOUSERTEAM");
            League league = (League) getIntent().getExtras().getSerializable("ADDPLAYERTOUSERTEAMLEAGUE");
//            adapter = new RVPlayerAddToTeamAdapter(this, PersonalActivity.people, team, league);
            adapter = new RVPlayerAddToTeamAdapter(this, people, team, league);
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
        }
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/manrope_regular.otf");
        searchView = findViewById(R.id.userCommandSearchView);
        SearchView.SearchAutoComplete theTextArea = searchView.findViewById(R.id.search_src_text);
        theTextArea.setTextColor(getResources().getColor(R.color.colorBottomNavigationUnChecked));
        theTextArea.setTypeface(tf);
        theTextArea.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        searchView.setQueryHint(Html.fromHtml("<font color = #63666F>" + getResources().getString(R.string.search) + "</font>"));
        ImageView icon = searchView.findViewById(androidx.appcompat.R.id.search_button);
        icon.setColorFilter(getResources().getColor(R.color.colorLightGrayForText), PorterDuff.Mode.SRC_ATOP);
        ImageView searchViewClose = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        searchViewClose.setColorFilter(getResources().getColor(R.color.colorLightGrayForText), PorterDuff.Mode.SRC_ATOP);
        scroller.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                offset++;
                int temp = limit*offset;
                if (temp<=count && result.size()==0) {
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

    }

    @SuppressLint("CheckResult")
    private void SearchUsers(String search) {
        String type = "player";
        Controller.getApi().getAllPersons(search, "32575", "0")
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
        adapter.dataChanged(people);
        result.clear();
        result.addAll(people);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        UserCommandInfoEdit.hideDialog();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @SuppressLint("CheckResult")
    private void getAllPlayers(String limit, String offset) {
        CheckError checkError = new CheckError();
        Controller.getApi().getAllPersons(null, limit, offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::saveAllPlayers,
                        error -> checkError.checkError(this, error)
                );
    }

    private void saveAllPlayers(List<Person> peopleList) {
        count = peopleList.size();
        people.addAll(people.size(), peopleList);
        List<Person> list = new ArrayList<>(people);
        allPeople.clear();
        allPeople.addAll(people);
        adapter.dataChanged(list);
    }

    private void showDialog() {

        if (mProgressDialog != null && !mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    private void hideDialog() {

        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

}
