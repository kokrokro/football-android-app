package baikal.web.footballapp.tournament.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Match;
import baikal.web.footballapp.model.Stadium;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.tournament.adapter.RVCommandMatchAdapter;
import baikal.web.footballapp.viewmodel.MainViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommandMatchFragment extends Fragment {
    Logger log = LoggerFactory.getLogger(PersonalActivity.class);
    private Team team;
    private List<Match> matches = new ArrayList<>();
    private LinearLayout layout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view;


        view = inflater.inflate(R.layout.command_info_match, container, false);
        swipeRefreshLayout = view.findViewById(R.id.CIM_swipe_to_refresh);
        recyclerView = view.findViewById(R.id.recyclerViewCommandMatch);

        swipeRefreshLayout.setOnRefreshListener(this::loadData);

        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        layout = view.findViewById(R.id.teamMatchEmpty);

        try {
            team = (Team) getArguments().getSerializable("TEAMSTRUCTURE");

            final List<Stadium> allStadiums = new ArrayList<>();
            final List<Team> allTeams = new ArrayList<>();

            RVCommandMatchAdapter adapter = new RVCommandMatchAdapter(matches, allStadiums, allTeams);
            recyclerView.setAdapter(adapter);

            MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
            mainViewModel.getAllStadiums().observe(getViewLifecycleOwner(),stadiums -> {
                allStadiums.clear();
                allStadiums.addAll(stadiums);
                adapter.notifyDataSetChanged();
            });

            mainViewModel.getTeams(null).observe(getViewLifecycleOwner(), teams -> {
                allTeams.clear();
                allTeams.addAll(teams);
                adapter.notifyDataSetChanged();
            });

            loadData();
        } catch (Exception ignored){}
        return view;
    }

    private void loadData ()
    {
        Controller.getApi().getMatchByLeagueAndTeamOne(team.getLeague(), team.getId()).enqueue(new Callback<List<Match>>() {
            @Override
            public void onResponse(@NonNull Call<List<Match>> call, @NonNull Response<List<Match>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    matches.clear();
                    matches.addAll(response.body());

                    if (matches.size() > 0) {
                        layout.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    } else {
                        layout.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }

                    Controller.getApi().getMatchByLeagueAndTeamTwo(team.getLeague(), team.getId()).enqueue(new Callback<List<Match>>() {
                        @Override
                        public void onResponse(@NonNull Call<List<Match>> call, @NonNull Response<List<Match>> response) {
                            if (response.isSuccessful() && response.body() != null)
                                matches.addAll(response.body());
                            swipeRefreshLayout.setRefreshing(false);

                            if (matches.size() > 0) {
                                layout.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                            } else {
                                layout.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }

                            Log.d("CMF", " match size is " + matches.size());
                        }

                        @Override
                        public void onFailure(@NonNull Call<List<Match>> call, @NonNull Throwable t) {swipeRefreshLayout.setRefreshing(false);}
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Match>> call, @NonNull Throwable t) {swipeRefreshLayout.setRefreshing(false);}
        });
    }
}