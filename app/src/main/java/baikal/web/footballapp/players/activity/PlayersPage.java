package baikal.web.footballapp.players.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import baikal.web.footballapp.DataSourceUtilities.LoadStates;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.players.adapter.PlayersAdapter;
import baikal.web.footballapp.viewmodel.PlayersPageViewModel;

public class PlayersPage extends Fragment {
    private static final String TAG = "PlayerPage";
    private final Logger log = LoggerFactory.getLogger(PlayersPage.class);

    private PlayersPageViewModel playersPageViewModel;
    private PlayersAdapter playersAdapter;

    private HashSet<View> switchableViews;
    private ProgressBar progressBar;
    private TextView errorText;
    private LinearLayout emptyText;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private PlayersAdapter.OnItemListener mOnItemListener;
    private PlayersAdapter.OnSwitchListener mOnSwitchListener;

    private Team team;
    private List<String> teamIds;

    public PlayersPage (Team team, List<String> teamIds, PlayersAdapter.OnItemListener mOnItemListener, PlayersAdapter.OnSwitchListener mOnSwitchListener) {
        this.mOnItemListener = mOnItemListener;
        this.mOnSwitchListener = mOnSwitchListener;
        this.team = team;
        this.teamIds = teamIds;
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
                showOneView(errorText, switchableViews);
                break;

            case Empty:
                swipeRefreshLayout.setRefreshing(false);
                showOneView(emptyText, switchableViews);
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.page_players2, container, false);
        final Toolbar toolbar = view.findViewById(R.id.toolbarPlayers);
        progressBar = view.findViewById(R.id.progress);
        errorText = view.findViewById(R.id.errorText);
        emptyText = view.findViewById(R.id.PP2_emptyText);
        recyclerView = view.findViewById(R.id.recyclerViewPlayers);
        swipeRefreshLayout = view.findViewById(R.id.PP_swipe_to_refresh);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        switchableViews = new HashSet<>();
        switchableViews.add(progressBar);
        switchableViews.add(errorText);
        switchableViews.add(emptyText);
        switchableViews.add(recyclerView);

        playersAdapter = new PlayersAdapter(mOnItemListener, mOnSwitchListener, team, teamIds);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(null); // вырубил ради индикатора загрузки (анимация смены списка выглядит плохо)
//        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(playersAdapter);

        playersPageViewModel = ViewModelProviders.of(getActivity()).get(PlayersPageViewModel.class);
        loadData();

        swipeRefreshLayout.setOnRefreshListener(this::loadData);

        return view;
    }

    private void loadData ()
    {
        playersPageViewModel.clearSearchAndReload();
        playersPageViewModel.getLoadDataState().observe(this, this::switchViewVisible);
        playersPageViewModel.getPlayers().observe(this, playersAdapter::submitList);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.player_search, menu);
//        searchView.setIconified(true);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                playersPageViewModel.clearSearchAndReload();
                return true;
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                playersPageViewModel.onQueryTextSubmit(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                playersPageViewModel.onQueryTextChange(newText);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, menuInflater);
    }
}



