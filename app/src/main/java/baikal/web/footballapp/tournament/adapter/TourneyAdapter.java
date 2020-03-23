package baikal.web.footballapp.tournament.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import baikal.web.footballapp.App;
import baikal.web.footballapp.DateToString;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.model.Tourney;

public class TourneyAdapter extends PagedListAdapter<Tourney, TourneyAdapter.ViewHolder> {
//    private static final String TAG = "TourneyAdapter";
    private ChooseTourneyListener mListener;

    public interface ChooseTourneyListener {
        void onClick(String id,Boolean isChecked);
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


    public TourneyAdapter(ChooseTourneyListener mListener){
        super(DIFF_CALLBACK);
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public TourneyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tourney, parent, false);
        return new TourneyAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TourneyAdapter.ViewHolder holder, int position) {
        Tourney tourney = getItem(position);

        if (tourney != null)
            holder.bindTo(tourney);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textTitle;
        final TextView textDate;
        final TextView textCommandNum;
        final TextView textStatusFinish;
        final View view;
        final SwitchCompat favBtn;

        ViewHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.tourneyLine);
            favBtn = itemView.findViewById(R.id.T_switch);
            textCommandNum = itemView.findViewById(R.id.tourneyTeamNumber);
            textDate = itemView.findViewById(R.id.tourneyDate);
            textTitle = itemView.findViewById(R.id.tourneyTitle);
            textStatusFinish = itemView.findViewById(R.id.tourneyFinish);
        }

        void bindTo(Tourney tourney) {
            if (SaveSharedPreference.getObject() != null) {
                if (SaveSharedPreference.getObject().getUser().getFavouriteTourney().contains(tourney.getId()))
                    favBtn.setChecked(true);
                else
                    favBtn.setChecked(false);
            }
            else {
                favBtn.setChecked(false);
            }

            String str = App.getAppContext().getString(R.string.tournamentFilterCommandNum) + tourney.getMaxTeams();
            textCommandNum.setText(str);
            str = DateToString.ChangeDate(tourney.getBeginDate()) + " - " + DateToString.ChangeDate(tourney.getEndDate());
            textDate.setText(str);
            textTitle.setText(tourney.getName());


            favBtn.setOnDragListener((v, e)-> true);
            favBtn.setOnClickListener(v -> {
                mListener.onClick(tourney.getId(), favBtn.isChecked());

                if (SaveSharedPreference.getObject() == null)
                    favBtn.setChecked(false);
            });
        }
    }
}
