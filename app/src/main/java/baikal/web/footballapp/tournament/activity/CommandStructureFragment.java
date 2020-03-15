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
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SetImage;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.PersonStats;
import baikal.web.footballapp.model.Player;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.tournament.adapter.RVTournamentPlayersAdapter;
import baikal.web.footballapp.viewmodel.PersonViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommandStructureFragment extends Fragment {
    private static final String TAG = "CommandStructureFragment";
    Logger log = LoggerFactory.getLogger(PersonalActivity.class);
    private List<PersonStats> personStats = new ArrayList<>();
    private PersonViewModel personViewModel;
    private TextView textCouch;
    private ImageView imageCoach;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    private Team team;
    private RVTournamentPlayersAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view;
        team = (Team) getArguments().getSerializable("TEAMSTRUCTURE");
        view = inflater.inflate(R.layout.command_info_structure, container, false);
//        fab = (FloatingActionButton) view.findViewById(R.id.commandInfoButton);
        CommandInfoActivity commandInfoActivity = (CommandInfoActivity) getActivity();
        fab = commandInfoActivity.getFab();
        textCouch = view.findViewById(R.id.commandTrainer);
        recyclerView = view.findViewById(R.id.recyclerViewCommandStructure);
        imageCoach = view.findViewById(R.id.commandTrainerPhoto);

        personViewModel = ViewModelProviders.of(getActivity()).get(PersonViewModel.class);

        Person coach = personViewModel.getPersonById(team.getTrainer(), (p)->{
            textCouch.setText(p.getSurnameNameLastName());
            SetImage.setImage(getActivity(), imageCoach, p.getPhoto());
        });

        textCouch.setText(coach==null?"":coach.getSurnameNameLastName());
        SetImage.setImage(getActivity(), imageCoach, coach==null?"":coach.getPhoto());

        adapter = new RVTournamentPlayersAdapter(team.getPlayers(), personStats, personViewModel);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

        loadData();
        return view;
    }

    private void loadData() {
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
    }
}
