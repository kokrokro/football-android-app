package baikal.web.footballapp.user.activity;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.model.Match;
import baikal.web.footballapp.model.Referee;
import baikal.web.footballapp.model.RefereeRequest;
import baikal.web.footballapp.user.adapter.RVMyMatchesAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MyMatches extends Fragment {
    private final Logger log = LoggerFactory.getLogger(MyMatches.class);
    static RVMyMatchesAdapter adapter;
    public static List<Match> matches;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        RecyclerView recyclerView;
        LinearLayout layout;
        view = inflater.inflate(R.layout.user_matches, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewMyMatches);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        try {
            matches = new ArrayList<>(SaveSharedPreference.getObject().getUser().getParticipationMatches());
            List<Match> myMatches = new ArrayList<>();
            for (Match match : matches) {
                List<Referee> referees = new ArrayList<>(match.getReferees());
                for (Referee referee : referees) {
                    if (referee.getType().equals("3 судья") && referee.getPerson().equals(SaveSharedPreference.getObject().getUser().getId())) {
                        myMatches.add(match);
                    }
                }
            }
            matches.removeAll(myMatches);
            try {
                myMatches.addAll(myMatches.size(), matches);
            } catch (Exception e) {
            }

            matches.clear();
            matches.addAll(myMatches);
            layout = view.findViewById(R.id.emptyMatch);
            if (myMatches.size() != 0) {
                layout.setVisibility(View.GONE);
            }
            adapter = new RVMyMatchesAdapter(getActivity(), this, matches);
            recyclerView.setAdapter(adapter);
        } catch (NullPointerException e) {
        }
        return view;
    }

    @Override
    public void onDestroy() {
        log.info("INFO: InvitationFragment onDestroy");
        super.onDestroy();
    }
}
