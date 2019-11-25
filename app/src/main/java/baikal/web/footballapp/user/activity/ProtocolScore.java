package baikal.web.footballapp.user.activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import android.util.Log;
import android.widget.ImageButton;

import baikal.web.footballapp.R;
import baikal.web.footballapp.SetImage;
import baikal.web.footballapp.model.MatchPopulate;
import baikal.web.footballapp.model.PlayerEvent;
import baikal.web.footballapp.model.TeamTitleClubLogoMatchEvents;
import baikal.web.footballapp.protocol.CustomProtocolInput.EventAdder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Objects;

public class ProtocolScore extends AppCompatActivity{
    private static final String TAG = "ProtocolScore: ";
    private final Logger log = LoggerFactory.getLogger(ProtocolMatchScore.class);

    EventAdder eventAdder = new EventAdder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()...");
        ImageButton buttonBack;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.protocol_match_score);
        buttonBack = findViewById(R.id.protocolScoreBack);
        buttonBack.setOnClickListener(v -> finish());

        try{
            getSupportFragmentManager().beginTransaction().add(R.id.eventAdderFragment, eventAdder, "EventAdder").show(eventAdder);

            Intent arguments = getIntent();
            TeamTitleClubLogoMatchEvents playerEvents = (TeamTitleClubLogoMatchEvents) Objects.requireNonNull(arguments.getExtras()).getSerializable("PROTOCOLEVENTS");
            MatchPopulate match = (MatchPopulate) arguments.getExtras().getSerializable("PROTOCOLMATCH");
            int i = 0;
            if (playerEvents.getPlayerEvents()!=null) {
                for (PlayerEvent playerEvent : playerEvents.getPlayerEvents()) {

                }
            }


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
