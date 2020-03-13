package baikal.web.footballapp.DataSourceUtilities;

import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

public class PagedListWithLoadingState<T> {
    public LiveData<LoadStates> loadState;
    public LiveData<PagedList<T>> list;

    public PagedListWithLoadingState(LiveData<LoadStates> loadState, LiveData<PagedList<T>> list) {
        this.loadState = loadState;
        this.list = list;
    }
}
