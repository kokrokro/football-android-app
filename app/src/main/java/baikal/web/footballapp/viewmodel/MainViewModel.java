package baikal.web.footballapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import baikal.web.footballapp.model.News;
import baikal.web.footballapp.model.News_;
import baikal.web.footballapp.repository.MainRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends ViewModel {
    private MutableLiveData<List<News_>> newsData;

    public LiveData<List<News_>> getNews(String limit, String offset) {
        if (newsData == null) {
            newsData = new MutableLiveData<>();
            loadData(limit, offset);
        }
        return newsData;
    }

    private void loadData(String limit, String offset) {
        new MainRepository().getNews(limit, offset, new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        List<News_> news = response.body().getNews();
                        if (news != null){
                            newsData.setValue(news);
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {

            }
        })
        ;
    }
}
