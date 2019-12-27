package baikal.web.footballapp.home.activity;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.R;
import baikal.web.footballapp.home.adapter.RVComingMatchesAdapter;
import baikal.web.footballapp.model.ActiveMatch;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComingMatches extends Fragment {
    private final Logger log = LoggerFactory.getLogger(ComingMatches.class);
    private LinearLayout layout;
    private RVComingMatchesAdapter adapter;
    private List<ActiveMatch> matches = new ArrayList<>();
    private List<String> leagues;

    ComingMatches(List<String> favLeagues){
        this.leagues = favLeagues;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view;
        RecyclerView recyclerView;
        view = inflater.inflate(R.layout.coming_matches, container, false);
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.CM_swipe_to_refresh_layout);
        layout = view.findViewById(R.id.emptyComingMatches);
        recyclerView = view.findViewById(R.id.recyclerViewComingMatches);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        matches = new ArrayList<>();

        layout.setVisibility(View.VISIBLE);

        swipeRefreshLayout.setOnRefreshListener(()->{
            swipeRefreshLayout.setRefreshing(false);
            loadData();
        });

        //checkConnection();
        loadData();

        try {
            adapter = new RVComingMatchesAdapter(getActivity(), matches);
            recyclerView.setAdapter(adapter);
        }catch (NullPointerException e){
            layout.setVisibility(View.VISIBLE);
        }
        return view;
    }

    private void loadData () {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", getResources().getConfiguration().locale);
        String strDate =">="+sdf.format(now);
        StringBuilder query = new StringBuilder();
        for(String l : leagues)
            query.append(",").append(l);

        Controller.getApi().getUpcomingMatches(strDate, query.toString(), "35").enqueue(new Callback<List<ActiveMatch>>() {
            @Override
            public void onResponse(@NonNull Call<List<ActiveMatch>> call, @NonNull Response<List<ActiveMatch>> response) {
                if(response.isSuccessful()){
                    if(response.body()!=null){
                        matches.clear();
                        matches.addAll(response.body());
                        adapter.notifyDataSetChanged();
                        layout.setVisibility(View.GONE);
                        if (matches.size() == 0)
                            layout.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ActiveMatch>> call, @NonNull Throwable t) { }
        });
    }
}
