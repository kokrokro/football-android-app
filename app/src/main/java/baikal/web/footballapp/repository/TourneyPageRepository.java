package baikal.web.footballapp.repository;

import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PagedList;

import baikal.web.footballapp.DataSourceUtilities.DoIt;
import baikal.web.footballapp.DataSourceUtilities.LoadStates;
import baikal.web.footballapp.DataSourceUtilities.PagedListWithLoadingState;
import baikal.web.footballapp.model.Tourney;
import baikal.web.footballapp.tournament.datasource.TourneyItemDataSource;
import baikal.web.footballapp.tournament.datasource.TourneyPositionalDataSource;

public class TourneyPageRepository {
    private static final PagedList.Config config = new PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(7)
            .build();

    public PagedListWithLoadingState<Tourney> getTourneys (String searchStr, String region, String ids) {
        MutableLiveData<LoadStates> loadStatesLiveData = new MutableLiveData<>(LoadStates.Loading);
        MutableLiveData<PagedList<Tourney>> pagedListLiveData = new MutableLiveData<>();

        final DoIt errorCallback = () -> loadStatesLiveData.postValue(LoadStates.Error);
        final DoIt goodCallback = () -> loadStatesLiveData.postValue(LoadStates.Loaded);
        final DoIt emptyCallback = () -> loadStatesLiveData.postValue(LoadStates.Empty);

        if (searchStr == null && region == null) {
            TourneyItemDataSource tourneyItemDataSource= new TourneyItemDataSource (
                    goodCallback,
                    errorCallback,
                    emptyCallback,
                    ids);

            executeBuildPagedList(pagedListLiveData, tourneyItemDataSource);
        } else {
            TourneyPositionalDataSource tourneyPositionalDataSource = new TourneyPositionalDataSource(
                    goodCallback,
                    errorCallback, emptyCallback, searchStr, region);

            executeBuildPagedList(pagedListLiveData, tourneyPositionalDataSource);
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
