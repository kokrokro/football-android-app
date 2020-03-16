package baikal.web.footballapp.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import baikal.web.footballapp.DataSourceUtilities.LoadStates;
import baikal.web.footballapp.DataSourceUtilities.PagedListWithLoadingState;
import baikal.web.footballapp.model.Tourney;
import baikal.web.footballapp.repository.TourneyPageRepository;

public class SearchTournamentPageViewModel extends ViewModel {
    private final String TAG = "PlayersPageViewModel";

    private final TourneyPageRepository tourneyPageRepository;
    private final MutableLiveData<PagedList<Tourney>> tourneys;
    private PagedListWithLoadingState<Tourney> tourneysList;
    private MutableLiveData<LoadStates> loadDataState;

    public SearchTournamentPageViewModel() {
        tourneyPageRepository = new TourneyPageRepository();
        tourneys = new MutableLiveData<>();
        loadDataState = new MutableLiveData<>(LoadStates.Loading);

        initPlayersPageList(null, null);
    }

    private void initPlayersPageList(String searchStr, String region) {
        tourneysList = tourneyPageRepository.getTourneys(searchStr, region);
        tourneysList.loadState.observeForever(new Observer<LoadStates>() {
            @Override
            public void onChanged(LoadStates loadState) {
                loadDataState.setValue(loadState);
                if (loadState.compareTo(LoadStates.Loading) != 0) {
                    tourneysList.loadState.removeObserver(this);
                }
                if (loadState.compareTo(LoadStates.Loaded) == 0) {
                    tourneys.setValue(tourneysList.list.getValue());
                }
            }
        });
    }

    private void searchPlayers(String searchString, String region) {
        initPlayersPageList(searchString, region);
    }

    public LiveData<PagedList<Tourney>> getTourneys() {
        return tourneys;
    }

    public LiveData<LoadStates> getLoadDataState() {
        return loadDataState;
    }

    public void clearSearchAndReload() {
        initPlayersPageList(null, null);
    }

//    public LiveData<Boolean>

    public void onQueryTextChange(String txt, String region) {
        Log.d(TAG, "onQueryTextChange: " + region + " " + txt);
    }

    public void onQueryTextSubmit(String txt, String region) {
        Log.d(TAG, "onQueryTextSubmit: " + txt);
        searchPlayers(txt, region);
    }
}
