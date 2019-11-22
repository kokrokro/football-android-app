package baikal.web.footballapp.user.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import baikal.web.footballapp.CheckError;
import baikal.web.footballapp.Controller;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.home.activity.ComingMatches;
import baikal.web.footballapp.model.ActiveMatch;
import baikal.web.footballapp.model.ActiveMatches;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.Match;
import baikal.web.footballapp.model.MatchPopulate;
import baikal.web.footballapp.user.adapter.RVTimeTableAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimeTableFragment extends Fragment {
    private RVTimeTableAdapter adapter;
    private final List<MatchPopulate> matches = new ArrayList<>();
    private NestedScrollView scroller;
    private final Logger log = LoggerFactory.getLogger(TimeTableFragment.class);
    private LinearLayout layout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        RecyclerView recyclerView;
        view = inflater.inflate(R.layout.user_timetable, container, false);
        scroller = view.findViewById(R.id.scrollerTimeTable);
        layout = view.findViewById(R.id.emptyTimetable);
        getActiveMatches("10", "0");
        recyclerView = view.findViewById(R.id.recyclerViewTimeTable);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        try {
            adapter = new RVTimeTableAdapter(getActivity(), this, matches);
            recyclerView.setAdapter(adapter);
        } catch (NullPointerException e) {
            log.error("ERROR: ", e);
        }

        return view;
    }

    @SuppressLint("CheckResult")
    private void getActiveMatches(String limit, String offset) {
        Controller.getApi().getMainRefsLeagues(PersonalActivity.id).enqueue(new Callback<List<League>>() {
            @Override
            public void onResponse(Call<List<League>> call, Response<List<League>> response) {
                if(response.isSuccessful())
                    if(response.body()!=null)
                        saveData(response.body());
            }

            @Override
            public void onFailure(Call<List<League>> call, Throwable t) {
                log.error(t.getMessage());
                layout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void saveData(List<League> leagueList) {
        matches.clear();
        String str="";
        for(League l: leagueList)
           for (Match m : l.getMatches())
               if(!m.getPlayed())
                   str+=","+m.getId();

        Controller.getApi().getMatches(str).enqueue(new Callback<List<MatchPopulate>>() {
            @Override
            public void onResponse(Call<List<MatchPopulate>> call, Response<List<MatchPopulate>> response) {
                if(response.isSuccessful()){
                    if(response.body()!=null){
                        matches.addAll(response.body());
                        adapter.notifyDataSetChanged();
                        if (matches.size()==0){
                            layout.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<MatchPopulate>> call, Throwable t) { }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK)
            return;

        int i = data.getExtras().getInt("MatchIndex", -1);

        if (i==-1)
            return;


    }
}
