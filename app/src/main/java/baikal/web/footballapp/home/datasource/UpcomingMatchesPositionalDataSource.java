package baikal.web.footballapp.home.datasource;

import androidx.annotation.NonNull;
import androidx.paging.PositionalDataSource;

import java.util.List;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.DataSourceUtilities.DoIt;
import baikal.web.footballapp.model.MatchPopulate;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpcomingMatchesPositionalDataSource extends PositionalDataSource<MatchPopulate> {
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
    public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<MatchPopulate> callback) {
        Callback<List<MatchPopulate>> responseCallback = new Callback<List<MatchPopulate>>() {
            @Override
            public void onResponse(@NonNull Call<List<MatchPopulate>> call, @NonNull Response<List<MatchPopulate>> response) {
                if (response.body()==null || response.body().size() == 0)
                    feelEmpty.doIt();
                else
                    doItSoGood.doIt();
                if (response.body() != null)
                    callback.onResult(response.body(), 0);
//                Log.d(TAG, "O N R E S P O N S E %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%" + response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<MatchPopulate>> call, @NonNull Throwable t) {
                dontFeelSoGood.doIt();
            }
        };

        Controller.getApi().getUpcomingMatches(
                date, leagueIds,
                String.valueOf(params.pageSize), "0")
                .enqueue(responseCallback);
    }

    @Override
    public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<MatchPopulate> callback) {
        Callback<List<MatchPopulate>> responseCallback = new Callback<List<MatchPopulate>>() {
            @Override
            public void onResponse(@NonNull Call<List<MatchPopulate>> call, @NonNull Response<List<MatchPopulate>> response) {
                doItSoGood.doIt();
                if (response.body() != null)
                    callback.onResult(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<MatchPopulate>> call, @NonNull Throwable t) {
                dontFeelSoGood.doIt();
            }
        };

        Controller.getApi().getUpcomingMatches(
                date, leagueIds,
                String.valueOf(params.loadSize), String.valueOf(params.startPosition))
                .enqueue(responseCallback);
    }
}
