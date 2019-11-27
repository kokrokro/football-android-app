package baikal.web.footballapp.tournament.activity;


import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import baikal.web.footballapp.viewmodel.MainViewModel;
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
    public static List<League> favLeague = new ArrayList<>( );
    private static List<Tourney> favTourney = new ArrayList<>();
    private static List<String> favTourneyId = new ArrayList<>();

    private MainViewModel mainViewModel;
    private TournamentPage tournamentPage;
    @SuppressLint("ValidFragment")
    public TournamentsFragment(TournamentPage tournamentPage) { this.tournamentPage = tournamentPage; }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view;
        view = inflater.inflate(R.layout.fragment_tournaments, container, false);
        scroller = view.findViewById(R.id.scrollerLeague);
        try {
            recyclerView = view.findViewById(R.id.recyclerViewTournament);
            recyclerView.setNestedScrollingEnabled(false);
            RVFavTourneyAdapter.Listener listener = id -> showTournamentInfo(id);
            mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
            mainViewModel.getFavTourney(PersonalActivity.id).observe(this, tourneys -> {
                favTourney.clear();
                favTourney.addAll(tourneys);
                favTourneyId.clear();
                favLeague.clear();

                String tourneyIds = "";

                for (Tourney tr : tourneys){
                    favTourneyId.add(tr.getId());
                    tourneyIds += "," + tr.getId();
                }
                getFavLeagues(tourneyIds, leagues -> {

                    mainViewModel.setFavTourneysId(favTourneyId);
                    adapter = new RVFavTourneyAdapter(favTourney , getActivity(), favLeague, listener);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter.dataChanged(tourneys);
                });


            });

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

    private void getFavLeagues(String tr, MyCallback callback){
        List<League> leagues = new ArrayList<>();
        Controller.getApi().getLeaguesByTourney(tr).enqueue(new Callback<List<League>>() {
            @Override
            public void onResponse(Call<List<League>> call, Response<List<League>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null)  {
                        favLeague.clear();
                        favLeague.addAll(response.body());
                        adapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onFailure(Call<List<League>> call, Throwable t) {
            }
        });
        callback.onDataGot(leagues);
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
        Log.d("deeeeeed","showtournamentinfo");
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
    public interface MyCallback {
        void onDataGot(List<League> leagues);
    }
}
