package baikal.web.footballapp.home.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import baikal.web.footballapp.CheckError;
import baikal.web.footballapp.Controller;
import baikal.web.footballapp.R;
import baikal.web.footballapp.home.adapter.RecyclerViewHomeAdapter;
import baikal.web.footballapp.model.News;
import baikal.web.footballapp.model.News_;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsFragment extends Fragment {
    Logger log = LoggerFactory.getLogger(NewsFragment.class);
    private final List<News_> allNews = new ArrayList<>();
    private RecyclerView recyclerView;
    private NestedScrollView scroller;
    private RecyclerViewHomeAdapter adapter;
    private int count = 0;
    private final int limit = 5;
    private int offset = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        final View view;
        view = inflater.inflate(R.layout.page_home, container, false);
        scroller = view.findViewById(R.id.scrollerNews);
//        checkConnection();
        recyclerView = view.findViewById(R.id.recyclerViewHome);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        GetAllNews("20","0");
        try {
            adapter = new RecyclerViewHomeAdapter(getActivity(), NewsFragment.this , allNews);
            recyclerView.setAdapter(adapter);
        }catch (Exception e){}
        scroller.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                offset++;
                int temp = limit*offset;
                if (temp<=count) {
                    String str = String.valueOf(temp);
                    GetAllNews("5", str);
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
                    if (isConnected){
//                        GetAllNews("5", "0");
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void GetAllNews(String limit, String offset){
        Controller.getApi().getAllNewsCrud(limit, offset).enqueue(new Callback<List<News_>>() {
            @Override
            public void onResponse(Call<List<News_>> call, Response<List<News_>> response) {
                if(response.isSuccessful()){
                    if(response.body()!=null){
                        allNews.clear();
                        allNews.addAll(response.body());
                        adapter.notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onFailure(Call<List<News_>> call, Throwable t) {
//                CheckError checkError = new CheckError();
//                checkError.checkError(getActivity(), t);
            }
        });

    }

//    private void saveData(News matches) {
//        count = matches.getCount();
//        allNews.addAll(allNews.size(), matches.getNews());
//        List<News_> list = new ArrayList<>(allNews);
//        adapter.dataChanged(list);
//    }
}
