package baikal.web.footballapp.tournament.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import baikal.web.footballapp.App;
import baikal.web.footballapp.DateToString;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.Tourney;

public class RVFavTourneyAdapter extends PagedListAdapter<Tourney, RVFavTourneyAdapter.ViewHolder> {
    final Logger log = LoggerFactory.getLogger(RVFavTourneyAdapter.class);
    private RecyclerViewTournamentAdapter.Listener mListener;
    private LoadLeagueByTourney loadLeagueByTourney;

    public interface LoadLeagueByTourney {
        void loadLeague(String tourneyId, RecyclerViewTournamentAdapter adapter, List<League> leagueList);
    }

    private static final DiffUtil.ItemCallback<Tourney> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Tourney>() {
                @Override
                public boolean areItemsTheSame(@NonNull Tourney oldPerson, @NonNull Tourney newPerson) {
                    return oldPerson.getId().equals(newPerson.getId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull Tourney oldPerson, @NonNull Tourney newPerson) {
                    return oldPerson.getId().equals(newPerson.getId());
                }
            };

    public RVFavTourneyAdapter(LoadLeagueByTourney loadLeagueByTourney, RecyclerViewTournamentAdapter.Listener mListener){
        super(DIFF_CALLBACK);
        this.mListener = mListener;
        this.loadLeagueByTourney = loadLeagueByTourney;
    }

    @NonNull
    @Override
    public RVFavTourneyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_tourney, parent, false);
        return new RVFavTourneyAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVFavTourneyAdapter.ViewHolder holder, int position) {
        final Tourney tourney = getItem(position);

        if (tourney != null)
            holder.bindTo(tourney);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textTitle;
        final TextView textDate;
        final TextView textCommandNum;
        final TextView textStatusFinish;
        final View view;
        final RecyclerView rvLeagues;
        final RelativeLayout linearLayout;
        final RecyclerViewTournamentAdapter adapter;
        final List<League> leagueList;

        ViewHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.tourneyLine);
            textCommandNum = itemView.findViewById(R.id.tourneyTeamNumber);
            textDate = itemView.findViewById(R.id.tourneyDate);
            textTitle = itemView.findViewById(R.id.tourneyTitle);
            textStatusFinish = itemView.findViewById(R.id.tourneyFinish);
            rvLeagues = itemView.findViewById(R.id.recyclerViewFavLeagues);
            linearLayout = itemView.findViewById(R.id.tourneyShow);

            leagueList = new ArrayList<>();
            rvLeagues.setLayoutManager(new LinearLayoutManager(App.getAppContext()));
            adapter = new RecyclerViewTournamentAdapter(leagueList, mListener);
            rvLeagues.setAdapter(adapter);
        }

        void bindTo (Tourney tourney) {
            String str = DateToString.ChangeDate(tourney.getBeginDate()) + "-" + DateToString.ChangeDate(tourney.getEndDate());
            textDate.setText(str);
            textTitle.setText(tourney.getName());

            str = App.getAppContext().getString(R.string.tournamentFilterCommandNum) + ": " + tourney.getMaxTeams();
            textCommandNum.setText(str);

            loadLeagueByTourney.loadLeague(tourney.getId(), adapter, leagueList);
        }
    }
}
