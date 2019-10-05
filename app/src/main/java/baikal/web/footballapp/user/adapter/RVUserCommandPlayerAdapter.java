package baikal.web.footballapp.user.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
        str = players.get(position).getNumber();
        try {
            holder.editNum.setText(str);
        }catch (Exception e){}
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
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.optionalCircleCrop();
            requestOptions.format(DecodeFormat.PREFER_ARGB_8888);
            requestOptions.error(R.drawable.ic_logo2);
            requestOptions.override(500, 500);
            requestOptions.priority(Priority.HIGH);
            Glide.with(context)
                    .asBitmap()
                    .load(R.drawable.ic_logo2)
                    .apply(requestOptions)
                    .into(holder.image);
        }

        if (position == (players.size() - 1)) {
            holder.line.setVisibility(View.INVISIBLE);
        }
        if (player.getId().equals(SaveSharedPreference.getObject().getUser().getId())){
            holder.buttonDelete.setVisibility(View.INVISIBLE);
        }
        holder.buttonDelete.setOnClickListener(v -> {
            //post
            UserCommandInfo.players.remove(players.get(position));
            UserCommandInfo.adapter.notifyDataSetChanged();
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
