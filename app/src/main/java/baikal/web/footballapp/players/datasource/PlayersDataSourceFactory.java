package baikal.web.footballapp.players.datasource;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import baikal.web.footballapp.model.Person;

public class PlayersDataSourceFactory extends DataSource.Factory<String, Person> {
    private MutableLiveData<PlayersDataSource> sourceLiveData =
            new MutableLiveData<>();

    private PlayersDataSource latestSource;

    @Override
    public DataSource<String, Person> create() {
        latestSource = new PlayersDataSource();
        sourceLiveData.postValue(latestSource);
        return latestSource;
    }
}