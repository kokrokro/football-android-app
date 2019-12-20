package baikal.web.footballapp.home.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.R;
import baikal.web.footballapp.home.adapter.RVComingMatchesAdapter;
import baikal.web.footballapp.home.adapter.RVHorizontalNews;
import baikal.web.footballapp.model.ActiveMatch;
import baikal.web.footballapp.model.News_;
import baikal.web.footballapp.viewmodel.MainViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsAdsAndUpcomingMatches extends Fragment {

    private List<ActiveMatch> matches = new ArrayList<>();
    private List<News_> allNews = new ArrayList<>();
    private List<String> leagues;

    private RVComingMatchesAdapter adapter;

    private int offset=0;
    private final int limit = 15;

    NewsAdsAndUpcomingMatches (List<String> favLeagues){
        this.leagues = favLeagues;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_page_fragment, container, false);

        NestedScrollView scrollView = view.findViewById(R.id.MPF_nested_scroll_view);

        RecyclerView newsRV = view.findViewById(R.id.MPF_news_RV);
        RecyclerView matchesRV = view.findViewById(R.id.MPF_matches_RV);
        newsRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        matchesRV.setLayoutManager(new LinearLayoutManager(getContext()));

        RVHorizontalNews adapter1 = new RVHorizontalNews(getContext(), allNews);
        newsRV.setAdapter(adapter1);

        adapter = new RVComingMatchesAdapter(getActivity(), matches);
        matchesRV.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        MainViewModel mainViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(MainViewModel.class);
        mainViewModel.getNews("10","0").observe(this,
                adapter1::dataChanged);

        scrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                offset++;
                int temp = limit*offset;
                if (temp<=matches.size()) {
                    String str = "" + offset;
                    getUpcomingMatches(String.valueOf(limit), str);
                }
            }
        });

        return view;
    }

    private void getUpcomingMatches (String limit, String offset)
    {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", getResources().getConfiguration().locale);
        String strDate =">="+sdf.format(now);
        StringBuilder query = new StringBuilder();
        for(String l : leagues){
            query.append(",").append(l);
        }
        Controller.getApi().getUpcomingMatches(strDate, query.toString(), limit, offset).enqueue(new Callback<List<ActiveMatch>>() {
            @Override
            public void onResponse(@NonNull Call<List<ActiveMatch>> call, @NonNull Response<List<ActiveMatch>> response) {
                if(response.isSuccessful())
                    if(response.body()!=null){
                        matches.addAll(response.body());
                        adapter.notifyDataSetChanged();
                    }
            }

            @Override
            public void onFailure(@NonNull Call<List<ActiveMatch>> call, @NonNull Throwable t) { }
        });
    }
}
