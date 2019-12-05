package baikal.web.footballapp.home.activity;

import android.annotation.SuppressLint;
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

import baikal.web.footballapp.CheckError;
import baikal.web.footballapp.Controller;
import baikal.web.footballapp.R;
import baikal.web.footballapp.home.adapter.RVComingMatchesAdapter;
import baikal.web.footballapp.model.ActiveMatch;
import baikal.web.footballapp.model.ActiveMatches;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.Match;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComingMatches extends Fragment{
    private final Logger log = LoggerFactory.getLogger(ComingMatches.class);
    private LinearLayout layout;
    private RVComingMatchesAdapter adapter;
    private List<ActiveMatch> matches = new ArrayList<>();
    private List<String> leagues;
    public ComingMatches( List<String> favLeagues){
        this.leagues = favLeagues;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view;
        RecyclerView recyclerView;
        view = inflater.inflate(R.layout.coming_matches, container, false);
        layout = view.findViewById(R.id.emptyComingMatches);
        recyclerView = view.findViewById(R.id.recyclerViewComingMatches);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        matches = new ArrayList<>();
        //checkConnection();
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", getResources().getConfiguration().locale);
        String strDate =">="+sdf.format(now);
        String query = "";
        Log.d("UpcomingMatches", "leagues "+leagues.toString());
        for(String l : leagues){
            query+=","+l;
        }
        Log.d("UpcomingMatches", "query "+query);
        Controller.getApi().getUpcomingMatches(strDate, query, "20").enqueue(new Callback<List<ActiveMatch>>(){
            @Override
            public void onResponse(Call<List<ActiveMatch>> call, Response<List<ActiveMatch>> response) {
                if(response.isSuccessful()){
                    if(response.body()!=null){
                        Log.d("UpcomingMatches","msg1");
                        matches.clear();
                        matches.addAll(response.body());
                        adapter.notifyDataSetChanged();
                        Log.d("UpcomingMatches","matches1 "+matches.size());
                    }
                    else {
                        Log.d("UpcomingMatches", "error1");
                    }
                }
            }
            @Override
            public void onFailure(Call<List<ActiveMatch>> call, Throwable t) {
                Log.d("UpcomingMatches", "error2");
            }
        });
        try {

            Log.d("UpcomingMatches","matches2 "+matches.size());
            adapter = new RVComingMatchesAdapter(getActivity(), matches);
            recyclerView.setAdapter(adapter);
        }catch (NullPointerException e){
            layout.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @SuppressLint("CheckResult")
    private void checkConnection() {
        ReactiveNetwork
                .observeNetworkConnectivity(getActivity().getApplicationContext())
                .flatMapSingle(connectivity -> ReactiveNetwork.checkInternetConnectivity())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isConnected -> {
                    // isConnected can be true or false
                    if (isConnected){
                        getActiveMatches();
                    }
                });
    }
    @SuppressLint("CheckResult")
    private void getActiveMatches() {
        Controller.getApi().getComingMatches()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::saveData
                        ,
                        error -> {
                            layout.setVisibility(View.VISIBLE);
                            CheckError checkError = new CheckError();
                            checkError.checkError(getActivity(), error);
                        }
                );
    }
    private void saveData(ActiveMatches matches1) {
        try {
            List<ActiveMatch> result;
            result = matches1.getMatches();
            if (result.size() != 0) {
                layout.setVisibility(View.GONE);
                adapter.dataChanged(result);
                matches.clear();
                matches.addAll(result);
            } else {
                layout.setVisibility(View.VISIBLE);
            }
        }catch (Exception e){
            log.error("ERROR: ", e);
        }
    }

}
