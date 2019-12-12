package baikal.web.footballapp.user.activity;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Event;
import baikal.web.footballapp.model.MatchPopulate;
import baikal.web.footballapp.user.adapter.RVMatchEventsAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Objects;

public class MatchEvents extends AppCompatActivity {
    Logger log = LoggerFactory.getLogger(MatchEvents.class);
    private boolean isEditable = false;
    MatchPopulate match;
    RecyclerView recyclerView;


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
        buttonBack.setOnClickListener(v -> finish());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        try {
            Intent arguments = getIntent();
            MatchPopulate match = (MatchPopulate) Objects.requireNonNull(getIntent().getExtras()).getSerializable("MATCH");
            HashMap<Integer, String> halves = new HashMap<>();
            int i = 0;
            if (match != null && match.getEvents() != null) {
                for (Event playerEvent : match.getEvents()) {
                    if (!halves.containsValue(playerEvent.getTime())) {
                        halves.put(i, playerEvent.getTime());
                        i++;
                    }
                }
            }
//            Collections.sort(halves, new HalvesComparator());
            RVMatchEventsAdapter adapter = null;
            if (match != null)
                adapter = new RVMatchEventsAdapter(halves, match.getEvents());

            recyclerView.setAdapter(adapter);
        } catch (NullPointerException e) {
            LinearLayout layout = findViewById(R.id.emptyEvents);
            layout.setVisibility(View.VISIBLE);
        }
    }

}
