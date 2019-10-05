package baikal.web.footballapp.user.adapter;

import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Match;

import io.reactivex.annotations.NonNull;

public class RVTimeTablePagAdapter extends PagedListAdapter<Match, MatchViewHolder> {

    protected RVTimeTablePagAdapter(DiffUtil.ItemCallback<Match> diffUtilCallback) {
        super(diffUtilCallback);
    }

    @NonNull
    @Override
    public MatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timetable, parent, false);
        return new MatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchViewHolder holder, int position) {
//        holder.bind(getItem(position));
    }

}