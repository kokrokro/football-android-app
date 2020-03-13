package baikal.web.footballapp.players.datasource;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.ItemKeyedDataSource;

import java.util.List;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.DataSourceUtilities.DoIt;
import baikal.web.footballapp.model.Person;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayersItemDataSource extends ItemKeyedDataSource<String, Person> {
    private static final String TAG = "PlayersItemDataSource";
    private final DoIt doItSoGood;
    private final DoIt dontFeelSoGood;
    private final DoIt feelEmpty;

    public PlayersItemDataSource(DoIt doItSoGood, DoIt dontFeelSoGood, DoIt feelEmpty) {
        this.doItSoGood = doItSoGood;
        this.dontFeelSoGood = dontFeelSoGood;
        this.feelEmpty = feelEmpty;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<String> params, @NonNull LoadInitialCallback<Person> callback) {
        Log.d(TAG, "requestedLoadSize:" + params.requestedLoadSize);
        Callback<List<Person>> responseCallback = new Callback<List<Person>>() {
            @Override
            public void onResponse(@NonNull Call<List<Person>> call, @NonNull Response<List<Person>> response) {
                if (response.body() != null) {
                    if (response.body().size() == 0) {
                        feelEmpty.doIt();
                    } else {
                        doItSoGood.doIt();
                    }
                        callback.onResult(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Person>> call, @NonNull Throwable t) {
                dontFeelSoGood.doIt();
            }
        };

        Controller.getApi().getAllPersonsWithSort(String.valueOf(params.requestedLoadSize), null)
                .enqueue(responseCallback);
    }

    @Override
    public void loadAfter(@NonNull LoadParams<String> params, @NonNull LoadCallback<Person> callback) {
        Callback<List<Person>> responseCallback = new Callback<List<Person>>() {
            @Override
            public void onResponse(@NonNull Call<List<Person>> call, @NonNull Response<List<Person>> response) {
                doItSoGood.doIt();
                if (response.body() != null) {
                    callback.onResult(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Person>> call, @NonNull Throwable t) {
                dontFeelSoGood.doIt();
            }
        };

        Controller.getApi().getAllPersonsWithSort(String.valueOf(params.requestedLoadSize), "<" + params.key)
                .enqueue(responseCallback);
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
