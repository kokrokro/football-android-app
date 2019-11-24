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

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.Match;
import baikal.web.footballapp.model.MatchPopulate;
import baikal.web.footballapp.user.adapter.RVTimeTableAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimeTableFragment extends Fragment {
    private static final String TAG = "TimeTableFragment: ";
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
            log.error(TAG, e);
        }

        return view;
    }

    @SuppressLint("CheckResult")
    private void getActiveMatches(String limit, String offset) {
        Controller.getApi().getMainRefsLeagues(PersonalActivity.id).enqueue(new Callback<List<League>>() {
            @Override
            public void onResponse(@NonNull Call<List<League>> call, @NonNull Response<List<League>> response) {
                if(response.isSuccessful())
                    if(response.body()!=null)
                        saveData(response.body(), limit, offset);
            }

            @Override
            public void onFailure(@NonNull Call<List<League>> call, @NonNull Throwable t) {
                log.error(t.getMessage());
                layout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void saveData(List<League> leagueList, String limit, String offset) {
        matches.clear();
        StringBuilder str= new StringBuilder();
        for(League l: leagueList)
           for (Match m : l.getMatches())
               if(!m.getPlayed())
                   str.append(",").append(m.getId());

        Controller.getApi().getMatches(str.toString()).enqueue(new Callback<List<MatchPopulate>>() {
            @Override
            public void onResponse(@NonNull Call<List<MatchPopulate>> call, @NonNull Response<List<MatchPopulate>> response) {
                if(response.isSuccessful())
                    if(response.body()!=null){
                        matches.addAll(response.body());
                        adapter.notifyDataSetChanged();
                        if (matches.size()==0)
                            layout.setVisibility(View.VISIBLE);
                    }
            }

            @Override
            public void onFailure(@NonNull Call<List<MatchPopulate>> call, @NonNull Throwable t) {
                Log.e(TAG, t.getMessage());
            }
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
