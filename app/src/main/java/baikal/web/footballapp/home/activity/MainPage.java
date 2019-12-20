package baikal.web.footballapp.home.activity;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import baikal.web.footballapp.CheckError;
import baikal.web.footballapp.Controller;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.home.adapter.RecyclerViewMainAdsAdapter;
import baikal.web.footballapp.model.Announce;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.tournament.adapter.ViewPagerTournamentInfoAdapter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPage extends Fragment {
    private TabLayout tabLayout;
    private List<League> favLeagues = new ArrayList<>();
    private List<String> favTourneysId = new ArrayList<>();
    private List<String> favLeaguesId = new ArrayList<>();

    private final List<Announce> announces = new ArrayList<>();
    private RecyclerViewMainAdsAdapter adsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view;
        view = inflater.inflate(R.layout.page_main, container, false);
        tabLayout = view.findViewById(R.id.mainPageTab);
        ViewPager viewPager = view.findViewById(R.id.mainPageViewPager);
        tabLayout.setupWithViewPager(viewPager);
        setupViewPager(viewPager);
        setCustomFont();

        String personId = null;

        try {
            personId = SaveSharedPreference.getObject().getUser().getId();
        } catch (Exception ignored) {}

        Controller.getApi().getFavTourneysId(personId).enqueue(new Callback<List<Person>>() {
            @Override
            public void onResponse(@NonNull Call<List<Person>> call, @NonNull Response<List<Person>> response) {
                if (response.isSuccessful()){
                    if (response.body() != null && response.body().size()>0) {
                        favTourneysId.clear();
                        favTourneysId.addAll(response.body().get(0).getFavouriteTourney());
                        StringBuilder s= new StringBuilder();
                        for(String str : favTourneysId){
                            s.append(',').append(str);
                        }
                        Controller.getApi().getLeaguesByTourney(s.toString()).enqueue(new Callback<List<League>>() {
                            @Override
                            public void onResponse(@NonNull Call<List<League>> call, @NonNull Response<List<League>> response) {
                                if(response.isSuccessful())
                                    if(response.body()!=null){
                                        favLeagues.clear();
                                        favLeagues.addAll(response.body());
                                        favLeaguesId.clear();
                                        for(League l :favLeagues){
                                            favLeaguesId.add(l.getId());
                                        }
                                    }
                            }

                            @Override
                            public void onFailure(@NonNull Call<List<League>> call, @NonNull Throwable t) {

                            }
                        });
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<Person>> call, @NonNull Throwable t) {

            }
        });

        RecyclerView recyclerViewAds = view.findViewById(R.id.recyclerViewMainAds);

        try {
            recyclerViewAds.setLayoutManager(new LinearLayoutManager(getActivity()));
            adsAdapter = new RecyclerViewMainAdsAdapter(announces);
            recyclerViewAds.setAdapter(adsAdapter);
        } catch (Exception e) {
            Log.e("ERROR: ", e.toString());
        }

        GetAllAds();

        return view;
    }


    private void setupViewPager(ViewPager viewPager) {
        NewsAndAds newsAndAds = new NewsAndAds();
        ComingMatches comingMatches = new ComingMatches(favLeaguesId);
        NewsAdsAndUpcomingMatches newsAdsAndUpcomingMatches = new NewsAdsAndUpcomingMatches(favLeaguesId);

        try {
            ViewPagerTournamentInfoAdapter adapter = new ViewPagerTournamentInfoAdapter(this.getChildFragmentManager());
            adapter.addFragment(newsAdsAndUpcomingMatches, "Главная");
            adapter.addFragment(newsAndAds, "Новости");
            adapter.addFragment(comingMatches, "Ближайшие матчи");
            viewPager.setAdapter(adapter);
        } catch (IllegalStateException ignored) {
        }

    }


    private void setCustomFont() {

        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();

        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);

            int tabChildsCount = vgTab.getChildCount();

            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(Typeface.createFromAsset(Objects.requireNonNull(getActivity()).getAssets(), "fonts/manrope_regular.otf"));
                }
            }
        }
    }

    @SuppressLint("CheckResult")
    private void GetAllAds() {
        //noinspection ResultOfMethodCallIgnored
        Controller.getApi().getAllAnnounce("2", "0")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .repeatWhen(completed -> completed.delay(5, TimeUnit.MINUTES))
                .subscribe(this::saveAds
                        ,
                        error -> {
                            CheckError checkError = new CheckError();
                            checkError.checkError(getActivity(), error);
                        }
                );
    }

    private void saveAds(List<Announce> matches) {
        announces.addAll(announces.size(), matches);
        List<Announce> list = new ArrayList<>(announces);
        adsAdapter.dataChanged(list);
    }
}