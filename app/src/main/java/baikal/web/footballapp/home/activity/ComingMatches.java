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
import baikal.web.footballapp.home.adapter.UpcomingMatchesAdapter;
import baikal.web.footballapp.viewmodel.ActiveMatchPageViewModel;
import baikal.web.footballapp.viewmodel.ActiveMatchPageViewModel2;

public class ComingMatches extends Fragment {
    final private static String TAG = "ComingMatches";
    private final Logger log = LoggerFactory.getLogger(ComingMatches.class);

    private ActiveMatchPageViewModel activeMatchPageViewModel;
    private ActiveMatchPageViewModel2 activeMatchPageViewModel2;

    private UpcomingMatchesAdapter upcomingMatchesAdapter;

    private HashSet<View> switchableViews;
    private ProgressBar progressBar;
    private TextView errorText;
    private LinearLayout emptyText;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private UpcomingMatchesAdapter.OnItemListener onItemListener;

    private boolean useSecondViewModel;

    ComingMatches(UpcomingMatchesAdapter.OnItemListener onItemListener, boolean useSecondViewModel){
        this.onItemListener = onItemListener;
        this.useSecondViewModel = useSecondViewModel;
    }

    private static void showOneView(View viewToShow, Set<View> views) {
        for (View view : views) {
            if (view.hashCode() == viewToShow.hashCode()) {
                view.setVisibility(View.VISIBLE);
            } else {
                view.setVisibility(View.GONE);
            }
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
        view = inflater.inflate(R.layout.coming_matches, container, false);
        progressBar = view.findViewById(R.id.CM_progress);
        errorText = view.findViewById(R.id.CM_errorText);
        emptyText = view.findViewById(R.id.CM_emptyText);

        swipeRefreshLayout = view.findViewById(R.id.CM_swipe_to_refresh_layout);
        recyclerView = view.findViewById(R.id.recyclerViewComingMatches);

        switchableViews = new HashSet<>();
        switchableViews.add(progressBar);
        switchableViews.add(errorText);
        switchableViews.add(emptyText);
        switchableViews.add(recyclerView);

        upcomingMatchesAdapter = new UpcomingMatchesAdapter(onItemListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(upcomingMatchesAdapter);

        if (useSecondViewModel) {
            activeMatchPageViewModel2 = ViewModelProviders.of(getActivity()).get(ActiveMatchPageViewModel2.class);
            swipeRefreshLayout.setEnabled(false);
        } else
            activeMatchPageViewModel = ViewModelProviders.of(getActivity()).get(ActiveMatchPageViewModel.class);

//        if (!isDataLoaded)
        loadData();

        swipeRefreshLayout.setOnRefreshListener(this::loadData);
        return view;
    }

    public void loadData () {
        if (useSecondViewModel) {
            activeMatchPageViewModel2.reload();
            activeMatchPageViewModel2.getLoadDataState().observe(this, this::switchViewVisible);
            activeMatchPageViewModel2.getMatches().observe(this, upcomingMatchesAdapter::submitList);
        } else {
            activeMatchPageViewModel.reload();
            activeMatchPageViewModel.getLoadDataState().observe(this, this::switchViewVisible);
            activeMatchPageViewModel.getMatches().observe(this, upcomingMatchesAdapter::submitList);
        }
    }
}
