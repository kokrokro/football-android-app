package baikal.web.footballapp.tournament.activity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import baikal.web.footballapp.viewmodel.MainViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class TournamentsFragment extends Fragment {

    private RVFavTourneyAdapter adapter;
    public static final List<Person> referees = new ArrayList<>();
    private final Logger log = LoggerFactory.getLogger(FullscreenNewsActivity.class);
//    private ProgressBar progressBar;
//    private int count = 0;
//    private int offset = 0;
//    private final int limit = 5;

    private SwipeRefreshLayout swipeRefreshLayout;

    private List<League> favLeague = new ArrayList<>( );
    private List<Tourney> favTourney = new ArrayList<>();
    private List<String> favTourneyId = new ArrayList<>();
    public final List<Team> allTeams = new ArrayList<>();
    public List<Stadium> allStadiums = new ArrayList<>();
    private MainViewModel mainViewModel;
    private PersonalActivity activity;

    private LinearLayout emptyFavTourneys;

    @SuppressLint("ValidFragment")
    TournamentsFragment(PersonalActivity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tournaments, container, false);
//        scroller = view.findViewById(R.id.scrollerLeague);
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        emptyFavTourneys = view.findViewById(R.id.emptyFavTourneys);
        swipeRefreshLayout = view.findViewById(R.id.FT_swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this::loadData);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewTournament);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RVFavTourneyAdapter(favTourney , getActivity(), favLeague, this::showTournamentInfo);
        recyclerView.setAdapter(adapter);

        emptyFavTourneys.setVisibility(View.VISIBLE);
        if (favTourneyId.size() > 0)
            emptyFavTourneys.setVisibility(View.GONE);

        loadData();
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

    void loadData()
    {
        try {
            if (SaveSharedPreference.getObject() == null)
                return;
            String personId = SaveSharedPreference.getObject().getUser().getId();
            mainViewModel.getTeams(personId).observe(getViewLifecycleOwner(),teams -> {
                allTeams.clear();
                allTeams.addAll(teams);
            });
            mainViewModel.getAllStadiums().observe(getViewLifecycleOwner(),stadiums -> {
                allStadiums.clear();
                allStadiums.addAll(stadiums);
            });

            mainViewModel.getFavTourney(personId).observe(getViewLifecycleOwner(), tourneys -> {
                swipeRefreshLayout.setRefreshing(false);
                favTourney.clear();
                favTourney.addAll(tourneys);
                favTourneyId.clear();

                if(favTourney.size()>0)
                    emptyFavTourneys.setVisibility(View.GONE);
                else
                    emptyFavTourneys.setVisibility(View.VISIBLE);

                StringBuilder tourneyIds = new StringBuilder();
                for (Tourney tr : tourneys){
                    favTourneyId.add(tr.getId());
                    tourneyIds.append(",").append(tr.getId());
                }
                mainViewModel.setFavTourneysId(favTourneyId);
                mainViewModel.getFavLeagues(tourneyIds.toString()).observe(getViewLifecycleOwner(), leagues -> {
                    favLeague.clear();
                    favLeague.addAll(leagues);
                    adapter.dataChanged(tourneys);
                });

            });
        } catch (Exception e) {
            if (swipeRefreshLayout != null)
                swipeRefreshLayout.setRefreshing(false);
            log.error("ERROR: ", e);
        }
    }

    @SuppressLint("CheckResult")
    private void showTournamentInfo(League leagueId){
        saveData(leagueId);
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
