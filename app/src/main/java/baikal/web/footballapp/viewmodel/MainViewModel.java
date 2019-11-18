package baikal.web.footballapp.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.News;
import baikal.web.footballapp.model.News_;
import baikal.web.footballapp.model.PersonPopulate;
import baikal.web.footballapp.model.Tourney;
import baikal.web.footballapp.repository.MainRepository;
import baikal.web.footballapp.tournament.activity.TournamentsFragment;
import baikal.web.footballapp.tournament.adapter.RVFavTourneyAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends ViewModel {
    private MutableLiveData<List<News_>> newsData = null;
    private MutableLiveData<List<Tourney>> favTourney = null;
    private MutableLiveData<List<List<League>>> favLeagues= null;
    private MutableLiveData<List<String>> favTourneysId = null;


    public LiveData<List<List<League>>> getFavLeagues(){
        if(favLeagues==null){
            favLeagues = new MutableLiveData<>();
        }
        if(favTourneysId==null){
            loadTourneys(PersonalActivity.id);
        }
        if(favTourneysId!=null){
            List<String> favList = favTourneysId.getValue();
            List<List<League>> leagueList = new ArrayList<>();
            for (String tr : favList){
                getFavLeagues(tr, new TournamentsFragment.MyCallback() {
                    @Override
                    public void onDataGot(List<League> leagues) {
                        leagueList.add(leagues);
                    }
                });
            }
            favLeagues.setValue(leagueList);

        }
        return favLeagues;
    }
    public void setFavTourneysId(List<String> favTourney){
        if(favTourneysId==null){
            favTourneysId = new MutableLiveData<>();
        }
        this.favTourneysId.setValue(favTourney);
    }
    private LiveData<List<String>> getFavTourneysId(){
        if(favTourneysId==null){
            favTourneysId = new MutableLiveData<>();
        }
        return favTourneysId;
    }
    public LiveData<List<News_>> getNews(String limit, String offset) {
        if (newsData == null) {
            newsData = new MutableLiveData<>();

        }
        loadData(limit, offset);
        return newsData;
    }
    public LiveData<List<Tourney>> getFavTourney(String id){
        if(favTourney == null){
            favTourney = new MutableLiveData<>();

        }
        loadTourneys(id);

        return favTourney;
    }
    private void loadTourneys(String id){
        new MainRepository().getFavTourneys(id, new Callback<List<PersonPopulate>>() {
            @Override
            public void onResponse(Call<List<PersonPopulate>> call, Response<List<PersonPopulate>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().size()>0) {
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
    private void getFavLeagues(String tr, TournamentsFragment.MyCallback callback){
        List<League> leagues = new ArrayList<>();
        Controller.getApi().getLeaguesByTourney(tr).enqueue(new Callback<List<League>>() {
            @Override
            public void onResponse(Call<List<League>> call, Response<List<League>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null)
                    {
                        leagues.addAll(response.body());
                    }
                }
            }
            @Override
            public void onFailure(Call<List<League>> call, Throwable t) {
            }
        });
        callback.onDataGot(leagues);
    }
}
