package baikal.web.footballapp.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import baikal.web.footballapp.DateToString;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.News_;

public class NewsAdapter extends PagedListAdapter<News_, NewsAdapter.ViewHolder> {
    private static final String TAG = "PlayersAdapter";
    private final OnItemListener mOnItemListener;

    public interface OnItemListener {
        void OnClick(News_ feed);
    }

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


    public NewsAdapter(OnItemListener onItemListener){
        super(DIFF_CALLBACK);
        this.mOnItemListener = onItemListener;
    }

    @NonNull
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news, parent, false);
        return new NewsAdapter.ViewHolder(view);
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
        final ConstraintLayout showSingleFeed;
        final View line;
        ViewHolder(View itemView) {
            super(itemView);
            textDate = itemView.findViewById(R.id.newsDate);
            textTitle = itemView.findViewById(R.id.newsTitle);
            showSingleFeed = itemView.findViewById(R.id.newsButtonShow);
            line = itemView.findViewById(R.id.newsLine);
        }

        void bindTo (@NonNull News_ feed) {
            textTitle.setText(feed.getCaption());
            textDate.setText(DateToString.stringDate(feed.getCreatedAt()));
            showSingleFeed.setOnClickListener(v->mOnItemListener.OnClick(feed));
        }
    }
}

