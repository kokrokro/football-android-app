package baikal.web.footballapp.user.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Tourney;

public class TourneyListInMenuAdapter extends RecyclerView.Adapter<TourneyListInMenuAdapter.ViewHolder> {
    private final List<Tourney> tourneys;
    private OnTourneyClick onTourneyClick;

    public interface OnTourneyClick{
        void onTourneyClick(Tourney tourney);
    }

    public TourneyListInMenuAdapter (List<Tourney> tourneys, OnTourneyClick onTourneyClick) {
        this.tourneys = tourneys;
        this.onTourneyClick = onTourneyClick;
    }

    @NonNull
    @Override
    public TourneyListInMenuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tourney_list_in_menu_cell, parent, false);
        return new TourneyListInMenuAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TourneyListInMenuAdapter.ViewHolder holder, int position) {
        Tourney tourney = tourneys.get(position);

        if (tourney != null)
            holder.bindTo(tourney);
    }

    @Override
    public int getItemCount() {
        return tourneys.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tourneyName;
        final LinearLayout linearLayout;

        ViewHolder (View item) {
            super(item);

            tourneyName = item.findViewById(R.id.TLIMC_tourney_name);
            linearLayout = item.findViewById(R.id.TLIMC_view_holder);
        }

        void bindTo (Tourney tourney) {
            tourneyName.setText(tourney.getName().equals("")?"-":tourney.getName());
            if (onTourneyClick != null)
                linearLayout.setOnClickListener(v -> onTourneyClick.onTourneyClick(tourney));
        }
    }
}
