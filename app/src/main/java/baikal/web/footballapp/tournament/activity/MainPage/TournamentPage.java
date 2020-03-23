package baikal.web.footballapp.tournament.activity.MainPage;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import baikal.web.footballapp.R;
import baikal.web.footballapp.tournament.adapter.CustomViewPagerAdapter;


public class TournamentPage extends Fragment {
    private final static String TAG = "TournamentPage";
    private TabLayout tabLayout;

    private SearchTournaments.OnDialogEnterClicked onDialogEnterClicked;

    public TournamentPage (SearchTournaments.OnDialogEnterClicked onDialogEnterClicked) {
        this.onDialogEnterClicked = onDialogEnterClicked;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.page_tournament, container, false);

        tabLayout = view.findViewById(R.id.tournamentsTabLayout);
        ViewPager viewPager = view.findViewById(R.id.tournamentsPager);
        tabLayout.setupWithViewPager(viewPager);
        setupViewPager(viewPager);
        setCustomFont();

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        TournamentsFragment tournamentsFragment = new TournamentsFragment();
        SearchTournaments searchTournaments = new SearchTournaments(onDialogEnterClicked);
        viewPager.setOffscreenPageLimit(2);
        try {
            CustomViewPagerAdapter adapter = new CustomViewPagerAdapter(this.getChildFragmentManager());

            adapter.addFragment(tournamentsFragment, "Турниры");
            adapter.addFragment(searchTournaments, "Поиск");
            viewPager.setAdapter(adapter);
        } catch (Exception ignore) { }
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
                    ((TextView) tabViewChild).setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                }
            }
        }
    }
}
