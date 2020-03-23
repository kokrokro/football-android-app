package baikal.web.footballapp.tournament.activity.MainPage;


import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.DataSourceUtilities.LoadStates;
import baikal.web.footballapp.Dialogs.DialogRegion;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.model.FavoriteTourneys;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.Region;
import baikal.web.footballapp.model.User;
import baikal.web.footballapp.tournament.adapter.TourneyAdapter;
import baikal.web.footballapp.viewmodel.TournamentPageViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import androidx.core.widget.NestedScrollView;
//import android.widget.ProgressBar;

public class SearchTournaments extends Fragment{// implements DialogRegion.mListener {
    private static final String TAG = "SearchTournaments";
//    private final Logger log = LoggerFactory.getLogger(SearchTournaments.class);

    private TournamentPageViewModel tournamentPageViewModel;
    private TourneyAdapter adapter;

    private HashSet<View> switchableViews;
    private ProgressBar progressBar;
    private TextView errorText;
    private LinearLayout emptyText;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageButton openRegionFilter;

    private Region region;
    private HashSet<String> favTourneysId = new HashSet<>();

    private OnDialogEnterClicked onDialogEnterClicked;

    public interface OnDialogEnterClicked {
        void onDialogEnterClicked();
    }

    SearchTournaments(OnDialogEnterClicked onDialogEnterClicked) {
        this.onDialogEnterClicked = onDialogEnterClicked;
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            final View view = inflater.inflate(R.layout.fragment_search_tournaments, container, false);

            progressBar = view.findViewById(R.id.FST_progress);
            errorText = view.findViewById(R.id.FST_errorText);
            emptyText = view.findViewById(R.id.FST_emptyText);
            recyclerView = view.findViewById(R.id.FST_recyclerView);
            swipeRefreshLayout = view.findViewById(R.id.FST_swipe_to_refresh);

            switchableViews = new HashSet<>();
            switchableViews.add(progressBar);
            switchableViews.add(errorText);
            switchableViews.add(emptyText);
            switchableViews.add(recyclerView);

            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapter = new TourneyAdapter((id, isChecked) -> {
                if (SaveSharedPreference.getObject() == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Внимание")
                            .setMessage("Войдите или зарегистрируйтесь")
                            .setCancelable(true)
                            .setNegativeButton("Позже", (dialog, idd) -> dialog.cancel())
                            .setPositiveButton("Войти", (dialog, which) -> onDialogEnterClicked.onDialogEnterClicked())
                            .create().show();

                    Log.d(TAG, "trying to show dialog !!!");

                } else {
                    if (isChecked)
                        favTourneysId.add(id);
                    else
                        favTourneysId.remove(id);
                    setFavTourney();
                }
            });
            recyclerView.setAdapter(adapter);

            if (SaveSharedPreference.getObject() != null)
                favTourneysId.addAll(SaveSharedPreference.getObject().getUser().getFavouriteTourney());

            loadData();
            setUpSearchView(view);
            swipeRefreshLayout.setOnRefreshListener(this::loadData);
            return view;
        } catch (Exception e) {
            Log.d(TAG, e.toString());
            return null;
        }
    }

    private void setUpSearchView (View view)
    {
        SearchView searchView = view.findViewById(R.id.searchView);
        Typeface tf = Typeface.createFromAsset(Objects.requireNonNull(getActivity()).getAssets(), "fonts/manrope_regular.otf");
        SearchView.SearchAutoComplete theTextArea = searchView.findViewById(R.id.search_src_text);
        theTextArea.setTextColor(getResources().getColor(R.color.colorBottomNavigationUnChecked));
        theTextArea.setTypeface(tf);
        theTextArea.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        searchView.setQueryHint(Html.fromHtml("<font color = #63666F>" + getResources().getString(R.string.searchTourneys) + "</font>"));

        ImageView icon = searchView.findViewById(androidx.appcompat.R.id.search_button);
        icon.setColorFilter(getResources().getColor(R.color.colorLightGrayForText), PorterDuff.Mode.SRC_ATOP);

        ImageView searchViewClose = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        searchViewClose.setColorFilter(getResources().getColor(R.color.colorLightGrayForText), PorterDuff.Mode.SRC_ATOP);
        searchViewClose.setImageDrawable(getActivity().getDrawable(R.drawable.ic_close_black_24dp));

        searchView.setOnCloseListener(onSearchCloseListener);
        searchView.setOnQueryTextListener(onQueryTextListener);

        openRegionFilter = view.findViewById(R.id.filterRegionButton);
        openRegionFilter.setOnClickListener(v-> {
                    FragmentManager fm = getChildFragmentManager();
                    DialogRegion dialogRegion =  new DialogRegion(r -> {
                        region = r;
                        if (r != null)
                            openRegionFilter.setColorFilter(getResources().getColor(R.color.Black), PorterDuff.Mode.MULTIPLY);
                        else
                            openRegionFilter.clearColorFilter();
                        tournamentPageViewModel.onQueryTextSubmit(null, region == null?null:region.getId());
                    });
                    dialogRegion.show(fm, "fragment_edit_name");
                }
        );
    }

    private void loadData () {
        tournamentPageViewModel.clearSearchAndReload();
        tournamentPageViewModel.getLoadDataState().observe(this, this::switchViewVisible);
        tournamentPageViewModel.getTourneys().observe(this, adapter::submitList);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tournamentPageViewModel = ViewModelProviders.of(getActivity()).get(TournamentPageViewModel.class);
    }

    private void setFavTourney()
    {
        FavoriteTourneys favTourneyNew = new FavoriteTourneys(favTourneysId);

        String id;
        String token;
        try {
            id = SaveSharedPreference.getObject().getUser().getId();
            token = SaveSharedPreference.getObject().getToken();
        } catch (Exception e) {
            Log.e("SearchTourney", e.toString());
            return;
        }

        Controller.getApi().editPlayerInfo(id, token, favTourneyNew).enqueue(new Callback<Person>() {
            @Override
            public void onResponse(@NonNull Call<Person> call, @NonNull Response<Person> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = SaveSharedPreference.getObject();
                    user.setUser(response.body());
                    SaveSharedPreference.saveObject(user);
                    favTourneysId.clear();
                    favTourneysId.addAll(user.getUser().getFavouriteTourney());
                    tournamentPageViewModel.setFavTourneysIds(new ArrayList<>(favTourneysId));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Person> call, @NonNull Throwable t) {
                Log.d("SearchTournaments: ", "can\'t edit player info...");
            }
        });
    }

    private SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            tournamentPageViewModel.onQueryTextSubmit(query, region == null?null:region.getId());
            return false;
        }
        @Override
        public boolean onQueryTextChange(String newText) {
            tournamentPageViewModel.onQueryTextChange(newText, region == null?null:region.getId());
            return false;
        }
    };

    private SearchView.OnCloseListener onSearchCloseListener = new SearchView.OnCloseListener() {
        @Override
        public boolean onClose() {
            tournamentPageViewModel.clearSearchAndReload();
            region = null;
            openRegionFilter.clearColorFilter();

            return false;
        }
    };
}
