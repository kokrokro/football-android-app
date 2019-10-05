package baikal.web.footballapp.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.model.News;
import baikal.web.footballapp.model.News_;

public class MainRepository {
    public LiveData<List<News_>> getNews(String limit, String offset, retrofit2.Callback<News> callback) {
        MutableLiveData<List<News_>> returnNews = new MutableLiveData<>();
        Controller
                .getApi()
                .getAllNews(limit, offset)
                .enqueue(callback);
        return returnNews;
    }
}
