package baikal.web.footballapp.tournament.activity;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.google.android.material.tabs.TabLayout;

import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.model.User;

import baikal.web.footballapp.tournament.adapter.ViewPagerTournamentInfoAdapter;


public class TournamentPage extends Fragment {
    private TabLayout tabLayout;
    public String token;
    public String id;
    private PersonalActivity activity;

    public TournamentPage (PersonalActivity activity) {
        this.activity = activity;
    }

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

        if(SaveSharedPreference.getLoggedStatus(getContext()))
        {
            User user = SaveSharedPreference.getObject();
            token = user.getToken();
        }

        setCustomFont();
        return view;
    }
    private void setupViewPager(ViewPager viewPager) {
        TournamentsFragment tournamentsFragment = new TournamentsFragment(activity);
        SearchTournaments searchTournaments = new SearchTournaments();

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
        } catch (IllegalStateException ignore) {
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
                if (tabViewChild instanceof TextView)
                    ((TextView) tabViewChild).setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/manrope_regular.otf"));
            }
        }
    }

}
