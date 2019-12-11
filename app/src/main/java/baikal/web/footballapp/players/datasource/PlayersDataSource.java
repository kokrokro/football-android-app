package baikal.web.footballapp.players.datasource;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.ItemKeyedDataSource;

import java.util.List;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.model.Person;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayersDataSource extends ItemKeyedDataSource<String, Person> {
    private static final String TAG = "PlayersDataSource";
    private final DoIt doItSoGood;
    private final DoIt dontFeelSoGood;
    private final String searchString;

    public PlayersDataSource(DoIt doItLocal, DoIt doItBad, String searchString) {
        this.doItSoGood = doItLocal;
        this.dontFeelSoGood = doItBad;
        this.searchString = searchString;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<String> params, @NonNull LoadInitialCallback<Person> callback) {
        Log.d(TAG, "requestedLoadSize:" + params.requestedLoadSize);
        Controller.getApi().getAllPersonsWithSort(searchString, searchString, searchString, String.valueOf(params.requestedLoadSize), null)
                .enqueue(new Callback<List<Person>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Person>> call, @NonNull Response<List<Person>> response) {
                        if (response.body() != null) {
                            callback.onResult(response.body());
                            doItSoGood.doIt();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Person>> call, @NonNull Throwable t) {
                        dontFeelSoGood.doIt();
                    }
                });
    }

    @Override
    public void loadAfter(@NonNull LoadParams<String> params, @NonNull LoadCallback<Person> callback) {
        Controller.getApi().getAllPersonsWithSort(searchString, searchString, searchString, String.valueOf(params.requestedLoadSize), "<" + params.key)
                .enqueue(new Callback<List<Person>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Person>> call, @NonNull Response<List<Person>> response) {
                        if (response.body() != null) {
                            callback.onResult(response.body());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Person>> call, @NonNull Throwable t) {
                    }
                });

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
