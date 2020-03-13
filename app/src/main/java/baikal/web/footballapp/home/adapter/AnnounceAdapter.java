package baikal.web.footballapp.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Announce;

public class AnnounceAdapter extends RecyclerView.Adapter<AnnounceAdapter.ViewHolder> {

    private final List<Announce> announces;

    public AnnounceAdapter(List<Announce> announces){
        this.announces = announces;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ads, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textDate.setVisibility(View.GONE);
        String str = announces.get(position).getContent();
        holder.textTitle.setText(str);
    }

    @Override
    public int getItemCount() {
        return announces.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        final TextView textTitle;
        final TextView textDate;
        final View line;
        ViewHolder(View item) {
            super(item);
            textDate = item.findViewById(R.id.adsDate);
            textTitle = item.findViewById(R.id.adsTitle);
            line = item.findViewById(R.id.adsLine);
        }
    }

    public void dataChanged(List<Announce> allPlayers1) {
        announces.clear();
        announces.addAll(allPlayers1);
        notifyDataSetChanged();
    }
}
