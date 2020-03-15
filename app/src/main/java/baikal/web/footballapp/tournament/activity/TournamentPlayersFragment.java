package baikal.web.footballapp.tournament.activity;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.PersonStats;
import baikal.web.footballapp.model.Player;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.tournament.PlayerGoalsComparator;
import baikal.web.footballapp.tournament.PlayerMatchComparator;
import baikal.web.footballapp.tournament.PlayerRCComparator;
import baikal.web.footballapp.tournament.PlayerYCComparator;
import baikal.web.footballapp.tournament.adapter.RVTournamentPlayersAdapter;
import baikal.web.footballapp.viewmodel.PersonViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TournamentPlayersFragment extends Fragment {
    private final Logger log = LoggerFactory.getLogger(TournamentPlayersFragment.class);
    private boolean scrollStatus;
    private final List<Player> playerList = new ArrayList<>();
    private RVTournamentPlayersAdapter adapter;
    private static List<PersonStats> personStats = new ArrayList<>();
    private League league;
    private RecyclerView recyclerView;
    private LinearLayout layout;
    private RelativeLayout layout1;
    private FloatingActionButton fab;
    private PersonViewModel personViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view;
        Spinner spinner;
        NestedScrollView scroller;

        Bundle arguments = getArguments();
        league = (League) arguments.getSerializable("TOURNAMENTINFOMATCHESLEAGUE");

        view = inflater.inflate(R.layout.tournament_info_tab_players, container, false);
        layout = view.findViewById(R.id.tournamentPlayersEmpty);
        layout1 = view.findViewById(R.id.mainview);
//        LayoutInflater factory = getLayoutInflater();
//        View newRow = factory.inflate(R.layout.tournament_info_two, null,   false);
//        fab = (FloatingActionButton) view.findViewById(R.id.playersInfoButton);
        Tournament tournament = (Tournament) this.getParentFragment();
        fab = tournament.getFabPlayers();
        spinner = view.findViewById(R.id.playersSpinner);
        scroller = view.findViewById(R.id.tournamentInfoPlayersScroll);
        recyclerView = view.findViewById(R.id.recyclerViewTournamentPlayersStats);

        personViewModel = ViewModelProviders.of(getActivity()).get(PersonViewModel.class);

        scrollStatus = false;
        adapter = new RVTournamentPlayersAdapter(playerList, personStats, personViewModel);
        recyclerView.setAdapter(adapter);
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setVisibility(View.INVISIBLE);

        loadData();

        final ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getActivity(), R.array.spinnerItem, R.layout.spinner_item);
        try {
            adapter1.setDropDownViewResource(R.layout.spinner_dropdown);
        } catch (Exception t) {
            log.error("ERROR: from TournamentPlayersFragment", t);
        }

        try {
            spinner.setAdapter(adapter1);
        } catch (Exception t) {
            log.error("ERROR: from TournamentPlayersFragment set Adapter", t);
        }

        Drawable spinnerDrawable = spinner.getBackground().getConstantState().newDrawable();


        spinnerDrawable.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

        spinner.setBackground(spinnerDrawable);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] choose = getResources().getStringArray(R.array.spinnerItem);
                switch (choose[position]) {
                    case "по проведенным матчам": {
                        log.error("по проведенным матчам");
                        List<PersonStats> players = new ArrayList<>(personStats);
                        Collections.sort(players, new PlayerMatchComparator());
                        personStats.clear();
                        personStats.addAll(players);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                    case "по забитым мячам": {
                        log.error("по забитым мячам");
                        List<PersonStats> players = new ArrayList<>(personStats);
                        Collections.sort(players, new PlayerGoalsComparator());
                        personStats.clear();
                        personStats.addAll(players);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                    case "по количеству ЖК": {
                        log.error("по количеству ЖК");
                        List<PersonStats> players = new ArrayList<>(personStats);
                        Collections.sort(players, new PlayerYCComparator());
                        personStats.clear();
                        personStats.addAll(players);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                    case "по количеству КК": {
                        log.error("по количеству КК");
                        List<PersonStats> players = new ArrayList<>(personStats);
                        Collections.sort(players, new PlayerRCComparator());
                        personStats.clear();
                        personStats.addAll(players);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
//                Toast.makeText(getActivity(), choose[position], Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        scroller.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {

            if (scrollY > oldScrollY) {
                    log.info("INFO: RecyclerView scrolled: scroll down!");
            }
            if (scrollY < oldScrollY) {
                scrollStatus = false;
            }

            if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                scrollStatus = true;
            }
        });


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//nothing to do
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                    fab.show();
                else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                    fab.hide();

                if (scrollStatus)
                    fab.hide();
                else
                    fab.show();
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        return view;
    }

    private void loadData ()
    {
        Controller.getApi().getTeamByLeagueId(league.getId()).enqueue(new Callback<List<Team>>() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onResponse(@NonNull Call<List<Team>> call, @NonNull Response<List<Team>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    StringBuilder playersId= new StringBuilder();
                    List<Team> team = response.body();
                    playerList.clear();
                    for (Team team1 : team) {
                        playerList.addAll(team1.getPlayers());
                        for(Player player : team1.getPlayers())
                            playersId.append(",").append(player.getId());
                    }

                    if (playerList.size() != 0) {
                        layout.setVisibility(View.GONE);
                    } else {
                        layout1.setVisibility(View.GONE);
                        fab.setVisibility(View.INVISIBLE);
                    }
                    Controller.getApi().getPersonStats("league",null,league.getId()).enqueue(new Callback<List<PersonStats>>() {
                        @Override
                        public void onResponse(@NonNull Call<List<PersonStats>> call, @NonNull Response<List<PersonStats>> response) {
                            if(response.isSuccessful() && response.body()!=null){
                                personStats.clear();
                                Log.d("RESPONSE STATs", response.body().size()+"");
                                personStats.addAll(response.body());
                                adapter.notifyDataSetChanged();
                                recyclerView.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<List<PersonStats>> call, @NonNull Throwable t) { }
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Team>> call, @NonNull Throwable t) { }
        });
    }

    @Override
    public void onPause() {
        log.info("INFO: TournamentPlayersFragment onPause 3");

        super.onPause();
    }

    @Override
    public void onDestroy() {
        log.info("INFO: TournamentPlayersFragment onDestroy 3");
//        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        super.onDestroy();
    }
}
