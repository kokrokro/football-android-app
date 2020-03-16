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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

import baikal.web.footballapp.DataSourceUtilities.LoadStates;
import baikal.web.footballapp.R;
import baikal.web.footballapp.home.adapter.HorizontalNewsAdapter;
import baikal.web.footballapp.viewmodel.NewsPageViewModel;

public class HorizontalNewsFragment extends Fragment {
    final private static String TAG = "NewsAndAds";
    private final Logger log = LoggerFactory.getLogger(HorizontalNewsFragment.class);

    private NewsPageViewModel newsPageViewModel;
    private HorizontalNewsAdapter newsAdapter;

    private HashSet<View> switchableViews;
    private ProgressBar progressBar;
    private TextView errorText;
    private LinearLayout emptyText;

    private RecyclerView recyclerView;
    private HorizontalNewsAdapter.OnItemListener onItemListener;

    private boolean isDataLoaded = false;

    HorizontalNewsFragment (HorizontalNewsAdapter.OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
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
                showOneView(recyclerView, switchableViews);
                break;

            case Error:
                Log.d(TAG, "NO CONNECTION");
                showOneView(errorText, switchableViews);
                break;

            case Empty:
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
        view = inflater.inflate(R.layout.horizontal_news_fragment, container, false);
        progressBar = view.findViewById(R.id.HNF_progress);
        errorText = view.findViewById(R.id.HNF_errorText);
        emptyText = view.findViewById(R.id.HNF_emptyText);

        recyclerView = view.findViewById(R.id.HNF_news_RV);

        switchableViews = new HashSet<>();
        switchableViews.add(progressBar);
        switchableViews.add(errorText);
        switchableViews.add(emptyText);
        switchableViews.add(recyclerView);

        newsAdapter = new HorizontalNewsAdapter(onItemListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(newsAdapter);

        newsPageViewModel = ViewModelProviders.of(getActivity()).get(NewsPageViewModel.class);

//        if (!isDataLoaded)
        loadData();

        return view;
    }

    public void loadData ()
    {
        isDataLoaded = true;
        newsPageViewModel.reload();
        newsPageViewModel.getLoadDataState().observe(this, this::switchViewVisible);
        newsPageViewModel.getNews().observe(this, newsAdapter::submitList);
    }
}
