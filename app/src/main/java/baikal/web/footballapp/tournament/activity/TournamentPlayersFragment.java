package baikal.web.footballapp.tournament.activity;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;


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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TournamentPlayersFragment extends Fragment {
    private final Logger log = LoggerFactory.getLogger(TournamentTimeTableFragment.class);
    private boolean scrollStatus;
    private final List<Player> playerList = new ArrayList<>();
    private RVTournamentPlayersAdapter adapter;
    private static List<PersonStats> personStats = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view;
        Spinner spinner;
        LinearLayout layout;
        RelativeLayout layout1;
        final FloatingActionButton fab;
        NestedScrollView scroller;
        List<String> categories = new ArrayList<>();
        categories.add("по проведенным матчам");
        categories.add("по забитым мячам");
        categories.add("по количеству ЖК");
        categories.add("по количеству КК");

        RecyclerView recyclerView;
        Bundle arguments = getArguments();
        List<Team> team = (List<Team>) arguments.getSerializable("TOURNAMENTINFOTEAMS");
        League league = (League) arguments.getSerializable("TOURNAMENTINFOMATCHESLEAGUE");
        List<String> clubs = new ArrayList<>();
        StringBuilder playersId= new StringBuilder();
        for (Team team1 : team) {
            //                if (team1.getClub().)
            //                clubs.add(team1.getClub());
            playerList.addAll(team1.getPlayers());
            for(Player player : team1.getPlayers()){
                playersId.append(",").append(player.getId());
            }
        }
        Controller.getApi().getPersonStats(playersId.toString(),league.getId()).enqueue(new Callback<List<PersonStats>>() {
            @Override
            public void onResponse(Call<List<PersonStats>> call, Response<List<PersonStats>> response) {
                if(response.isSuccessful()){
                    if(response.body()!=null){
                        personStats.clear();
                        personStats.addAll(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<PersonStats>> call, Throwable t) {

            }
        });

//        Collections.sort(playerList, new PlayerMatchComparator());
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
        recyclerView = view.findViewById(R.id.recyclerViewTournamentPlayers);
        scrollStatus = false;
        adapter = new RVTournamentPlayersAdapter(this, playerList, clubs);
        recyclerView.setAdapter(adapter);
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);


        if (playerList.size() != 0) {
            layout.setVisibility(View.GONE);
        } else {
            layout1.setVisibility(View.GONE);
            fab.setVisibility(View.INVISIBLE);

        }


        final ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getActivity(), R.array.spinnerItem, R.layout.spinner_item);
        try {
//            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
                        List<Player> players = new ArrayList<>(playerList);
//                        Collections.sort(players, new PlayerMatchComparator());
                        adapter.dataChanged(players);
                        break;
                    }
                    case "по забитым мячам": {
                        log.error("по забитым мячам");
                        List<Player> players = new ArrayList<>(playerList);
                        Collections.sort(players, new PlayerGoalsComparator());
                        adapter.dataChanged(players);
                        break;
                    }
                    case "по количеству ЖК": {
                        log.error("по количеству ЖК");
                        List<Player> players = new ArrayList<>(playerList);
                        Collections.sort(players, new PlayerYCComparator());
                        adapter.dataChanged(players);
                        break;
                    }
                    case "по количеству КК": {
                        log.error("по количеству КК");
                        List<Player> players = new ArrayList<>(playerList);
                        Collections.sort(players, new PlayerRCComparator());
                        adapter.dataChanged(players);
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
//                    log.info("INFO: RecyclerView scrolled: scroll down!");

            }
            if (scrollY < oldScrollY) {
//                    log.info("INFO: RecyclerView scrolled: scroll up!");
                scrollStatus = false;
            }

            if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
//                    log.info("INFO: RecyclerView scrolled: bottom scroll!");
                scrollStatus = true;
//                    fab.hide();
            }
        });


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//nothing to do
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                int currentFirstVisible = myLayoutManager.findFirstVisibleItemPosition();
//
//                if (currentFirstVisible > firstVisibleInListview) {
////                    log.info("INFO: RecyclerView scrolled: scroll up!");
//                    PersonalActivity.navigation.animate().translationY(PersonalActivity.navigation.getHeight());
//
//                } else {
////                    log.info("INFO: RecyclerView scrolled: scroll down!");
//                    PersonalActivity.navigation.animate().translationY(0);
//                }
//                firstVisibleInListview = currentFirstVisible;
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    fab.show();
                } else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    fab.hide();
                }
                if (scrollStatus) {
                    fab.hide();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        return view;
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
