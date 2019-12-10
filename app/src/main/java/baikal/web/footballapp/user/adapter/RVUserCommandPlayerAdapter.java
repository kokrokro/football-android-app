package baikal.web.footballapp.user.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.FullScreenImage;
import baikal.web.footballapp.MankindKeeper;
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

import java.util.List;

import static baikal.web.footballapp.Controller.BASE_URL;

public class RVUserCommandPlayerAdapter extends RecyclerView.Adapter<RVUserCommandPlayerAdapter.ViewHolder> {
    private final List<Player> players;
    private final UserCommandInfo context;



    public RVUserCommandPlayerAdapter(Activity context, List<Player> players) {
        this.context = (UserCommandInfo) context;
        this.players = players;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.command_player, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        int count = position + 1;
//        String str = Integer.toString(count);
        String str = String.valueOf(count);
        holder.textNum.setText(str);
        Person player;

        player = MankindKeeper.getInstance().getPersonById(players.get(position).getPerson());

        holder.textName.setText(player.getSurnameWithInitials());
        str = String.valueOf(players.get(position).getNumber());
        try {
            holder.editNum.setText(str);
        } catch (Exception ignored){}
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
        } catch (Exception ignored) {

        }

        if (position == (players.size() - 1)) {
            holder.line.setVisibility(View.INVISIBLE);
        }
//        if (player.getId().equals(SaveSharedPreference.getObject().getUser().getId())){
//            holder.buttonDelete.setVisibility(View.INVISIBLE);
//        }
        holder.buttonDelete.setOnClickListener(v -> {
            //post
            UserCommandInfo.players.remove(players.get(position));
            UserCommandInfo.adapter.notifyDataSetChanged();
            Log.d("cancel invite id ", ""+UserCommandInfo.accepted.get(position).get_id());
            Controller.getApi()
                    .cancelInv(UserCommandInfo.accepted.get(position).get_id(), SaveSharedPreference.getObject().getToken())
                    .enqueue(new Callback<Invite>() {
                @Override
                public void onResponse(@NonNull Call<Invite> call, @NonNull Response<Invite> response) {
                    if(response.isSuccessful()){
                        if(response.body()!=null){
                            Log.d("cancel invite", "__SUCCCESS");
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Invite> call, @NonNull Throwable t) {
                    Log.d("cancel invite", "__FAIL");
                }
            });
        });
        holder.editNum.getBackground().setColorFilter(context.getResources().getColor(R.color.colorLightGray), PorterDuff.Mode.SRC_IN);
        holder.editNum.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                holder.editNum.getBackground().clearColorFilter();
            } else {
                holder.editNum.getBackground().setColorFilter(context.getResources().getColor(R.color.colorLightGray), PorterDuff.Mode.SRC_IN);
            }
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
    }
}
