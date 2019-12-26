package baikal.web.footballapp.tournament.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import baikal.web.footballapp.CheckName;
import baikal.web.footballapp.Controller;
import baikal.web.footballapp.MankindKeeper;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SetImage;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.PersonStats;
import baikal.web.footballapp.model.Player;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.tournament.PlayerComparator;
import baikal.web.footballapp.tournament.adapter.RVTournamentPlayersAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommandStructureFragment extends Fragment {
    private static final String TAG = "CommandStructureFragment";
    Logger log = LoggerFactory.getLogger(PersonalActivity.class);
    private List<PersonStats> personStats = new ArrayList<>();
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
        Person coach = MankindKeeper.getInstance().getPersonById(team.getCreator());

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
        SetImage.setImage(getActivity(), imageCoach, coach.getPhoto());




        List<Player> players = team.getPlayers();
//        players.sort(Comparator.comparing(Player::getDisquals));
        try {
            Collections.sort(players, new PlayerComparator());
        } catch (Exception e) {
            log.error(TAG, e);
        }
        RVTournamentPlayersAdapter adapter = new RVTournamentPlayersAdapter( players, personStats);

        StringBuilder idQuery = new StringBuilder();
        for (Player player : team.getPlayers())
            idQuery.append(",").append(player.getPerson());

        Controller.getApi().getPersonStats("", idQuery.toString(), null).enqueue(new Callback<List<PersonStats>>() {
            @Override
            public void onResponse(@NonNull Call<List<PersonStats>> call, @NonNull Response<List<PersonStats>> response) {
                if(response.isSuccessful() && response.body()!=null) {
                    personStats.clear();
                    personStats.addAll(response.body());
                    Log.d("CommandStructure", personStats.size()+"");
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<PersonStats>> call, @NonNull Throwable t) { }
        });


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
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
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
