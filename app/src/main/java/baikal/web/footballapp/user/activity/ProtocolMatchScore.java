package baikal.web.footballapp.user.activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;

import baikal.web.footballapp.R;
import baikal.web.footballapp.SetImage;
import baikal.web.footballapp.model.Match;
import baikal.web.footballapp.model.PlayerEvent;
import baikal.web.footballapp.model.TeamTitleClubLogoMatchEvents;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Objects;

public class ProtocolMatchScore extends AppCompatActivity {
    private final Logger log = LoggerFactory.getLogger(ProtocolMatchScore.class);
    private HashMap<Integer, String> halves;
    private final SetImage setImage = new SetImage();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ImageButton buttonBack;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.protocol_match_score);
        halves = new HashMap<>();
        buttonBack = findViewById(R.id.protocolScoreBack);
        buttonBack.setOnClickListener(v -> finish());
//        recyclerViewFouls = findViewById(R.id.recyclerViewScoreFouls);

//        recyclerViewFouls.setLayoutManager(new LinearLayoutManager(this));
//        recyclerViewScore = findViewById(R.id.recyclerViewScore);
//        recyclerViewScore.setLayoutManager(new LinearLayoutManager(this));
        try{

            Intent arguments = getIntent();
            TeamTitleClubLogoMatchEvents playerEvents = (TeamTitleClubLogoMatchEvents) Objects.requireNonNull(arguments.getExtras()).getSerializable("PROTOCOLEVENTS");
//            List<PlayerEvent> playerEvents = (List<PlayerEvent>) Objects.requireNonNull(arguments.getExtras()).getSerializable("PROTOCOLEVENTS");
            Match match = (Match) arguments.getExtras().getSerializable("PROTOCOLMATCH");
            int i = 0;
            if (playerEvents.getPlayerEvents()!=null) {
                for (PlayerEvent playerEvent : playerEvents.getPlayerEvents()) {
                    if (!halves.containsValue(playerEvent.getEvent().getTime())) {
                        halves.put(i, playerEvent.getEvent().getTime());
                        i++;
                    }
                }
            }

//            recyclerViewScore.setAdapter(adapter1);

            boolean containsFouls = false;
            try {
                for (PlayerEvent playerEv1 : playerEvents.getPlayerEvents()){
                    if (playerEv1.getEvent().getEventType().equals("foul")){
                        containsFouls = true;
                        break;
                    }
                }
            }catch (NullPointerException e){
                containsFouls = false;
            }



            if (containsFouls){
//                recyclerViewFouls.setAdapter(adapter);
            }else {
//                LinearLayout textFouls = findViewById(R.id.protocolScoreFouls);
//                textFouls.setVisibility(View.GONE);
            }


            setScore(playerEvents, match);

            if (playerEvents.getPlayerEvents()==null){
//                TextView textScore = findViewById(R.id.protocolScore);
//                textScore.setVisibility(View.GONE);
//                View line = findViewById(R.id.protocolScoreLine);
//                line.setVisibility(View.GONE);
//                LinearLayout textFouls = findViewById(R.id.protocolScoreFouls);
//                textFouls.setVisibility(View.GONE);
            }
            if (match.getPenalty() == null ){
//            if (match.getPenalty() == null && !match.getPenalty().equals("")){
//                LinearLayout layout = findViewById(R.id.penalty);
//                layout.setVisibility(View.GONE);
            }else {
//                setPenalty(playerEvents.getPlayerEvents(), match);
                setPenalty(playerEvents, match);
            }
            if (match.getAutoGoal() == null ){
//            if (match.getAutoGoal() == null && !match.getAutoGoal().equals("")){
//                LinearLayout layout = findViewById(R.id.protocolScoreOwnGoals);
//                layout.setVisibility(View.GONE);
            }else {
                setAutoGoal(playerEvents, match);
            }
//            recyclerView.setAdapter(adapter);
        }catch (Exception e){
            log.error("ERROR: ", e);
        }
    }

//    private  void setPenalty(List<PlayerEvent> playerEvents, Match match){
    private  void setScore(TeamTitleClubLogoMatchEvents playerEvents, Match match){
//        ImageView image1 = findViewById(R.id.scoreCommand1Logo);
//        ImageView image2 = findViewById(R.id.scoreCommand2Logo);
//        TextView text1 = findViewById(R.id.scoreCommand1Title);
//        TextView text2 = findViewById(R.id.scoreCommand2Title);
//        TextView score = findViewById(R.id.protocolEditMatchScore);
        String str;
        str = playerEvents.getNameTeam1();
//        text1.setText(str);
        str = playerEvents.getNameTeam2();
//        text2.setText(str);
        try {
            str = match.getScore();
            if (str.equals("")) {
                str = "-";
            }
        } catch (Exception e) {
            str = "-";
        }
        int count1=0;
        int count2=0;

        if (!str.equals("-")){
//            String temp[] = str.split(":");
//            count1 = Integer.parseInt(temp[0]);
//            count2 = Integer.parseInt(temp[1]);
            count1 = 0;
            count2 = 0;
        }
        try {
            for (PlayerEvent playerEvent : playerEvents.getPlayerEvents()){
                if (playerEvent.getNameTeam().equals(playerEvents.getNameTeam1())
                        && playerEvent.getEvent().getEventType().equals("goal"))
                {
                    count1++;
                }
                if (playerEvent.getNameTeam().equals(playerEvents.getNameTeam2())
                        && playerEvent.getEvent().getEventType().equals("goal"))
                {
                    count2++;
                }
            }
        }catch (NullPointerException e){}


        str = count1 + ":" + count2;
//        score.setText(str);
//        setImage.setImage(this, image1, playerEvents.getClubLogo1());
//        setImage.setImage(this, image2, playerEvents.getClubLogo2());


    }
    private  void setPenalty(TeamTitleClubLogoMatchEvents playerEvents, Match match){
//        ImageView image1 = findViewById(R.id.penaltyCommand1Logo);
//        ImageView image2 = findViewById(R.id.penaltyCommand2Logo);
//        TextView text1 = findViewById(R.id.penaltyCommand1Title);
//        TextView text2 = findViewById(R.id.penaltyCommand2Title);
//        TextView penalty = findViewById(R.id.protocolEditMatchPenalty);
//        penalty.setText(match.getPenalty());
        String str;
        str = playerEvents.getNameTeam1();
//        text1.setText(str);
        str = playerEvents.getNameTeam2();
//        text2.setText(str);
//        setImage.setImage(this, image1, playerEvents.getClubLogo1());
//        setImage.setImage(this, image2, playerEvents.getClubLogo2());
    }
    private  void setAutoGoal(TeamTitleClubLogoMatchEvents playerEvents, Match match){
//        ImageView image1 = findViewById(R.id.ownGoalsFoulsCommand1Logo);
//        ImageView image2 = findViewById(R.id.ownGoalsFoulsCommand2Logo);
//        TextView text1 = findViewById(R.id.ownGoalsFoulsCommand1Title);
//        TextView text2 = findViewById(R.id.ownGoalsFoulsCommand2Title);
//        TextView autoGoal = findViewById(R.id.protocolEditMatchAuthoGoul);
//        autoGoal.setText(match.getAutoGoal());
        String str;
        str = playerEvents.getNameTeam1();
//        text1.setText(str);
        str = playerEvents.getNameTeam2();
//        text2.setText(str);
//        setImage.setImage(this, image1, playerEvents.getClubLogo1());
//        setImage.setImage(this, image2, playerEvents.getClubLogo2());


    }

}
