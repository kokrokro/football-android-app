package baikal.web.footballapp.home.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.home.adapter.AnnounceAdapter;
import baikal.web.footballapp.model.Announce;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.tournament.adapter.ViewPagerTournamentInfoAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPage extends Fragment {
    private final static String TAG = "MainPage";
    private TabLayout tabLayout;

    private final List<Announce> announces = new ArrayList<>();
    private AnnounceAdapter announceAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view;
        view = inflater.inflate(R.layout.page_main, container, false);
        tabLayout = view.findViewById(R.id.mainPageTab);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(view.findViewById(R.id.PM_bottom_sheet));
        CoordinatorLayout coordinatorLayout = view.findViewById(R.id.PM_coordinator_layout);
        ViewPager viewPager = view.findViewById(R.id.mainPageViewPager);
        tabLayout.setupWithViewPager(viewPager);
        setupViewPager(viewPager);
        setCustomFont();

        RecyclerView recyclerViewAds = view.findViewById(R.id.recyclerViewMainAds);
        bottomSheetBehavior.setHideable(false);
//        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//            @Override
//            public void onStateChanged(@NonNull View view, int i) {
//                Rect rect = new Rect();
//                bottomSheetBehavior.getInsetDodgeRect(coordinatorLayout, recyclerViewAds, rect);
//                Log.d(TAG, rect + " ***********************************************");
//            }
//
//            @Override
//            public void onSlide(@NonNull View view, float v) {
//
//            }
//        });

        try {
            recyclerViewAds.setLayoutManager(new LinearLayoutManager(getActivity()));
            announceAdapter = new AnnounceAdapter(announces);
            recyclerViewAds.setAdapter(announceAdapter);
        } catch (Exception e) {
            Log.e("ERROR: ", e.toString());
        }

        GetAllAds();

//        ViewCompat.postOnAnimation(coordinatorLayout, () -> ViewCompat.postInvalidateOnAnimation(coordinatorLayout));

        return view;
    }


    private void setupViewPager(ViewPager viewPager) {
        NewsAndAds newsAndAds = new NewsAndAds((feed)->{
            Intent intent = new Intent(getActivity(), FullscreenNewsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("NEWS", feed);
            intent.putExtras(bundle);
            getContext().startActivity(intent);
        });
        ComingMatches comingMatches = new ComingMatches((x)->{}, false);
        NewsAdsAndUpcomingMatches newsAdsAndUpcomingMatches = new NewsAdsAndUpcomingMatches(
                (x)->{

                },
                (feed)->{
                    Intent intent = new Intent(getActivity(), FullscreenNewsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("NEWS", feed);
                    intent.putExtras(bundle);
                    getContext().startActivity(intent);
                });

        try {
            ViewPagerTournamentInfoAdapter adapter = new ViewPagerTournamentInfoAdapter(this.getChildFragmentManager());
            adapter.addFragment(newsAdsAndUpcomingMatches, "Главная");
            adapter.addFragment(newsAndAds, "Новости");
            adapter.addFragment(comingMatches, "Ближайшие матчи");
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(1);
            viewPager.setOffscreenPageLimit(3);

        } catch (IllegalStateException ignored) { }
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

    @SuppressLint("CheckResult")
    private void GetAllAds() {
        if (SaveSharedPreference.getObject() == null)
            return;

        Person p = SaveSharedPreference.getObject().getUser();
        StringBuilder tourneyIds = new StringBuilder();

        for (String t: p.getFavouriteTourney())
            tourneyIds.append(",").append(t);

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
                tourneyIds.toString(),
                "120", "0")
                .enqueue(responseCallback);
    }

    private void saveAds(List<Announce> matches) {
        announces.addAll(announces.size(), matches);
        List<Announce> list = new ArrayList<>(announces);
        announceAdapter.dataChanged(list);
    }
}