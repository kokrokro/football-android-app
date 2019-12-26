package baikal.web.footballapp.home.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import baikal.web.footballapp.R;
import baikal.web.footballapp.home.adapter.RecyclerViewMainNewsAdapter;
import baikal.web.footballapp.model.News_;
import baikal.web.footballapp.viewmodel.MainViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class NewsAndAds extends Fragment {
    private final Logger log = LoggerFactory.getLogger(MainPage.class);

    private List<News_> allNews = new ArrayList<>();
    private RecyclerViewMainNewsAdapter newsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view;
        view = inflater.inflate(R.layout.activity_main_page, container, false);

        try {
            RecyclerView recyclerViewNews = view.findViewById(R.id.recyclerViewMainNews);
            recyclerViewNews.setLayoutManager(new LinearLayoutManager(getActivity()));
            newsAdapter = new RecyclerViewMainNewsAdapter(getActivity(), NewsAndAds.this, allNews);
            recyclerViewNews.setAdapter(newsAdapter);
        } catch (Exception e) {
            log.error("ERROR: ", e);
        }

        MainViewModel mainViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(MainViewModel.class);
        mainViewModel.getNews("30","0").observe(getViewLifecycleOwner(), newsAdapter::dataChanged);

        return view;
    }

    @SuppressLint("CheckResult")
    private void checkConnection() {
        //noinspection ResultOfMethodCallIgnored
        ReactiveNetwork
                .observeNetworkConnectivity(Objects.requireNonNull(getActivity()).getApplicationContext())
                .flatMapSingle(connectivity -> ReactiveNetwork.checkInternetConnectivity())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isConnected -> {
                    // isConnected can be true or false

                });
    }
}