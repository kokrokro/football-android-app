package baikal.web.footballapp.user.activity;

import android.content.Intent;
import android.os.Bundle;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.model.MatchPopulate;
import baikal.web.footballapp.user.activity.Protocol.ConfirmProtocol;
import baikal.web.footballapp.user.adapter.RVMyMatchesAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyMatches extends Fragment {
    private final Logger log = LoggerFactory.getLogger(MyMatches.class);
    private RVMyMatchesAdapter adapter;
    private List<MatchPopulate> matches = new ArrayList<>();
    private LinearLayout layout;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_matches, container, false);
        swipeRefreshLayout = view.findViewById(R.id.UM_swipe_to_refresh);
        recyclerView = view.findViewById(R.id.recyclerViewMyMatches);
        layout = view.findViewById(R.id.emptyMatch);

        adapter = new RVMyMatchesAdapter(matches, this::showMatch);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setVisibility(View.INVISIBLE);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(this::loadData);
        loadData();
        return view;
    }

    private void loadData ()
    {
        Controller.getApi().getMatches(null,SaveSharedPreference.getObject().getUser().get_id() ).enqueue(new Callback<List<MatchPopulate>>() {
            @Override
            public void onResponse(@NonNull Call<List<MatchPopulate>> call, @NonNull Response<List<MatchPopulate>> response) {
                if(response.isSuccessful()){
                    if(response.body()!=null){
                        if(response.body().size()>0){
                            layout.setVisibility(View.GONE);
                        }
                        matches.clear();
                        matches.addAll(response.body());
                        recyclerView.setVisibility(View.VISIBLE);
                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<MatchPopulate>> call, @NonNull Throwable t) { swipeRefreshLayout.setRefreshing(false); }
        });
    }

    private void showMatch(MatchPopulate match, String status) {
        Intent intent = new Intent(getContext(), ConfirmProtocol.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("CONFIRMPROTOCOL", match);
        bundle.putString("STATUS", status);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        log.info("INFO: InvitationFragment onDestroy");
        super.onDestroy();
    }
}
