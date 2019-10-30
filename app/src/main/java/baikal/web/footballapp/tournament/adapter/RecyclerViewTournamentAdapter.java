package baikal.web.footballapp.tournament.adapter;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import baikal.web.footballapp.DateToString;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.Tourney;
import baikal.web.footballapp.tournament.activity.TournamentPage;
import baikal.web.footballapp.tournament.activity.TournamentsFragment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class RecyclerViewTournamentAdapter extends RecyclerView.Adapter<RecyclerViewTournamentAdapter.ViewHolder>{
    private final Logger log = LoggerFactory.getLogger(PersonalActivity.class);
    private final Fragment context;
    private final List<League> tournaments;
    private final PersonalActivity activity;
    private final ListAdapterListener mListener;
    private final List<Tourney> tourneys;
    public RecyclerViewTournamentAdapter(Activity activity, Fragment context, List<League> tournaments, ListAdapterListener mListener, List<Tourney> tourneys){
        this.tournaments = tournaments;
        this.activity = (PersonalActivity) activity;
        this.context = context;
        this.mListener = mListener;
        this.tourneys = tourneys;
    }

    public interface ListAdapterListener {
        void onClickSwitch(String leagueId);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tournament, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        boolean status = false;
        final League league = tournaments.get(position);
        if (league.getStatus().equals("Finished")) {status = true;}
        DateToString dateToString = new DateToString();
        String str = dateToString.ChangeDate(league.getBeginDate()) + "-" + dateToString.ChangeDate(league.getEndDate());
        holder.textDate.setText(str);
        String tourneyName = "";
        for(Tourney t : tourneys){
            if(league.getTourney().equals(t.getId())){
                tourneyName = t.getName();
            }
        }
        str = tourneyName + ". " + league.getName();
        holder.textTitle.setText(str);
        str = activity.getString(R.string.tournamentFilterCommandNum) + ": " + league.getMaxTeams();
        holder.textCommandNum.setText(str);
        if (status){
            holder.textStatusFinish.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .asBitmap()
                    .load(R.drawable.ic_fin)
                    .apply(new RequestOptions()
                            .format(DecodeFormat.PREFER_ARGB_8888)
                            .priority(Priority.HIGH))
                    .into(holder.imageView);
        }
        else {
            Glide.with(context)
                    .asBitmap()
                    .load(R.drawable.ic_con)
                    .apply(new RequestOptions()
                            .format(DecodeFormat.PREFER_ARGB_8888)
                            .priority(Priority.HIGH))
                    .into(holder.imageView);}

        holder.imageButton.setOnClickListener(v -> {

            try {
//                showTournamentInfo(league.getId());
                mListener.onClickSwitch(league.getId());
            }catch (Exception e){
                log.error("ERROR: " , e);
            }


//                fragmentManager.beginTransaction().replace(context.getId(), new Tournament()).commit();

//                            Intent intent = new Intent(activity, Tornament.class);
//                            String title = "Some title";
//                            Bundle bundle = new Bundle();
//                            bundle.putString("TOURNAMENT", title);
//                            intent.putExtra("TOURNAMENT", bundle);
//                            context.startActivity(intent);
        });
        if (position==tournaments.size()-1){
            holder.view.setVisibility(View.INVISIBLE);
        }
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
        final ImageView imageView;
        final View view;
        final RelativeLayout imageButton;
        ViewHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.tournamentLine);
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override public void onClick(View v) {
//                    // item clicked
//                }
//            });
            textCommandNum = itemView.findViewById(R.id.tournamentCommandNum);
            textDate = itemView.findViewById(R.id.tournamentDate);
            textTitle = itemView.findViewById(R.id.tournamentTitle);
            textStatusFinish = itemView.findViewById(R.id.tournamentStatusFinish);
            imageView = itemView.findViewById(R.id.tournamentStatusImg);
            imageButton = itemView.findViewById(R.id.tournamentButtonShow);


//            mProgressDialog.setIndeterminate(true);
        }
    }


    public void dataChanged(List<League> allPlayers1){
        //tournaments.clear();
        //tournaments.addAll(allPlayers1);
        notifyDataSetChanged();
    }

}

