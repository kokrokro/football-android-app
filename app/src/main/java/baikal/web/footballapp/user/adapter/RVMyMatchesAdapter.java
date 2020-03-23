package baikal.web.footballapp.user.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import baikal.web.footballapp.DateToString;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.model.MatchPopulate;
import baikal.web.footballapp.model.Referee;
import baikal.web.footballapp.model.Team;

public class RVMyMatchesAdapter extends RecyclerView.Adapter<RVMyMatchesAdapter.ViewHolder>{
//    Logger log = LoggerFactory.getLogger(RVMyMatchesAdapter.class);
    private final List<MatchPopulate> matches;
    private OnMyReffedMatchClicked onMyReffedMatchClicked;

    public interface OnMyReffedMatchClicked {
        void onClick(MatchPopulate match, String status);
    }

    public RVMyMatchesAdapter(List<MatchPopulate> matches, OnMyReffedMatchClicked onMyReffedMatchClicked){
        this.matches = matches;
        this.onMyReffedMatchClicked = onMyReffedMatchClicked;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.match, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MatchPopulate match = matches.get(position);
        if (match != null) {
            holder.layout.setVisibility(View.VISIBLE);
            holder.bindTo(match);
        }
        else
            holder.layout.setVisibility(View.GONE);

        if (position==(matches.size()-1))
            holder.line.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return matches.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textDate;
        final TextView textTime;
        final TextView textTour;
        final TextView textStadium;
        final TextView textScore;
        final TextView textCommand1;
        final TextView textCommand2;
        final ImageView image1;
        final ImageView image2;
        final TextView matchStatus;
        final RelativeLayout layout;
        final View line;
        final TextView textPenalty;

        ViewHolder(View item) {
            super(item);
            matchStatus = item.findViewById(R.id.matchStatus);
            textDate = item.findViewById(R.id.myMatchDate);
            textTime = item.findViewById(R.id.myMatchTime);
            textTour = item.findViewById(R.id.myMatchLeague);
            textStadium = item.findViewById(R.id.myMatchStadium);
            textScore = item.findViewById(R.id.myMatchScore);
            textCommand1 = item.findViewById(R.id.myMatchCommandTitle1);
            textCommand2 = item.findViewById(R.id.myMatchCommandTitle2);
            image1 = item.findViewById(R.id.myMatchCommandLogo1);
            image2 = item.findViewById(R.id.myMatchCommandLogo2);
            layout = item.findViewById(R.id.myMatchShowProtocol);
            line = item.findViewById(R.id.myMatchLine);
            textPenalty = item.findViewById(R.id.myMatchPenalty);
        }

        void bindTo(MatchPopulate match) {
            textDate.setText(DateToString.ChangeDate(match.getDate()));
            textTime.setText(DateToString.ChangeTime(match.getDate()));

            textStadium.setText(match.getPlace()==null?"Неизвестно":match.getPlace().getName());
            textTour.setText(match.getTour()==null?"":match.getTour());


            Team team1 = match.getTeamOne();
            Team team2 = match.getTeamTwo();
            textCommand1.setText(team1==null?"Неизвестно":team1.getName());
            textCommand2.setText(team2==null?"Неизвестно":team2.getName());

            String str = "Ваш статус: ";
            String status = "";
            for (Referee referee : match.getReferees()) {
                if (referee.getPerson().equals(SaveSharedPreference.getObject().getUser().get_id())) {
                    status = referee.getType();
                    switch (referee.getType()) {
                        case "firstReferee":
                            str += "1 судья";
                            break;
                        case "secondReferee":
                            str += "2 судья";
                            break;
                        case "thirdReferee":
                            str += "3 судья";
                            break;
                        case "timekeeper":
                            str += "хронометрист";
                            break;
                    }
                    break;
                }
            }

            matchStatus.setText(str);

            textPenalty.setText(match.getPenalty()==null?"":match.getPenalty());
            textPenalty.setVisibility(match.getPenalty()==null?View.GONE:View.VISIBLE);

            String finalStatus = status;
            layout.setOnClickListener(v -> onMyReffedMatchClicked.onClick(match, finalStatus));
        }
    }


    public void dataChanged(List<MatchPopulate> allPlayers1) {
        matches.clear();
        matches.addAll(allPlayers1);
        notifyDataSetChanged();
    }
}
