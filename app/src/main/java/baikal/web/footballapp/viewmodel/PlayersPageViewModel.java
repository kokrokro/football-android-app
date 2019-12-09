package baikal.web.footballapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.players.datasource.PlayersDataSourceFactory;
import baikal.web.footballapp.repository.PlayersPageRepository;

public class PlayersPageViewModel extends ViewModel {
    public LiveData<PagedList<Person>> playersList;
    private MutableLiveData<Boolean> progressBarVisible;
    private final PlayersPageRepository playersPageRepository;

    public PlayersPageViewModel() {
        this.playersPageRepository = new PlayersPageRepository();
        initPlayersPageList();

        progressBarVisible = new MutableLiveData<>(true);
    }

    public void initPlayersPageList() {
        playersList = playersPageRepository.getPlayersInitial();
    }

    public void onQueryTextChange(String txt) {}
}
