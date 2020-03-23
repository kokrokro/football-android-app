package baikal.web.footballapp.user.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import baikal.web.footballapp.App;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SetImage;
import baikal.web.footballapp.model.MatchPopulate;
import baikal.web.footballapp.model.Player;
import baikal.web.footballapp.viewmodel.PersonViewModel;

public class RVWorkProtocolEditTeamAdapter extends RecyclerView.Adapter<RVWorkProtocolEditTeamAdapter.ViewHolder>{
    private final List<Player> players;
    private final MatchPopulate match;
    private boolean isEditable;
    private PersonViewModel personViewModel;

    public RVWorkProtocolEditTeamAdapter(PersonViewModel personViewModel, List<Player> players, MatchPopulate match, boolean isEditable){
        this.players = players;
        this.match = match;
        this.isEditable = isEditable;
        this.personViewModel = personViewModel;

        Log.d("RVWorkProtocolEditTeamA", "" + isEditable);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_first_command, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Player player = players.get(position);

        personViewModel.getPersonById(player.getPerson(), p -> {
            SetImage.setImage(App.getAppContext(), holder.image, p.getPhoto());
            holder.textName.setText(p.getSurnameAndName());
        });

        holder.textNum.setText(player.getNumber().toString());

        if (!match.getPlayersList().contains(player.getPerson())){
            holder.textName.setTextColor(ContextCompat.getColor(App.getAppContext(), R.color.colorLightGrayForText));
            holder.textNum.setTextColor(ContextCompat.getColor(App.getAppContext(), R.color.colorLightGrayForText));
            holder.aSwitch.setChecked(false);
        } else {
            holder.textName.setTextColor(ContextCompat.getColor(App.getAppContext(), R.color.colorBottomNavigationUnChecked));
            holder.textNum.setTextColor(ContextCompat.getColor(App.getAppContext(), R.color.colorBottomNavigationUnChecked));
            holder.aSwitch.setChecked(true);
        }

        holder.aSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                holder.textName.setTextColor(ContextCompat.getColor(App.getAppContext(), R.color.colorBottomNavigationUnChecked));
                holder.textNum.setTextColor(ContextCompat.getColor(App.getAppContext(), R.color.colorBottomNavigationUnChecked));
                match.getPlayersList().add(player.getPerson());
            } else {
                holder.textName.setTextColor(ContextCompat.getColor(App.getAppContext(), R.color.colorLightGrayForText));
                holder.textNum.setTextColor(ContextCompat.getColor(App.getAppContext(), R.color.colorLightGrayForText));
                try {
                    match.getPlayersList().remove(player.getPerson());
                } catch (Exception ignored) {
                }
            }
        });

        holder.aSwitch.setEnabled(isEditable);

        if (position==(players.size()-1))
            holder.line.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textNum;
        final TextView textName;
        final ImageView image;
        final View line;
        final Switch aSwitch;

        ViewHolder(View item) {
            super(item);
            textNum = item.findViewById(R.id.playerCommandFirstNum);
            textName = item.findViewById(R.id.playerCommandFirstName);
            image = item.findViewById(R.id.playerCommandFirstLogo);
            line = item.findViewById(R.id.playerCommandFirstLine);
            aSwitch = item.findViewById(R.id.PFC_switch);
        }
    }
}
