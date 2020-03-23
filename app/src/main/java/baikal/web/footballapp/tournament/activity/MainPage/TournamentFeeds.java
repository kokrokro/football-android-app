package baikal.web.footballapp.tournament.activity.MainPage;

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

import java.util.HashSet;
import java.util.Set;

import baikal.web.footballapp.DataSourceUtilities.LoadStates;
import baikal.web.footballapp.R;
import baikal.web.footballapp.tournament.adapter.TournamentFeedsAdapter;
import baikal.web.footballapp.viewmodel.NewsPageViewModel;

public class TournamentFeeds extends Fragment {
    final private static String TAG = "TournamentFeeds";
//    private final Logger log = LoggerFactory.getLogger(TournamentFeeds.class);

    private NewsPageViewModel newsPageViewModel;
    private TournamentFeedsAdapter newsAdapter;

    private HashSet<View> switchableViews;
    private ProgressBar progressBar;
    private TextView errorText;
    private LinearLayout emptyText;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    private String tourneyId;

    public TournamentFeeds(String tourneyId) {
        this.tourneyId = tourneyId;
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
            newsAdapter = new TournamentFeedsAdapter();
        if (recyclerView.getLayoutManager() == null)
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (recyclerView.getAdapter() == null)
            recyclerView.setAdapter(newsAdapter);

        newsPageViewModel = ViewModelProviders.of(getActivity()).get(NewsPageViewModel.class);

        boolean isDataLoaded = false;
        if (!isDataLoaded)
            loadData();

        swipeRefreshLayout.setOnRefreshListener(this::loadData);
        return view;
    }

    private void loadData()
    {
        newsPageViewModel.reloadForSingleTourney(tourneyId);
        newsPageViewModel.getLoadDataState().observe(this, this::switchViewVisible);
        newsPageViewModel.getNewsByTourneys(tourneyId).observe(this, newsAdapter::submitList);
    }
}
