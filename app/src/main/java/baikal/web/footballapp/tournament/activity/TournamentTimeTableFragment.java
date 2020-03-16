package baikal.web.footballapp.tournament.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import baikal.web.footballapp.R;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.Match;
import baikal.web.footballapp.tournament.adapter.RecyclerViewTournamentTimeTableAdapter;
import baikal.web.footballapp.viewmodel.MainViewModel;

public class TournamentTimeTableFragment extends Fragment {
    private final Logger log = LoggerFactory.getLogger(TournamentTimeTableFragment.class);
//    private boolean scrollStatus;

    TournamentTimeTableFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view;
        RecyclerView recyclerView;
        LinearLayout layout;
        Bundle arguments = getArguments();
        League league = (League) arguments.getSerializable("TOURNAMENTINFOMATCHESLEAGUE");
        List<Match> matches = league.getMatches();
        view = inflater.inflate(R.layout.tournament_info_tab_timetable, container, false);
        recyclerView = view.findViewById(R.id.tournamentInfoTabTimetable);
        recyclerView.setNestedScrollingEnabled(false);
        layout = view.findViewById(R.id.tournamentInfoTabTimetableEmpty);
        if ( matches!=null && matches.size()!=0)
            layout.setVisibility(View.GONE);

        MainViewModel mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);

        try {
            RecyclerViewTournamentTimeTableAdapter adapter = new RecyclerViewTournamentTimeTableAdapter(league, mainViewModel);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        catch (NullPointerException ignored){ }

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
        super.onDestroy();
    }
}
