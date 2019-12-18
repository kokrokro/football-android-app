package baikal.web.footballapp.players.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import baikal.web.footballapp.R;
import baikal.web.footballapp.players.adapter.PlayersAdapter;
import baikal.web.footballapp.players.adapter.RecyclerViewPlayersAdapter;
import baikal.web.footballapp.players.datasource.LoadStates;
import baikal.web.footballapp.viewmodel.PlayersPageViewModel;

public class PlayersPage extends Fragment {
    private static final String TAG = "PlayerPage";
    private final Logger log = LoggerFactory.getLogger(PlayersPage.class);
    private final List<String> allPeople = new ArrayList<>();
    public RecyclerViewPlayersAdapter adapter;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private PlayersPageViewModel playersPageViewModel;
    private PlayersAdapter playersAdapter;
    private HashSet<View> switchableViews;
    private ProgressBar progressBar;
    private TextView errorText;
    private TextView emptyText;

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
                showOneView(errorText, switchableViews);
                break;

            case Empty:
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
        emptyText = view.findViewById(R.id.emptyText);
        recyclerView = view.findViewById(R.id.recyclerViewPlayers);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        switchableViews = new HashSet<>();
        switchableViews.add(progressBar);
        switchableViews.add(errorText);
        switchableViews.add(emptyText);
        switchableViews.add(recyclerView);


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(null); // вырубил ради индикатора загрузки (анимация смены списка выглядит плохо)

        playersAdapter = new PlayersAdapter(getFragmentManager());
        recyclerView.setAdapter(playersAdapter);

        playersPageViewModel = ViewModelProviders.of(getActivity()).get(PlayersPageViewModel.class);

        playersPageViewModel.clearSearchAndReload();

        playersPageViewModel.getLoadDataState().observe(this, this::switchViewVisible);
        playersPageViewModel.getPlayers().observe(this, playersAdapter::submitList);


        return view;
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
        searchView = (SearchView) searchItem.getActionView();

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



