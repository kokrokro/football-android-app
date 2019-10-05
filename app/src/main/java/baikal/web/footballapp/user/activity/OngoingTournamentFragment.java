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

import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.PersonTeams;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.user.adapter.RVOngoingTournamentAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class OngoingTournamentFragment extends Fragment{
    private final Logger log = LoggerFactory.getLogger(OngoingTournamentFragment.class);
    private static RVOngoingTournamentAdapter adapter;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view;
        RecyclerView recyclerView;
        LinearLayout linearLayout;
        LinearLayout linear;
        List<PersonTeams> list = new ArrayList<>();
        view = inflater.inflate(R.layout.user_tournaments, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewUserTournament);
        linearLayout = view.findViewById(R.id.emptyTournament);
        linear = view.findViewById(R.id.notEmptyTournament);

        try{
        for (PersonTeams personTeams: AuthoUser.personOngoingLeagues){
            String teamId = personTeams.getTeam();
//            League league = personTeams.getLeague();
            League league = null;
            for (League league1 : PersonalActivity.tournaments){
                if (league1.getId().equals(personTeams.getLeague())){
                    league = league1;
                    break;
                }
            }
            List<Team> teams = league.getTeams();
            for (Team team: teams){
//                if (team.getId().equals(teamId) && team.getStatus().equals("Pending")){
                if (team.getId().equals(teamId) && team.getStatus().equals("Approved")){
                    list.add(personTeams);
                }
            }
        }

        if (list.size()!=0){
            linearLayout.setVisibility(View.GONE);
            adapter = new RVOngoingTournamentAdapter(this, list);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        else {
            linear.setVisibility(View.GONE);
        }
        }catch (NullPointerException e){}
        log.info("INFO: OngoingTournament onCreateView");
        return view;
    }

    @Override
    public void onDestroy() {
        log.info("INFO: OngoingTournament onDestroy");
        super.onDestroy();
    }

}
