package baikal.web.footballapp.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import baikal.web.footballapp.R;
import baikal.web.footballapp.SetImage;
import baikal.web.footballapp.model.News_;

public class RVHorizontalNews extends RecyclerView.Adapter<RVHorizontalNews.ViewHolder> {

    private List<News_> feeds;
    private Context context;

    public RVHorizontalNews (Context context, List<News_> feeds) {
        this.feeds = feeds;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_news_cell, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        News_ news = feeds.get(position);
        holder.newsTitle.setText(news.getCaption());
        new SetImage().setImage(context, holder.newsImg, news.getImg());
    }

    @Override
    public int getItemCount() {
        return feeds.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView newsImg;
        TextView newsTitle;

        ViewHolder(View item) {
            super(item);

            newsImg = item.findViewById(R.id.HNC_feed_image);
            newsTitle = item.findViewById(R.id.HNC_feed_title);
        }
    }

    public void dataChanged (List<News_> feeds) {
        this.feeds.clear();
        this.feeds.addAll(feeds);
        notifyDataSetChanged();
    }
}
