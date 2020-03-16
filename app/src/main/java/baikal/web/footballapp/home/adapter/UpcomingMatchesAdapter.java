package baikal.web.footballapp.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import baikal.web.footballapp.DateToString;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.MatchPopulate;

public class UpcomingMatchesAdapter extends PagedListAdapter<MatchPopulate, UpcomingMatchesAdapter.ViewHolder> {
    private static final String TAG = "UpcomingMatchesAdapter";
    private final OnItemListener mOnItemListener;

    public interface OnItemListener {
        void OnClick(MatchPopulate feed);
    }

    private static final DiffUtil.ItemCallback<MatchPopulate> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<MatchPopulate>() {
                @Override
                public boolean areItemsTheSame(@NonNull MatchPopulate oldItem, @NonNull MatchPopulate newItem) {
                    return newItem.getId().equals(oldItem.getId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull MatchPopulate oldItem, @NonNull MatchPopulate newItem) {
                    return newItem.getId().equals(oldItem.getId());
                }
            };

    public UpcomingMatchesAdapter(OnItemListener mOnItemListener) {
        super(DIFF_CALLBACK);
        this.mOnItemListener = mOnItemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timetable_fragment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MatchPopulate match = getItem(position);

        if (match != null)
            holder.bindTo(match);

        if (position == getItemCount()-1)
            holder.line.setVisibility(View.INVISIBLE);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textCommandTitle1;
        final TextView textCommandTitle2;
        final ImageView imgCommandLogo1;
        final ImageView imgCommandLogo2;
        final TextView textStadium;
        final TextView textDate;
        final TextView textTime;
        final TextView textTour;
        final TextView textLastScore;
        final TextView textPenalty;
        final TextView textScore;
        final RelativeLayout layout;
        final View line;

        ViewHolder(View item) {
            super(item);
            textCommandTitle1 = item.findViewById(R.id.timetableCommandTitle1);
            textCommandTitle2 = item.findViewById(R.id.timetableCommandTitle2);
            imgCommandLogo1 = item.findViewById(R.id.timetableCommandLogo1);
            imgCommandLogo2 = item.findViewById(R.id.timetableCommandLogo2);
            textStadium = item.findViewById(R.id.timetableStadium);
            textDate = item.findViewById(R.id.timetableDate);
            textTime = item.findViewById(R.id.timetableTime);
            textTour = item.findViewById(R.id.timetableTour);
            textLastScore = item.findViewById(R.id.timetableLastScore);
            textScore = item.findViewById(R.id.timetableGameScore);
            textPenalty = item.findViewById(R.id.timetablePenalty);
            layout = item.findViewById(R.id.TF_relative_layout);
            line = item.findViewById(R.id.timetableLine);
        }

        void bindTo (MatchPopulate match) {
            textDate.setText(DateToString.stringDate(match.getDate()));
            textTime.setText(DateToString.ChangeTime(match.getDate()));
            textTour.setText(match.getTour()!=null?match.getTour():"");
            textScore.setText(match.getScore()!=null?match.getScore():"-");
            if (match.getPenalty() != null) {
                textPenalty.setText(match.getPenalty());
                textPenalty.setVisibility(View.VISIBLE);
            }
            else
                textPenalty.setVisibility(View.GONE);


            if (match.getTeamOne() != null)
                textCommandTitle1.setText(match.getTeamOne().getName()!=null?match.getTeamOne().getName():"Команда не назначена");
            if (match.getTeamTwo() != null)
                textCommandTitle2.setText(match.getTeamTwo().getName()!=null?match.getTeamTwo().getName():"Команда не назначена");
            if (match.getPlace() != null)
                textStadium.setText(match.getPlace().getName()!=null?match.getPlace().getName():"");

            layout.setOnClickListener(v->mOnItemListener.OnClick(match));
        }
    }
}
