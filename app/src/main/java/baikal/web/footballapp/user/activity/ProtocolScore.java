package baikal.web.footballapp.user.activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import baikal.web.footballapp.R;
import baikal.web.footballapp.SetImage;
import baikal.web.footballapp.model.ActiveMatch;
import baikal.web.footballapp.model.Match;
import baikal.web.footballapp.model.MatchPopulate;
import baikal.web.footballapp.model.PlayerEvent;
import baikal.web.footballapp.model.TeamTitleClubLogoMatchEvents;
import baikal.web.footballapp.user.adapter.RVScoreFoulsAdapter;
import baikal.web.footballapp.user.adapter.RVScoreHalfAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Objects;

public class ProtocolScore extends AppCompatActivity{
    private final Logger log = LoggerFactory.getLogger(ProtocolMatchScore.class);
    private HashMap<Integer, String> halves;
    private SetImage setImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        RecyclerView recyclerViewFouls;
        RecyclerView recyclerViewScore;
        ImageButton buttonBack;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.protocol_match_score);
        halves = new HashMap<>();
        buttonBack = findViewById(R.id.protocolScoreBack);
        buttonBack.setOnClickListener(v -> finish());
        recyclerViewFouls = findViewById(R.id.recyclerViewScoreFouls);

        recyclerViewFouls.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewScore = findViewById(R.id.recyclerViewScore);
        recyclerViewScore.setLayoutManager(new LinearLayoutManager(this));
        try{

            setImage = new SetImage();
            Intent arguments = getIntent();
            TeamTitleClubLogoMatchEvents playerEvents = (TeamTitleClubLogoMatchEvents) Objects.requireNonNull(arguments.getExtras()).getSerializable("PROTOCOLEVENTS");
//            ActiveMatch match = (ActiveMatch) arguments.getExtras().getSerializable("PROTOCOLMATCH");
            MatchPopulate match = (MatchPopulate) arguments.getExtras().getSerializable("PROTOCOLMATCH");
            int i = 0;
            if (playerEvents.getPlayerEvents()!=null) {
                for (PlayerEvent playerEvent : playerEvents.getPlayerEvents()) {
                    if (!halves.containsValue(playerEvent.getEvent().getTime())) {
                        halves.put(i, playerEvent.getEvent().getTime());
                        i++;
                    }
                }
            }

            RVScoreHalfAdapter adapter1 = new RVScoreHalfAdapter(this, halves, playerEvents);
            recyclerViewScore.setAdapter(adapter1);

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
                RVScoreFoulsAdapter adapter = new RVScoreFoulsAdapter(this, halves, playerEvents);
                recyclerViewFouls.setAdapter(adapter);
            }else {
                LinearLayout textFouls = findViewById(R.id.protocolScoreFouls);
                textFouls.setVisibility(View.GONE);
            }


            setScore(playerEvents, match);

            if (playerEvents.getPlayerEvents()==null){
                TextView textScore = findViewById(R.id.protocolScore);
                textScore.setVisibility(View.GONE);
                View line = findViewById(R.id.protocolScoreLine);
                line.setVisibility(View.GONE);
                LinearLayout textFouls = findViewById(R.id.protocolScoreFouls);
                textFouls.setVisibility(View.GONE);
            }
            if (match.getPenalty() == null ){
                LinearLayout layout = findViewById(R.id.penalty);
                layout.setVisibility(View.GONE);
            }else {
                setPenalty(playerEvents, match);
            }
            if (match.getAutoGoal() == null ){
//            if (match.getAutoGoal() == null && !match.getAutoGoal().equals("")){
                LinearLayout layout = findViewById(R.id.protocolScoreOwnGoals);
                layout.setVisibility(View.GONE);
            }else {
                setAutoGoal(playerEvents, match);
            }

        }catch (Exception e){
            log.error("ERROR: ", e);
        }
    }

//        private  void setScore(TeamTitleClubLogoMatchEvents playerEvents, Match match){
    private  void setScore(TeamTitleClubLogoMatchEvents playerEvents, MatchPopulate match){
        ImageView image1 = findViewById(R.id.scoreCommand1Logo);
        ImageView image2 = findViewById(R.id.scoreCommand2Logo);
        TextView text1 = findViewById(R.id.scoreCommand1Title);
        TextView text2 = findViewById(R.id.scoreCommand2Title);
        TextView score = findViewById(R.id.protocolEditMatchScore);
        String str;
        str = playerEvents.getNameTeam1();
        text1.setText(str);
        str = playerEvents.getNameTeam2();
        text2.setText(str);
        try {
            str = match.getScore();
            if (str.equals("")) {
                str = "-";
            }
        } catch (Exception e) {
            str = "-";
        }
        score.setText(str);
        setImage.setImage(image1.getContext(), image1, playerEvents.getClubLogo1());
        setImage.setImage(image2.getContext(),image2, playerEvents.getClubLogo2());


    }
    private  void setPenalty(TeamTitleClubLogoMatchEvents playerEvents, MatchPopulate match){
//    private  void setPenalty(TeamTitleClubLogoMatchEvents playerEvents, Match match){
        ImageView image1 = findViewById(R.id.penaltyCommand1Logo);
        ImageView image2 = findViewById(R.id.penaltyCommand2Logo);
        TextView text1 = findViewById(R.id.penaltyCommand1Title);
        TextView text2 = findViewById(R.id.penaltyCommand2Title);
        TextView penalty = findViewById(R.id.protocolEditMatchPenalty);
        penalty.setText(match.getPenalty());
        String str;
        str = playerEvents.getNameTeam1();
        text1.setText(str);
        str = playerEvents.getNameTeam2();
        text2.setText(str);
        setImage.setImage(image1.getContext(), image1, playerEvents.getClubLogo1());
        setImage.setImage(image2.getContext(), image2, playerEvents.getClubLogo2());
    }
    private  void setAutoGoal(TeamTitleClubLogoMatchEvents playerEvents, MatchPopulate match){
//    private  void setAutoGoal(TeamTitleClubLogoMatchEvents playerEvents, Match match){
        ImageView image1 = findViewById(R.id.ownGoalsFoulsCommand1Logo);
        ImageView image2 = findViewById(R.id.ownGoalsFoulsCommand2Logo);
        TextView text1 = findViewById(R.id.ownGoalsFoulsCommand1Title);
        TextView text2 = findViewById(R.id.ownGoalsFoulsCommand2Title);
        TextView autoGoal = findViewById(R.id.protocolEditMatchAuthoGoul);
        autoGoal.setText(match.getAutoGoal());
        String str;
        str = playerEvents.getNameTeam1();
        text1.setText(str);
        str = playerEvents.getNameTeam2();
        text2.setText(str);
        setImage.setImage(image1.getContext(), image1, playerEvents.getClubLogo1());
        setImage.setImage(image2.getContext(), image2, playerEvents.getClubLogo2());
    }

}
