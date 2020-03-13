package baikal.web.footballapp.home.datasource;

import androidx.annotation.NonNull;
import androidx.paging.PositionalDataSource;

import java.util.List;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.DataSourceUtilities.DoIt;
import baikal.web.footballapp.model.ActiveMatch;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpcomingMatchesPositionalDataSource extends PositionalDataSource<ActiveMatch> {
    private static final String TAG = "UpcomingMatchesPosDS";

    private final DoIt doItSoGood;
    private final DoIt dontFeelSoGood;
    private final DoIt feelEmpty;
    private String date;
    private String leagueIds;

    public UpcomingMatchesPositionalDataSource(DoIt doItSoGood, DoIt dontFeelSoGood, DoIt feelEmpty, String leagueIds, String date)
    {
        this.doItSoGood = doItSoGood;
        this.dontFeelSoGood = dontFeelSoGood;
        this.feelEmpty = feelEmpty;
        this.leagueIds = leagueIds;
        this.date = date;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<ActiveMatch> callback) {
        Callback<List<ActiveMatch>> responseCallback = new Callback<List<ActiveMatch>>() {
            @Override
            public void onResponse(@NonNull Call<List<ActiveMatch>> call, @NonNull Response<List<ActiveMatch>> response) {
                if (response.body()==null || response.body().size() == 0)
                    feelEmpty.doIt();
                else
                    doItSoGood.doIt();
                if (response.body() != null)
                    callback.onResult(response.body(), 0);
//                Log.d(TAG, "O N R E S P O N S E %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%" + response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<ActiveMatch>> call, @NonNull Throwable t) {
                dontFeelSoGood.doIt();
            }
        };

        Controller.getApi().getUpcomingMatches(
                date, leagueIds,
                String.valueOf(params.pageSize), "0")
                .enqueue(responseCallback);
    }

    @Override
    public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<ActiveMatch> callback) {
        Callback<List<ActiveMatch>> responseCallback = new Callback<List<ActiveMatch>>() {
            @Override
            public void onResponse(@NonNull Call<List<ActiveMatch>> call, @NonNull Response<List<ActiveMatch>> response) {
                doItSoGood.doIt();
                if (response.body() != null)
                    callback.onResult(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<ActiveMatch>> call, @NonNull Throwable t) {
                dontFeelSoGood.doIt();
            }
        };

        Controller.getApi().getUpcomingMatches(
                date, leagueIds,
                String.valueOf(params.loadSize), String.valueOf(params.startPosition))
                .enqueue(responseCallback);
    }
}
