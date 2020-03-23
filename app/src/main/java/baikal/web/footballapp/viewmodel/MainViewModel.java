package baikal.web.footballapp.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.model.Stadium;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.repository.MainRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends ViewModel {

    private MutableLiveData<List<Stadium>> allStadiums = null;
    private TreeMap<String, Stadium> stadiumTreeMap = null;
    private MutableLiveData<List<Team>> allTeams = null;

    public interface OnObjectLoaded {
        void onObjectLoaded (Object o);
    }

    public interface OnTeamLoaded {
        void onTeamLoaded (Team team);
    }

    public Team getTeamById (String id, OnTeamLoaded onTeamLoaded) {
        if(allTeams == null)
            allTeams = new MutableLiveData<>();
        if (allTeams.getValue() == null)
            allTeams.setValue(new ArrayList<>());
        for (Team t: allTeams.getValue())
            if (t.getId().equals(id)) {
                onTeamLoaded.onTeamLoaded(t);
                return t;
            }

        loadTeamById(id, onTeamLoaded);
        return null;
    }

    public LiveData<List<Team>> getTeams(String creatorId){
        if(allTeams==null)
            allTeams = new MutableLiveData<>();

        loadTeams(creatorId);
        return allTeams;
    }

    public LiveData<List<Stadium>> getAllStadiums(){
        if(allStadiums==null){
            allStadiums = new MutableLiveData<>();
        }
        loadStadiums();
        return allStadiums;
    }

    public Stadium getStadiumById (String id, OnObjectLoaded onObjectLoaded) {
        if (id == null || id.equals(""))
            return null;
        if (allStadiums==null) {
            loadStadiums(id, onObjectLoaded);
            return null;
        }
        if (stadiumTreeMap == null) {
            stadiumTreeMap = new TreeMap<>();
            return null;
        }
        if (stadiumTreeMap.get(id)!=null)
            return stadiumTreeMap.get(id);

        loadStadiums(id, onObjectLoaded);
        return null;
    }

    private void loadTeamById(String id, OnTeamLoaded onTeamLoaded) {
        Callback<List<Team>> responseCallback = new Callback<List<Team>>() {
            @Override
            public void onResponse(@NonNull Call<List<Team>> call, @NonNull Response<List<Team>> response) {
                if (response.isSuccessful() &&
                        response.body()!=null &&
                        response.body().size() > 0) {
                    onTeamLoaded.onTeamLoaded(response.body().get(0));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Team>> call, @NonNull Throwable t) { }
        };
        Controller.getApi().getTeamById(id).enqueue(responseCallback);
    }

    private void loadTeams(String creatorId) {
        new MainRepository().getTeams(creatorId, new Callback<List<Team>>() {
            @Override
            public void onResponse(@NonNull Call<List<Team>> call, @NonNull Response<List<Team>> response) {
                if (response.isSuccessful())
                    if (response.body() != null )
                        allTeams.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<Team>> call, @NonNull Throwable t) { }
        });
    }

    private void loadStadiums() {
        new MainRepository().getStadiums(null, null, new Callback<List<Stadium>>() {
            @Override
            public void onResponse(@NonNull Call<List<Stadium>> call, @NonNull Response<List<Stadium>> response) {
                if (response.isSuccessful())
                    if (response.body() != null ) {
                        if (stadiumTreeMap == null)
                            stadiumTreeMap = new TreeMap<>();
                        allStadiums.setValue(response.body());
                        for (Stadium s: response.body())
                            stadiumTreeMap.put(s.get_id(), s);
                    }
            }

            @Override
            public void onFailure(@NonNull Call<List<Stadium>> call, @NonNull Throwable t) { }
        });
    }

    private void loadStadiums(String id, OnObjectLoaded onObjectLoaded) {
        new MainRepository().getStadiums(null, id, new Callback<List<Stadium>>() {
            @Override
            public void onResponse(@NonNull Call<List<Stadium>> call, @NonNull Response<List<Stadium>> response) {
                if (response.isSuccessful() && response.body()!=null && response.body().size() > 0) {
                    if (stadiumTreeMap == null)
                        stadiumTreeMap = new TreeMap<>();

                    for (Stadium s: response.body())
                        stadiumTreeMap.put(s.get_id(), s);
                    onObjectLoaded.onObjectLoaded(id);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Stadium>> call, @NonNull Throwable t) { }
        });
    }
}
