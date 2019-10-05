package baikal.web.footballapp.tournament.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.LeagueInfo;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.tournament.adapter.ViewPagerTournamentInfoAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;

public class Tournament extends Fragment {

    private final Logger log = LoggerFactory.getLogger(PersonalActivity.class);
    private TabLayout tabLayout;

    private FloatingActionButton fabCommand;
    private FloatingActionButton fabPlayers;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view;
        ViewPager viewPager;
        TextView textTitle;
        final CommandAbbrev dialogFragment;
        final AbbreviationDialogFragment dialogFragment2;
        view = inflater.inflate(R.layout.tournament_info_two, container, false);
        fabCommand = view.findViewById(R.id.tournamentInfoTabCommandAbbrev);
        fabPlayers = view.findViewById(R.id.playersInfoButton);
        tabLayout = view.findViewById(R.id.tournamentInfoTab);
        viewPager = view.findViewById(R.id.tournamentInfoViewPager);
        textTitle = view.findViewById(R.id.tournamentInfoTitle);
        try {
            Bundle arguments = getArguments();
            LeagueInfo league = (LeagueInfo) arguments.getSerializable("TOURNAMENTINFO");
            if (league.getTeams().size() != 0){
                viewPager.addOnPageChangeListener(onPageChangeListener);
                tabLayout.addOnTabSelectedListener(onTabSelectedListener);
                fabCommand.hide();
                fabPlayers.hide();
            }

            String str;
            str = league.getTourney() + ". " + league.getName();
            textTitle.setText(str);
            tabLayout.setupWithViewPager(viewPager);
            setupViewPager(viewPager, league);
            setCustomFont();
        } catch (Exception e) {
            log.error("ERROR: ", e);
        }

        dialogFragment = new CommandAbbrev();
        fabCommand.setOnClickListener(v -> dialogFragment.show(getFragmentManager(), "commandAbbrev"));

        dialogFragment2 = new AbbreviationDialogFragment();
        fabPlayers.setOnClickListener(v -> dialogFragment2.show(getFragmentManager(), "abbrev"));

        fabCommand.hide();
        fabPlayers.hide();

        return view;
    }


    private void setupViewPager(ViewPager viewPager, LeagueInfo league) {
        TournamentTimeTableFragment tournamentTimeTableFragment = new TournamentTimeTableFragment();
        TournamentCommandFragment tournamentCommandFragment = new TournamentCommandFragment();
        TournamentPlayersFragment tournamentPlayersFragment = new TournamentPlayersFragment();

        Bundle leagueMatch = new Bundle();
        leagueMatch.putSerializable("TOURNAMENTINFOMATCHESLEAGUE", league);
        tournamentTimeTableFragment.setArguments(leagueMatch);
        Bundle teams = new Bundle();
        List<Team> teamList = league.getTeams();
        teams.putSerializable("TOURNAMENTINFOTEAMS", (Serializable) teamList);
        teams.putSerializable("TOURNAMENTINFOMATCHESLEAGUE", league);
        tournamentCommandFragment.setArguments(teams);
        tournamentPlayersFragment.setArguments(teams);
//        ViewPagerTournamentInfoAdapter adapter = new ViewPagerTournamentInfoAdapter(getActivity().getSupportFragmentManager());
        try {
            ViewPagerTournamentInfoAdapter adapter = new ViewPagerTournamentInfoAdapter(this.getChildFragmentManager());
            adapter.addFragment(tournamentTimeTableFragment, "Расписание");
            adapter.addFragment(tournamentCommandFragment, "Команды");
            adapter.addFragment(tournamentPlayersFragment, "Игроки");

            viewPager.setAdapter(adapter);
        } catch (IllegalStateException e) {
            log.error("ERROR: ", e);
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
                    //Put your font in assests folder
                    //assign name of the font here (Must be case sensitive)
                    ((TextView) tabViewChild).setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/manrope_regular.otf"));
                }
            }
        }
    }

    private void animateFab(int position) {
        switch (position) {
            case 0:
                fabCommand.hide();
                fabPlayers.hide();
                break;
            case 1:
                fabCommand.show();
                fabPlayers.hide();
                break;

            case 2:
                fabPlayers.show();
                fabCommand.hide();
                break;

            default:
                fabCommand.hide();
                fabPlayers.hide();
                break;
        }
    }

    @Override
    public void onPause() {
        log.info("INFO: tournament onPause");
        super.onPause();
        this.onDestroy();
    }

    @Override
    public void onDestroy() {
        log.info("INFO: tournament onDestroy");
        super.onDestroy();
    }

    private final TabLayout.OnTabSelectedListener onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            animateFab(tab.getPosition());
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    private final ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            animateFab(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public FloatingActionButton getFabCommand() {
        return fabCommand;
    }

    public FloatingActionButton getFabPlayers() {
        return fabPlayers;
    }

}