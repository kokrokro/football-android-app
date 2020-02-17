package baikal.web.footballapp.user.activity.UserTeams;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import baikal.web.footballapp.R;
import baikal.web.footballapp.user.activity.AuthoUser;
import baikal.web.footballapp.user.activity.NewCommand;
import baikal.web.footballapp.user.activity.UserTeams.Adapters.VPUserCommandTabAdapter;

import static android.app.Activity.RESULT_OK;


public class UserCommands extends Fragment {
    private final Logger log = LoggerFactory.getLogger(UserCommands.class);
    private final int REQUEST_CODE_NEWCOMMAND = 286;
    private int num;

    private TabLayout teamTabLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        log.info("INFO: UserCommands onCreate");
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        log.info("INFO: onCreateView onCreate");
        View view = inflater.inflate(R.layout.user_commands, container, false);

        ViewPager viewPager = view.findViewById(R.id.UC_viewPager);
        teamTabLayout = view.findViewById(R.id.UC_tabLayout);
        teamTabLayout.setupWithViewPager(viewPager);
        setupViewPager(viewPager);

//        MainViewModel mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
//        mainViewModel.getTeams(id).observe(getViewLifecycleOwner(), teamsToTrain -> {
//            trainerTeams.clear();
//            trainerTeams.addAll(teamsToTrain);
//
//            if(trainerTeams.size()==0)
//                linearLayoutTrainerTeams.setVisibility(View.GONE);
//            else
//                textTrainerTeams.setVisibility(View.VISIBLE);
//        });

//        scroller.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
//            if (scrollY < oldScrollY) {
//                scrollStatus = false;
//            }
//            if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
//                scrollStatus = true;
//                AuthoUser.fab.hide();
//            }
//        });
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
////nothing to do
//            }
//
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    AuthoUser.fab.show();
//                } else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
//                    AuthoUser.fab.hide();
//                }
//                if (scrollStatus) {
//                    AuthoUser.fab.hide();
//                }
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//        });
//
        AuthoUser.fab.setOnClickListener(v -> {
            //add command
            num = AuthoUser.personOwnCommand.size();
            Intent intent = new Intent(getActivity(), NewCommand.class);
            startActivityForResult(intent, REQUEST_CODE_NEWCOMMAND);
        });

        setCustomFont();
        return view;
    }

    private void setupViewPager (ViewPager viewPager) {
        UsersTeamListFragment participationTeamList = new UsersTeamListFragment("part");
        UsersTeamListFragment teamToTrainList       = new UsersTeamListFragment("train");
        UsersTeamListFragment createdTeamList       = new UsersTeamListFragment("creator");

        VPUserCommandTabAdapter adapter = new VPUserCommandTabAdapter(getChildFragmentManager());
        adapter.addFragment(participationTeamList, "Участие");
        adapter.addFragment(teamToTrainList      , "Тренерство");
        adapter.addFragment(createdTeamList      , "Созданные");

        viewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void setCustomFont() {
        ViewGroup vg = (ViewGroup) teamTabLayout.getChildAt(0);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK)
            if (requestCode == REQUEST_CODE_NEWCOMMAND) {
                if (num == 0){
                    FragmentTransaction ft = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
//                    FragmentTransaction ft = this.getChildFragmentManager().beginTransaction();
                    try {
                        ft.detach(this).attach(this).commit();
                    } catch (Exception ignored) { }
                } else {
                    AuthoUser.adapterCommand.notifyDataSetChanged();
                    Toast.makeText(getContext(), "Команда добавлена", Toast.LENGTH_LONG).show();
                }
            }
        else
            log.error("ERROR: onActivityResult");
    }

    @Override
    public void onDestroy() {
        log.info("INFO: UserCommands onDestroy");
        super.onDestroy();
    }
}