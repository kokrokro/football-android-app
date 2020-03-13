package baikal.web.footballapp.user.activity.UserTeams;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.model.Player;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.tournament.activity.CommandInfoActivity;
import baikal.web.footballapp.user.activity.DialogTeam;
import baikal.web.footballapp.user.activity.UserTeams.Adapters.RVUserCommandAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersTeamListFragment extends Fragment {
    private RVUserCommandAdapter adapter;
    private final List<Team> teams = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout emptyLayout;
    private String status;

    UsersTeamListFragment (String status) {
        this.status = status;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_comands_tr_pa_cr_lists, container, false);

        emptyLayout = view.findViewById(R.id.UCTPCL_emptyCommand);
        RecyclerView recyclerView = view.findViewById(R.id.UCTPCL_team_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        swipeRefreshLayout = view.findViewById(R.id.UCTPCL_swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this::loadData);

        adapter = new RVUserCommandAdapter(teams, status, (team, league) -> {
            if (status.equals("creator") || status.equals("train")) {
                DialogTeam dialogRegion = new DialogTeam(i -> {
                    if (i == 0) {
                        Intent intent = new Intent(getActivity(), CommandInfoActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("TOURNAMENTMATCHCOMMANDINFO", team);
                        intent.putExtras(bundle);
                        getActivity().startActivity(intent);
                    } else {
                        Intent intent = new Intent(getActivity(), UserCommandInfoEdit.class);
                        Bundle bundle = new Bundle();
                        Bundle bundle1 = new Bundle();
                        bundle.putSerializable("COMMANDEDIT", team);
                        bundle1.putSerializable("COMMANDEDITLEAGUE", league);
                        intent.putExtras(bundle);
                        intent.putExtras(bundle1);
                        Objects.requireNonNull(getActivity()).startActivity(intent);
                    }
                });
                dialogRegion.show(getChildFragmentManager(), "fragment_edit_name");
            }
            else {
                Intent intent = new Intent(getActivity(), CommandInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("TOURNAMENTMATCHCOMMANDINFO", team);
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
            }
        });

        recyclerView.setAdapter(adapter);
        adapter.setData(teams);

        loadData ();
        return view;
    }

    private void loadData ()
    {
        swipeRefreshLayout.setRefreshing(false);
        if (status.equals("creator")) {
            Log.d("UTLF_creator", "loading response 1");
            String id = SaveSharedPreference.getObject().getUser().getId();
            Controller.getApi().getTeams(id).enqueue(new Callback<List<Team>>() {
                @Override
                public void onResponse(@NonNull Call<List<Team>> call, @NonNull Response<List<Team>> response) {
                    Log.d("UTLF_creator", "loaded response 2");
                    if (response.isSuccessful() && response.body() != null) {
                        Log.d("UTLF_creator", "loaded response 3");
                        teams.clear();
                        teams.addAll(response.body());
                        adapter.setData(teams);
                        if (teams.size() > 0)
                            emptyLayout.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Team>> call, @NonNull Throwable t) { }
            });
        }

        if (status.equals("part")) {
            String id = SaveSharedPreference.getObject().getUser().getId();
            Controller.getApi().getAllTeams().enqueue(new Callback<List<Team>>() {
                @Override
                public void onResponse(@NonNull Call<List<Team>> call, @NonNull Response<List<Team>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        teams.clear();
                        List<Team> allTeams = response.body();
                        for (Team team: allTeams)
                            for (Player p: team.getPlayers())
                                if (p.getPerson().equals(id)) {
                                    teams.add(team);
                                    break;
                                }
                        if (teams.size() > 0)
                            emptyLayout.setVisibility(View.GONE);
                        Log.d("UTListFragment_part", "team size = " + teams.size());
                        adapter.setData(teams);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Team>> call, @NonNull Throwable t) { }
            });
        }

        if (status.equals("train")) {
            String id = SaveSharedPreference.getObject().getUser().getId();
            Controller.getApi().getTeamsByTrainer(id).enqueue(new Callback<List<Team>>() {
                @Override
                public void onResponse(@NonNull Call<List<Team>> call, @NonNull Response<List<Team>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        teams.clear();
                        teams.addAll(response.body());
                        if (teams.size() > 0)
                            emptyLayout.setVisibility(View.GONE);
                        Log.d("UTListFragment_train", "team size = " + teams.size());
                        adapter.setData(teams);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Team>> call, @NonNull Throwable t) { }
            });
        }
    }
}
