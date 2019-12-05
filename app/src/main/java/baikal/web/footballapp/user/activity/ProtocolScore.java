package baikal.web.footballapp.user.activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import android.util.Log;
import android.widget.ImageButton;

import baikal.web.footballapp.R;
import baikal.web.footballapp.protocol.Adapters.RVCommandEventListAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProtocolScore extends AppCompatActivity{
    private static final String TAG = "ProtocolScore: ";
    private final Logger log = LoggerFactory.getLogger(ProtocolMatchScore.class);
    private RecyclerView firstTeamListRecyclerView;
    private RecyclerView secondTeamListRecyclerView;
    private RVCommandEventListAdapter adapter1;
    private RVCommandEventListAdapter adapter2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()...");
        ImageButton buttonBack;
        setContentView(R.layout.protocol_match_score);
        buttonBack = findViewById(R.id.protocolScoreBack);
        buttonBack.setOnClickListener(v -> finish());

        try{
            Intent arguments = getIntent();


        }catch (Exception e){
            log.error("ERROR: ", e);
        }
    }
}
