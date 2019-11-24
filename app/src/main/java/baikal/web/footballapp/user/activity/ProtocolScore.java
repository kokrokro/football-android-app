package baikal.web.footballapp.user.activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
    private static final String TAG = "ProtocolScore: ";
    private final Logger log = LoggerFactory.getLogger(ProtocolMatchScore.class);
    private HashMap<Integer, String> halves;
    private SetImage setImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()...");
        RecyclerView recyclerViewFouls;
        RecyclerView recyclerViewScore;
        ImageButton buttonBack;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.protocol_match_score);
        halves = new HashMap<>();
        buttonBack = findViewById(R.id.protocolScoreBack);
        buttonBack.setOnClickListener(v -> finish());
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


        }catch (Exception e){
            log.error("ERROR: ", e);
        }
    }
}
