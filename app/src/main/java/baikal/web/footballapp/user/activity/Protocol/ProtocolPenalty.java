package baikal.web.footballapp.user.activity.Protocol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Event;
import baikal.web.footballapp.model.MatchPopulate;


public class ProtocolPenalty extends AppCompatActivity {
    private static final String TAG = "ProtocolPenalty";

    private TextView teamName1;
    private TextView teamName2;
    private TextView scoreTeam1;
    private TextView scoreTeam2;
    private RecyclerView recyclerViewTeam1;
    private RecyclerView recyclerViewTeam2;
    private Button setTeamOrderBtn1;
    private Button setTeamOrderBtn2;
    private ImageButton penaltySuccessBtn;
    private ImageButton penaltyFailureBtn;

    private MatchPopulate match;

    int order=0;

    private int[] bgBtnColor;

    private final String[] teamIds = {null, null};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate ...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.protocol_penalty_series);
        ImageButton buttonBack = findViewById(R.id.PPS_protocolPenaltyBack);
        buttonBack.setOnClickListener(v -> finish());

        teamName1 = findViewById(R.id.PPS_team_name1);
        teamName2 = findViewById(R.id.PPS_team_name2);
        scoreTeam1 = findViewById(R.id.PPS_team_score1);
        scoreTeam2 = findViewById(R.id.PPS_team_score2);
        recyclerViewTeam1 = findViewById(R.id.PPS_RV_team1);
        recyclerViewTeam2 = findViewById(R.id.PPS_RV_team2);
        setTeamOrderBtn1 = findViewById(R.id.PPS_set_penalty_order_btn1);
        setTeamOrderBtn2 = findViewById(R.id.PPS_set_penalty_order_btn2);
        penaltySuccessBtn = findViewById(R.id.PPS_penalty_successful);
        penaltyFailureBtn = findViewById(R.id.PPS_penalty_failure);

        bgBtnColor = new int[]{getResources().getColor(R.color.blue), getResources().getColor(R.color.red)};

        match = (MatchPopulate) getIntent().getExtras().getSerializable("MATCH");
        teamIds[0] = match.getTeamOne().getId();
        teamIds[1] = match.getTeamTwo().getId();

        setTeamOrderBtn1.setOnClickListener(v->changeOrder(2));
        setTeamOrderBtn2.setOnClickListener(v->changeOrder(1));

        penaltySuccessBtn.setOnClickListener(v->{
            addEvent();
            changeOrder(order++);
        });
    }

    private void changeOrder (int order) {
        this.order = order;

        penaltySuccessBtn.setBackgroundColor(bgBtnColor[(this.order&1)]);
        penaltyFailureBtn.setBackgroundColor(bgBtnColor[(this.order&1)]);
    }

    private void addEvent()
    {
        Event event = new Event();
    }
}
