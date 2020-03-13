package baikal.web.footballapp.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import java.util.List;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.DataSourceUtilities.LoadStates;
import baikal.web.footballapp.DataSourceUtilities.PagedListWithLoadingState;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.model.ActiveMatch;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.repository.ActiveMatchPageRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActiveMatchPageViewModel extends ViewModel {
    private final static String TAG = "ActiveMatchPageViewModel";

    private final ActiveMatchPageRepository activeMatchPageRepository;
    private final MutableLiveData<PagedList<ActiveMatch>> matches;
    public PagedListWithLoadingState<ActiveMatch> matchesList;
    private MutableLiveData<LoadStates> loadDataState;
    private String leagueIds;

    public ActiveMatchPageViewModel () {
        activeMatchPageRepository = new ActiveMatchPageRepository();
        matches = new MutableLiveData<>();
        loadDataState = new MutableLiveData<>(LoadStates.Loading);
        this.leagueIds = "";
        reload();
    }

    private void initNewsPageList(String leagueIds) {
        matchesList = activeMatchPageRepository.getMatches(leagueIds, ">=0");
        matchesList.loadState.observeForever(new Observer<LoadStates>() {
            @Override
            public void onChanged(LoadStates loadState) {
                loadDataState.setValue(loadState);
                if (loadState.compareTo(LoadStates.Loading) != 0)
                    matchesList.loadState.removeObserver(this);
                if (loadState.compareTo(LoadStates.Loaded) == 0)
                    matches.setValue(matchesList.list.getValue());
            }
        });
    }

    public void getNewsByTourneys(String tourneyIds) {
        getLeaguesData(tourneyIds);
    }

    public LiveData<PagedList<ActiveMatch>> getMatches() {
        return matches;
    }

    public LiveData<LoadStates> getLoadDataState() {
        return loadDataState;
    }

    public void reload() {
        Callback<List<Person>> responseCallback = new Callback<List<Person>>() {
            @Override
            public void onResponse(@NonNull Call<List<Person>> call, @NonNull Response<List<Person>> response) {
                if (response.isSuccessful() && response.body().size() > 0) {
                    Person p = response.body().get(0);
                    StringBuilder newTourneyIds = new StringBuilder();
                    for (String t: p.getFavouriteTourney())
                        newTourneyIds.append(",").append(t);

                    getLeaguesData(newTourneyIds.toString());
                    return;
                }
                initNewsPageList(leagueIds);
            }

            @Override
            public void onFailure(@NonNull Call<List<Person>> call, @NonNull Throwable t) { initNewsPageList(leagueIds); }
        };
        if (SaveSharedPreference.getObject() != null)
            Controller.getApi().getFavTourneysId(SaveSharedPreference.getObject().getUser().getId()).enqueue(responseCallback);
        else
            initNewsPageList(leagueIds);
    }

    private void getLeaguesData(String tourneyIds) {
        Callback<List<League>> responseCallback = new Callback<List<League>>() {
            @Override
            public void onResponse(@NonNull Call<List<League>> call, @NonNull Response<List<League>> response) {
                if (response.isSuccessful() && response.body().size() > 0) {
                    StringBuilder leagues = new StringBuilder();
                    for (League l: response.body())
                        leagues.append(",").append(l.getId());
                    leagueIds = leagues.toString();
                    initNewsPageList(leagueIds);
                }
                initNewsPageList(leagueIds);
            }

            @Override
            public void onFailure(@NonNull Call<List<League>> call, @NonNull Throwable t) {
                initNewsPageList(leagueIds);
            }
        };

        Controller.getApi().getLeagueIdsByTourney(tourneyIds).enqueue(responseCallback);
    }
}
