package baikal.web.footballapp.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import baikal.web.footballapp.model.Invite;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.News_;
import baikal.web.footballapp.model.PersonPopulate;
import baikal.web.footballapp.model.PersonStatus;
import baikal.web.footballapp.model.Stadium;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.model.Tourney;
import baikal.web.footballapp.repository.MainRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends ViewModel {
    private MutableLiveData<List<News_>> newsData = null;
    private MutableLiveData<List<Tourney>> favTourney = null;
    private MutableLiveData<List<League>> favLeagues= null;
    private MutableLiveData<List<String>> favTourneysId = null;
    private MutableLiveData<List<Invite>> allInvites = null;
    private MutableLiveData<List<Stadium>> allStadiums = null;
    private MutableLiveData<List<Team>> allTeams = null;
    private MutableLiveData<List<PersonStatus>> allPersonStatus =null;

    public LiveData<List<Team>> getTeams(){
        if(allTeams==null){
            allTeams = new MutableLiveData<>();
        }
        loadTeams();
        return allTeams;
    }
    public LiveData<List<PersonStatus>> getPersonStatus(){
        if(allPersonStatus == null){
            allPersonStatus = new MutableLiveData<>();
        }
        loadPersonStatus();

        return allPersonStatus;
    }
    private void loadPersonStatus(){
        new MainRepository().getPersonStatus(null, null, null, new Callback<List<PersonStatus>>() {
            @Override
            public void onResponse(Call<List<PersonStatus>> call, Response<List<PersonStatus>> response) {
                if(response.isSuccessful()){
                    if(response.body()!=null){
                        allPersonStatus.setValue(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<PersonStatus>> call, Throwable t) {

            }
        });
    }

    public LiveData<List<Stadium>> getAllStadiums(){
        if(allStadiums==null){
            allStadiums = new MutableLiveData<>();
        }
        loadStadiums();
        return allStadiums;
    }
    private void loadTeams() {
        new MainRepository().getTeams(null, new Callback<List<Team>>() {
            @Override
            public void onResponse(@NonNull Call<List<Team>> call, @NonNull Response<List<Team>> response) {
                if (response.isSuccessful()){
                    if (response.body() != null ){
                        allTeams.setValue(response.body());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Team>> call, @NonNull Throwable t) {

            }
        });
    }

    private void loadStadiums() {
        new MainRepository().getStadiums(null, null, new Callback<List<Stadium>>() {
            @Override
            public void onResponse(@NonNull Call<List<Stadium>> call, @NonNull Response<List<Stadium>> response) {
                if (response.isSuccessful()){
                    if (response.body() != null ){
                        allStadiums.setValue(response.body());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Stadium>> call, @NonNull Throwable t) {

            }
        });
    }
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
            public void onResponse(@NonNull Call<List<Invite>> call, @NonNull Response<List<Invite>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null ){
                        allInvites.setValue(response.body());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Invite>> call, @NonNull Throwable t) {

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
//    private LiveData<List<String>> getFavTourneysId(){
//        if(favTourneysId==null){
//            favTourneysId = new MutableLiveData<>();
//        }
//        return favTourneysId;
//    }
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
            public void onResponse(@NonNull Call<List<PersonPopulate>> call, @NonNull Response<List<PersonPopulate>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().size()>0) {
                        List<Tourney> tourneys = response.body().get(0).getFavouriteTourney();
                        favTourney.setValue( tourneys);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<PersonPopulate>> call, @NonNull Throwable t) {

            }
        });
    }

    private void loadData(String limit, String offset) {
        new MainRepository().getNews(limit, offset, new Callback<List<News_>>() {
            @Override
            public void onResponse(@NonNull Call<List<News_>> call, @NonNull Response<List<News_>> response) {
                if (response.isSuccessful())
                    if (response.body() != null) {
                        List<News_> news = response.body();
                        newsData.setValue(news);
                    }
            }

            @Override
            public void onFailure(@NonNull Call<List<News_>> call, @NonNull Throwable t) {

            }
        })
        ;
    }
    private void loadFavLeagues(String tourney){
        new MainRepository().getLegues(tourney, new Callback<List<League>>() {
            @Override
            public void onResponse(@NonNull Call<List<League>> call, @NonNull Response<List<League>> response) {
                if(response.isSuccessful()){
                    if(response.body()!=null){
                        favLeagues.setValue(response.body());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<League>> call, @NonNull Throwable t) {

            }
        });
    }
}
