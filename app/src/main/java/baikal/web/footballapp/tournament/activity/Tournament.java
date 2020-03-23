package baikal.web.footballapp.tournament.activity;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.MankindKeeper;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.home.adapter.AnnounceAdapter;
import baikal.web.footballapp.model.Announce;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.Tourney;
import baikal.web.footballapp.tournament.activity.MainPage.TournamentFeeds;
import baikal.web.footballapp.tournament.adapter.CustomViewPagerAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Tournament extends Fragment {

    private final Logger log = LoggerFactory.getLogger(PersonalActivity.class);
    private TabLayout tabLayout;

    private FloatingActionButton fabCommand;
    private FloatingActionButton fabPlayers;

    private League league;

    private final List<Announce> announces = new ArrayList<>();
    private AnnounceAdapter announceAdapter;

    public Tournament() { }

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
            league = (League) arguments.getSerializable("TOURNAMENTINFO");

            String str = "";
            for(Tourney t: MankindKeeper.getInstance().allTourneys)
                if(league.getTourney().equals(t.getId()))
                    str = t.getName();
            str +=". " + league.getName();
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

        RecyclerView recyclerViewAds = view.findViewById(R.id.recyclerViewMainAds);

        try {
            recyclerViewAds.setLayoutManager(new LinearLayoutManager(getActivity()));
            announceAdapter = new AnnounceAdapter(announces);
            recyclerViewAds.setAdapter(announceAdapter);
        } catch (Exception e) {
            Log.e("ERROR: ", e.toString());
        }

        GetAllAds();
        return view;
    }


    private void setupViewPager(ViewPager viewPager, League league) {
        TournamentTimeTableFragment tournamentTimeTableFragment = new TournamentTimeTableFragment();
        TournamentCommandFragment tournamentCommandFragment = new TournamentCommandFragment();
        TournamentPlayersFragment tournamentPlayersFragment = new TournamentPlayersFragment();
        TournamentFeeds tournamentFeeds = new TournamentFeeds(league.getTourney());

        Bundle leagueData = new Bundle();
        leagueData.putSerializable("TOURNAMENTINFOMATCHESLEAGUE", league);
        tournamentCommandFragment.setArguments(leagueData);
        tournamentPlayersFragment.setArguments(leagueData);
        tournamentTimeTableFragment.setArguments(leagueData);

        try {
            CustomViewPagerAdapter adapter = new CustomViewPagerAdapter(this.getChildFragmentManager());
            adapter.addFragment(tournamentTimeTableFragment, "Расписание");
            adapter.addFragment(tournamentCommandFragment, "Команды");
            adapter.addFragment(tournamentPlayersFragment, "Игроки");
            adapter.addFragment(tournamentFeeds, "Новости");

            viewPager.addOnPageChangeListener(onPageChangeListener);
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
                    ((TextView) tabViewChild).setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/manrope_regular.otf"));
                }
            }
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

    FloatingActionButton getFabCommand() {
        return fabCommand;
    }

    FloatingActionButton getFabPlayers() {
        return fabPlayers;
    }

    private void GetAllAds() {
        Callback<List<Announce>> responseCallback = new Callback<List<Announce>>() {
            @Override
            public void onResponse(@NonNull Call<List<Announce>> call, @NonNull Response<List<Announce>> response) {
                if (response.isSuccessful() && response.body() != null)
                    saveAds(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<Announce>> call, @NonNull Throwable t) { }
        };

        Controller.getApi().getAnnounceByTourney(
                league.getTourney(),
                "120", "0")
                .enqueue(responseCallback);
    }

    private void saveAds(List<Announce> matches) {
        announces.addAll(announces.size(), matches);
        List<Announce> list = new ArrayList<>(announces);
        announceAdapter.dataChanged(list);
    }
}