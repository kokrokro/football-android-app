package baikal.web.footballapp.tournament.activity;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.core.widget.NestedScrollView;
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
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import baikal.web.footballapp.CheckError;
import baikal.web.footballapp.Controller;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.EditProfile;
import baikal.web.footballapp.model.GetLeagueInfo;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.LeagueInfo;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.Region;
import baikal.web.footballapp.model.Tournaments;
import baikal.web.footballapp.model.Tourney;
import baikal.web.footballapp.players.activity.PlayersPage;
import baikal.web.footballapp.players.adapter.RecyclerViewPlayersAdapter;
import baikal.web.footballapp.tournament.adapter.RVTourneyAdapter;
import baikal.web.footballapp.tournament.adapter.RecyclerViewTournamentAdapter;
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
    private static final String TAG = "Search_tournaments";
    private RecyclerView recyclerView;
//    private int count = 0;
//    private int offset = 0;
//    private final int limit = 10;
    //    RecyclerViewPlayersAdapter adapter;
    private final Logger log = LoggerFactory.getLogger(PlayersPage.class);
    private SearchView searchView;
    private ProgressBar progressBar;
    private static RVTourneyAdapter adapter;
    private ProgressDialog mProgressDialog;
    private NestedScrollView scroller;
//    private final List<League> tournaments= new ArrayList<>();
    private List<Tourney> tourneyList = new ArrayList<>();
    private ImageButton filter;
    private List<Region> regions = new ArrayList<>();
    private List<String> regionsId = new ArrayList<>();
    private List<String> regionsNames = new ArrayList<>();
    private List<String> favTourneysId = new ArrayList<>();
    public SearchTournaments() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mProgressDialog = new ProgressDialog(getActivity(), R.style.MyProgressDialogTheme);
        mProgressDialog.setIndeterminate(true);
        final View view = inflater.inflate(R.layout.fragment_search_tournaments, container, false);
        scroller = view.findViewById(R.id.scrollerPlayersPage);
        searchView = view.findViewById(R.id.searchView);
        progressBar = view.findViewById(R.id.progressSearch);
        filter = view.findViewById(R.id.filterRegionButton);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/manrope_regular.otf");
        SearchView.SearchAutoComplete theTextArea = searchView.findViewById(R.id.search_src_text);
        theTextArea.setTextColor(getResources().getColor(R.color.colorBottomNavigationUnChecked));
        theTextArea.setTypeface(tf);
        theTextArea.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        searchView.setQueryHint(Html.fromHtml("<font color = #63666F>" + getResources().getString(R.string.searchTourneys) + "</font>"));
        ImageView icon = searchView.findViewById(androidx.appcompat.R.id.search_button);
        icon.setColorFilter(getResources().getColor(R.color.colorLightGrayForText), PorterDuff.Mode.SRC_ATOP);
        ImageView searchViewClose = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        searchViewClose.setColorFilter(getResources().getColor(R.color.colorLightGrayForText), PorterDuff.Mode.SRC_ATOP);
        tourneyList = new ArrayList<>(PersonalActivity.allTourneys);
        regions = new ArrayList<>(PersonalActivity.regions);
        MainViewModel mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        mainViewModel.getFavTourney(PersonalActivity.id).observe(this, tourneys -> {
            favTourneysId.clear();
            for(Tourney tr :tourneys){
                favTourneysId.add(tr.getId());
            }
            adapter.notifyDataSetChanged();
        });
        for( Region reg : regions){
            regionsId.add(reg.getId());
            regionsNames.add(reg.getName());
        }
        try {
            recyclerView = view.findViewById(R.id.recyclerViewSearch);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapter = new RVTourneyAdapter(tourneyList, getActivity(), favTourneysId, (id,isChecked)-> {
//                favTourneys.clear();
                        if (isChecked) {
                            favTourneysId.add(id);
                        } else {
                            favTourneysId.remove(id);

                        }
                        Log.d("checked", isChecked+" "+id);
                        List<RequestBody> favTourneyNew = new ArrayList<>();
                        for(int i = 0; i < favTourneysId.size(); i++){
                            favTourneyNew.add(RequestBody.create(MediaType.parse("text/plain"),favTourneysId.get(i)));
                        }
                        Controller.getApi().editPlayerInfo(PersonalActivity.id,PersonalActivity.token,favTourneyNew).enqueue(new Callback<EditProfile>() {
                            @Override
                            public void onResponse(Call<EditProfile> call, Response<EditProfile> response) {
                            }

                            @Override
                            public void onFailure(Call<EditProfile> call, Throwable t) {
                            }
                        });
                        mainViewModel.setFavTourneysId(favTourneysId);
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
                SearchTournaments(query,null);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                SearchTournaments(newText,null);
                return false;
            }
        });
        return view;
    }

    private void saveAllData(List<Tourney> tourneys) {
//        count = tourneys.size();
        this.tourneyList.clear();
        this.tourneyList.addAll(tourneys);
        adapter.dataChanged(tourneyList);
    }
//    private void saveData(GetLeagueInfo getLeagueInfo) {
//        LeagueInfo tournament1 = getLeagueInfo.getLeagueInfo();
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("TOURNAMENTINFO", tournament1);
//        Tournament tournament = new Tournament();
//        tournament.setArguments(bundle);
//        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//        try{
//            fragmentManager.beginTransaction().add(R.id.pageContainer, tournament, "LEAGUEINFO").commit();
//        }catch (Exception e){
//            log.error("ERROR: ", e);
//        }
//        PersonalActivity.active = tournament;
//    }
    @SuppressLint("CheckResult")
    private void SearchTournaments(String search, String region){
//        PersonalActivity.people.clear();
//        String type = "player";
        if (search!=null && search.equals("")) {
            saveAllData(PersonalActivity.allTourneys);
        } else {

            Controller.getApi().getTourneys(search,region)
                    .subscribeOn(Schedulers.io())
//                .doOnSubscribe(__ -> showDialog())
//                .doOnTerminate(this::hideDialog)
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
    public void onFinishEditDialog(int pos) {

        SearchTournaments(null,regionsId.get(pos));
    }
}
