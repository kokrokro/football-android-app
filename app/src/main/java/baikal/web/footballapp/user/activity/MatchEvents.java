package baikal.web.footballapp.user.activity;

import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
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
    private FloatingActionButton fab;
    private boolean scrollStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Abbrev dialogFragment;
        NestedScrollView scroller;
        ImageButton buttonBack;
        RecyclerView recyclerView;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match_events);
        dialogFragment = new Abbrev();
        scroller = findViewById(R.id.scrollMatchEvents);
        fab = findViewById(R.id.buttonAbbreviation);
        buttonBack = findViewById(R.id.matchEventsBack);
        recyclerView = findViewById(R.id.recyclerViewMatchEvents);
        buttonBack.setOnClickListener(v -> finish());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        scrollStatus = false;
        fab.setOnClickListener(v -> dialogFragment.show(getSupportFragmentManager(), "abbrevMatch"));
        scroller.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY > oldScrollY) {
                fab.show();
            }
            if (scrollY < oldScrollY) {
                scrollStatus = false;
                fab.show();
            }

            if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                scrollStatus = true;
                fab.hide();
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                //nothing to do
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    fab.hide();
                } else {
                    fab.show();
                }
                if (scrollStatus) {
                    fab.hide();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
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
                adapter = new RVMatchEventsAdapter(this, halves, match.getEvents());

            recyclerView.setAdapter(adapter);
        } catch (NullPointerException e) {
            LinearLayout layout = findViewById(R.id.emptyEvents);
            layout.setVisibility(View.VISIBLE);
            fab.hide();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return super.onTouchEvent(event);
    }
}
