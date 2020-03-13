package baikal.web.footballapp.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import java.util.List;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.DataSourceUtilities.LoadStates;
import baikal.web.footballapp.DataSourceUtilities.PagedListWithLoadingState;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.model.News_;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.repository.NewsPageRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsPageViewModel extends ViewModel {
    private final static String TAG = "NewsPageViewModel";

    private final NewsPageRepository newsPageRepository;
    private final MutableLiveData<PagedList<News_>> news;
    public PagedListWithLoadingState<News_> newsList;
    private MutableLiveData<LoadStates> loadDataState;
    private String tourneyIds;

    public NewsPageViewModel() {
        newsPageRepository = new NewsPageRepository();
        news = new MutableLiveData<>();
        loadDataState = new MutableLiveData<>(LoadStates.Loading);
        this.tourneyIds = "";
        reload();
    }

    private void initNewsPageList(String tourneyIds) {
        newsList = newsPageRepository.getNews(tourneyIds);
        newsList.loadState.observeForever(new Observer<LoadStates>() {
            @Override
            public void onChanged(LoadStates loadState) {
                loadDataState.setValue(loadState);
                if (loadState.compareTo(LoadStates.Loading) != 0)
                    newsList.loadState.removeObserver(this);
                if (loadState.compareTo(LoadStates.Loaded) == 0)
                    news.setValue(newsList.list.getValue());
            }
        });
    }

    public void getNewsByTourneys(String tourneyIds) {
        initNewsPageList(tourneyIds);
    }

    public LiveData<PagedList<News_>> getNews() {
        return news;
    }

    public LiveData<LoadStates> getLoadDataState() {
        return loadDataState;
    }

    public void reload() {
        Callback<List<Person>> responseCallback = new Callback<List<Person>>() {
            @Override
            public void onResponse(@NonNull Call<List<Person>> call, @NonNull Response<List<Person>> response) {
                if (response.isSuccessful() && response.body().size() > 0) {
                    Person p = response.body().get(0);
                    StringBuilder newTourneyIds = new StringBuilder();
                    for (String t: p.getFavouriteTourney())
                        newTourneyIds.append(",").append(t);
                    tourneyIds = newTourneyIds.toString();
                    initNewsPageList(tourneyIds);
                    return;
                }
                initNewsPageList(tourneyIds);
            }

            @Override
            public void onFailure(@NonNull Call<List<Person>> call, @NonNull Throwable t) { initNewsPageList(tourneyIds); }
        };
        if (SaveSharedPreference.getObject() != null)
            Controller.getApi().getPerson(SaveSharedPreference.getObject().getUser().getId()).enqueue(responseCallback);
        else
            initNewsPageList(tourneyIds);
    }
}
