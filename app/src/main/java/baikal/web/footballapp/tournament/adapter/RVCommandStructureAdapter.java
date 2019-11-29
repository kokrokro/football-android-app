package baikal.web.footballapp.tournament.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import baikal.web.footballapp.CheckName;
import baikal.web.footballapp.MankindKeeper;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.Player;
import baikal.web.footballapp.tournament.activity.CommandInfoActivity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RVCommandStructureAdapter extends RecyclerView.Adapter<RVCommandStructureAdapter.ViewHolder> {
    Logger log = LoggerFactory.getLogger(CommandInfoActivity.class);
    private final List<Player> players;
    public RVCommandStructureAdapter(List<Player> players){
        this.players = players;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.command_structure, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Person player = MankindKeeper.getInstance().allPlayers.get(players.get(position).getPlayerId());
        
        try {
            String str;
            CheckName checkName = new CheckName();
            str = checkName.check(player.getSurname(), player.getName(), player.getLastname());
            holder.textName.setText(str);
            int count = players.get(position).getMatches();
            str = String.valueOf(count);
            holder.textPoint2.setText(str);
            count = players.get(position).getGoals();
            str = String.valueOf(count);
            holder.textPoint3.setText(str);
            count = players.get(position).getYellowCards();
//            count = players.get(position).getActiveYellowCards();
            str = String.valueOf(count);
            holder.textPoint4.setText(str);
//            count = players.get(position).getActiveDisquals();
            count = players.get(position).getDisquals();
            str = String.valueOf(count);
            holder.textPoint5.setText(str);
            if (!str.equals("0")){
                holder.linearLayout.setBackgroundResource(R.color.colorBadgeScale);
            }
        }catch (NullPointerException e){
            String str = "Удален";
            holder.textName.setText(str);
            str="";
            holder.textPoint2.setText(str);
            holder.textPoint3.setText(str);
            holder.textPoint4.setText(str);
            holder.textPoint5.setText(str);
        }


    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        final TextView textName;
//        TextView textPoint1;
final TextView textPoint2;
        final TextView textPoint3;
        final TextView textPoint4;
        final TextView textPoint5;
        final LinearLayout linearLayout;
        ViewHolder(View item) {
            super(item);
            textName = item.findViewById(R.id.commandPlayer);
//            textPoint1 = item.findViewById(R.id.commandPlayerPoint1);
            textPoint2 = item.findViewById(R.id.commandPlayerPoint2);
            textPoint3 = item.findViewById(R.id.commandPlayerPoint3);
            textPoint4 = item.findViewById(R.id.commandPlayerPoint4);
            textPoint5 = item.findViewById(R.id.commandPlayerPoint5);
            linearLayout = item.findViewById(R.id.commandPlayerLayout);
        }
    }
}
