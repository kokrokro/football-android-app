package baikal.web.footballapp.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.model.Invite;
import baikal.web.footballapp.model.News;
import baikal.web.footballapp.model.News_;
import baikal.web.footballapp.model.PersonPopulate;
import baikal.web.footballapp.model.Tourney;

public class MainRepository {
    public LiveData<List<News_>> getNews(String limit, String offset, retrofit2.Callback<List<News_>> callback) {
        MutableLiveData<List<News_>> returnNews = new MutableLiveData<>();
        Controller
                .getApi()
                .getAllNewsCrud(limit, offset)
                .enqueue(callback);
        return returnNews;
    }

    public LiveData<List<PersonPopulate>> getFavTourneys(String id, retrofit2.Callback<List<PersonPopulate>> callback) {
        MutableLiveData<List<PersonPopulate>> returnNews = new MutableLiveData<>();
        Controller
                .getApi()
                .getFavTourneysByPerson(id)
                .enqueue(callback);
        return returnNews;
    }

    public LiveData<List<Invite>> getInvites(String person, String team, retrofit2.Callback<List<Invite>> callback) {
        MutableLiveData<List<Invite>> invites = new MutableLiveData<>();
        Controller
                .getApi()
                .getInvites(person, team)
                .enqueue(callback);

        return invites;
    }
}
