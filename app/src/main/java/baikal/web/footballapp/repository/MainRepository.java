package baikal.web.footballapp.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.model.Invite;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.News_;
import baikal.web.footballapp.model.PersonPopulate;
import baikal.web.footballapp.model.PersonStatus;
import baikal.web.footballapp.model.Stadium;
import baikal.web.footballapp.model.Team;
import retrofit2.Callback;

public class MainRepository {


    public void getPersonStatus(String team, String league, String activeDisquals, Callback<List<PersonStatus>> callback) {
        Controller
                .getApi()
                .getPersonStatus(team, league, activeDisquals)
                .enqueue(callback);
    }
    public LiveData<List<Team>> getTeams(String creator, retrofit2.Callback<List<Team>> callback) {
        MutableLiveData<List<Team>> returnNews = new MutableLiveData<>();
        Controller
                .getApi()
                .getTeams(creator)
                .enqueue(callback);
        return returnNews;
    }

    public void getNewsByTourney(String tourneyIds,String limit, String offset, Callback<List<News_>> callback) {
        Controller
                .getApi()
                .getNewsByTourney(tourneyIds, limit, offset)
                .enqueue(callback);
    }
    public void getStadiums(String tourney, String id, Callback<List<Stadium>> callback) {
        Controller
                .getApi()
                .getStadium(tourney,id)
                .enqueue(callback);
    }

    public void getFavTourneys(String id, Callback<List<PersonPopulate>> callback) {
        Controller
                .getApi()
                .getFavTourneysByPerson(id)
                .enqueue(callback);
    }

    public void getInvites(String person, String team, Callback<List<Invite>> callback) {
        Controller
                .getApi()
                .getInvites(person, team, null)
                .enqueue(callback);

    }
    public void getLeagues(String tourney, Callback<List<League>> callback){
        Controller
                .getApi()
                .getLeaguesByTourney(tourney)
                .enqueue(callback);
    }
}
