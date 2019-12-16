package baikal.web.footballapp.user.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MatchEvents extends AppCompatActivity {
    Logger log = LoggerFactory.getLogger(MatchEvents.class);
    private boolean isEditable = false;
    MatchPopulate match;
    RecyclerView recyclerView;
    LinearLayout emptyEvents;
    Set<Integer> eventsToDelete;
    ImageButton saveEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        eventsToDelete = new HashSet<>();
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
        saveEvents = findViewById(R.id.ME_save_events);

        if (isEditable) {
            saveEvents.setVisibility(View.VISIBLE);
            saveEvents.setOnClickListener(v -> {
                List<Integer> eventsToDeleteList = new ArrayList<>(eventsToDelete);

                for (int i=eventsToDeleteList.size()-1; i>=0; i--)
                    match.getEvents().remove(eventsToDeleteList.get(i).intValue());

                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("MATCH", match);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            });
        }

        buttonBack.setOnClickListener(v -> finish());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        try {
            if (match != null) {
                RVMatchEventsAdapter adapter = new RVMatchEventsAdapter(this, match.getEvents(), match.getTeamOne().getId(), match.getTeamTwo().getId(), isEditable, eventsToDelete);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                if (match.getEvents() == null || match.getEvents().size() == 0)
                    emptyEvents.setVisibility(View.VISIBLE);
            }
        } catch (NullPointerException e) {
            emptyEvents.setVisibility(View.VISIBLE);
        }
    }
}
