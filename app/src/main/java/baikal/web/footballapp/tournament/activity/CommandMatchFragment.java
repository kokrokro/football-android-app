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

import java.util.ArrayList;
import java.util.List;

import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Match;
import baikal.web.footballapp.model.Stadium;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.tournament.adapter.RVCommandMatchAdapter;
import baikal.web.footballapp.viewmodel.MainViewModel;

public class CommandMatchFragment extends Fragment {
    Logger log = LoggerFactory.getLogger(PersonalActivity.class);

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view;
        RecyclerView recyclerView;
        LinearLayout layout;
        view = inflater.inflate(R.layout.command_info_match, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewCommandMatch);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setVisibility(View.VISIBLE);
        layout = view.findViewById(R.id.teamMatchEmpty);
        try {
            Bundle bundle = getArguments();
            List<Match> matches = (List<Match>) bundle.getSerializable("TEAMSTRUCTUREMATCHES");
            if (matches.size()!=0){
                layout.setVisibility(View.GONE);
            }

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
        } catch (Exception ignored){}
        return view;
    }
}