package baikal.web.footballapp.home.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.R;
import baikal.web.footballapp.home.adapter.RVComingMatchesAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import baikal.web.footballapp.model.MatchPopulate;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComingMatches extends Fragment{
    private final Logger log = LoggerFactory.getLogger(ComingMatches.class);
    private RVComingMatchesAdapter adapter;
    private List<MatchPopulate> matches = new ArrayList<>();
    private List<String> leagues;
    ComingMatches(List<String> favLeagues){
        this.leagues = favLeagues;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view;
        RecyclerView recyclerView;
        view = inflater.inflate(R.layout.coming_matches, container, false);
        LinearLayout layout = view.findViewById(R.id.emptyComingMatches);
        recyclerView = view.findViewById(R.id.recyclerViewComingMatches);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        matches = new ArrayList<>();
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", getResources().getConfiguration().locale);
        String strDate =">="+sdf.format(now);
        String query = "";
        for(String l : leagues){
            query+=","+l;
        }
        Controller.getApi().getUpcomingMatches(strDate, query, "20").enqueue(new Callback<List<MatchPopulate>>(){
            @Override
            public void onResponse(Call<List<MatchPopulate>> call, Response<List<MatchPopulate>> response) {
                if(response.isSuccessful()){
                    if(response.body()!=null){
                        matches.clear();
                        matches.addAll(response.body());
                        adapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onFailure(Call<List<MatchPopulate>> call, Throwable t) {
            }
        });
        try {
            adapter = new RVComingMatchesAdapter(getActivity(), matches);
            recyclerView.setAdapter(adapter);
            layout.setVisibility(View.GONE);
        }catch (NullPointerException e){
            layout.setVisibility(View.VISIBLE);
        }

        return view;
    }
}
