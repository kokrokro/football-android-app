package baikal.web.footballapp.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.model.Invite;
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
    private MutableLiveData<List<League>> favLeagues= null;
    private MutableLiveData<List<String>> favTourneysId = null;
    private MutableLiveData<List<Invite>> allInvites = null;
    public LiveData<List<Invite>> getAllInvites(){
        if(allInvites==null){
            allInvites = new MutableLiveData<>();
        }
        loadInvites();
        return allInvites;
    }

    private void loadInvites() {
        new MainRepository().getInvites(null, null, new Callback<List<Invite>>() {
            @Override
            public void onResponse(Call<List<Invite>> call, Response<List<Invite>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null ){
                        allInvites.setValue(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Invite>> call, Throwable t) {

            }
        });
    }


    public LiveData<List<League>> getFavLeagues(String tourney){
        if(favLeagues==null){
            favLeagues = new MutableLiveData<>();
        }
        loadFavLeagues(tourney);
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

    public void setFavTourney(List<Tourney> favTourney) {
        if (this.favTourney==null) {
            this.favTourney = new MutableLiveData<>();
        }
        this.favTourney.setValue(favTourney);
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
                if (response.isSuccessful())
                    if (response.body() != null) {
                        List<News_> news = response.body();
                        newsData.setValue(news);
                    }
            }

            @Override
            public void onFailure(Call<List<News_>> call, Throwable t) {

            }
        })
        ;
    }
    private void loadFavLeagues(String tourney){
        new MainRepository().getLegues(tourney, new Callback<List<League>>() {
            @Override
            public void onResponse(Call<List<League>> call, Response<List<League>> response) {
                if(response.isSuccessful()){
                    if(response.body()!=null){
                        favLeagues.setValue(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<League>> call, Throwable t) {

            }
        });
    }
}
