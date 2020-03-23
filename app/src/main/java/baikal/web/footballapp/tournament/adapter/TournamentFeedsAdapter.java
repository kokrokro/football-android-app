package baikal.web.footballapp.tournament.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import baikal.web.footballapp.App;
import baikal.web.footballapp.DateToString;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SetImage;
import baikal.web.footballapp.model.News_;

public class TournamentFeedsAdapter extends PagedListAdapter<News_, TournamentFeedsAdapter.ViewHolder> {
    private static final String TAG = "TournamentFeedsAdapter";

    private static final DiffUtil.ItemCallback<News_> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<News_>() {
                @Override
                public boolean areItemsTheSame(@NonNull News_ oldItem, @NonNull News_ newItem) {
                    return newItem.getId().equals(oldItem.getId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull News_ oldItem, @NonNull News_ newItem) {
                    return newItem.getId().equals(oldItem.getId());
                }
            };

    public TournamentFeedsAdapter () {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.full_feed_cell, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        News_ feed = getItem(position);

        if (feed != null)
            holder.bindTo(feed);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textDate;
        final TextView textTitle;
        final TextView text;
        final ImageView imgView;

        ViewHolder(View itemView) {
            super(itemView);
            textDate = itemView.findViewById(R.id.FFC_date);
            textTitle = itemView.findViewById(R.id.FFC_title);
            imgView = itemView.findViewById(R.id.FFC_image);
            text = itemView.findViewById(R.id.FFC_text);
        }

        void bindTo (@NonNull News_ feed) {
            textTitle.setText(feed.getCaption());
            textDate.setText(DateToString.stringDate(feed.getCreatedAt()));
            text.setText(feed.getContent());
            SetImage.setImage(App.getAppContext(), imgView, null, null, feed.getImg());
        }
    }
}
