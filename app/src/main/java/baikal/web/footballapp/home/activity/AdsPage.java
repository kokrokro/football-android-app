package baikal.web.footballapp.home.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import baikal.web.footballapp.CheckError;
import baikal.web.footballapp.Controller;
import baikal.web.footballapp.R;
import baikal.web.footballapp.home.adapter.RecyclerViewAdsAdapter;
import baikal.web.footballapp.model.Announce;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AdsPage extends Fragment {
    private final Logger log = LoggerFactory.getLogger(AdsPage.class);
    private final List<Announce> allNews = new ArrayList<>();
    private RecyclerView recyclerView;
    private NestedScrollView scroller;
    private RecyclerViewAdsAdapter adapter;
    private int count = 0;
    private final int limit = 5;
    private int offset = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view;
        view = inflater.inflate(R.layout.ads_page, container, false);
        scroller = view.findViewById(R.id.scrollerAds);
        checkConnection();
        recyclerView = view.findViewById(R.id.recyclerViewAds);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        try {
            adapter = new RecyclerViewAdsAdapter(getActivity(), AdsPage.this, allNews);
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            log.error("ERROR: ", e);
        }
        scroller.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                offset++;
                int temp = limit * offset;
                if (temp <= count) {
                    String str = String.valueOf(temp);
                    GetAllAds("5", str);
                }
            }
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
                        GetAllAds("5", "0");
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void GetAllAds(String limit, String offset) {
        Controller.getApi().getAllAnnounce(limit, offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                //.repeatWhen(completed -> completed.delay(5, TimeUnit.MINUTES))
                .subscribe(this::saveData
                        ,
                        error -> {
                            CheckError checkError = new CheckError();
                            checkError.checkError(getActivity(), error);
                        }
                );

    }

    private void saveData(List<Announce> matches) {
        count = matches.size();
        allNews.clear();
        allNews.addAll( matches);
        List<Announce> list = new ArrayList<>(allNews);
        adapter.dataChanged(list);
    }
}

