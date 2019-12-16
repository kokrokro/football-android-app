package baikal.web.footballapp.players.datasource;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.PositionalDataSource;

import java.util.List;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.model.Person;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayersPositionalDataSource extends PositionalDataSource<Person> {
    private static final String TAG = "PlayersItemDataSource";
    private final DoIt doItSoGood;
    private final DoIt dontFeelSoGood;
    private final DoIt feelEmpty;
    private final String searchString;

    public PlayersPositionalDataSource(DoIt doItSoGood, DoIt dontFeelSoGood, DoIt feelEmpty, String searchString) {
        this.doItSoGood = doItSoGood;
        this.dontFeelSoGood = dontFeelSoGood;
        this.searchString = searchString;
        this.feelEmpty = feelEmpty;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<Person> callback) {
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
                    callback.onResult(response.body(), 0);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Person>> call, @NonNull Throwable t) {
                dontFeelSoGood.doIt();
            }
        };

        Controller.getApi().getFilteredPersonsWithSort(
                searchString, searchString, searchString,
                String.valueOf(params.requestedLoadSize), "0")
                .enqueue(responseCallback);
    }

    @Override
    public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<Person> callback) {
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

        Controller.getApi().getFilteredPersonsWithSort(
                searchString, searchString, searchString,
                String.valueOf(params.loadSize), String.valueOf(params.startPosition))
                .enqueue(responseCallback);
    }
}
