package baikal.web.footballapp.user.activity;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.model.Match;
import baikal.web.footballapp.model.MatchPopulate;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.Referee;
import baikal.web.footballapp.model.RefereeRequest;
import baikal.web.footballapp.user.adapter.RVMyMatchesAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MyMatches extends Fragment {
    private final Logger log = LoggerFactory.getLogger(MyMatches.class);
    static RVMyMatchesAdapter adapter;
    public static List<MatchPopulate> matches = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        RecyclerView recyclerView;

        view = inflater.inflate(R.layout.user_matches, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewMyMatches);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        LinearLayout layout = view.findViewById(R.id.emptyMatch);
        adapter = new RVMyMatchesAdapter(getActivity(),this, matches);
        recyclerView.setAdapter(adapter);
        Controller.getApi().getMatches(null, PersonalActivity.id).enqueue(new Callback<List<MatchPopulate>>() {
            @Override
            public void onResponse(Call<List<MatchPopulate>> call, Response<List<MatchPopulate>> response) {
                if(response.isSuccessful()){
                    if(response.body()!=null){
                        matches.clear();
                        matches.addAll(response.body());
                        if(matches.size()>0){
                            layout.setVisibility(View.GONE);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<MatchPopulate>> call, Throwable t) {

            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        log.info("INFO: InvitationFragment onDestroy");
        super.onDestroy();
    }
}
