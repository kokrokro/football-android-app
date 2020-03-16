package baikal.web.footballapp.tournament.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import baikal.web.footballapp.App;
import baikal.web.footballapp.DateToString;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.League;


public class RecyclerViewTournamentAdapter extends RecyclerView.Adapter<RecyclerViewTournamentAdapter.ViewHolder>{
//    private final Logger log = LoggerFactory.getLogger(PersonalActivity.class);

    private final List<League> tournaments;
    private final Listener mListener;

    RecyclerViewTournamentAdapter(List<League> tournaments, Listener mListener){
        this.tournaments = tournaments;
        this.mListener = mListener;
    }

    public interface Listener {
        void onClick(League league);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tournament, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final League league = tournaments.get(position);
        String str = DateToString.ChangeDate(league.getBeginDate()) + "-" + DateToString.ChangeDate(league.getEndDate());
        holder.textDate.setText(str);

        str =  league.getName();
        holder.textTitle.setText(str);
        str = App.getAppContext().getString(R.string.tournamentFilterCommandNum) + ": " + league.getMaxTeams();
        holder.textCommandNum.setText(str);
        holder.bind(league,mListener);
    }

    @Override
    public int getItemCount() {
        return tournaments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        final TextView textTitle;
        final TextView textDate;
        final TextView textCommandNum;
        final TextView textStatusFinish;
        final View view;
        final RelativeLayout imageButton;
        ViewHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.tournamentLine);

            textCommandNum = itemView.findViewById(R.id.tournamentCommandNum);
            textDate = itemView.findViewById(R.id.tournamentDate);
            textTitle = itemView.findViewById(R.id.tournamentTitle);
            textStatusFinish = itemView.findViewById(R.id.tournamentStatusFinish);
            imageButton = itemView.findViewById(R.id.tournamentButtonShow);
        }
        void bind(League id, final Listener listener) {
            imageButton.setOnClickListener(v ->
                    listener.onClick(id)
            );
        }
    }
}

