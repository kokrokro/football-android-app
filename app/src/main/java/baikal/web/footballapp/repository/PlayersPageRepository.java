package baikal.web.footballapp.repository;

import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PagedList;

import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.DataSourceUtilities.DoIt;
import baikal.web.footballapp.DataSourceUtilities.LoadStates;
import baikal.web.footballapp.DataSourceUtilities.PagedListWithLoadingState;
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
        final DoIt emptyCallback = () -> loadStatesLiveData.postValue(LoadStates.Empty);

        if (searchStr == null) {
            PlayersItemDataSource playersItemDataSource = new PlayersItemDataSource(
                    goodCallback,
                    errorCallback,
                    emptyCallback);

            executeBuildPagedList(pagedListLiveData, playersItemDataSource);
        } else {
            PlayersPositionalDataSource playersPositionalDataSource = new PlayersPositionalDataSource(
                    goodCallback,
                    errorCallback, emptyCallback, searchStr);

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
