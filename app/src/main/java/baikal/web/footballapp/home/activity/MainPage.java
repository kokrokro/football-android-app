package baikal.web.footballapp.home.activity;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.tournament.adapter.ViewPagerTournamentInfoAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPage extends Fragment {
    private TabLayout tabLayout;
    private List<League> favLeagues = new ArrayList<>();
    private List<String> favTourneysId = new ArrayList<>();
    private List<String> favLeaguesId = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view;
        ViewPager viewPager;
        view = inflater.inflate(R.layout.page_main, container, false);
        tabLayout = view.findViewById(R.id.mainPageTab);
        viewPager = view.findViewById(R.id.mainPageViewPager);
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
        return view;
    }


    private void setupViewPager(ViewPager viewPager) {
        NewsAndAds newsAndAds = new NewsAndAds();
        ComingMatches comingMatches = new ComingMatches(favLeaguesId);

        try {
            ViewPagerTournamentInfoAdapter adapter = new ViewPagerTournamentInfoAdapter(this.getChildFragmentManager());
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
}