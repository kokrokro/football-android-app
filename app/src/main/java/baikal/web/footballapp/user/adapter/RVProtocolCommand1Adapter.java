package baikal.web.footballapp.user.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import baikal.web.footballapp.CheckName;
import baikal.web.footballapp.FullScreenImage;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.Player;
import baikal.web.footballapp.user.activity.ProtocolCommand1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static baikal.web.footballapp.Controller.BASE_URL;

public class RVProtocolCommand1Adapter extends RecyclerView.Adapter<RVProtocolCommand1Adapter.ViewHolder> {
    private final ProtocolCommand1 context;
    private final List<Player> players;
    private final List<String> playerList;
    Logger log = LoggerFactory.getLogger(ProtocolCommand1.class);

    public RVProtocolCommand1Adapter(Activity context, List<Player> players, List<String> playerList, ListAdapterListener mListener) {
        this.context = (ProtocolCommand1) context;
        this.players = players;
        this.playerList = playerList;
        this.mListener = mListener;
    }

    private final ListAdapterListener mListener;

    public interface ListAdapterListener {
        void onClickSwitch(int position, String personId, Boolean check);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_command1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        String uriPic = BASE_URL;
        Person player = null;
        for (Person person : PersonalActivity.people) {
            if (person.getId().equals(players.get(position).getPlayerId())) {
                player = person;
                break;
            }
        }
        holder.editNum.getBackground().setColorFilter(context.getResources().getColor(R.color.colorLightGray), PorterDuff.Mode.SRC_IN);
        holder.editNum.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                holder.editNum.getBackground().clearColorFilter();
            } else {
                holder.editNum.getBackground().setColorFilter(context.getResources().getColor(R.color.colorLightGray), PorterDuff.Mode.SRC_IN);
            }
        });
        String str;
        CheckName checkName = new CheckName();
        str = checkName.check(player.getSurname(), player.getName(), player.getLastname());
        holder.textName.setText(str);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.optionalCircleCrop();
        requestOptions.format(DecodeFormat.PREFER_ARGB_8888);
        requestOptions.error(R.drawable.ic_logo2);
        requestOptions.override(500, 500);
        requestOptions.priority(Priority.HIGH);
        try {
            uriPic += "/" + player.getPhoto();
            URL url = new URL(uriPic);
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
            Glide.with(context)
                    .asBitmap()
                    .load(R.drawable.ic_logo2)
                    .apply(requestOptions)
                    .into(holder.image);
        }
        if (players.get(position).getActiveDisquals() != 0) {
            holder.layout.setBackgroundResource(R.color.colorBadgeScale);
            holder.layout.setVisibility(View.GONE);
        }
        boolean check = false;
        holder.switchCompat.setChecked(check);
        final Person finalPlayer = player;
        holder.switchCompat.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    // TODO: handle your switch toggling logic here
                    mListener.onClickSwitch(position, finalPlayer.getId(), isChecked);
                });
        holder.switchCompat.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                holder.switchCompat.getParent().requestDisallowInterceptTouchEvent(true);
            }
            return false;
        });
        if (position == (players.size() - 1)) {
            holder.line.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final LinearLayout layout;
        final EditText editNum;
        final ImageView image;
        final TextView textName;
        final SwitchCompat switchCompat;
        final View line;

        ViewHolder(View item) {
            super(item);
            editNum = item.findViewById(R.id.playerCommand1Num);
            image = item.findViewById(R.id.playerCommand1Logo);
            textName = item.findViewById(R.id.playerCommand1Name);
            switchCompat = item.findViewById(R.id.playerCommand1Switch);
            line = item.findViewById(R.id.playerCommand1Line);
            layout = item.findViewById(R.id.playerCommandLayout);
        }
    }
    public void dataChanged(List<String> allPlayers1){
        playerList.clear();
        playerList.addAll(allPlayers1);
        notifyDataSetChanged();
    }
}
