package baikal.web.footballapp.user.activity;

import android.content.Intent;
import com.google.android.material.appbar.AppBarLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Club;
import baikal.web.footballapp.model.Event;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.Match;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.Player;
import baikal.web.footballapp.model.PlayerEvent;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.user.adapter.RVProtocolEditAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProtocolEventsEdit extends AppCompatActivity {
    private final Logger log = LoggerFactory.getLogger(ProtocolEventsEdit.class);
    private List<Integer> items;
    private NestedScrollView scroller;
    private CoordinatorLayout coordinatorLayout;
    private AppBarLayout appBarLayout;
    private AppBarLayout.Behavior behavior;
    private final int REQUEST_CODE_ADDEVENT = 246;
    private RVProtocolEditAdapter adapter;
    private List<Event> events;
    private List<PlayerEvent> playerEvents;
    private Match match;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.protocol_events_edit);
        items = new ArrayList<>();
        items.add(1);
        FloatingActionButton buttonAddEvent;
        ImageButton imageClose;
        ImageButton imageSave;
        final RecyclerView recyclerView;
        coordinatorLayout = findViewById(R.id.editProtocolCoordinatorLayout);
        appBarLayout = findViewById(R.id.editProtocolAppbar);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        behavior = (AppBarLayout.Behavior) params.getBehavior();
        imageClose = findViewById(R.id.editProtocolEventsClose);
        imageSave = findViewById(R.id.editProtocolEventsSave);
        buttonAddEvent = findViewById(R.id.editProtocolAddEvent);
        scroller = findViewById(R.id.scrollProtocolEdit);
        imageClose.setOnClickListener(v -> finish());
        recyclerView = findViewById(R.id.recyclerViewEditProtocol);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        try {
            Intent arguments = getIntent();
            playerEvents = (List<PlayerEvent>) Objects.requireNonNull(arguments.getExtras()).getSerializable("PROTOCOLEVENTS");
            final ArrayList<String> countPlayers = Objects.requireNonNull(arguments.getExtras()).getStringArrayList("PROTOCOLTEAMMATCH");
            match = (Match) Objects.requireNonNull(arguments.getExtras()).getSerializable("PROTOCOLMATCH");
            events = new ArrayList<>(match.getEvents());
            if (playerEvents.size() != 0) {
                LinearLayout layout = findViewById(R.id.emptyEvent);
                layout.setVisibility(View.GONE);
            }

            adapter = new RVProtocolEditAdapter(this, playerEvents, position -> {
                if (playerEvents.size() == 1) {
                    LinearLayout layout = findViewById(R.id.emptyEvent);
                    layout.setVisibility(View.VISIBLE);
                }
                playerEvents.remove(position);
                List<PlayerEvent> list = new ArrayList<>(playerEvents);
                adapter.dataChanged(list);
            });
            recyclerView.setAdapter(adapter);
            buttonAddEvent.setOnClickListener(v -> {
                if (countPlayers.size() == 0) {
                    Toast.makeText(ProtocolEventsEdit.this, "Выберите игроков обеих команд!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(ProtocolEventsEdit.this, AddEvent.class);
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("PROTOCOLCOUNTPLAYERS", countPlayers);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, REQUEST_CODE_ADDEVENT);
                }

            });
            imageSave.setOnClickListener(v -> {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("EDITEVENTRESULT", (Serializable) playerEvents);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);finish(); //post
            });
        } catch (Exception e) {
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_ADDEVENT) {
                Event event = (Event) data.getExtras().getSerializable("ADDEVENT");
                Person person = (Person) data.getExtras().getSerializable("ADDEVENTPERSON");
                PlayerEvent playerEvent = new PlayerEvent();
                Team team1 = null;
                Club club = null;
                for (League league : PersonalActivity.tournaments) {
                    if (team1 == null && league.getId().equals(match.getLeague())) {
                        for (Team team : league.getTeams()) {
                            if (team.getId().equals(match.getTeamTwo())
                                    || team.getId().equals(match.getTeamOne())) {
                                for (Player player : team.getPlayers()) {
                                    if (player.getPlayerId().equals(person.getId())) {
                                        team1 = team;
                                        for (Club club1 : PersonalActivity.allClubs) {
                                            if (club1.getId().equals(team.getClub())) {
                                                club = club1;
                                            }
                                        }
                                    }
                                }

                            }
                        }
                    }
                }
                playerEvent.setNameTeam(team1.getName());
                try {
                    playerEvent.setClubLogo(club.getLogo());
                } catch (NullPointerException e) {
                    playerEvent.setClubLogo(null);
                }
                playerEvent.setPerson(person);
                playerEvent.setEvent(event);
//                List<PlayerEvent> playerEventList = new ArrayList<>(playerEvents);
                playerEvents.add(playerEvent);
                List<PlayerEvent> playerEventList = new ArrayList<>();
                playerEventList.addAll(playerEvents);
                if (playerEventList.size() == 1) {
//                    finish();
//                    startActivity(getIntent());
                    LinearLayout layout = findViewById(R.id.emptyEvent);
                    layout.setVisibility(View.GONE);
                }

                adapter.dataChanged(playerEventList);

            }
        } else {
            log.error("ERROR: onActivityResult");
        }
    }
}
