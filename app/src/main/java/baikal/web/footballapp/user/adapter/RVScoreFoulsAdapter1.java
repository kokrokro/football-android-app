package baikal.web.footballapp.user.adapter;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import baikal.web.footballapp.R;
import baikal.web.footballapp.SetImage;
import baikal.web.footballapp.model.Match;
import baikal.web.footballapp.model.PlayerEvent;
import baikal.web.footballapp.model.TeamTitleClubLogoMatchEvents;
import baikal.web.footballapp.user.activity.ProtocolMatchScore;

import java.util.HashMap;

public class RVScoreFoulsAdapter1 extends RecyclerView.Adapter<RVScoreFoulsAdapter1.ViewHolder>{
    private final ProtocolMatchScore context;
    private final HashMap<Integer, String> halves;
    private final TeamTitleClubLogoMatchEvents playerEvents;
    private final Match match;
    public RVScoreFoulsAdapter1(Activity context, HashMap<Integer, String> halves,
                               TeamTitleClubLogoMatchEvents playerEvents, Match match){
        this.context = (ProtocolMatchScore) context;
        this.halves = halves;
        this.match = match;
        this.playerEvents = playerEvents;
    }
    @NonNull
    @Override
    public RVScoreFoulsAdapter1.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fouls, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVScoreFoulsAdapter1.ViewHolder holder, int position) {
        String half = halves.get(position);
        holder.textHalf.setText(half);
        String str;
        str = playerEvents.getNameTeam1();
        holder.textTitle1.setText(str);
        str = playerEvents.getNameTeam2();
        holder.textTitle2.setText(str);

        int count1=0;
        int count2=0;

        for (PlayerEvent playerEvent : playerEvents.getPlayerEvents()){
            if (playerEvent.getNameTeam().equals(playerEvents.getNameTeam1())
                    && playerEvent.getEvent().getEventType().equals("foul")
                    && playerEvent.getEvent().getTime().equals(half))
            {
                count1++;
            }
            if (playerEvent.getNameTeam().equals(playerEvents.getNameTeam2())
                    && playerEvent.getEvent().getEventType().equals("foul")
                    && playerEvent.getEvent().getTime().equals(half))
            {
                count2++;
            }
        }

        str = count1 + ":" + count2;

        holder.textScore.setText(str);

        SetImage setImage = new SetImage();
        setImage.setImage(context, holder.image1, playerEvents.getClubLogo1());
        setImage.setImage(context, holder.image2, playerEvents.getClubLogo2());
    }

    @Override
    public int getItemCount() {
        return halves.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textHalf;
        final TextView textTitle1;
        final TextView textTitle2;
        final TextView textScore;
        final ImageView image1;
        final ImageView image2;
        ViewHolder(View item) {
            super(item);
            textHalf = item.findViewById(R.id.foulsHalf);
            image1 = item.findViewById(R.id.foulsCommand1Logo);
            image2 = item.findViewById(R.id.foulsCommand2Logo);
            textTitle1 = item.findViewById(R.id.foulsCommand1Title);
            textTitle2 = item.findViewById(R.id.foulsCommand2Title);
            textScore = item.findViewById(R.id.foulsCommand1FoulsNum);
        }
    }

}
