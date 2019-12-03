package baikal.web.footballapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import baikal.web.footballapp.model.Person;

public class PlayersPageViewModel extends ViewModel {
    public final LiveData<PagedList<Person>> playersList;

    public PlayersPageViewModel(LiveData<PagedList<Person>> playersList) {
        playersList = new LivePagedListBuilder<>(asdf, 50).build();
    }
//    public LiveData<List<Person>> getPlayers() {
//        if (playersList == null) {
//            playersList = new MutableLiveData<>();
//        }
//
//        return playersList;
//    }
}
