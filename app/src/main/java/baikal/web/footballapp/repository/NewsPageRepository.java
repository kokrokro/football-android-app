package baikal.web.footballapp.repository;

import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PagedList;

import baikal.web.footballapp.DataSourceUtilities.DoIt;
import baikal.web.footballapp.DataSourceUtilities.LoadStates;
import baikal.web.footballapp.DataSourceUtilities.PagedListWithLoadingState;
import baikal.web.footballapp.home.datasource.NewsPositionalDataSource;
import baikal.web.footballapp.model.News_;

public class NewsPageRepository {
    private static final PagedList.Config config = new PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(15)
            .build();

    public PagedListWithLoadingState<News_> getNews(String tourneyIds) {
        MutableLiveData<LoadStates> loadStatesLiveData = new MutableLiveData<>(LoadStates.Loading);
        MutableLiveData<PagedList<News_>> pagedListLiveData = new MutableLiveData<>();

        final DoIt errorCallback = () -> loadStatesLiveData.postValue(LoadStates.Error);
        final DoIt goodCallback = () -> loadStatesLiveData.postValue(LoadStates.Loaded);
        final DoIt emptyCallback = () -> loadStatesLiveData.postValue(LoadStates.Empty);

        NewsPositionalDataSource newsPositionalDataSource = new NewsPositionalDataSource(
                goodCallback,
                errorCallback,
                emptyCallback,
                tourneyIds);

        executeBuildPagedList(pagedListLiveData, newsPositionalDataSource);

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
