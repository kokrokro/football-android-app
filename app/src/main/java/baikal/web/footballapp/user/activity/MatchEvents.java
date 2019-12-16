package baikal.web.footballapp.user.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import baikal.web.footballapp.R;
import baikal.web.footballapp.model.MatchPopulate;
import baikal.web.footballapp.user.adapter.RVMatchEventsAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MatchEvents extends AppCompatActivity {
    Logger log = LoggerFactory.getLogger(MatchEvents.class);
    private boolean isEditable = false;
    MatchPopulate match;
    RecyclerView recyclerView;
    LinearLayout emptyEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent().getExtras() != null) {
            isEditable = getIntent().getExtras().getBoolean("IS_EDITABLE", false);
            match = (MatchPopulate) getIntent().getExtras().getSerializable("MATCH");
        }
        ImageButton buttonBack;
        RecyclerView recyclerView;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match_events);
        buttonBack = findViewById(R.id.matchEventsBack);
        recyclerView = findViewById(R.id.recyclerViewMatchEvents);
        emptyEvents = findViewById(R.id.emptyEvents);

        buttonBack.setOnClickListener(v -> finish());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        try {
            if (match != null) {
                RVMatchEventsAdapter adapter = new RVMatchEventsAdapter(this, match.getEvents());
                recyclerView.setAdapter(adapter);
            }
        } catch (NullPointerException e) {
            emptyEvents.setVisibility(View.VISIBLE);
        }
    }
}
