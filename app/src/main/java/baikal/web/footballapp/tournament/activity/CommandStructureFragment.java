package baikal.web.footballapp.tournament.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;


import baikal.web.footballapp.CheckName;
import baikal.web.footballapp.MankindKeeper;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SetImage;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.Player;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.tournament.PlayerComparator;
import baikal.web.footballapp.tournament.adapter.RVCommandStructureAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

public class CommandStructureFragment extends Fragment {
    Logger log = LoggerFactory.getLogger(PersonalActivity.class);

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view;
        TextView textCouch;
        ImageView imageCoach;
        final FloatingActionButton fab;
        RecyclerView recyclerView;
        Bundle arguments = getArguments();
        Team team = (Team) arguments.getSerializable("TEAMSTRUCTURE");
        view = inflater.inflate(R.layout.command_info_structure, container, false);
//        fab = (FloatingActionButton) view.findViewById(R.id.commandInfoButton);
        CommandInfoActivity commandInfoActivity = (CommandInfoActivity) getActivity();
        fab = commandInfoActivity.getFab();
        textCouch = view.findViewById(R.id.commandTrainer);
        recyclerView = view.findViewById(R.id.recyclerViewCommandStructure);
        imageCoach = view.findViewById(R.id.commandTrainerPhoto);
        Person coach = MankindKeeper.getInstance().allPlayers.get(team.getCreator());

        SetImage setImage = new SetImage();
        String str;
        CheckName checkName = new CheckName();
        if (coach==null){
            coach = new Person();
            coach.setName("Удален");
            coach.setLastname("");
            coach.setSurname("");
        }
        str = checkName.check(coach.getSurname(), coach.getName(), coach.getLastname());
        textCouch.setText(str);
        setImage.setImage(getActivity(), imageCoach, coach.getPhoto());




        List<Player> players = team.getPlayers();
//        players.sort(Comparator.comparing(Player::getDisquals));
        Collections.sort(players, new PlayerComparator());
        RVCommandStructureAdapter adapter = new RVCommandStructureAdapter(players);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            //            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
//                if (dy > 0 ||dy<0 && fab.isShown())
//                    fab.hide();
//                log.info("INFO: SCROLL=================");
//            }
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    fab.show();
                } else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    // Do something
                    fab.hide();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        return view;
    }
}
