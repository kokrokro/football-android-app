package baikal.web.footballapp.user.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import baikal.web.footballapp.FullScreenImage;
import baikal.web.footballapp.MankindKeeper;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SetImage;
import baikal.web.footballapp.model.Person;

import static baikal.web.footballapp.Controller.BASE_URL;

public class RecentRefereeAdapter extends RecyclerView.Adapter<RecentRefereeAdapter.ViewHolder> {
    private List<String> referees;
    private Context context;
    private final Listener listener;

    public RecentRefereeAdapter(List<String> referees, Context context, Listener listener) {
        this.referees = referees;
        this.context = context;
        this.listener = listener;
    }

    public interface Listener{
        void onClick(Person person);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_referee, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Person player;

        player = MankindKeeper.getInstance().getPersonById(referees.get(position));

        holder.textName.setText(player.getSurnameWithInitials());
        holder.setRefereeButton.setOnClickListener(v -> {
            listener.onClick(player);
        });
        try {

            String uriPic = BASE_URL;
            uriPic += "/" + player.getPhoto();
            (new SetImage()).setImage(context, holder.image, player.getPhoto());
            final String finalUriPic = uriPic;
            holder.image.setOnClickListener(v -> {
                if (finalUriPic.contains(".jpg") || finalUriPic.contains(".jpeg") || finalUriPic.contains(".png")) {
                    Intent intent = new Intent(context, FullScreenImage.class);
                    intent.putExtra("player_photo", finalUriPic);
                    context.startActivity(intent);
                }
            });
        } catch (Exception ignored) {        }

        if (position == (referees.size() - 1)) {
            holder.line.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return referees.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textNum;
        final ImageView image;
        final TextView textName;
        final EditText editNum;
        final ImageButton buttonDelete;
        final View line;
        final Button setRefereeButton;

        ViewHolder(View item) {
            super(item);
            textNum = item.findViewById(R.id.userCommandPlayerTextNum);
            image = item.findViewById(R.id.userCommandPlayerLogo);
            textName = item.findViewById(R.id.userCommandPlayerName);
            editNum = item.findViewById(R.id.userCommandPlayerNum);
            buttonDelete = item.findViewById(R.id.userCommandPlayerDelete);
            line = item.findViewById(R.id.userCommandPlayerLine);
            setRefereeButton = item.findViewById(R.id.setRefereeButton);
        }
    }
}
