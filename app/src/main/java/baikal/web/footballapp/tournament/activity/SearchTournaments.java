package baikal.web.footballapp.tournament.activity;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
//import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
//import android.widget.ProgressBar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import baikal.web.footballapp.CheckError;
import baikal.web.footballapp.Controller;
import baikal.web.footballapp.MankindKeeper;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.model.EditProfile;
import baikal.web.footballapp.model.Region;
import baikal.web.footballapp.model.Tourney;
import baikal.web.footballapp.players.activity.PlayersPage;
import baikal.web.footballapp.tournament.adapter.RVTourneyAdapter;
import baikal.web.footballapp.viewmodel.MainViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchTournaments extends Fragment implements DialogRegion.mListener {
//    private static final String TAG = "Search_tournaments";
    //    private int count = 0;
//    private int offset = 0;
//    private final int limit = 10;
    //    RecyclerViewPlayersAdapter adapter;
    private final Logger log = LoggerFactory.getLogger(PlayersPage.class);
    private static RVTourneyAdapter adapter;
    //    private final List<League> allLeagues= new ArrayList<>();
    private List<Tourney> tourneyList = new ArrayList<>();
    private List<String> regionsId = new ArrayList<>();
    private List<String> regionsNames = new ArrayList<>();
    private List<String> favTourneysId = new ArrayList<>();

    private MainViewModel mainViewModel;

    SearchTournaments() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ProgressDialog mProgressDialog = new ProgressDialog(getActivity(), R.style.MyProgressDialogTheme);
        mProgressDialog.setIndeterminate(true);
        final View view = inflater.inflate(R.layout.fragment_search_tournaments, container, false);

//        NestedScrollView scroller = view.findViewById(R.id.scrollerPlayersPage);
//        ProgressBar progressBar = view.findViewById(R.id.progressSearch);

        SearchView searchView = view.findViewById(R.id.searchView);
        ImageButton filter = view.findViewById(R.id.filterRegionButton);
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
        tourneyList = new ArrayList<>(MankindKeeper.getInstance().allTourneys);
        List<Region> regions = new ArrayList<>(MankindKeeper.getInstance().regions);
        mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);

        String userId = null;
        try {
            userId = SaveSharedPreference.getObject().getUser().getId();
        } catch (Exception ignored) { }

        mainViewModel.getFavTourney(userId).observe(this, tourneys -> {
            favTourneysId.clear();
            for(Tourney tr :tourneys)
                favTourneysId.add(tr.getId());

            adapter.notifyDataSetChanged();
        });
        for( Region reg : regions){
            regionsId.add(reg.getId());
            regionsNames.add(reg.getName());
        }
        try {
            RecyclerView recyclerView = view.findViewById(R.id.recyclerViewSearch);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapter = new RVTourneyAdapter(tourneyList, getActivity(), favTourneysId, (id,isChecked)-> {
                if (isChecked)
                    favTourneysId.add(id);
                else
                    favTourneysId.remove(id);

                Log.d("checked", isChecked+" "+id);
            });
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            log.error("ERROR: ", e);
        }
        filter.setOnClickListener(v-> {
                    FragmentManager fm = getChildFragmentManager();
                    DialogRegion dialogRegion =  new DialogRegion(regionsNames);

                    dialogRegion.show(fm, "fragment_edit_name");
                }
        );
//        scroller.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
//            if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
//                offset++;
//                int temp = limit*offset;
//                if (temp<=count) {
//                    String str = "";
//                    GetAllTournaments(str);
//                }
//            }
//        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchTournamentss(query,null);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                SearchTournamentss(newText,null);
                return false;
            }
        });
        return view;
    }

    void setFavTourneys()
    {
        List<RequestBody> favTourneyNew = new ArrayList<>();

        for(int i = 0; i < favTourneysId.size(); i++){
            favTourneyNew.add(RequestBody.create(MediaType.parse("text/plain"),favTourneysId.get(i)));
        }
        Controller.getApi().editPlayerInfo(SaveSharedPreference.getObject().getUser().getId(),SaveSharedPreference.getObject().getToken(),favTourneyNew).enqueue(new Callback<EditProfile>() {
            @Override
            public void onResponse(@NonNull Call<EditProfile> call, @NonNull Response<EditProfile> response) {
                StringBuilder tourneyIds = new StringBuilder();
                for (String ft : favTourneysId)
                    tourneyIds.append(",").append(ft);
                changeDataForTournamentFragment(tourneyIds.toString());
            }

            @Override
            public void onFailure(@NonNull Call<EditProfile> call, @NonNull Throwable t) {
                Log.d("SearchTournaments: ", "can\'t edit player info...");
            }
        });

    }

    @SuppressLint("CheckResult")
    private void changeDataForTournamentFragment (String tourneyIds)
    {
        //noinspection ResultOfMethodCallIgnored
        Controller.getApi().getTourneysById(tourneyIds)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( this::saveDataForTournamentFragment
                        , error -> {
                            Log.d("SearchTournaments: ", "error while getting tourneys by id (207):\n\t" + error.toString());
                            CheckError checkError = new CheckError();
                            checkError.checkError(getActivity(), error);
                        }
                );
    }

    private void saveDataForTournamentFragment (List<Tourney> tourneys) {
        mainViewModel.setFavTourney(tourneys);
    }

    private void saveAllData(List<Tourney> tourneys) {
//        count = tourneys.size();
        this.tourneyList.clear();
        this.tourneyList.addAll(tourneys);
        adapter.notifyDataSetChanged();
    }

    @SuppressLint("CheckResult")
    private void SearchTournamentss(String search, String region){
//        PersonalActivity.people.clear();
//        String type = "player";
        if (search!=null && search.equals("")) {
            saveAllData(MankindKeeper.getInstance().allTourneys);
        } else {

            //noinspection ResultOfMethodCallIgnored
            Controller.getApi().getTourneys(search,region)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::saveAllData,
                            error -> {
                                CheckError checkError = new CheckError();
                                checkError.checkError(getActivity(), error);
                            }
                    );
        }

    }
    @Override
    public void onPause() {

        super.onPause();
        setFavTourneys();
    }
    @Override
    public void onFinishEditDialog(int pos) {
        SearchTournamentss(null,regionsId.get(pos));
    }
}
