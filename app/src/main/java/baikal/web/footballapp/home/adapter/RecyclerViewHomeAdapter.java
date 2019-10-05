package baikal.web.footballapp.home.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import baikal.web.footballapp.DateToString;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.home.activity.NewsFragment;
import baikal.web.footballapp.home.activity.FullscreenNewsActivity;
import baikal.web.footballapp.model.News_;

import java.util.List;

public class RecyclerViewHomeAdapter extends RecyclerView.Adapter<RecyclerViewHomeAdapter.ViewHolder> {
    private final List<News_> news;
    private final NewsFragment context;
    private final PersonalActivity activity;
    public RecyclerViewHomeAdapter(Activity activity, NewsFragment context, List<News_> news){
        this.news = news;
        this.activity = (PersonalActivity) activity;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        String str = news.get(position).getCaption();
        holder.textTitle.setText(str);
        DateToString dateToString = new DateToString();
        str = news.get(position).getCreatedAt();
        holder.textDate.setText(dateToString.ChangeDate(str));
        holder.imageButtonShow.setOnClickListener(v -> {
            Intent intent = new Intent(activity, FullscreenNewsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("NEWS" ,news.get(position));
            intent.putExtras(bundle);
            context.startActivity(intent);
        });
        if (position==(news.size()-1)){
            holder.line.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textDate;
        final TextView textTitle;
        final LinearLayout imageButtonShow;
        final View line;
        ViewHolder(View itemView) {
            super(itemView);
            textDate = itemView.findViewById(R.id.newsDate);
            textTitle = itemView.findViewById(R.id.newsTitle);
            imageButtonShow = itemView.findViewById(R.id.newsButtonShow);
            line = itemView.findViewById(R.id.newsLine);
        }
    }

    public void dataChanged(List<News_> allPlayers1) {
        news.clear();
        news.addAll(allPlayers1);
        notifyDataSetChanged();
    }
}

