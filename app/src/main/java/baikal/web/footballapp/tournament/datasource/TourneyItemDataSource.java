package baikal.web.footballapp.tournament.datasource;

import androidx.annotation.NonNull;
import androidx.paging.ItemKeyedDataSource;

import java.util.List;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.DataSourceUtilities.DoIt;
import baikal.web.footballapp.model.Tourney;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TourneyItemDataSource extends ItemKeyedDataSource<String, Tourney> {
    private static final String TAG = "TourneyItemDataSource";
    private final DoIt doItSoGood;
    private final DoIt dontFeelSoGood;
    private final DoIt feelEmpty;

    public TourneyItemDataSource(DoIt doItSoGood, DoIt dontFeelSoGood, DoIt feelEmpty) {
        this.doItSoGood = doItSoGood;
        this.dontFeelSoGood = dontFeelSoGood;
        this.feelEmpty = feelEmpty;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<String> params, @NonNull LoadInitialCallback<Tourney> callback) {
        Callback<List<Tourney>> responseCallback = new Callback<List<Tourney>>() {
            @Override
            public void onResponse(@NonNull Call<List<Tourney>> call, @NonNull Response<List<Tourney>> response) {
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
            public void onFailure(@NonNull Call<List<Tourney>> call, @NonNull Throwable t) {
                dontFeelSoGood.doIt();
            }
        };
        Controller.getApi().getTourneysWithSort(
                String.valueOf(params.requestedLoadSize), null)
                .enqueue(responseCallback);
    }

    @Override
    public void loadAfter(@NonNull LoadParams<String> params, @NonNull LoadCallback<Tourney> callback) {
        Callback<List<Tourney>> responseCallback = new Callback<List<Tourney>>() {
            @Override
            public void onResponse(@NonNull Call<List<Tourney>> call, @NonNull Response<List<Tourney>> response) {
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
            public void onFailure(@NonNull Call<List<Tourney>> call, @NonNull Throwable t) {
                dontFeelSoGood.doIt();
            }
        };
        Controller.getApi().getTourneysWithSort(
                String.valueOf(params.requestedLoadSize),
                "<" + params.key)
                .enqueue(responseCallback);
    }

    @Override
    public void loadBefore(@NonNull LoadParams<String> params, @NonNull LoadCallback<Tourney> callback) {

    }

    @NonNull
    @Override
    public String getKey(@NonNull Tourney item) {
        return item.getBeginDate();
    }
}
