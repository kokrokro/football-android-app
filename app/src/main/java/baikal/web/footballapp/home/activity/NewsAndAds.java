package baikal.web.footballapp.home.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

import baikal.web.footballapp.DataSourceUtilities.LoadStates;
import baikal.web.footballapp.R;
import baikal.web.footballapp.home.adapter.NewsAdapter;
import baikal.web.footballapp.viewmodel.NewsPageViewModel;

public class NewsAndAds extends Fragment {
    final private static String TAG = "NewsAndAds";
    private final Logger log = LoggerFactory.getLogger(NewsAndAds.class);

    private NewsPageViewModel newsPageViewModel;
    private NewsAdapter newsAdapter;

    private HashSet<View> switchableViews;
    private ProgressBar progressBar;
    private TextView errorText;
    private LinearLayout emptyText;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private NewsAdapter.OnItemListener onItemListener;

    private boolean isDataLoaded = false;

    NewsAndAds(NewsAdapter.OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    private static void showOneView(View viewToShow, Set<View> views) {
        for (View view : views) {
            if (view.hashCode() == viewToShow.hashCode())
                view.setVisibility(View.VISIBLE);
            else
                view.setVisibility(View.GONE);
        }
    }

    private void switchViewVisible(LoadStates loadState) {
        switch (loadState) {
            case Loading:
                showOneView(progressBar, switchableViews);
                break;

            case Loaded:
                swipeRefreshLayout.setRefreshing(false);
                showOneView(recyclerView, switchableViews);
                break;

            case Error:
                swipeRefreshLayout.setRefreshing(false);
                Log.d(TAG, "NO CONNECTION");
                showOneView(errorText, switchableViews);
                break;

            case Empty:
                swipeRefreshLayout.setRefreshing(false);
                showOneView(emptyText, switchableViews);
                break;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        Log.d(TAG, " FRAGMENT HAS BEEN CREATED !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view;
        view = inflater.inflate(R.layout.activity_main_page, container, false);
        progressBar = view.findViewById(R.id.AMP_progress);
        errorText = view.findViewById(R.id.AMP_errorText);
        emptyText = view.findViewById(R.id.AMP_emptyText);

        swipeRefreshLayout = view.findViewById(R.id.AMP_swipe_to_refresh);
        recyclerView = view.findViewById(R.id.recyclerViewMainNews);

        switchableViews = new HashSet<>();
        switchableViews.add(progressBar);
        switchableViews.add(errorText);
        switchableViews.add(emptyText);
        switchableViews.add(recyclerView);

        if (newsAdapter == null)
            newsAdapter = new NewsAdapter(onItemListener);
        if (recyclerView.getLayoutManager() == null)
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (recyclerView.getAdapter() == null)
            recyclerView.setAdapter(newsAdapter);

        newsPageViewModel = ViewModelProviders.of(getActivity()).get(NewsPageViewModel.class);

        if (!isDataLoaded)
            loadData();

        swipeRefreshLayout.setOnRefreshListener(this::loadData);
        return view;
    }

    private void loadData()
    {
        isDataLoaded = true;
        newsPageViewModel.reload();
        newsPageViewModel.getLoadDataState().observe(this, this::switchViewVisible);
        newsPageViewModel.getNews().observe(this, newsAdapter::submitList);
    }
}