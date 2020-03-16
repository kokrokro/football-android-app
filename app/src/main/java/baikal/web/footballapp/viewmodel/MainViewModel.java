package baikal.web.footballapp.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.PersonPopulate;
import baikal.web.footballapp.model.Stadium;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.model.Tourney;
import baikal.web.footballapp.repository.MainRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends ViewModel {

    private MutableLiveData<List<Tourney>> favTourney = null;
    private MutableLiveData<List<League>> favLeagues= null;
    private MutableLiveData<List<String>> favTourneysId = null;
    private MutableLiveData<List<Stadium>> allStadiums = null;
    private MutableLiveData<List<Team>> allTeams = null;

    public interface OnTeamLoaded {
        void onTeamLoaded (Team team);
    }

    public Team getTeamById (String id, OnTeamLoaded onTeamLoaded) {
        for (Team t: allTeams.getValue())
            if (t.getId().equals(id))
                return t;

        loadTeamById(id, onTeamLoaded);
        return null;
    }

    public LiveData<List<Team>> getTeams(String creatorId){
        if(allTeams==null){
            allTeams = new MutableLiveData<>();
        }
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

    public Stadium getStadiumById (String id) {
        for (Stadium s: allStadiums.getValue())
            if (s.get_id().equals(id))
                return s;

        loadStadiums();
        return new Stadium();
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
                    if (response.body() != null )
                        allStadiums.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<Stadium>> call, @NonNull Throwable t) { }
        });
    }


    public LiveData<List<League>> getFavLeagues(String tourney){
        if(favLeagues==null) {
            favLeagues = new MutableLiveData<>();
        }
        loadFavLeagues(tourney);
        return favLeagues;
    }
    public void setFavTourneysId(List<String> favTourney){
        if(favTourneysId==null){
            favTourneysId = new MutableLiveData<>();
        }
        this.favTourneysId.setValue(favTourney);
    }

    public LiveData<List<Tourney>> getFavTourney(String id){
        if(favTourney == null){
            favTourney = new MutableLiveData<>();
        }
        loadTourneys(id);

        return favTourney;
    }

    private void loadTourneys(String id){
        new MainRepository().getFavTourneys(id, new Callback<List<PersonPopulate>>() {
            @Override
            public void onResponse(@NonNull Call<List<PersonPopulate>> call, @NonNull Response<List<PersonPopulate>> response) {
                if (response.isSuccessful())
                    if (response.body() != null && response.body().size()>0)
                        favTourney.setValue(response.body().get(0).getFavouriteTourney());
            }

            @Override
            public void onFailure(@NonNull Call<List<PersonPopulate>> call, @NonNull Throwable t) { }
        });
    }

    private void loadFavLeagues(String tourney){
        new MainRepository().getLeagues(tourney, new Callback<List<League>>() {
            @Override
            public void onResponse(@NonNull Call<List<League>> call, @NonNull Response<List<League>> response) {
                if(response.isSuccessful())
                    if(response.body()!=null)
                        favLeagues.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<League>> call, @NonNull Throwable t) { }
        });
    }
}
