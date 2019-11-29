package baikal.web.footballapp.home.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.Tourney;
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
        Controller.getApi().getFavTourneysId(PersonalActivity.id).enqueue(new Callback<List<Person>>() {
            @Override
            public void onResponse(Call<List<Person>> call, Response<List<Person>> response) {
                if (response.isSuccessful()){
                    if (response.body() != null && response.body().size()>0) {
                        favTourneysId.clear();
                        favTourneysId.addAll(response.body().get(0).getFavouriteTourney());
                        String s="";
                        for(String str : favTourneysId){
                            s= s+','+str;
                        }
                        Controller.getApi().getLeaguesByTourney(s).enqueue(new Callback<List<League>>() {
                            @Override
                            public void onResponse(Call<List<League>> call, Response<List<League>> response) {
                                if(response.isSuccessful()){
                                    if(response.body()!=null){
                                        favLeagues.clear();
                                        favLeagues.addAll(response.body());
                                        favLeaguesId.clear();
                                        Log.d("UpcomingMatches", favLeagues.toString());
                                        for(League l :favLeagues){
                                            favLeaguesId.add(l.getId());
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<List<League>> call, Throwable t) {

                            }
                        });
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Person>> call, Throwable t) {

            }
        });
        return view;
    }


    private void setupViewPager(ViewPager viewPager) {
        NewsAndAds newsAndAds = new NewsAndAds();
        Log.d("UpcomingMatches", ""+favLeaguesId.size());
        ComingMatches comingMatches = new ComingMatches(favLeaguesId);

        try {
            ViewPagerTournamentInfoAdapter adapter = new ViewPagerTournamentInfoAdapter(this.getChildFragmentManager());
            adapter.addFragment(newsAndAds, "Новости");
            adapter.addFragment(comingMatches, "Ближайшие матчи");
            viewPager.setAdapter(adapter);
        } catch (IllegalStateException e) {
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
                    ((TextView) tabViewChild).setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/manrope_regular.otf"));
                }
            }
        }
    }
}