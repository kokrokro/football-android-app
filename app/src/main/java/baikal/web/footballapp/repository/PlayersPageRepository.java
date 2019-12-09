package baikal.web.footballapp.repository;

import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.players.datasource.PlayersDataSourceFactory;

public class PlayersPageRepository {
    public LiveData<PagedList<Person>> getPlayersInitial() {
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(7)
                .build();

        PlayersDataSourceFactory dataSourceFactory = new PlayersDataSourceFactory(null, null);
        return new LivePagedListBuilder<>(dataSourceFactory, config).build();
    }
}
