package baikal.web.footballapp.tournament.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;


import baikal.web.footballapp.CheckError;
import baikal.web.footballapp.Controller;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.home.activity.FullscreenNewsActivity;
import baikal.web.footballapp.model.GetLeagueInfo;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.LeagueInfo;
import baikal.web.footballapp.model.People;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.Tournaments;
import baikal.web.footballapp.tournament.adapter.RecyclerViewTournamentAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@SuppressLint("ValidFragment")
public class TournamentPage extends Fragment {
    private static RecyclerViewTournamentAdapter adapter;
    public static final List<Person> referees = new ArrayList<>();
    private final Logger log = LoggerFactory.getLogger(FullscreenNewsActivity.class);
    private RecyclerView recyclerView;
    private final FragmentManager fragmentManager;
    private ProgressBar progressBar;
    private NestedScrollView scroller;
    private final List<League> tournaments= new ArrayList<>();
    private int count = 0;
    private int offset = 0;
    private final int limit = 5;
    @SuppressLint("ValidFragment")
    public TournamentPage(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view;
        view = inflater.inflate(R.layout.page_tournament, container, false);
        scroller = view.findViewById(R.id.scrollerLeague);
        progressBar =view.findViewById(R.id.progresLeague);
        GetAllReferees();
        GetAllTournaments("5", "0");
        try {
            recyclerView = view.findViewById(R.id.recyclerViewTournament);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapter = new RecyclerViewTournamentAdapter(getActivity(), TournamentPage.this, PersonalActivity.tournaments, leagueId -> showTournamentInfo(leagueId));
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
        adapter.dataChanged(list);
    }

    @SuppressLint("CheckResult")
    private void showTournamentInfo(String leagueId){
        Controller.getApi().getLeagueInfo(leagueId)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(__ -> progressBarShow())
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
        fragmentManager.beginTransaction().hide(PersonalActivity.active).show(tournament).addToBackStack(null).commit();
        PersonalActivity.active = tournament;
        progressBarHide();
    }

    @SuppressLint("CheckResult")
    private void GetAllReferees() {
        String type = "referee";
        Controller.getApi().getAllUsers(type, null, "32575", "0")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .repeatWhen(completed -> completed.delay(5, TimeUnit.MINUTES))
                .subscribe(this::saveReferees,
                        error -> {
                            CheckError checkError = new CheckError();
                            checkError.checkError(getActivity(), error);
                        }
                );
    }

    private void saveReferees(People people) {
        referees.clear();
        referees.addAll(people.getPeople());
    }

    private void progressBarShow(){
        progressBar.setVisibility(View.VISIBLE);
    }
    private void progressBarHide(){
        progressBar.setVisibility(View.GONE);
    }

}
