package baikal.web.footballapp.tournament.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.tournament.adapter.ViewPagerCommandInfoAdapter;


public class CommandInfoActivity extends AppCompatActivity {
    private static final String TAG = "CommandInfoActivity";
    private Team team;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_command_info);
        final TabLayout tabLayout;
        ImageButton buttonBack;
        ViewPager viewPager;
        TextView textView;
        final AbbreviationDialogFragment dialogFragment;
        Intent intent;
        intent = getIntent();
        team = (Team) intent.getExtras().getSerializable("TOURNAMENTMATCHCOMMANDINFO");

//        String title = intent.getExtras().getString("COMMANDTITLE");
        buttonBack = findViewById(R.id.commandInfoBack);
        tabLayout = findViewById(R.id.commandInfoTab);
        viewPager = findViewById(R.id.commandInfoViewPager);
        textView = findViewById(R.id.commandInfoTitle);
        fab = findViewById(R.id.commandInfoButton);
        dialogFragment = new AbbreviationDialogFragment();
        fab.setOnClickListener(v -> dialogFragment.show(getSupportFragmentManager(), "abbrev2"));
        buttonBack.setOnClickListener(v -> finish());
        String str;
        str = team.getName();
        viewPager.addOnPageChangeListener(onPageChangeListener);
        tabLayout.addOnTabSelectedListener(onTabSelectedListener);
        textView.setText(str);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
//        setCustomFont();
        Typeface tf = Typeface.createFromAsset(this.getAssets(), "fonts/manrope_regular.otf");
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TextView tv = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_text,null);
            tv.setTypeface(tf);
            tabLayout.getTabAt(i).setCustomView(tv);
        }
        TabLayout.Tab tab = tabLayout.getTabAt(0);
        TextView selectedText = (TextView) tab.getCustomView();
        selectedText.setTextColor(getResources().getColor(R.color.colorBottomNavigationUnChecked));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TextView selectedText = (TextView) tab.getCustomView();
                selectedText.setTextColor(getResources().getColor(R.color.colorBottomNavigationUnChecked));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView selectedText = (TextView) tab.getCustomView();
                selectedText.setTextColor(getResources().getColor(R.color.colorLightGrayForText));
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

    @Override
    public void onBackPressed() {
        this.finish();
    }


    private void animateFab(int position) {
        switch (position) {
            case 0:
                fab.show();
                break;
            case 1:
                fab.hide();
                break;
            default:
                fab.show();
                break;
        }
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

    public FloatingActionButton getFab() {
        return fab;
    }
}
