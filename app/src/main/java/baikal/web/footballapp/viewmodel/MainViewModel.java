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
        new MainRepository().getNews(limit, offset, new Callback<List<News_>>() {
            @Override
            public void onResponse(Call<List<News_>> call, Response<List<News_>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        List<News_> news = response.body();
                        if (news != null){
                            newsData.setValue(news);
                        }
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
