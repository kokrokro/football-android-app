package baikal.web.footballapp.home.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import baikal.web.footballapp.R;
import baikal.web.footballapp.home.adapter.HorizontalNewsAdapter;
import baikal.web.footballapp.home.adapter.UpcomingMatchesAdapter;

public class NewsAdsAndUpcomingMatches extends Fragment {
    final private static String TAG = "NewsAds&UpcomingMatches";
    private final Logger log = LoggerFactory.getLogger(NewsAdsAndUpcomingMatches.class);

    private UpcomingMatchesAdapter.OnItemListener onMatchListener;
    private HorizontalNewsAdapter.OnItemListener onFeedListener;

    private SwipeRefreshLayout swipeRefreshLayout;

    private HorizontalNewsFragment horizontalNewsFragment;
    private ComingMatches comingMatches;

    NewsAdsAndUpcomingMatches(UpcomingMatchesAdapter.OnItemListener onItemListener,
                              HorizontalNewsAdapter.OnItemListener onFeedListener){
        this.onMatchListener = onItemListener;
        this.onFeedListener = onFeedListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_page_fragment, container, false);
        swipeRefreshLayout = view.findViewById(R.id.MPF_swipe_to_refresh);

        swipeRefreshLayout.setOnRefreshListener(this::loadData);

        FragmentManager fragmentManager = getChildFragmentManager();

        horizontalNewsFragment = new HorizontalNewsFragment(onFeedListener);
        fragmentManager.beginTransaction().add(R.id.MPF_HRV_holder, horizontalNewsFragment).commit();

        comingMatches = new ComingMatches(onMatchListener, true);
        fragmentManager.beginTransaction().add(R.id.MPF_VRV_holder, comingMatches).commit();

        return view;
    }

    private void loadData() {
        swipeRefreshLayout.setRefreshing(false);
        comingMatches.loadData();
        horizontalNewsFragment.loadData();
    }
}
