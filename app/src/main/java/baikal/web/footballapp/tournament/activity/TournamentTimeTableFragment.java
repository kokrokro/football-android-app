package baikal.web.footballapp.tournament.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import baikal.web.footballapp.Controller;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.LeagueInfo;
import baikal.web.footballapp.model.Match;
import baikal.web.footballapp.model.PersonStatus;
import baikal.web.footballapp.tournament.adapter.RecyclerViewTournamentTimeTableAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TournamentTimeTableFragment extends Fragment {
    private final Logger log = LoggerFactory.getLogger(TournamentTimeTableFragment.class);
    private boolean scrollStatus;

    private TournamentsFragment tournamentsFragment;

    public TournamentTimeTableFragment (TournamentsFragment tournamentsFragment) {
        this.tournamentsFragment = tournamentsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view;
        RecyclerView recyclerView;
        NestedScrollView scroller;
        LinearLayout layout;
        Bundle arguments = getArguments();
        League league = (League) arguments.getSerializable("TOURNAMENTINFOMATCHESLEAGUE");
        List<Match> matches = league.getMatches();
        view = inflater.inflate(R.layout.tournament_info_tab_timetable, container, false);
        recyclerView = view.findViewById(R.id.tournamentInfoTabTimetable);
        recyclerView.setNestedScrollingEnabled(false);
        scroller = view.findViewById(R.id.tournamentInfoTimetableScroll);
        layout = view.findViewById(R.id.tournamentInfoTabTimetableEmpty);
        if ( matches!=null && matches.size()!=0){
            layout.setVisibility(View.GONE);
        }

        scrollStatus = false;
        try {
            RecyclerViewTournamentTimeTableAdapter adapter = new RecyclerViewTournamentTimeTableAdapter(getActivity(),league, tournamentsFragment);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        catch (NullPointerException e){

        }
        scroller.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {

            if (scrollY > oldScrollY) {
//                    PersonalActivity.navigation.animate().translationY(PersonalActivity.navigation.getHeight());

            }
            if (scrollY < oldScrollY) {
//                    PersonalActivity.navigation.animate().translationY(0);
                scrollStatus = false;
            }
            if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                scrollStatus = true;
            }
        });
        return view;
    }


    @Override
    public void onPause() {
        log.info("INFO: TournamentTimeTableFragment onPause  1");
        super.onPause();
    }

    @Override
    public void onDestroy() {
        log.info("INFO: TournamentTimeTableFragment onDestroy 1");
//        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        super.onDestroy();
    }
}
