package baikal.web.footballapp.players.adapter;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import baikal.web.footballapp.CheckName;
import baikal.web.footballapp.DateToString;
import baikal.web.footballapp.MankindKeeper;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SetImage;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.players.activity.Player;
import baikal.web.footballapp.players.activity.PlayersPage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

public class RecyclerViewPlayersAdapter extends RecyclerView.Adapter<RecyclerViewPlayersAdapter.ViewHolder> {
    private static final String TAG = "RVPlayerAdapter";
    private final Logger log = LoggerFactory.getLogger(PlayersPage.class);
    private final List<String> allPlayers;
    private final Context context;
    private final PersonalActivity activity;

    public RecyclerViewPlayersAdapter(Context context, PersonalActivity activity, List<String> allPlayers) {
        this.allPlayers = allPlayers;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.player, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        try {
            Log.d(TAG, String.valueOf(position));
            Person p = MankindKeeper.getInstance().getPersonById(allPlayers.get(position));
            String DOB = p != null ? p.getBirthdate() : "error";
            DateToString dateToString = new DateToString();
            holder.textDOB.setText(dateToString.ChangeDate(DOB));
            String str;
            CheckName checkName = new CheckName();
            str = checkName.check(Objects.requireNonNull(p).getSurname(), p.getName(), p.getLastname());
            holder.textName.setText(str);
            SetImage setImage = new SetImage();
            setImage.setImage(context, holder.imageLogo, p.getPhoto());

            holder.buttonShow2.setOnClickListener(v -> {
                Player player = new Player();
                Bundle bundle = new Bundle();
                bundle.putSerializable("PLAYERINFO", p);
                player.setArguments(bundle);
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.pageContainer, player)
                        .hide(activity.getActive())
                        .show(player)
                        .addToBackStack(null)
                        .commit();
                activity.setActive(player);
            });
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

    public void dataChanged(List<String> allPlayers1) {
        allPlayers.clear();
        allPlayers.addAll(allPlayers1);
        notifyDataSetChanged();
    }

}
