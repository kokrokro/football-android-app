package baikal.web.footballapp.user.adapter;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import baikal.web.footballapp.CheckName;
import baikal.web.footballapp.DateToString;
import baikal.web.footballapp.MankindKeeper;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SetImage;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.players.activity.PlayersPage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TrainerAdapter extends RecyclerView.Adapter<TrainerAdapter.ViewHolder> {
    private final Logger log = LoggerFactory.getLogger(PlayersPage.class);
    private final List<String> allPlayers;
    private final Context context;
    private final MyListener myListener;

    public TrainerAdapter(Activity activity, List<String> allPlayers, MyListener myListener) {
        this.allPlayers = new ArrayList<>();
        this.allPlayers.addAll(allPlayers);
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
            Person currentPlayer = MankindKeeper.getInstance().allPlayers.get(allPlayers.get(position));
            String DOB = currentPlayer != null ? currentPlayer.getBirthdate() : "error";
            DateToString dateToString = new DateToString();
            holder.textDOB.setText(dateToString.ChangeDate(DOB));
            String str;
            CheckName checkName = new CheckName();
            str = checkName.check(Objects.requireNonNull(currentPlayer).getSurname(), currentPlayer.getName(), currentPlayer.getLastname());
            holder.textName.setText(str);
            SetImage setImage = new SetImage();
            setImage.setImage(context, holder.imageLogo, currentPlayer.getPhoto());

            holder.buttonShow2.setOnClickListener(v -> myListener.onClick(currentPlayer.getId()));
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

    public void dataChanged(List<String> allPlayers1){
        allPlayers.clear();
        allPlayers.addAll(allPlayers1);
        notifyDataSetChanged();
    }
}
