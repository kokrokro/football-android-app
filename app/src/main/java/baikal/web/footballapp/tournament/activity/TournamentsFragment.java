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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.Tourney;
import baikal.web.footballapp.tournament.adapter.RVFavTourneyAdapter;
import baikal.web.footballapp.viewmodel.MainViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class TournamentsFragment extends Fragment {

    private RVFavTourneyAdapter adapter;
    private final Logger log = LoggerFactory.getLogger(TournamentsFragment.class);

    private SwipeRefreshLayout swipeRefreshLayout;

    private List<League> favLeague = new ArrayList<>( );
    private List<Tourney> favTourney = new ArrayList<>();
    private List<String> favTourneyId = new ArrayList<>();
    private MainViewModel mainViewModel;

    private LinearLayout emptyFavTourneys;

    TournamentsFragment(MainViewModel mainViewModel) {
        this.mainViewModel = mainViewModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tournaments, container, false);
//        scroller = view.findViewById(R.id.scrollerLeague);
//        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        emptyFavTourneys = view.findViewById(R.id.emptyFavTourneys);
        swipeRefreshLayout = view.findViewById(R.id.FT_swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this::loadData);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewTournament);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RVFavTourneyAdapter(favTourney , favLeague, this::showTournamentInfo);
        recyclerView.setAdapter(adapter);

        emptyFavTourneys.setVisibility(View.VISIBLE);
        if (favTourneyId.size() > 0)
            emptyFavTourneys.setVisibility(View.GONE);

        loadData();
        return view;
    }

    private void loadData()
    {
        try {
            if (SaveSharedPreference.getObject() == null) {
                if (emptyFavTourneys != null)
                    emptyFavTourneys.setVisibility(View.GONE);
//                favTourney.clear();
//                favTourney.addAll(MankindKeeper.getInstance().allTourneys);
//                favLeague.clear();
//                favLeague.addAll(MankindKeeper.getInstance().allLeagues);
//                if (swipeRefreshLayout != null)
//                    swipeRefreshLayout.setRefreshing(false);
                return;
            }
            String personId = SaveSharedPreference.getObject().getUser().getId();

            mainViewModel.getFavTourney(personId).observe(getViewLifecycleOwner(), tourneys -> {
//                if (swipeRefreshLayout != null)
//                    swipeRefreshLayout.setRefreshing(false);
//                favTourney.clear();
//                favTourney.addAll(tourneys);
//                favTourneyId.clear();
//
//                if(favTourney.size()>0)
//                    emptyFavTourneys.setVisibility(View.GONE);
//                else
//                    emptyFavTourneys.setVisibility(View.VISIBLE);
//
//                StringBuilder tourneyIds = new StringBuilder();
//                for (Tourney tr : tourneys){
//                    favTourneyId.add(tr.getId());
//                    tourneyIds.append(",").append(tr.getId());
//                }
//                mainViewModel.setFavTourneysId(favTourneyId);
//                mainViewModel.getFavLeagues(tourneyIds.toString()).observe(getViewLifecycleOwner(), leagues -> {
//                    favLeague.clear();
//                    favLeague.addAll(leagues);
//                    adapter.dataChanged(tourneys);
//                });

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
        } catch (Exception e){
            log.error("ERROR: ", e);
        }
    }
}
