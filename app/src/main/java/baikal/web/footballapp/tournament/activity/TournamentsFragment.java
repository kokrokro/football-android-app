package baikal.web.footballapp.tournament.activity;


import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import baikal.web.footballapp.CheckError;
import baikal.web.footballapp.Controller;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.home.activity.FullscreenNewsActivity;
import baikal.web.footballapp.model.GetLeagueInfo;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.LeagueInfo;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.PersonPopulate;
import baikal.web.footballapp.model.Tournaments;
import baikal.web.footballapp.model.Tourney;
//import baikal.web.footballapp.tournament.CustomLinearLayoutManager;
import baikal.web.footballapp.tournament.adapter.RVFavTourneyAdapter;
import baikal.web.footballapp.tournament.adapter.RVTourneyAdapter;
import baikal.web.footballapp.tournament.adapter.RecyclerViewTournamentAdapter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class TournamentsFragment extends Fragment {

    private static RVFavTourneyAdapter adapter;
    public static final List<Person> referees = new ArrayList<>();
    private final Logger log = LoggerFactory.getLogger(FullscreenNewsActivity.class);
    private RecyclerView recyclerView;
    //private final FragmentManager fragmentManager;
    private ProgressBar progressBar;
    private NestedScrollView scroller;
    private final List<League> tournaments= new ArrayList<>();
    private int count = 0;
    private int offset = 0;
    private final int limit = 5;
    public static List<ArrayList<League>> favLeague = new ArrayList<>( );
    private static List<Tourney> favTourney = new ArrayList<>();

    @SuppressLint("ValidFragment")
    public TournamentsFragment( ) {
        //this.fragmentManager = fragmentManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view;
        view = inflater.inflate(R.layout.fragment_tournaments, container, false);
        scroller = view.findViewById(R.id.scrollerLeague);
        try {
            recyclerView = view.findViewById(R.id.recyclerViewTournament);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            Controller.getApi().getFavTourneysByPerson(TournamentPage.id).enqueue(new Callback<List<PersonPopulate>>() {
                @Override
                public void onResponse(Call<List<PersonPopulate>> call, Response<List<PersonPopulate>> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            favTourney.clear();
                            favTourney.addAll(response.body().get(0).getFavouriteTourney());
                        }
                        }
                }

                @Override
                public void onFailure(Call<List<PersonPopulate>> call, Throwable t) {

                }
            });
            int i =0;
            for (String tr : TournamentPage.favTourneys){
                 List<League> leagues = new ArrayList<>();
                Controller.getApi().getLeaguesByTourney(tr).enqueue(new Callback<List<League>>() {
                    @Override
                    public void onResponse(Call<List<League>> call, Response<List<League>> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null)
                            {
                                leagues.addAll(response.body());
                            }
                            }
                        }

                    @Override
                    public void onFailure(Call<List<League>> call, Throwable t) {

                    }
                });
                favLeague.add(new ArrayList<>());
                favLeague.get(i).addAll(leagues);
                i++;
            }
            adapter = new RVFavTourneyAdapter(favTourney,getActivity() , favLeague);
            recyclerView.setAdapter(adapter);
            //adapter = new RecyclerViewTournamentAdapter(getActivity(), TournamentsFragment.this, PersonalActivity.tournaments, leagueId -> showTournamentInfo(leagueId), PersonalActivity.allTourneys);
            //recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            log.error("ERROR: ", e);
        }
//        scroller.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
//            if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
//                offset++;
//                int temp = limit*offset;
//                if (temp<=count) {
//                    String str = String.valueOf(temp);
//                    GetAllTournaments("10", str);
//                }
//            }
//        });
        return view;
    }
//
//    @SuppressLint("CheckResult")
//    private void GetAllTournaments(String limit, String offset) {
//        Controller.getApi().getAllLeagues(limit, offset )
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(this::saveAllData
//                        ,
//                        error -> {
//                            CheckError checkError = new CheckError();
//                            checkError.checkError(getActivity(), error);
//                        }
//                );
//    }

//    private void saveAllData(List<League> tournaments1) {
//        count += tournaments1.size();
//        Log.d("couuuunt leagues", " "+ tournaments1.size());
//        tournaments.addAll(tournaments.size(), tournaments1);
//        List<League> list = new ArrayList<>(tournaments);
//        adapter.dataChanged(list);
//    }

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


}
