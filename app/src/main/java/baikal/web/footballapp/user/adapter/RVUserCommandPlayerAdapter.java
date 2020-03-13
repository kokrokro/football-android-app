package baikal.web.footballapp.user.adapter;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import baikal.web.footballapp.R;
import baikal.web.footballapp.SetImage;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.Player;

public class RVUserCommandPlayerAdapter extends RecyclerView.Adapter<RVUserCommandPlayerAdapter.ViewHolder> {
    private final List<Player> players;
    private final Activity context;
    private final Listener listener;



    public RVUserCommandPlayerAdapter(Activity context, List<Player> players, Listener listener) {
        this.context = context;
        this.players = players;
        this.listener = listener;
    }
    public interface Listener{
        void onClick(int position);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.command_player, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
//        Person player = players.get(position).getPerson();
//        fillData(holder, position, player);
    }

    private void fillData (@NonNull final ViewHolder holder, final int position, Person player)
    {
        holder.textNum.setText(String.valueOf(position + 1));
        try {
            holder.textName.setText(player.getSurnameWithInitials());
            holder.editNum.setText(String.valueOf(players.get(position).getNumber()));
        } catch (Exception e){
            Log.e("RVUserTeamPlayerAdapt", e.toString());
        }

        SetImage.setImage(context, holder.image, player.getPhoto());

        if (position == (players.size() - 1))
            holder.line.setVisibility(View.INVISIBLE);

        holder.buttonDelete.setOnClickListener(v -> listener.onClick(position));
        holder.editNum.getBackground().setColorFilter(context.getResources().getColor(R.color.colorLightGray), PorterDuff.Mode.SRC_IN);
        holder.editNum.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                holder.editNum.getBackground().clearColorFilter();
            else
                holder.editNum.getBackground().setColorFilter(context.getResources().getColor(R.color.colorLightGray), PorterDuff.Mode.SRC_IN);
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
        final EditText editNum;
        final ImageButton buttonDelete;
        final View line;

        ViewHolder(View item) {
            super(item);
            textNum = item.findViewById(R.id.userCommandPlayerTextNum);
            image = item.findViewById(R.id.userCommandPlayerLogo);
            textName = item.findViewById(R.id.userCommandPlayerName);
            editNum = item.findViewById(R.id.userCommandPlayerNum);
            buttonDelete = item.findViewById(R.id.userCommandPlayerDelete);
            line = item.findViewById(R.id.userCommandPlayerLine);
        }

//        void bindTo (Player )
    }
}
