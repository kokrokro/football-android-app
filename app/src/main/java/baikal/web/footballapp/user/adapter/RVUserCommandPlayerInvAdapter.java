package baikal.web.footballapp.user.adapter;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import baikal.web.footballapp.CheckName;
import baikal.web.footballapp.FullScreenImage;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.Player;
import baikal.web.footballapp.user.activity.UserCommandInfo;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static baikal.web.footballapp.Controller.BASE_URL;

public class RVUserCommandPlayerInvAdapter extends RecyclerView.Adapter<RVUserCommandPlayerInvAdapter.ViewHolder>{
    private final UserCommandInfo context;
    private final List<Player> players;
    public RVUserCommandPlayerInvAdapter (UserCommandInfo context, List<Player> players){
        this.context = context;
        this.players = players;
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
        Person player = null;
        for (Person person : PersonalActivity.people) {
            if (person.getId().equals(players.get(position).getPlayerId())){
                player = person;
                break;
            }
        }

        CheckName checkName = new CheckName();
        str = checkName.check(player.getSurname(), player.getName(), player.getLastname());
        holder.textName.setText(str);
        try {
            String uriPic = BASE_URL;
            uriPic += "/" + player.getPhoto();
            URL url = new URL(uriPic);
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.optionalCircleCrop();
            requestOptions.format(DecodeFormat.PREFER_ARGB_8888);
            requestOptions.error(R.drawable.ic_logo2);
            requestOptions.override(500, 500);
            requestOptions.priority(Priority.HIGH);
            Glide.with(context)
                    .asBitmap()
                    .load(url)
                    .apply(requestOptions)
                    .into(holder.image);
            final String finalUriPic = uriPic;
            holder.image.setOnClickListener(v -> {
                if (finalUriPic.contains(".jpg") || finalUriPic.contains(".jpeg") || finalUriPic.contains(".png")) {
                    Intent intent = new Intent(context, FullScreenImage.class);
                    intent.putExtra("player_photo", finalUriPic);
                    context.startActivity(intent);
                }
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (position == (players.size() - 1)) {
            holder.line.setVisibility(View.INVISIBLE);
        }
        if (player.getId().equals(SaveSharedPreference.getObject().getUser().getId())){
            holder.buttonDelete.setVisibility(View.INVISIBLE);
        }
        holder.buttonDelete.setOnClickListener(v -> {
            //post
            UserCommandInfo.playersInv.remove(players.get(position));
            List<Player> players = new ArrayList<>(UserCommandInfo.playersInv);
            UserCommandInfo.adapterInv.dataChanged(players);
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
    public void dataChanged(List<Player> allPlayers1){
        players.clear();
        players.addAll(allPlayers1);
        notifyDataSetChanged();
//        this.notifyDataSetChanged();
    }
}
