package baikal.web.footballapp.repository;

import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PagedList;

import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.players.datasource.DoIt;
import baikal.web.footballapp.players.datasource.LoadStates;
import baikal.web.footballapp.players.datasource.PagedListWithLoadingState;
import baikal.web.footballapp.players.datasource.PlayersItemDataSource;
import baikal.web.footballapp.players.datasource.PlayersPositionalDataSource;

public class PlayersPageRepository {
    private static final PagedList.Config config = new PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(7)
            .build();

    public PagedListWithLoadingState<Person> getPlayers(String searchStr) {
        MutableLiveData<LoadStates> loadStatesLiveData = new MutableLiveData<>(LoadStates.Loading);
        MutableLiveData<PagedList<Person>> pagedListLiveData = new MutableLiveData<>();

        final DoIt errorCallback = () -> loadStatesLiveData.postValue(LoadStates.Error);
        final DoIt goodCallback = () -> loadStatesLiveData.postValue(LoadStates.Loaded);

        if (searchStr == null) {
            PlayersItemDataSource playersItemDataSource = new PlayersItemDataSource(
                    goodCallback,
                    errorCallback);

            executeBuildPagedList(pagedListLiveData, playersItemDataSource);
        } else {
            PlayersPositionalDataSource playersPositionalDataSource = new PlayersPositionalDataSource(
                    goodCallback,
                    errorCallback, searchStr);

            executeBuildPagedList(pagedListLiveData, playersPositionalDataSource);
        }

        return new PagedListWithLoadingState<>(loadStatesLiveData, pagedListLiveData);
    }

    private <K, V> void executeBuildPagedList(MutableLiveData<PagedList<V>> pagedListLive, DataSource<K, V> dataSource) {
        ArchTaskExecutor.getIOThreadExecutor().execute(() ->
                pagedListLive.postValue(
                        new PagedList.Builder<>(dataSource, config)
                                .setNotifyExecutor(ArchTaskExecutor.getMainThreadExecutor())
                                .setFetchExecutor(ArchTaskExecutor.getIOThreadExecutor())
                                .build()));
    }

}
