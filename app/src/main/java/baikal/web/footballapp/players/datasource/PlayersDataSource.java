package baikal.web.footballapp.players.datasource;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.ItemKeyedDataSource;

import java.util.List;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.model.Person;
import retrofit2.Callback;

public class PlayersDataSource extends ItemKeyedDataSource<String, Person> {
    private static final String TAG = "PlayersDataSource";
    private final Callback<List<Person>> loadInitialCallback;
    private final Callback<List<Person>> loadAfterCallback;

    public PlayersDataSource(Callback<List<Person>> loadInitialCallback, Callback<List<Person>> loadAfterCallback) {
        this.loadInitialCallback = loadInitialCallback;
        this.loadAfterCallback = loadAfterCallback;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<String> params, @NonNull LoadInitialCallback<Person> callback) {
        Log.d(TAG, "requestedLoadSize:" + params.requestedLoadSize);
        Controller.getApi().getAllPersonsWithSort(null, String.valueOf(params.requestedLoadSize), null)
                .enqueue(loadInitialCallback);
    }

    @Override
    public void loadAfter(@NonNull LoadParams<String> params, @NonNull LoadCallback<Person> callback) {
        Controller.getApi().getAllPersonsWithSort(null, String.valueOf(params.requestedLoadSize), "<" + params.key)
                .enqueue(loadAfterCallback);

    }

    @Override
    public void loadBefore(@NonNull LoadParams<String> params, @NonNull LoadCallback<Person> callback) {

    }


    @NonNull
    @Override
    public String getKey(@NonNull Person person) {
        return person.getCreatedAt();
    }
}
