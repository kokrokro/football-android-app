package baikal.web.footballapp.home.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.DateToString;
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
    private LinearLayout layout;
    private RVComingMatchesAdapter adapter;

    private DateToString dts = new DateToString();

    NewsAdsAndUpcomingMatches (List<String> favLeagues){
        this.leagues = favLeagues;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_page_fragment, container, false);

        layout = view.findViewById(R.id.MPF_emptyComingMatches);
        RecyclerView newsRV = view.findViewById(R.id.MPF_news_RV);
        RecyclerView matchesRV = view.findViewById(R.id.MPF_matches_RV);
        newsRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        matchesRV.setLayoutManager(new LinearLayoutManager(getContext()));

        RVHorizontalNews adapter1 = new RVHorizontalNews(getContext(), allNews);
        newsRV.setAdapter(adapter1);

        adapter = new RVComingMatchesAdapter(getActivity(), matches);
        matchesRV.setAdapter(adapter);

        MainViewModel mainViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(MainViewModel.class);
        mainViewModel.getNews("20","0").observe(getViewLifecycleOwner(), adapter1::dataChanged);

        getUpcomingMatches (0);
        return view;
    }

    private void getUpcomingMatches (int dp)
    {
        if (dp==3)
            return;
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", getResources().getConfiguration().locale);
        String strDate = ">=" + sdf.format(now);
        StringBuilder query = new StringBuilder();
        for(String l : leagues)
            query.append(",").append(l);

        Controller.getApi().getUpcomingMatches(strDate, query.toString(), "50", "0").enqueue(new Callback<List<ActiveMatch>>() {
            @Override
            public void onResponse(@NonNull Call<List<ActiveMatch>> call, @NonNull Response<List<ActiveMatch>> response) {
                if (!response.isSuccessful()) {
                    getUpcomingMatches(dp+1);
                    return;
                }

                if(response.isSuccessful())
                    if (response.body() != null)
                        adapter.dataChanged(getNowDayMatches(response.body()));
            }

            @Override
            public void onFailure(@NonNull Call<List<ActiveMatch>> call, @NonNull Throwable t) {
                layout.setVisibility(View.GONE);
            }
        });
    }

    private List<ActiveMatch> getNowDayMatches (List<ActiveMatch> oldList) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);

        Date date = calendar.getTime();

        List<ActiveMatch> ans = new ArrayList<>();

        for (ActiveMatch match: oldList) {
            Date d = dts.getDate(match.getDate());

            if (date.before(d))
                break;

            ans.add(match);
        }

        if (ans.size() > 0)
            layout.setVisibility(View.GONE);

        return ans;
    }
}
