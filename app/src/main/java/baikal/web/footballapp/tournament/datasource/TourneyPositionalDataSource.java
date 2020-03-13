package baikal.web.footballapp.tournament.datasource;

import androidx.annotation.NonNull;
import androidx.paging.PositionalDataSource;

import java.util.List;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.DataSourceUtilities.DoIt;
import baikal.web.footballapp.model.Tourney;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TourneyPositionalDataSource extends PositionalDataSource<Tourney> {
    private static final String TAG = "TourneyPositionalDataSource";
    private final DoIt doItSoGood;
    private final DoIt dontFeelSoGood;
    private final DoIt feelEmpty;
    private final String searchString;
    private final String region;

    public TourneyPositionalDataSource(DoIt doItSoGood, DoIt dontFeelSoGood, DoIt feelEmpty, String searchString, String region) {
        this.doItSoGood = doItSoGood;
        this.dontFeelSoGood = dontFeelSoGood;
        this.searchString = searchString;
        this.feelEmpty = feelEmpty;
        this.region = region;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<Tourney> callback) {
        Callback<List<Tourney>> responseCallback = new Callback<List<Tourney>>() {
            @Override
            public void onResponse(@NonNull Call<List<Tourney>> call, @NonNull Response<List<Tourney>> response) {
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
            public void onFailure(@NonNull Call<List<Tourney>> call, @NonNull Throwable t) {
                dontFeelSoGood.doIt();
            }
        };

        Controller.getApi().getTourneysWithSortAndFilters(
                searchString, region,
                String.valueOf(params.requestedLoadSize), "0")
                .enqueue(responseCallback);
    }

    @Override
    public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<Tourney> callback) {
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

        Controller.getApi().getTourneysWithSortAndFilters(
                searchString, region,
                String.valueOf(params.loadSize),
                String.valueOf(params.startPosition))
                .enqueue(responseCallback);
    }
}
