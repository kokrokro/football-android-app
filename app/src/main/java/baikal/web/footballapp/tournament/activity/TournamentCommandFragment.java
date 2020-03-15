package baikal.web.footballapp.tournament.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.model.TeamStats;
import baikal.web.footballapp.tournament.adapter.RVLeaguePlayoffCommandAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TournamentCommandFragment extends Fragment{
    Logger log = LoggerFactory.getLogger(TournamentCommandFragment.class);
    private boolean scrollStatus;
    private FloatingActionButton fab;
    private List<TeamStats> teamStatsList = new ArrayList<>();
    private List<Team> teams = new ArrayList<>();
    private RVLeaguePlayoffCommandAdapter adapter;
    private League league;
    private LinearLayout layout;
    private LinearLayout layoutPlayoff;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view;
        NestedScrollView scroller;
        RecyclerView recyclerView;
        RecyclerView recyclerViewPlayoff;


        Bundle arguments = getArguments();
        league = (League) arguments.getSerializable("TOURNAMENTINFOMATCHESLEAGUE");

        view = inflater.inflate(R.layout.tournament_info_tab_command, container, false);
        Tournament tournament = (Tournament) this.getParentFragment();
        fab = tournament.getFabCommand();
        layout = view.findViewById(R.id.tournamentInfoTabCommandEmpty);
        layoutPlayoff = view.findViewById(R.id.commandsPlayoff);
        swipeRefreshLayout = view.findViewById(R.id.TITC_swipe_refresh_layout);
        recyclerView = view.findViewById(R.id.tournamentInfoTabCommand);
        recyclerViewPlayoff = view.findViewById(R.id.tournamentInfoTabCommandPlayoff);
        recyclerViewPlayoff.setLayoutManager(new LinearLayoutManager(getActivity()));
        scroller = view.findViewById(R.id.tournamentInfoCommandScroll);
        scrollStatus = false;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        swipeRefreshLayout.setOnRefreshListener(this::loadData);
        loadData();

        adapter = new RVLeaguePlayoffCommandAdapter(getActivity(), teams, league, teamStatsList);
        recyclerViewPlayoff.setAdapter(adapter);


        scroller.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY < oldScrollY)
                scrollStatus = false;
            if (scrollY == ( v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight() ))
                scrollStatus = true;
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) { }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE )
                    fab.show();
                else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                    fab.hide();
                if (scrollStatus)
                    fab.hide();

                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        return view;
    }

    private void loadData ()
    {
        Controller.getApi().getTeamByLeagueId(league.getId()).enqueue(new Callback<List<Team>>() {
            @Override
            public void onResponse(@NonNull Call<List<Team>> call, @NonNull Response<List<Team>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    teams.clear();
                    teams.addAll(response.body());
                    adapter.notifyDataSetChanged();

                    if (teams.size() != 0) {
                        Log.d("TCF", response.body().get(0).toString());
                        layout.setVisibility(View.GONE);
                        layoutPlayoff.setVisibility(View.VISIBLE);
                    }
//                    else {
//                        layout.setVisibility(View.VISIBLE);
//                        layoutPlayoff.setVisibility(View.GONE);
//                    }
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(@NonNull Call<List<Team>> call, @NonNull Throwable t) { swipeRefreshLayout.setRefreshing(false); }
        });
        Controller.getApi().getTeamStats(null, "league", league.getId(), null).enqueue(new Callback<List<TeamStats>>() {
            @Override
            public void onResponse(@NonNull Call<List<TeamStats>> call, @NonNull Response<List<TeamStats>> response) {
                if (response.isSuccessful() && response.body()!= null) {
                    teamStatsList.clear();
                    teamStatsList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(@NonNull Call<List<TeamStats>> call, @NonNull Throwable t) { swipeRefreshLayout.setRefreshing(false); }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
