package baikal.web.footballapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.players.datasource.LoadStates;
import baikal.web.footballapp.players.datasource.PagedListWithLoadingState;
import baikal.web.footballapp.repository.PlayersPageRepository;

public class PlayersPageViewModel extends ViewModel {
    public PagedListWithLoadingState<Person> playersList;
    private MutableLiveData<Boolean> progressBarVisible;
    private final PlayersPageRepository playersPageRepository;

    public PlayersPageViewModel() {
        this.playersPageRepository = new PlayersPageRepository();
        initPlayersPageList();

        progressBarVisible = new MutableLiveData<>(true);
    }

    public void initPlayersPageList() {
        playersList = playersPageRepository.getPlayersInitial();
        playersList.loadState.observeForever(new Observer<LoadStates>() {
            @Override
            public void onChanged(LoadStates loadStates) {
                switch (loadStates) {
                    case Loaded:
                        progressBarVisible.setValue(false);
                        playersList.loadState.removeObserver(this);
                }
            }
        });
    }

    public LiveData<Boolean> getProgressBarVisible() {
        return progressBarVisible;
    }

    public void onQueryTextChange(String txt) {}
}
