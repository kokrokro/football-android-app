package baikal.web.footballapp.user.adapter;

import android.graphics.PorterDuff;
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

import baikal.web.footballapp.App;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SetImage;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.Player;
import baikal.web.footballapp.viewmodel.PersonViewModel;

public class EditTeamPlayersAdapter extends RecyclerView.Adapter<EditTeamPlayersAdapter.ViewHolder> {
    private final List<Player> players;
    private final OnPlayerDelete listener;
    private PersonViewModel personViewModel;

    public EditTeamPlayersAdapter(PersonViewModel personViewModel, List<Player> players, OnPlayerDelete listener) {
        this.players = players;
        this.listener = listener;
        this.personViewModel = personViewModel;
    }
    public interface OnPlayerDelete {
        void onClick(Player player);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.command_player, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.textNum.setText(String.valueOf(position + 1));
        if (position == (players.size() - 1))
            holder.line.setVisibility(View.INVISIBLE);

        Player player = players.get(position);
        if (player != null)
            holder.bindTo(player);
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

        void bindTo (Player player) {
            editNum.setText(String.valueOf(player.getNumber()));

            Person person = personViewModel.getPersonById(player.getPerson(), person1 -> {
                SetImage.setImage(App.getAppContext(), image, person1.getPhoto());
                textName.setText(person1.getSurnameAndName());
            });

            if (person != null) {
                SetImage.setImage(App.getAppContext(), image, person.getPhoto());
                textName.setText(person.getSurnameAndName());
            }

            buttonDelete.setOnClickListener(v -> listener.onClick(player));
            editNum.getBackground().setColorFilter(App.getAppContext().getResources().getColor(R.color.colorLightGray), PorterDuff.Mode.SRC_IN);
            editNum.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus)
                    editNum.getBackground().clearColorFilter();
                else
                    editNum.getBackground().setColorFilter(App.getAppContext().getResources().getColor(R.color.colorLightGray), PorterDuff.Mode.SRC_IN);
            });

        }
    }
}
