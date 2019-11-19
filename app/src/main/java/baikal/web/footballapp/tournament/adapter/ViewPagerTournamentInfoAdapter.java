package baikal.web.footballapp.tournament.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;



import baikal.web.footballapp.PersonalActivity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerTournamentInfoAdapter extends FragmentPagerAdapter {

    Logger log = LoggerFactory.getLogger(PersonalActivity.class);

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    private final FragmentManager fm;
    public ViewPagerTournamentInfoAdapter(FragmentManager fm) {
        super(fm);
        this.fm = fm;
    }

    @Override
    public Fragment getItem(int position) {


//        Fragment fragment = new TournamentTimeTableFragment() ;
//        if (position == 1){
//            fragment = new TournamentCommandFragment();
//        } else if (position == 2){
//            fragment = new TournamentPlayersFragment();
//        }
//        return fragment;
//    }

//        fm.beginTransaction()
////                .replace(R.id.tournamentInfoViewPager, mFragmentList.get(position))
//                .addToBackStack(null)
//                .show(mFragmentList.get(position))
////                    .hide(new TournamentCommandFragment())
//                .commit();
//        log.info("INFO: position", position);
//        log.info("INFO: position", mFragmentList.get(position).getTag());

        return mFragmentList.get(position);
    }

    @Override
    public void finishUpdate(@NonNull ViewGroup container) {
        super.finishUpdate(container);
        Log.d("View pager Tournament info adapter", String.valueOf(123321));
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

}
