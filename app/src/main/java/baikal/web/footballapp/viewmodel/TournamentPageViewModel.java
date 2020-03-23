package baikal.web.footballapp.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.DataSourceUtilities.LoadStates;
import baikal.web.footballapp.DataSourceUtilities.PagedListWithLoadingState;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.Tourney;
import baikal.web.footballapp.repository.TourneyPageRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TournamentPageViewModel extends ViewModel {
    private final String TAG = "TournamentPageViewModel";

    private final TourneyPageRepository tourneyPageRepository;
    private final MutableLiveData<PagedList<Tourney>> tourneys;
    public PagedListWithLoadingState<Tourney> tourneysList;
    private MutableLiveData<LoadStates> loadDataState;

    private final MutableLiveData<PagedList<Tourney>> favTourneys;
    public PagedListWithLoadingState<Tourney> favTourneysList;
    private MutableLiveData<LoadStates> favLoadDataState;
    private MutableLiveData<TreeMap<String, List<League>>> favLeagues;
    private MutableLiveData<List<String>> favTourneyIds;

    public TournamentPageViewModel() {
        tourneyPageRepository = new TourneyPageRepository();
        tourneys    = new MutableLiveData<>();
        favTourneys = new MutableLiveData<>();
        favLeagues  = new MutableLiveData<>();
        favTourneyIds = new MutableLiveData<>();

        loadDataState = new MutableLiveData<>(LoadStates.Loading);
        favLoadDataState = new MutableLiveData<>(LoadStates.Loading);

        if (SaveSharedPreference.getObject() != null)
            setFavTourneysIds(SaveSharedPreference.getObject().getUser().getFavouriteTourney());

        initPageListData(null, null);
        initFavTourneyPageListData();
    }

    private void initFavTourneyPageListData() {
        StringBuilder favTourneysIds = new StringBuilder();

        if (SaveSharedPreference.getObject() != null)
            for (String t: SaveSharedPreference.getObject().getUser().getFavouriteTourney())
                favTourneysIds.append(",").append(t);
        else
            favTourneysIds = null;

        favTourneysList = tourneyPageRepository.getTourneys(null, null, favTourneysIds==null?null:favTourneysIds.toString());

        favTourneysList.loadState.observeForever(new Observer<LoadStates>() {
            @Override
            public void onChanged(LoadStates loadState) {
                favLoadDataState.setValue(loadState);
                if (loadState.compareTo(LoadStates.Loading) != 0)
                    favTourneysList.loadState.removeObserver(this);
                if (loadState.compareTo(LoadStates.Loaded) == 0)
                    favTourneys.setValue(favTourneysList.list.getValue());
            }
        });
    }

    private void initPageListData(String searchStr, String region) {
        tourneysList = tourneyPageRepository.getTourneys(searchStr, region, null);

        tourneysList.loadState.observeForever(new Observer<LoadStates>() {
            @Override
            public void onChanged(LoadStates loadState) {
                loadDataState.setValue(loadState);
                if (loadState.compareTo(LoadStates.Loading) != 0)
                    tourneysList.loadState.removeObserver(this);
                if (loadState.compareTo(LoadStates.Loaded) == 0)
                    tourneys.setValue(tourneysList.list.getValue());
            }
        });
    }

    private void searchData (String searchString, String region) {
        initPageListData(searchString, region);
    }

    public LiveData<PagedList<Tourney>> getTourneys() {
        return tourneys;
    }

    public LiveData<PagedList<Tourney>> getFavTourneys() {
        return favTourneys;
    }

    private void loadLeaguesByTourney(String id) {
        Callback<List<League>> responseCallback = new Callback<List<League>>() {
            @Override
            public void onResponse(@NonNull Call<List<League>> call, @NonNull Response<List<League>> response) {
                if (response.isSuccessful() && response.body()!=null) {
                    TreeMap<String, List<League>> tree = favLeagues.getValue();
                    for (League l: response.body()) {
                        if (tree.get(l.getTourney()) == null)
                            tree.put(l.getTourney(), new ArrayList<>());
                        tree.get(l.getTourney()).add(l);
                    }
                    favLeagues.postValue(tree);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<League>> call, @NonNull Throwable t) { }
        };

        Controller.getApi().getLeaguesByTourney(id).enqueue(responseCallback);
    }

    public LiveData<TreeMap<String, List<League>>> getFavLeagues (String id) {
        if (favLeagues == null)
            favLeagues  = new MutableLiveData<>();
        if (favLeagues.getValue() == null)
            favLeagues.setValue(new TreeMap<>());
        if (favLeagues.getValue().get(id) == null)
            loadLeaguesByTourney(id);
        return favLeagues;
    }

    public LiveData<LoadStates> getLoadDataState() {
        return loadDataState;
    }

    public LiveData<LoadStates> getFavLoadDataState() {
        return favLoadDataState;
    }

    public void setFavTourneysIds(List<String> newFavTourneys) {
        favTourneyIds.postValue(newFavTourneys);
        initFavTourneyPageListData();
    }

    public void reloadFav() {
        initFavTourneyPageListData();
    }

    public void clearSearchAndReload() {
        initPageListData(null, null);
    }

    public void onQueryTextChange(String txt, String region) {
        Log.d(TAG, "onQueryTextChange: " + region + " " + txt);
    }

    public void onQueryTextSubmit(String txt, String region) {
        Log.d(TAG, "onQueryTextSubmit: " + txt);
        searchData(txt, region);
    }
}
