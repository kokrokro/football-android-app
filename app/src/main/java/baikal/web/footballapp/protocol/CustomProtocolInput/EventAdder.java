package baikal.web.footballapp.protocol.CustomProtocolInput;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Player;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.protocol.CustomProtocolInput.adapters.GVPlayerNumberButtonsAdapter;
import baikal.web.footballapp.protocol.CustomProtocolInput.adapters.RVInputEventAdapter;

public class EventAdder extends Fragment {
    private final Logger log = LoggerFactory.getLogger(EventAdder.class);
    private static final String TAG = "EventAdder: ";

    private TextView inputProtocol;
    private RecyclerView protocolEditEvents;
    private Button sendNewEventButton;
    private Button editProtocolCommand1;
    private Button editProtocolCommand2;
    private GridView playerNumberButtons;

    private Team team1;
    private Team team2;

    ArrayList<Player> teamPlayers;

    GVPlayerNumberButtonsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_protocol_animated_fragment, container, false);

        inputProtocol = view.findViewById(R.id.inputProtocol);
        protocolEditEvents = view.findViewById(R.id.protocolEditEvents);
        sendNewEventButton = view.findViewById(R.id.sendNewEventButton);
        editProtocolCommand1 = view.findViewById(R.id.editProtocolCommand1);
        editProtocolCommand2 = view.findViewById(R.id.editProtocolCommand2);
        playerNumberButtons = view.findViewById(R.id.playerNumberButtons);

        ArrayList <InputEventData> inputEventData = new ArrayList<>();
        inputEventData.add(new InputEventData("", R.drawable.ic_goal));
        inputEventData.add(new InputEventData("", R.drawable.ic_red_card));
        inputEventData.add(new InputEventData("", R.drawable.ic_yellow_card));
        inputEventData.add(new InputEventData("", R.drawable.ic_faul));
        inputEventData.add(new InputEventData("", R.drawable.ic_penalty));

        RVInputEventAdapter inputAdapter = new RVInputEventAdapter(inputEventData);
        protocolEditEvents.setAdapter(inputAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        protocolEditEvents.setLayoutManager(manager);
        inputAdapter.notifyDataSetChanged();

        try {
            adapter = new GVPlayerNumberButtonsAdapter(teamPlayers, getContext(), getLayoutInflater());
            playerNumberButtons.setAdapter(adapter);
            playerNumberButtons.setNumColumns(5);
//            teamPlayers.addAll(team1.getPlayers());
            adapter.notifyDataSetChanged();

            initCommandButton(editProtocolCommand1, team1);
            initCommandButton(editProtocolCommand2, team2);
        } catch (Exception e) {
            log.error(TAG, e);
        }
        return view;
    }

    private void initCommandButton (Button btn, Team team) {
        btn.setOnClickListener(v -> {
            try {
                teamPlayers.clear();
                teamPlayers.addAll(team.getPlayers());
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                log.error(TAG, e);
            }
        });
    }
}
