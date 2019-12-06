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
import android.widget.ImageView;
import android.widget.ProgressBar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import baikal.web.footballapp.CheckError;
import baikal.web.footballapp.Controller;
import baikal.web.footballapp.MankindKeeper;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.players.activity.PlayersPage;
import baikal.web.footballapp.user.adapter.TrainerAdapter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ChooseTrainer extends AppCompatActivity {
    private int offset = 0;
    private final int limit = 10;
    public TrainerAdapter adapter;
    private final Logger log = LoggerFactory.getLogger(PlayersPage.class);
    private ProgressDialog mProgressDialog;
    private List<String> allPeople = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_players);
        mProgressDialog = new ProgressDialog(this, R.style.MyProgressDialogTheme);
        mProgressDialog.setIndeterminate(true);

        allPeople.addAll(MankindKeeper.getInstance().allPlayers.keySet());
//        mProgressDialog.setMessage("Загрузка...");
        getAllPlayers("10", "0");
        NestedScrollView scroller = findViewById(R.id.scrollerPlayersPage);
        SearchView searchView = findViewById(R.id.searchView);
        ProgressBar progressBar = findViewById(R.id.progressSearch);
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
                if (temp <= allPeople.size()) {
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
            adapter.dataChanged(allPeople);
            return false;
        });

        try{
            RecyclerView recyclerView = findViewById(R.id.recyclerViewPlayers);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            adapter = new TrainerAdapter(this, allPeople, (id, name, surname) -> {
                Intent data = new Intent();
//---set the data to pass back---
                data.setData(Uri.parse(id));
                setResult(RESULT_OK, data);
//---close the activity---
                data.putExtra("name",name );
                data.putExtra("surname", surname);
                finish();
            });
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }catch (Exception e){log.error("ERROR: ", e);}

    }

    @SuppressLint("CheckResult")
    private void SearchUsers(String search){
        Controller.getApi().getAllPersons( search, "50", "0")
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
        List<String> res = new ArrayList<>();
        for (Person p: people)
            if (!MankindKeeper.getInstance().allPlayers.containsKey(p.get_id())) {
                MankindKeeper.getInstance().allPlayers.put(p.get_id(), p);
                res.add(p.getId());
            }
        adapter.dataChanged(res);
    }

    @Override
    public void onPause() {
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
        for (Person p: peopleList)
            if (!MankindKeeper.getInstance().allPlayers.containsKey(p.get_id())) {
                MankindKeeper.getInstance().allPlayers.put(p.get_id(), p);
                allPeople.add(p.getId());
            }
        adapter.dataChanged(allPeople);
    }
}



