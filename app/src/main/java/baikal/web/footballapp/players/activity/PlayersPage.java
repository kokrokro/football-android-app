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
import java.util.List;

import baikal.web.footballapp.R;
import baikal.web.footballapp.players.adapter.PlayersAdapter;
import baikal.web.footballapp.players.adapter.RecyclerViewPlayersAdapter;
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

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.page_players2, container, false);
        final ProgressBar progressBar = view.findViewById(R.id.progress);
        final TextView errorText = view.findViewById(R.id.errorText);
        final Toolbar toolbar = view.findViewById(R.id.toolbarPlayers);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);


        recyclerView = view.findViewById(R.id.recyclerViewPlayers);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(null); // вырубил ради индикатора загрузки (анимация смены списка выглядит плохо)

        playersAdapter = new PlayersAdapter(getFragmentManager());
        recyclerView.setAdapter(playersAdapter);

        playersPageViewModel = ViewModelProviders.of(getActivity()).get(PlayersPageViewModel.class);

        playersPageViewModel.clearSearchAndReload();

        playersPageViewModel.getLoadDataState().observe(this, loadState -> {
            switch (loadState) {
                case Loading:
                    progressBar.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    errorText.setVisibility(View.GONE);
                    break;

                case Loaded:
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    errorText.setVisibility(View.GONE);
                    break;

                case Error:
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    errorText.setVisibility(View.VISIBLE);
                    break;
            }
        });
        playersPageViewModel.getPlayers().observe(this, playersAdapter::submitList);

//        searchView = view.findViewById(R.id.searchView);

//        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/manrope_regular.otf");
//        SearchView.SearchAutoComplete theTextArea = searchView.findViewById(R.id.search_src_text);
//        theTextArea.setTextColor(getResources().getColor(R.color.colorBottomNavigationUnChecked));
//        theTextArea.setTypeface(tf);
//        theTextArea.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
//        searchView.setQueryHint(Html.fromHtml("<font color = #63666F>" + getResources().getString(R.string.search) + "</font>"));
//        ImageView icon = searchView.findViewById(androidx.appcompat.R.id.search_button);
//        icon.setColorFilter(getResources().getColor(R.color.colorLightGrayForText), PorterDuff.Mode.SRC_ATOP);
//        ImageView searchViewClose = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
//        searchViewClose.setColorFilter(getResources().getColor(R.color.colorLightGrayForText), PorterDuff.Mode.SRC_ATOP);

//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                playersPageViewModel.onQueryTextSubmit(query);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                playersPageViewModel.onQueryTextChange(newText);
//                return false;
//            }
//        });
//
//        searchView.setOnCloseListener(() -> {
//            playersPageViewModel.clearSearchAndReload();
//            return false;
//        });


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



