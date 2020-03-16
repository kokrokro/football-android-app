package baikal.web.footballapp.tournament.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import baikal.web.footballapp.R;
import baikal.web.footballapp.tournament.adapter.ViewPagerTournamentInfoAdapter;
import baikal.web.footballapp.viewmodel.MainViewModel;
import baikal.web.footballapp.viewmodel.SearchTournamentPageViewModel;


public class TournamentPage extends Fragment {
    private TabLayout tabLayout;

    private MainViewModel mainViewModel;
    private SearchTournamentPageViewModel searchTournamentPageViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.page_tournament, container, false);

        tabLayout = view.findViewById(R.id.tournamentsTabLayout);
        ViewPager viewPager = view.findViewById(R.id.tournamentsPager);
        tabLayout.setupWithViewPager(viewPager);

        mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
//        searchTournamentPageViewModel = ViewModelProviders.of(getActivity()).get(SearchTournamentPageViewModel.class);

        setupViewPager(viewPager);
        setCustomFont();

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        TournamentsFragment tournamentsFragment = new TournamentsFragment(mainViewModel);
//        SearchTournaments searchTournaments = new SearchTournaments(searchTournamentPageViewModel);

        try {
            ViewPagerTournamentInfoAdapter adapter = new ViewPagerTournamentInfoAdapter(this.getChildFragmentManager());

            adapter.addFragment(tournamentsFragment, "Турниры");
//            adapter.addFragment(searchTournaments, "Поиск");
            viewPager.setAdapter(adapter);
        } catch (Exception ignore) { }
    }

    private void setCustomFont() {
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);

        for (int j = 0; j < vg.getChildCount(); j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildrenCount = vgTab.getChildCount();

            for (int i = 0; i < tabChildrenCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView)
                    ((TextView) tabViewChild).setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/manrope_regular.otf"));
            }
        }
    }

}
