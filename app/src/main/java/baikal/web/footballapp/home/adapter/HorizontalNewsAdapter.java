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

import baikal.web.footballapp.App;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SetImage;
import baikal.web.footballapp.model.News_;

public class HorizontalNewsAdapter extends PagedListAdapter<News_, HorizontalNewsAdapter.ViewHolder> {
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


    public HorizontalNewsAdapter(OnItemListener onItemListener){
        super(DIFF_CALLBACK);
        this.mOnItemListener = onItemListener;
    }

    @NonNull
    @Override
    public HorizontalNewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_news_cell, parent, false);
        return new HorizontalNewsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        News_ feed = getItem(position);

        if (feed != null)
            holder.bindTo(feed);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView newsImg;
        TextView newsTitle;
        RelativeLayout relativeLayout;

        ViewHolder(View item) {
            super(item);

            newsImg = item.findViewById(R.id.HNC_feed_image);
            newsTitle = item.findViewById(R.id.HNC_feed_title);
            relativeLayout = item.findViewById(R.id.HNC_relative_layout);
        }

        void bindTo (News_ feed) {
            SetImage.setImage(App.getAppContext(), newsImg, feed.getImg());
            newsTitle.setText(feed.getCaption());
            relativeLayout.setOnClickListener((v)->mOnItemListener.OnClick(feed));
        }
    }
}
