package baikal.web.footballapp.tournament.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import baikal.web.footballapp.App;
import baikal.web.footballapp.DateToString;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.model.Tourney;

public class TourneyAdapter extends PagedListAdapter<Tourney, TourneyAdapter.ViewHolder> {
    private static final String TAG = "TourneyAdapter";
    private ChooseTourneyListener mListener;

    public interface ChooseTourneyListener {
        void onClick(String id,Boolean isChecked);
    }

    private static final DiffUtil.ItemCallback<Tourney> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Tourney>() {
                @Override
                public boolean areItemsTheSame(
                        @NonNull Tourney oldPerson, @NonNull Tourney newPerson) {
                    // Person properties may have changed if reloaded from the DB, but ID is fixed
                    return oldPerson.getId().equals(newPerson.getId());
                }

                @Override
                public boolean areContentsTheSame(
                        @NonNull Tourney oldPerson, @NonNull Tourney newPerson) {
                    // NOTE: if you use equals, your object must properly override Object#equals()
                    // Incorrectly returning false here will result in too many animations.
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
        final CheckBox favBtn;

        ViewHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.tourneyLine);
            favBtn = itemView.findViewById(R.id.like_button_cb);
            textCommandNum = itemView.findViewById(R.id.tourneyTeamNumber);
            textDate = itemView.findViewById(R.id.tourneyDate);
            textTitle = itemView.findViewById(R.id.tourneyTitle);
            textStatusFinish = itemView.findViewById(R.id.tourneyFinish);


//            mProgressDialog.setIndeterminate(true);
        }

        @SuppressLint("SetTextI18n")
        void bindTo(Tourney tourney) {
            if (SaveSharedPreference.getObject() != null) {
                if (SaveSharedPreference.getObject().getUser().getFavouriteTourney().contains(tourney.getId()))
                    favBtn.setChecked(true);
                else
                    favBtn.setChecked(false);
            }
            else
                favBtn.setChecked(false);

            textCommandNum.setText(App.getAppContext().getString(R.string.tournamentFilterCommandNum) + tourney.getMaxTeams());
            textTitle.setText(tourney.getName());
            textDate.setText(DateToString.ChangeDate(tourney.getBeginDate()) + " - " + DateToString.ChangeDate(tourney.getEndDate()));

            favBtn.setOnClickListener((v) -> {
                if (SaveSharedPreference.getObject() == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(App.getAppContext());
                    builder.setTitle("Внимание")
                            .setMessage("Войдите или зарегистрируйтесь")
                            .setCancelable(true)
                            .setNegativeButton("Позже", (dialog, idd) -> dialog.cancel())
                            .setPositiveButton("Войти", (dialog, which) -> {});
                    AlertDialog alert = builder.create();
                    alert.show();
                    favBtn.setChecked(false);
                } else {
                    mListener.onClick(tourney.getId(), favBtn.isChecked());
                }
            });
        }
    }
}
