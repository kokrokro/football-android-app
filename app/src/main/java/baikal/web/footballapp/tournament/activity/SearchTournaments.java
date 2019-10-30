package baikal.web.footballapp.tournament.activity;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import baikal.web.footballapp.model.GetLeagueInfo;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.LeagueInfo;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.Tournaments;
import baikal.web.footballapp.players.activity.PlayersPage;
import baikal.web.footballapp.players.adapter.RecyclerViewPlayersAdapter;
import baikal.web.footballapp.tournament.adapter.RecyclerViewTournamentAdapter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchTournaments extends Fragment {
    private RecyclerView recyclerView;
    private int count = 0;
    private int offset = 0;
    private final int limit = 10;
    //    RecyclerViewPlayersAdapter adapter;
    private final Logger log = LoggerFactory.getLogger(PlayersPage.class);
    private SearchView searchView;
    private ProgressBar progressBar;
    private static RecyclerViewTournamentAdapter adapter;
    private final List<Person> result = new ArrayList<>();
    private ProgressDialog mProgressDialog;
    private final List<Person> people = new ArrayList<>();
    private final List<Person> allPeople = new ArrayList<>();
    private NestedScrollView scroller;
    private final List<League> tournaments= new ArrayList<>();
    public SearchTournaments() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mProgressDialog = new ProgressDialog(getActivity(), R.style.MyProgressDialogTheme);
        mProgressDialog.setIndeterminate(true);
        final View view = inflater.inflate(R.layout.fragment_search_tournaments, container, false);
        scroller = view.findViewById(R.id.scrollerPlayersPage);
        searchView = view.findViewById(R.id.searchView);
        progressBar = view.findViewById(R.id.progressSearch);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/manrope_regular.otf");
        SearchView.SearchAutoComplete theTextArea = searchView.findViewById(R.id.search_src_text);
        theTextArea.setTextColor(getResources().getColor(R.color.colorBottomNavigationUnChecked));
        theTextArea.setTypeface(tf);
        theTextArea.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        searchView.setQueryHint(Html.fromHtml("<font color = #63666F>" + getResources().getString(R.string.searchTourneys) + "</font>"));
        ImageView icon = searchView.findViewById(androidx.appcompat.R.id.search_button);
        icon.setColorFilter(getResources().getColor(R.color.colorLightGrayForText), PorterDuff.Mode.SRC_ATOP);
        ImageView searchViewClose = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        searchViewClose.setColorFilter(getResources().getColor(R.color.colorLightGrayForText), PorterDuff.Mode.SRC_ATOP);
        GetAllTournaments("20", "0");
        try {
            recyclerView = view.findViewById(R.id.recyclerViewSearch);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapter = new RecyclerViewTournamentAdapter(getActivity(), SearchTournaments.this, PersonalActivity.tournaments, leagueId -> showTournamentInfo(leagueId), PersonalActivity.allTourneys);
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            log.error("ERROR: ", e);
        }
        scroller.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                offset++;
                int temp = limit*offset;
                if (temp<=count) {
                    String str = String.valueOf(temp);
                    GetAllTournaments("10", str);
                }
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchTournaments(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return view;
    }
    @SuppressLint("CheckResult")
    private void GetAllTournaments(String limit, String offset) {
        Controller.getApi().getAllTournaments(limit, offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::saveAllData
                        ,
                        error -> {
                            CheckError checkError = new CheckError();
                            checkError.checkError(getActivity(), error);
                        }
                );
    }

    private void saveAllData(Tournaments tournaments1) {
        count = tournaments1.getCount();
        tournaments.addAll(tournaments.size(), tournaments1.getLeagues());
        List<League> list = new ArrayList<>(tournaments);
        //adapter.dataChanged(list);
    }
    @SuppressLint("CheckResult")
    private void showTournamentInfo(String leagueId){
        Controller.getApi().getLeagueInfo(leagueId)
                .subscribeOn(Schedulers.io())
//                .doOnTerminate(()->progressBarHide())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::saveData
                        ,
                        error -> {
                            CheckError checkError = new CheckError();
                            checkError.checkError(getActivity(), error);
                        }
                );
    }
    private void saveData(GetLeagueInfo getLeagueInfo) {
        LeagueInfo tournament1 = getLeagueInfo.getLeagueInfo();
        Bundle bundle = new Bundle();
        bundle.putSerializable("TOURNAMENTINFO", tournament1);
        Tournament tournament = new Tournament();
        tournament.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        try{
            fragmentManager.beginTransaction().add(R.id.pageContainer, tournament, "LEAGUEINFO").commit();
        }catch (Exception e){
            log.error("ERROR: ", e);
        }
        PersonalActivity.active = tournament;
    }
    @SuppressLint("CheckResult")
    private void SearchTournaments(String search){
//        PersonalActivity.people.clear();
//        String type = "player";
//        Controller.getApi().getAllTournaments(type, search, "32575", "0")
//                .subscribeOn(Schedulers.io())
//                .doOnSubscribe(__ -> showDialog())
//                .doOnTerminate(this::hideDialog)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(this::saveAllData,
//                        error -> {
//                            CheckError checkError = new CheckError();
//                            checkError.checkError(getActivity(), error);
//                        }
//                );

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
