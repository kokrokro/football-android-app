package baikal.web.footballapp.user.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import baikal.web.footballapp.R;


public class ProtocolPenalty extends AppCompatActivity {
    private static final String TAG = "ProtocolPenalty";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate ...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.protocol_penalty_series);
        ImageButton buttonBack = findViewById(R.id.PPS_protocolPenaltyBack);
        buttonBack.setOnClickListener(v -> finish());
    }
}
