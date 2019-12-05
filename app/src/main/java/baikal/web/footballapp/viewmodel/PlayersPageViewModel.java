package baikal.web.footballapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.players.datasource.PlayersDataSourceFactory;

public class PlayersPageViewModel extends ViewModel {
    public LiveData<PagedList<Person>> playersList;

    public PlayersPageViewModel() {
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(7)
                .build();
        PlayersDataSourceFactory dataSourceFactory = new PlayersDataSourceFactory();
        playersList = new LivePagedListBuilder<>(dataSourceFactory, config).build();
    }
}
