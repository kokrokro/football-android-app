package baikal.web.footballapp.tournament.activity.MainPage;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import baikal.web.footballapp.DataSourceUtilities.LoadStates;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.tournament.activity.Tournament;
import baikal.web.footballapp.tournament.adapter.RVFavTourneyAdapter;
import baikal.web.footballapp.tournament.adapter.RecyclerViewTournamentAdapter;
import baikal.web.footballapp.viewmodel.TournamentPageViewModel;

public class TournamentsFragment extends Fragment {
    private static final String TAG = "TournamentsFragment";
//    private final Logger log = LoggerFactory.getLogger(TournamentsFragment.class);
    private TournamentPageViewModel tournamentPageViewModel;
    private RVFavTourneyAdapter adapter;

    private HashSet<View> switchableViews;
    private ProgressBar progressBar;
    private TextView errorText;
    private LinearLayout emptyText;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    TournamentsFragment() { }

    private static void showOneView(View viewToShow, Set<View> views) {
        for (View view : views) {
            if (view.hashCode() == viewToShow.hashCode()) {
                view.setVisibility(View.VISIBLE);
            } else {
                view.setVisibility(View.GONE);
            }
        }
    }

    private void switchViewVisible(LoadStates loadState) {
        switch (loadState) {
            case Loading:
                showOneView(progressBar, switchableViews);
                break;

            case Loaded:
                swipeRefreshLayout.setRefreshing(false);
                showOneView(recyclerView, switchableViews);
                break;

            case Error:
                swipeRefreshLayout.setRefreshing(false);
                showOneView(errorText, switchableViews);
                break;

            case Empty:
                swipeRefreshLayout.setRefreshing(false);
                showOneView(emptyText, switchableViews);
                break;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tournaments, container, false);
//        scroller = view.findViewById(R.id.scrollerLeague);
//        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        progressBar = view.findViewById(R.id.FT_progress);
        errorText = view.findViewById(R.id.FT_errorText);
        emptyText = view.findViewById(R.id.FT_emptyText);
        recyclerView = view.findViewById(R.id.recyclerViewTournament);
        swipeRefreshLayout = view.findViewById(R.id.FST_swipe_to_refresh);

        switchableViews = new HashSet<>();
        switchableViews.add(progressBar);
        switchableViews.add(errorText);
        switchableViews.add(emptyText);
        switchableViews.add(recyclerView);

        swipeRefreshLayout = view.findViewById(R.id.FT_swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this::loadData);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RVFavTourneyAdapter(this::loadLeagueData, this::showTournamentInfo);
        recyclerView.setAdapter(adapter);

        loadData();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tournamentPageViewModel = ViewModelProviders.of(getActivity()).get(TournamentPageViewModel.class);
    }

    private void loadData()
    {
        if (SaveSharedPreference.getObject() == null) {
            tournamentPageViewModel.clearSearchAndReload();
            tournamentPageViewModel.getLoadDataState().observe(this, this::switchViewVisible);
            tournamentPageViewModel.getTourneys().observe(this, adapter::submitList);
            return;
        }

        tournamentPageViewModel.reloadFav();
        tournamentPageViewModel.getFavLoadDataState().observe(this, this::switchViewVisible);
        tournamentPageViewModel.getFavTourneys().observe(this, adapter::submitList);

    }

    private void loadLeagueData (String id, RecyclerViewTournamentAdapter adapter, List<League> leagueList) {
        tournamentPageViewModel.getFavLeagues(id).observe(getViewLifecycleOwner(), treeMap -> {
//            Log.d(TAG, "league live data has been changed !!!");
            if (treeMap.get(id) != null) {
                leagueList.clear();
                leagueList.addAll(treeMap.get(id));
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void showTournamentInfo (League league) {
//        LeagueInfo tournament1 = getLeagueInfo.getLeagueInfo();
        Bundle bundle = new Bundle();
        bundle.putSerializable("TOURNAMENTINFO", league);
        Tournament tournament = new Tournament();
        tournament.setArguments(bundle);
        FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        try{
            fragmentManager.beginTransaction().add(R.id.pageContainer, tournament, "LEAGUEINFO").addToBackStack(null).commit();
        } catch (Exception e){
            Log.e("ERROR: ", e.toString());
        }
    }
}
