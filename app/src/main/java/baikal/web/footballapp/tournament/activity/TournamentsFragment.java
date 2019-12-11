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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.home.activity.FullscreenNewsActivity;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.Stadium;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.model.Tourney;
import baikal.web.footballapp.tournament.adapter.RVFavTourneyAdapter;
import baikal.web.footballapp.tournament.adapter.RecyclerViewTournamentAdapter;
import baikal.web.footballapp.viewmodel.MainViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class TournamentsFragment extends Fragment {

    private static RVFavTourneyAdapter adapter;
    public static final List<Person> referees = new ArrayList<>();
    private final Logger log = LoggerFactory.getLogger(FullscreenNewsActivity.class);
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private NestedScrollView scroller;
    private final List<League> tournaments= new ArrayList<>();
    private int count = 0;
    private int offset = 0;
    private final int limit = 5;
    private List<League> favLeague = new ArrayList<>( );
    private List<Tourney> favTourney = new ArrayList<>();
    private List<String> favTourneyId = new ArrayList<>();
    public List<Team> allTeams = new ArrayList<>();
    public List<Stadium> allStadiums = new ArrayList<>();
    private MainViewModel mainViewModel;
    private PersonalActivity activity;

    @SuppressLint("ValidFragment")
    TournamentsFragment(PersonalActivity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view;
        view = inflater.inflate(R.layout.fragment_tournaments, container, false);
        scroller = view.findViewById(R.id.scrollerLeague);
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);


        try {
            mainViewModel.getTeams().observe(this,teams -> {
                allTeams.clear();
                allTeams.addAll(teams);
            });
            mainViewModel.getAllStadiums().observe(this,stadiums -> {
                allStadiums.clear();
                allStadiums.addAll(stadiums);
            });
            recyclerView = view.findViewById(R.id.recyclerViewTournament);
            recyclerView.setNestedScrollingEnabled(false);
            RecyclerViewTournamentAdapter.Listener listener = this::showTournamentInfo;

            String personId = null;

            try {
                personId = SaveSharedPreference.getObject().getUser().getId();
            } catch (Exception ignored) {}

            mainViewModel.getFavTourney(personId).observe(this, tourneys -> {
                favTourney.clear();
                favTourney.addAll(tourneys);
                favTourneyId.clear();
                favLeague.clear();

                StringBuilder tourneyIds = new StringBuilder();

                for (Tourney tr : tourneys){
                    favTourneyId.add(tr.getId());
                    tourneyIds.append(",").append(tr.getId());
                }
                mainViewModel.setFavTourneysId(favTourneyId);
                mainViewModel.getFavLeagues(tourneyIds.toString()).observe(this, leagues -> {
                    adapter = new RVFavTourneyAdapter(favTourney , getActivity(), leagues, listener);
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

    @SuppressLint("CheckResult")
    private void showTournamentInfo(League leagueId){
        saveData(leagueId);

//        Controller.getApi().getLeagueInfo(leagueId)
//                .subscribeOn(Schedulers.io())
////                .doOnTerminate(()->progressBarHide())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(this::saveData
//                        ,
//                        error -> {
//                            CheckError checkError = new CheckError();
//                            checkError.checkError(getActivity(), error);
//                        }
//                );
    }

    private void saveData(League league) {
//        LeagueInfo tournament1 = getLeagueInfo.getLeagueInfo();
        Bundle bundle = new Bundle();
        bundle.putSerializable("TOURNAMENTINFO", league);
        Tournament tournament = new Tournament(this);
        tournament.setArguments(bundle);
        FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        try{
            fragmentManager.beginTransaction().add(R.id.pageContainer, tournament, "LEAGUEINFO").addToBackStack(null).commit();
        }catch (Exception e){
            log.error("ERROR: ", e);
        }
        activity.setActive(tournament);
    }
}
