package baikal.web.footballapp.tournament.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.tournament.adapter.ViewPagerCommandInfoAdapter;

public class CommandInfoActivity extends AppCompatActivity {
//    private static final String TAG = "CommandInfoActivity";
    private Team team;
    private FloatingActionButton fab;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_command_info);

        ViewPager viewPager;
        TextView textView;
        final AbbreviationDialogFragment dialogFragment;
        Intent intent;
        intent = getIntent();
        team = (Team) intent.getExtras().getSerializable("TOURNAMENTMATCHCOMMANDINFO");

//        String title = intent.getExtras().getString("COMMANDTITLE");
        tabLayout = findViewById(R.id.commandInfoTab);
        viewPager = findViewById(R.id.commandInfoViewPager);
        textView = findViewById(R.id.commandInfoTitle);
        fab = findViewById(R.id.commandInfoButton);
        dialogFragment = new AbbreviationDialogFragment();
        fab.setOnClickListener(v -> dialogFragment.show(getSupportFragmentManager(), "abbrev2"));
        findViewById(R.id.commandInfoBack).setOnClickListener(v -> finish());
        viewPager.addOnPageChangeListener(onPageChangeListener);
        tabLayout.addOnTabSelectedListener(onTabSelectedListener);
        textView.setText(team.getName());

        tabLayout.setupWithViewPager(viewPager);
        setupViewPager(viewPager);
        setCustomFont();

        TabLayout.Tab tab = tabLayout.getTabAt(0);
//        TextView selectedText = (TextView) tab.getCustomView();
//        selectedText.setTextColor(getResources().getColor(R.color.colorBottomNavigationUnChecked));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                TextView selectedText = (TextView) tab.getCustomView();
//                selectedText.setTextColor(getResources().getColor(R.color.colorBottomNavigationUnChecked));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
//                TextView selectedText = (TextView) tab.getCustomView();
//                selectedText.setTextColor(getResources().getColor(R.color.colorLightGrayForText));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }
    private void setupViewPager(ViewPager viewPager){
        CommandStructureFragment commandStructureFragment = new CommandStructureFragment();
        CommandMatchFragment commandMatchFragment = new CommandMatchFragment();
        Bundle teams = new Bundle();
        teams.putSerializable("TEAMSTRUCTURE", team);
        commandStructureFragment.setArguments(teams);
        commandMatchFragment.setArguments(teams);
        ViewPagerCommandInfoAdapter adapter = new ViewPagerCommandInfoAdapter(getSupportFragmentManager());
        adapter.addFragment(commandStructureFragment, "СОСТАВ");
        adapter.addFragment(commandMatchFragment, "МАТЧИ");
        viewPager.setAdapter(adapter);
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
                    ((TextView) tabViewChild).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/manrope_regular.otf"));
            }
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    private void animateFab(int position) {
        if (position == 1) {
            fab.hide();
        } else {
            fab.show();
        }
    }

    private final TabLayout.OnTabSelectedListener onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            animateFab(tab.getPosition());
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) { }

        @Override
        public void onTabReselected(TabLayout.Tab tab) { }
    };

    private final ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

        @Override
        public void onPageSelected(int position) {
            animateFab(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) { }
    };

    public FloatingActionButton getFab() {
        return fab;
    }
}
