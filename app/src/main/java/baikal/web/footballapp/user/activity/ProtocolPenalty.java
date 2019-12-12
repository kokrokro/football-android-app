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
        setContentView(R.layout.protocol_match_score);
        ImageButton buttonBack = findViewById(R.id.protocolScoreBack);
        buttonBack.setOnClickListener(v -> finish());
    }
}
