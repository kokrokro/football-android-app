package baikal.web.footballapp.tournament.adapter;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.LeagueInfo;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.tournament.GroupTeamPlaceComparator;
import baikal.web.footballapp.tournament.activity.TournamentCommandFragment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

public class RVTournamentCommandAdapter extends RecyclerView.Adapter<RVTournamentCommandAdapter.ViewHolder>{
    Logger log = LoggerFactory.getLogger(PersonalActivity.class);

    private final TournamentCommandFragment context;
    private final PersonalActivity activity;
    private final List<String> groups;
    private final List<Team> teams;
    private final LeagueInfo leagueInfo;

    public RVTournamentCommandAdapter(Activity activity, TournamentCommandFragment context, List<String> groups, List<Team> teams
    ,LeagueInfo leagueInfo){
        this.activity = (PersonalActivity) activity;
        this.context = context;
        this.groups = groups;
        this.teams = teams;
        this.leagueInfo = leagueInfo;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.tournament_info_tab_command_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String str;
        str = "Группа " + groups.get(position);
        holder.textGroupTitle.setText(str);
        //add onClick in adapter
        //getChildFragmentManager()
        Collections.sort(teams, new GroupTeamPlaceComparator());
        RVTournamentCommandCardAdapter adapter = new RVTournamentCommandCardAdapter(activity, context, teams, groups.get(position), leagueInfo);
        holder.recyclerView.setAdapter(adapter);
//        holder.recyclerView.setLayoutManager(new CustomLinearLayoutManager(activity));
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        if (position==(groups.size()-1)){
            holder.line.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        final TextView textGroupTitle;
        final View cardView;
        final RecyclerView recyclerView;
        final View line;
        ViewHolder(View item) {
            super(item);
            textGroupTitle = item.findViewById(R.id.groupTitle);
            cardView = item.findViewById(R.id.cv);
            recyclerView = item.findViewById(R.id.tournamentInfoTabCommandCard);
            line = item.findViewById(R.id.tournamentCommandCardLine);
        }
    }
}
