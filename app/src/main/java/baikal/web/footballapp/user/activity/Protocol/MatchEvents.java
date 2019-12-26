package baikal.web.footballapp.user.activity.Protocol;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Event;
import baikal.web.footballapp.model.EventList;
import baikal.web.footballapp.model.MatchPopulate;
import baikal.web.footballapp.user.activity.Protocol.Adapters.RVMatchEventsAdapter;


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
                EventList newEvents = new EventList();
                for (int i=eventsToDeleteList.size()-1; i>=0; i--) {
                    Event e = match.getEvents().get(eventsToDeleteList.get(i));
                    Event newE = new Event();

                    newE.setEventType("disable");
                    newE.setEvent(e.getId());
                    newEvents.addEvent(newE);
                }

                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("EVENTS", newEvents);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            });
        }

        buttonBack.setOnClickListener(v -> finish());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        try {
            if (match != null) {
                RVMatchEventsAdapter adapter = new RVMatchEventsAdapter(this, match.getEvents(), match.getTeamOne(), match.getTeamTwo(), isEditable, eventsToDelete);
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
