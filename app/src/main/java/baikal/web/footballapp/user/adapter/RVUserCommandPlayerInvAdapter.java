package baikal.web.footballapp.user.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import baikal.web.footballapp.App;
import baikal.web.footballapp.MankindKeeper;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.SetImage;
import baikal.web.footballapp.model.Person;

public class RVUserCommandPlayerInvAdapter extends RecyclerView.Adapter<RVUserCommandPlayerInvAdapter.ViewHolder>{
    private final List<String> players;
    private final RVUserCommandPlayerAdapter.Listener listener;
    private final String tag;

    public RVUserCommandPlayerInvAdapter(List<String> players, RVUserCommandPlayerAdapter.Listener listener, String tag){
        this.players = players;
        this.listener = listener;
        this.tag = tag;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.command_player_inv
                , parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        int count = position + 1;
        String str = String.valueOf(count);
        holder.textNum.setText(str);
        Person player;
        if(tag.equals("ChangePlayersForMatch")){
            holder.buttonDelete.setImageResource(R.drawable.ic_plus);
        }
        player = MankindKeeper.getInstance().getPersonById(players.get(position));


        holder.textName.setText(player.getSurnameWithInitials());
        SetImage.setImage(App.getAppContext(), holder.image, player.getPhoto());

        if (position == (players.size() - 1)) {
            holder.line.setVisibility(View.INVISIBLE);
        }
        if (player.getId().equals(SaveSharedPreference.getObject().getUser().getId())){
            holder.buttonDelete.setVisibility(View.INVISIBLE);
        }
        holder.buttonDelete.setOnClickListener(v -> {
            //post
           listener.onClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textNum;
        final ImageView image;
        final TextView textName;
        final ImageButton buttonDelete;
        final View line;
        ViewHolder(View item) {
            super(item);
            textNum = item.findViewById(R.id.userCommandPlayerInvTextNum);
            image = item.findViewById(R.id.userCommandPlayerInvLogo);
            textName = item.findViewById(R.id.userCommandPlayerInvName);
            buttonDelete = item.findViewById(R.id.userCommandInvPlayerDelete);
            line = item.findViewById(R.id.userCommandPlayerInvLine);
        }
    }
    public void dataChanged(List<String> allPlayers1){
        players.clear();
        players.addAll(allPlayers1);
        notifyDataSetChanged();
//        this.notifyDataSetChanged();
    }
}
