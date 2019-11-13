package baikal.web.footballapp.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import baikal.web.footballapp.model.News;
import baikal.web.footballapp.model.News_;
import baikal.web.footballapp.model.PersonPopulate;
import baikal.web.footballapp.model.Tourney;
import baikal.web.footballapp.repository.MainRepository;
import baikal.web.footballapp.tournament.adapter.RVFavTourneyAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends ViewModel {
    private MutableLiveData<List<News_>> newsData;
    private MutableLiveData<List<Tourney>> favTourney;
    public LiveData<List<News_>> getNews(String limit, String offset) {
        if (newsData == null) {
            newsData = new MutableLiveData<>();
            loadData(limit, offset);
        }
        return newsData;
    }
    public LiveData<List<Tourney>> getFavTourney(String id){
        if(favTourney == null){
            favTourney = new MutableLiveData<>();
            loadTourneys(id);
        }
        return favTourney;
    }
    private void loadTourneys(String id){
        new MainRepository().getFavTourneys(id, new Callback<List<PersonPopulate>>() {
            @Override
            public void onResponse(Call<List<PersonPopulate>> call, Response<List<PersonPopulate>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        List<Tourney> tourneys = response.body().get(0).getFavouriteTourney();
                        favTourney.setValue( tourneys);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<PersonPopulate>> call, Throwable t) {

            }
        });
    }

    private void loadData(String limit, String offset) {
        new MainRepository().getNews(limit, offset, new Callback<List<News_>>() {
            @Override
            public void onResponse(Call<List<News_>> call, Response<List<News_>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        List<News_> news = response.body();
                        newsData.setValue(news);

                    }
                }
            }

            @Override
            public void onFailure(Call<List<News_>> call, Throwable t) {

            }
        })
        ;
    }
}
