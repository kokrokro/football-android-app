package baikal.web.footballapp.user.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import baikal.web.footballapp.CheckName;
import baikal.web.footballapp.DateToString;
import baikal.web.footballapp.FullScreenImage;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.players.activity.Player;
import baikal.web.footballapp.players.activity.PlayersPage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static baikal.web.footballapp.Controller.BASE_URL;

public class TrainerAdapter extends RecyclerView.Adapter<TrainerAdapter.ViewHolder> {
    private final Logger log = LoggerFactory.getLogger(PlayersPage.class);
    private final List<Person> allPlayers;
    private final Context context;
    private final Activity activity;
    private final MyListener myListener;

    public TrainerAdapter(Activity activity, List<Person> allPlayers,MyListener myListener) {
        this.allPlayers = allPlayers;
        this.activity = activity;
        this.context = activity;
        this.myListener = myListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.player, parent, false);
        return new ViewHolder(view);
    }
    public interface MyListener {
        void onClick(String id);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        try {
            String uriPic = BASE_URL;
            String DOB = allPlayers.get(position).getBirthdate();
            DateToString dateToString = new DateToString();
            holder.textDOB.setText(dateToString.ChangeDate(DOB));
            String str;
            CheckName checkName = new CheckName();
            str = checkName.check(allPlayers.get(position).getSurname(), allPlayers.get(position).getName(), allPlayers.get(position).getLastname());
            holder.textName.setText(str);
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.optionalCircleCrop();
            requestOptions.format(DecodeFormat.PREFER_ARGB_8888);
            requestOptions.error(R.drawable.ic_logo2);
            requestOptions.override(500, 500);
            requestOptions.priority(Priority.HIGH);
            try {
                uriPic += "/" + allPlayers.get(position).getPhoto();
                URL url = new URL(uriPic);
                Glide.with(activity)
                        .asBitmap()
                        .load(url)
                        .apply(requestOptions)
                        .into(holder.imageLogo);

                final String finalUriPic = uriPic;
                holder.imageLogo.setOnClickListener(v -> {
                    if (finalUriPic.contains(".jpg") || finalUriPic.contains(".jpeg") || finalUriPic.contains(".png")) {
                        Intent intent = new Intent(activity, FullScreenImage.class);
                        intent.putExtra("player_photo", finalUriPic);
                        context.startActivity(intent);
                    }

                });
            } catch (MalformedURLException e) {
                Glide.with(activity)
                        .asBitmap()
                        .load(R.drawable.ic_logo2)
                        .apply(requestOptions)
                        .into(holder.imageLogo);
            }

            holder.buttonShow2.setOnClickListener(v -> {
                myListener.onClick(allPlayers.get(position).getId());
            });
            if (position == (allPlayers.size() - 1)) {
                holder.line.setVisibility(View.INVISIBLE);
            }


        } catch (Exception e) {
            log.error("ERROR: ", e);
        }

    }

    @Override
    public int getItemCount() {
        return allPlayers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageLogo;
        final LinearLayout buttonShow2;
        final TextView textName;
        final TextView textDOB;
        final View line;

        public ViewHolder(View item) {
            super(item);
            line = item.findViewById(R.id.playersLine);
            imageLogo = item.findViewById(R.id.playerInfoLogo);
            buttonShow2 = item.findViewById(R.id.playerInfoButtonShow2);
            textName = item.findViewById(R.id.playerInfoName);
            textDOB = item.findViewById(R.id.playerInfoDOB);
        }
    }

    public void dataChanged(List<Person> allPlayers1){
        allPlayers.clear();
        allPlayers.addAll(allPlayers1);
        notifyDataSetChanged();
    }


}
