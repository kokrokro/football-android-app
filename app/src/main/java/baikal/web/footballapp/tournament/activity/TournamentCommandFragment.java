package baikal.web.footballapp.tournament.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.model.TeamStats;
import baikal.web.footballapp.tournament.adapter.RVLeaguePlayoffCommandAdapter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TournamentCommandFragment extends Fragment{
    Logger log = LoggerFactory.getLogger(TournamentCommandFragment.class);
    private boolean scrollStatus;
    private FloatingActionButton fab;
    private List<TeamStats> teamStatsList = new ArrayList<>();
    private  RVLeaguePlayoffCommandAdapter adapter;
    @SuppressLint({"RestrictedApi", "CheckResult"})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view;
        NestedScrollView scroller;
        RecyclerView recyclerView;
        RecyclerView recyclerViewPlayoff;
        LinearLayout layout;
        LinearLayout layoutPlayoff;

        Bundle arguments = getArguments();
        List<Team> teams = (List<Team>) arguments.getSerializable("TOURNAMENTINFOTEAMS");
        League leagueInfo = (League) arguments.getSerializable("TOURNAMENTINFOMATCHESLEAGUE");
//        HashMap<String, List<Team>> commandGroups = new HashMap<>();
        List<String> groups = new ArrayList<>();
//        try{
//            for (Team team : teams){
//                if (!groups.contains(team.getGroup())){
//                    groups.add(team.getGroup());
//                }
//            }
//        }catch (Exception e){}

        view = inflater.inflate(R.layout.tournament_info_tab_command, container, false);
        Tournament tournament = (Tournament) this.getParentFragment();
        fab = tournament.getFabCommand();
        layout = view.findViewById(R.id.tournamentInfoTabCommandEmpty);
        layoutPlayoff = view.findViewById(R.id.commandsPlayoff);
        recyclerView = view.findViewById(R.id.tournamentInfoTabCommand);
        recyclerViewPlayoff = view.findViewById(R.id.tournamentInfoTabCommandPlayoff);
        recyclerViewPlayoff.setLayoutManager(new LinearLayoutManager(getActivity()));
        scroller = view.findViewById(R.id.tournamentInfoCommandScroll);
        scrollStatus = false;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if(teams.size()!=0){
            layout.setVisibility(View.GONE);
            layoutPlayoff.setVisibility(View.VISIBLE);
        }

        //noinspection ResultOfMethodCallIgnored
        Controller.getApi().getTeamStats(null, "league", leagueInfo.getId(), null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .repeatWhen(completed -> completed.delay(5, TimeUnit.MINUTES))
                .subscribe(teamStats -> {
                    teamStatsList.clear();
                    teamStatsList.addAll(teamStats);
                    adapter.notifyDataSetChanged();
                        },
                        error -> {}
                );;
         adapter = new RVLeaguePlayoffCommandAdapter(getActivity(), teams, leagueInfo, teamStatsList);
        recyclerViewPlayoff.setAdapter(adapter);


//            if (groups.size()!=0){
//                layout.setVisibility(View.GONE);
//                if (!leagueInfo.getStatus().equals("Groups")){
//                    layoutPlayoff.setVisibility(View.VISIBLE);
//                    List<Team> list = new ArrayList<>(teams);
//                    for (Team team : teams){
////                    if (team.getPlace()!=null){
////                    if (team.getPlayoffPlace()!=null){
//                        if (team.getPlayoffPlace()!=null){
//                            list.remove(team);
//                        }
//                    }
//                    teams.removeAll(list);
////                    Collections.sort(teams, new PlayoffTeamPlaceComparator());
//                    int count = teams.size();
////                    Collections.sort(list, new GroupTeamPlaceComparator());
////                    Collections.sort(list, new PlayoffTeamMadeToPlayoffComparator());
//                    teams.addAll(count, list);
//                    RVLeaguePlayoffCommandAdapter adapter = new RVLeaguePlayoffCommandAdapter(getActivity(),this, teams, leagueInfo);
//                    recyclerViewPlayoff.setAdapter(adapter);
//                }
//                else {
//                    RVTournamentCommandAdapter adapter = new RVTournamentCommandAdapter(getActivity(),this, groups, teams, leagueInfo);
//                    recyclerView.setAdapter(adapter);
//                }
//            }
//            else {
//                fab.setVisibility(View.INVISIBLE);
//            }




        scroller.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {

            if (scrollY > oldScrollY) {
//                    PersonalActivity.navigation.animate().translationY(PersonalActivity.navigation.getHeight());

            }
            if (scrollY < oldScrollY) {
//                    PersonalActivity.navigation.animate().translationY(0);
                scrollStatus = false;
            }
            if (scrollY == ( v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight() )) {
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

                if (newState == RecyclerView.SCROLL_STATE_IDLE ) {
                    fab.show();
                } else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    fab.hide();
                }
                if (scrollStatus){
                    fab.hide();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
