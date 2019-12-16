package baikal.web.footballapp.user.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import baikal.web.footballapp.R;


public class ProtocolPenalty extends AppCompatActivity {
    private static final String TAG = "ProtocolPenalty";

    TextView teamName1;
    TextView teamName2;
    TextView scoreTeam1;
    TextView scoreTeam2;
    RecyclerView recyclerViewTeam1;
    RecyclerView recyclerViewTeam2;
    Button setTeamOrderBtn1;
    Button setTeamOrderBtn2;
    ImageButton penaltySuccessBtn;
    ImageButton penaltyFailureBtn;

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
    }
}
