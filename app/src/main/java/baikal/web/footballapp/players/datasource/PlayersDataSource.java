package baikal.web.footballapp.players.datasource;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.ItemKeyedDataSource;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.model.Person;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PlayersDataSource extends ItemKeyedDataSource<String, Person> {
    private static final String TAG = "PlayersDataSource";

    @SuppressLint("CheckResult")
    @Override
    public void loadInitial(@NonNull LoadInitialParams<String> params, @NonNull LoadInitialCallback<Person> callback) {
        Log.d(TAG, "requestedLoadSize:" + params.requestedLoadSize);
        Controller.getApi().getAllPersonsWithSort(null, String.valueOf(params.requestedLoadSize), null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onResult);
    }

    @Override
    public void loadAfter(@NonNull LoadParams<String> params, @NonNull LoadCallback<Person> callback) {
        Controller.getApi().getAllPersonsWithSort(null, String.valueOf(params.requestedLoadSize), "<" + params.key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onResult);

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
