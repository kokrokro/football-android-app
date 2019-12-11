package baikal.web.footballapp.repository;

import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.lifecycle.ComputableLiveData;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagedList;

import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.players.datasource.LoadStates;
import baikal.web.footballapp.players.datasource.PagedListWithLoadingState;
import baikal.web.footballapp.players.datasource.PlayersDataSource;

public class PlayersPageRepository {
    private static final PagedList.Config config = new PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(7)
            .build();

    public PagedListWithLoadingState<Person> getPlayersInitial(String searchStr) {
        MutableLiveData<LoadStates> loadStatesLiveData = new MutableLiveData<>(LoadStates.Loading);

        PlayersDataSource playersDataSource = new PlayersDataSource(
                () -> loadStatesLiveData.postValue(LoadStates.Loaded),
                () -> loadStatesLiveData.postValue(LoadStates.Error), searchStr);

        LiveData<PagedList<Person>> pagedListLiveData = new ComputableLiveData<PagedList<Person>>(ArchTaskExecutor.getIOThreadExecutor()) {
            @Override
            protected PagedList<Person> compute() {
                return new PagedList.Builder<>(playersDataSource, config)
                        .setNotifyExecutor(ArchTaskExecutor.getMainThreadExecutor())
                        .setFetchExecutor(ArchTaskExecutor.getIOThreadExecutor())
                        .build();
            }
        }.getLiveData();

        return new PagedListWithLoadingState<>(loadStatesLiveData, pagedListLiveData);
//        return new LivePagedListBuilder<>(dataSourceFactory, config).build();
    }

}
