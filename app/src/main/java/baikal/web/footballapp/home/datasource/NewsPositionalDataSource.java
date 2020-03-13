package baikal.web.footballapp.home.datasource;

import androidx.annotation.NonNull;
import androidx.paging.PositionalDataSource;

import java.util.List;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.DataSourceUtilities.DoIt;
import baikal.web.footballapp.model.News_;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsPositionalDataSource extends PositionalDataSource<News_> {
    private static final String TAG = "NewsPositionalDataSource";
    private final DoIt doItSoGood;
    private final DoIt dontFeelSoGood;
    private final DoIt feelEmpty;
    private final String tourneyIds;

    public NewsPositionalDataSource (DoIt doItSoGood, DoIt dontFeelSoGood, DoIt feelEmpty, String tourneyIds) {
        this.doItSoGood = doItSoGood;
        this.dontFeelSoGood = dontFeelSoGood;
        this.feelEmpty = feelEmpty;
        this.tourneyIds = tourneyIds;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<News_> callback) {
        Callback<List<News_>> responseCallback = new Callback<List<News_>>() {
            @Override
            public void onResponse(@NonNull Call<List<News_>> call, @NonNull Response<List<News_>> response) {
                if (response.body() == null || response.body().size() == 0)
                    feelEmpty.doIt();
                else
                    doItSoGood.doIt();
                if (response.body() != null)
                    callback.onResult(response.body(), 0);
            }

            @Override
            public void onFailure(@NonNull Call<List<News_>> call, @NonNull Throwable t) {
                dontFeelSoGood.doIt();
            }
        };

        Controller.getApi().getNewsByTourney(
                tourneyIds,
                String.valueOf(params.pageSize), "0")
                .enqueue(responseCallback);
    }

    @Override
    public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<News_> callback) {
        Callback<List<News_>> responseCallback = new Callback<List<News_>>() {
            @Override
            public void onResponse(@NonNull Call<List<News_>> call, @NonNull Response<List<News_>> response) {
                doItSoGood.doIt();
                if (response.body() != null)
                    callback.onResult(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<News_>> call, @NonNull Throwable t) {
                dontFeelSoGood.doIt();
            }
        };

        Controller.getApi().getNewsByTourney(
                tourneyIds,
                String.valueOf(params.loadSize), String.valueOf(params.startPosition))
                .enqueue(responseCallback);
    }
}
