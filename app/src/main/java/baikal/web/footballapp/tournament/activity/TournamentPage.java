package baikal.web.footballapp.tournament.activity;

import android.annotation.SuppressLint;
import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.google.android.material.tabs.TabLayout;

import baikal.web.footballapp.CheckError;
import baikal.web.footballapp.Controller;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.home.activity.ComingMatches;
import baikal.web.footballapp.home.activity.FullscreenNewsActivity;
import baikal.web.footballapp.home.activity.NewsAndAds;
import baikal.web.footballapp.model.GetLeagueInfo;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.LeagueInfo;
import baikal.web.footballapp.model.People;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.Tournaments;
import baikal.web.footballapp.model.Tourney;
import baikal.web.footballapp.model.User;
import baikal.web.footballapp.tournament.adapter.RecyclerViewTournamentAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import baikal.web.footballapp.tournament.adapter.ViewPagerTournamentInfoAdapter;
import baikal.web.footballapp.viewmodel.MainViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@SuppressLint("ValidFragment")
public class TournamentPage extends Fragment {
    private TabLayout tabLayout;
    public static final List<Person> referees = new ArrayList<>();
    public static List<String> favTourneys = new ArrayList<>();
    public static String token;
    public static String id;
    private TournamentsFragment tournamentsFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view;

        view = inflater.inflate(R.layout.page_tournament, container, false);
//        GetAllReferees();
        tabLayout = view.findViewById(R.id.tournamentsTabLayout);
        ViewPager viewPager;
        viewPager = view.findViewById(R.id.tournamentsPager);
        tabLayout.setupWithViewPager(viewPager);
        setupViewPager(viewPager);

        Person person = null;
        if(PersonalActivity.status)
        {
            User user = SaveSharedPreference.getObject();
            person  = user.getUser();
            token = user.getToken();
            favTourneys = person.getFavouriteTourney();
        }

        setCustomFont();
        return view;
    }
    private void setupViewPager(ViewPager viewPager) {
        tournamentsFragment = new TournamentsFragment(this);
        SearchTournaments searchTournaments = new SearchTournaments(this);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

            @Override
            public void onPageSelected(int position) {
                if (position == 0)
                    searchTournaments.setFavTourneys();

            }

            @Override
            public void onPageScrollStateChanged(int state) { }
        });

        try {
            ViewPagerTournamentInfoAdapter adapter = new ViewPagerTournamentInfoAdapter(this.getChildFragmentManager());

            adapter.addFragment(tournamentsFragment, "Турниры");
            adapter.addFragment(searchTournaments, "Поиск");
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

    @SuppressLint("CheckResult")
    private void GetAllReferees() {
        String type = "referee";
        Controller.getApi().getAllPersons(type, null, "32575", "0")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                //.repeatWhen(completed -> completed.delay(5, TimeUnit.MINUTES))
                .subscribe(this::saveReferees,
                        error -> {
                            CheckError checkError = new CheckError();
                            checkError.checkError(getActivity(), error);
                        }
                );
    }

    private void saveReferees(List<Person> people) {
        referees.clear();
        referees.addAll(people);
    }
}
