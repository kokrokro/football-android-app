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
import baikal.web.footballapp.user.activity.ProtocolEdit;
import baikal.web.footballapp.user.activity.ProtocolMatchScore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class RVScoreHalfAdapter1 extends RecyclerView.Adapter<RVScoreHalfAdapter1.ViewHolder>{
    private final ProtocolMatchScore context;
    Logger log = LoggerFactory.getLogger(ProtocolEdit.class);
    private final HashMap<Integer, String> halves;
    private final TeamTitleClubLogoMatchEvents playerEvents;
    public RVScoreHalfAdapter1(Activity context, HashMap<Integer, String> halves,
                              TeamTitleClubLogoMatchEvents playerEvents, Match match){
        this.context = (ProtocolMatchScore) context;
        this.halves = halves;
        this.playerEvents = playerEvents;
    }
    @NonNull
    @Override
    public RVScoreHalfAdapter1.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.score, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVScoreHalfAdapter1.ViewHolder holder, int position) {
        String half = halves.get(position);
        holder.textHalf.setText(half);
        String str;
        str = playerEvents.getNameTeam1();
        holder.textTitle1.setText(str);
        str = playerEvents.getNameTeam2();
        holder.textTitle2.setText(str);
//        try{
//            str = match.getScore();
//            if (str.equals("")){
//                str = "-";
//            }
//        }catch (Exception e){
//            str = "-";
//        }
        int count1=0;
        int count2=0;

        for (PlayerEvent playerEvent : playerEvents.getPlayerEvents()){
            if (playerEvent.getNameTeam().equals(playerEvents.getNameTeam1())
                    && playerEvent.getEvent().getEventType().equals("goal")
                    && playerEvent.getEvent().getTime().equals(half))
            {
                count1++;
            }
            if (playerEvent.getNameTeam().equals(playerEvents.getNameTeam2())
                    && playerEvent.getEvent().getEventType().equals("goal")
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
        View line;
        ViewHolder(View item) {
            super(item);
            textHalf = item.findViewById(R.id.scoreHalf);
            image1 = item.findViewById(R.id.scoreHalfCommand1Logo);
            image2 = item.findViewById(R.id.scoreHalfCommand2Logo);
            textTitle1 = item.findViewById(R.id.scoreHalfCommand1Title);
            textTitle2 = item.findViewById(R.id.scoreHalfCommand2Title);
            textScore = item.findViewById(R.id.protocolMatchScoreHalf);
        }
    }

}
