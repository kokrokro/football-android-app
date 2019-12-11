package baikal.web.footballapp.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.players.datasource.LoadStates;
import baikal.web.footballapp.players.datasource.PagedListWithLoadingState;
import baikal.web.footballapp.repository.PlayersPageRepository;

public class PlayersPageViewModel extends ViewModel {
    private final String TAG = "PlayersPageViewModel";

    private final PlayersPageRepository playersPageRepository;
    public PagedListWithLoadingState<Person> playersList;
    private MutableLiveData<LoadStates> loadDataState;

    public PlayersPageViewModel() {
        playersPageRepository = new PlayersPageRepository();
        loadDataState = new MutableLiveData<>(LoadStates.Loading);

        initPlayersPageList(null);
    }

    public void initPlayersPageList(String searchStr) {
        playersList = playersPageRepository.getPlayersInitial(searchStr);
        playersList.loadState.observeForever(new Observer<LoadStates>() {
            @Override
            public void onChanged(LoadStates loadState) {
                loadDataState.setValue(loadState);
                if (loadState.compareTo(LoadStates.Loading) != 0) {
                    playersList.loadState.removeObserver(this);
                }
            }
        });
    }

    void searchPlayers(String searchString) {
        initPlayersPageList(searchString);
    }

    public LiveData<LoadStates> getLoadDataState() {
        return loadDataState;
    }

//    public LiveData<Boolean>

    public void onQueryTextChange(String txt) {
        Log.d(TAG, "onQueryTextChange: " + txt);
    }

    public void onQueryTextSubmit(String txt) {
        Log.d(TAG, "onQueryTextSubmit: " + txt);

        if (txt.equalsIgnoreCase("")) {
            return;
        }
        searchPlayers(txt);
    }
}
