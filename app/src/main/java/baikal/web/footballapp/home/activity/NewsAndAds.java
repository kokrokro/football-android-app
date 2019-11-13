package baikal.web.footballapp.home.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import baikal.web.footballapp.CheckError;
import baikal.web.footballapp.Controller;
import baikal.web.footballapp.R;
import baikal.web.footballapp.home.adapter.RecyclerViewMainAdsAdapter;
import baikal.web.footballapp.home.adapter.RecyclerViewMainNewsAdapter;
import baikal.web.footballapp.model.Announce;
import baikal.web.footballapp.model.Announces;
import baikal.web.footballapp.model.News_;
import baikal.web.footballapp.viewmodel.MainViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class NewsAndAds extends Fragment {
    private final Logger log = LoggerFactory.getLogger(MainPage.class);
    private final NewsFragment newsFragment = new NewsFragment();
    private final AdsPage adsPage = new AdsPage();
    private final List<Announce> announces = new ArrayList<>();
    private RecyclerView recyclerViewNews;
    private RecyclerView recyclerViewAds;
    private List<News_> allNews = new ArrayList<>();
    private RecyclerViewMainNewsAdapter newsAdapter;
    private RecyclerViewMainAdsAdapter adsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view;
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        view = inflater.inflate(R.layout.activity_main_page, container, false);


        MainViewModel mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        //checkConnection();
        GetAllAds();


        final Button btnNews = view.findViewById(R.id.showAllNews);
        final Button btnAds = view.findViewById(R.id.showAllAds);
        //  fragmentManager.beginTransaction().add(R.id.pageContainer, newsFragment).add(R.id.pageContainer, adsPage).hide(newsFragment).hide(adsPage).commit();
        btnNews.setOnClickListener(v -> {
            //show all news
            fragmentManager.beginTransaction()
                    .replace(R.id.pageContainer, new NewsFragment())
                    .addToBackStack(null)
                    .commit();
        });
        btnAds.setOnClickListener(v -> {
            //show all ads
            fragmentManager.beginTransaction()
                    .replace(R.id.pageContainer, new AdsPage())
                    .addToBackStack(null)
                    .commit();
        });
        try {
            recyclerViewNews = view.findViewById(R.id.recyclerViewMainNews);
            recyclerViewNews.setLayoutManager(new LinearLayoutManager(getActivity()));
            newsAdapter = new RecyclerViewMainNewsAdapter(getActivity(), NewsAndAds.this, allNews);
            recyclerViewNews.setAdapter(newsAdapter);
        } catch (Exception e) {
            log.error("ERROR: ", e);
        }


        try {
            recyclerViewAds = view.findViewById(R.id.recyclerViewMainAds);
            recyclerViewAds.setLayoutManager(new LinearLayoutManager(getActivity()));
            adsAdapter = new RecyclerViewMainAdsAdapter(getActivity(), NewsAndAds.this, announces);
            recyclerViewAds.setAdapter(adsAdapter);
        } catch (Exception e) {
            log.error("ERROR: ", e);
        }

        mainViewModel.getNews("2","0").observe(this, news_ -> {
            Log.d("News and ads", "data is changed size " + news_.size());
            for (int i = 0; i < news_.size(); i++) {
                Log.d("News and ads", news_.get(i).getCaption());
            }
            newsAdapter.dataChanged(news_);
        });

        return view;
    }

    @SuppressLint("CheckResult")
    private void checkConnection() {
        ReactiveNetwork
                .observeNetworkConnectivity(getActivity().getApplicationContext())
                .flatMapSingle(connectivity -> ReactiveNetwork.checkInternetConnectivity())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isConnected -> {
                    // isConnected can be true or false
                    if (isConnected) {
//                        GetAllNews();
                        GetAllAds();
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void GetAllAds() {
        Controller.getApi().getAllAnnounce("2", "0")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .repeatWhen(completed -> completed.delay(5, TimeUnit.MINUTES))
                .subscribe(this::saveAds
                        ,
                        error -> {
                            CheckError checkError = new CheckError();
                            checkError.checkError(getActivity(), error);
                        }
                );
    }

    private void saveAds(List<Announce> matches) {
        announces.addAll(announces.size(), matches);
        List<Announce> list = new ArrayList<>(announces);
        adsAdapter.dataChanged(list);
    }

//    @SuppressLint("CheckResult")
//    private void GetAllNews() {
//        allNews = new ArrayList<>();
//        Controller.getApi().getAllNews("2", "0")
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .repeatWhen(completed -> completed.delay(5, TimeUnit.MINUTES))
//                .subscribe(this::saveData
//                        ,
//                        error -> {
//                            CheckError checkError = new CheckError();
//                            checkError.checkError(getActivity(), error);
//                        }
//                );
//
//    }
//
//    private void saveData(News matches) {
//        allNews.addAll(allNews.size(), matches.getNews());
//        List<News_> list = new ArrayList<>(allNews);
//        newsAdapter.dataChanged(list);
//    }
}