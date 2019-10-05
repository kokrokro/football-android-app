package baikal.web.footballapp.home.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import baikal.web.footballapp.R;
import baikal.web.footballapp.tournament.adapter.ViewPagerTournamentInfoAdapter;

public class MainPage extends Fragment {
    private TabLayout tabLayout;


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
        return view;
    }


    private void setupViewPager(ViewPager viewPager) {
        NewsAndAds newsAndAds = new NewsAndAds();
        ComingMatches comingMatches = new ComingMatches();

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