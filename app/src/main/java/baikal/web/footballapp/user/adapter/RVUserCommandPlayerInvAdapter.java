package baikal.web.footballapp.user.adapter;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import baikal.web.footballapp.CheckName;
import baikal.web.footballapp.Controller;
import baikal.web.footballapp.FullScreenImage;
import baikal.web.footballapp.MankindKeeper;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.SetImage;
import baikal.web.footballapp.model.Invite;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.Player;
import baikal.web.footballapp.user.activity.UserCommandInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static baikal.web.footballapp.Controller.BASE_URL;

public class RVUserCommandPlayerInvAdapter extends RecyclerView.Adapter<RVUserCommandPlayerInvAdapter.ViewHolder>{
    private final UserCommandInfo context;
    private final List<String> players;

    public RVUserCommandPlayerInvAdapter (UserCommandInfo context, List<String> players){
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

        if (MankindKeeper.getInstance().allPlayers.containsKey(players.get(position)))
            player = MankindKeeper.getInstance().allPlayers.get(players.get(position));

        try {
            holder.textName.setText(player.getSurnameWithInitials());
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
        } catch (Exception e) {
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
            Controller.getApi().cancelInv(UserCommandInfo.allInvites.get(position).get_id(), PersonalActivity.token).enqueue(new Callback<Invite>() {
                @Override
                public void onResponse(Call<Invite> call, Response<Invite> response) {
                    if(response.isSuccessful()){
                        Log.d("Debug", "____________");
                        Toast.makeText(context,"Приглашение отменено", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Invite> call, Throwable t) {
                    Toast.makeText(context,"Не удалось", Toast.LENGTH_SHORT).show();
                    Log.d("Debug", "neeeet");
                }
            });

            UserCommandInfo.playersInv.remove(players.get(position));
//            List<String> players = new ArrayList<>(UserCommandInfo.playersInv);
            UserCommandInfo.adapterInv.notifyDataSetChanged();
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
