package baikal.web.footballapp.players.datasource;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import java.util.List;

import baikal.web.footballapp.model.Person;
import retrofit2.Callback;

public class PlayersDataSourceFactory extends DataSource.Factory<String, Person> {
    private MutableLiveData<PlayersDataSource> sourceLiveData =
            new MutableLiveData<>();

    private final Callback<List<Person>> loadInitialCallback;
    private final Callback<List<Person>> loadAfterCallback;

    private PlayersDataSource latestSource;

    public PlayersDataSourceFactory(Callback<List<Person>> loadInitialCallback, Callback<List<Person>> loadAfterCallback) {
        super();
        this.loadInitialCallback = loadInitialCallback;
        this.loadAfterCallback = loadAfterCallback;
    }

    @NonNull
    @Override
    public DataSource<String, Person> create() {
        latestSource = new PlayersDataSource(loadInitialCallback, loadAfterCallback);
        sourceLiveData.postValue(latestSource);
        return latestSource;
    }
}