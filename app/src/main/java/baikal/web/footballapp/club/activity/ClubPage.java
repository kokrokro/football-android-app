package baikal.web.footballapp.club.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.club.adapter.RecyclerViewClubAdapter;
import baikal.web.footballapp.model.Club;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.User;

import static android.app.Activity.RESULT_OK;

public class ClubPage extends Fragment {
    public static RecyclerViewClubAdapter adapter;
    private static FloatingActionButton fab;
    private final int REQUEST_CODE_CLUB_CREATE = 276;
    private boolean scrollStatus;
    private RecyclerView recyclerView;
    private NestedScrollView scroller;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view;
        view = inflater.inflate(R.layout.page_club, container, false);
        scroller = view.findViewById(R.id.scrollClubs);
        recyclerView = view.findViewById(R.id.recyclerViewClubs);
//        recyclerView.setNestedScrollingEnabled(false);
        fab = view.findViewById(R.id.createClubButton);
        scrollStatus = false;
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/manrope_regular.otf");
        fab.setOnClickListener(v -> {
            if (SaveSharedPreference.getLoggedStatus(getActivity().getApplicationContext())) {
                Intent intent = new Intent(getActivity(), NewClub.class);
                startActivityForResult(intent, REQUEST_CODE_CLUB_CREATE);
            } else {
                Toast.makeText(getActivity(), "Необходимо авторизоваться", Toast.LENGTH_SHORT).show();
            }
        });


        try {
            if (SaveSharedPreference.getObject().getUser().getClub() != null) {
                CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
                p.setAnchorId(View.NO_ID);
                fab.setLayoutParams(p);
                fab.setVisibility(View.GONE);
            } else {
                HideShowFAB();
            }
        } catch (Exception e) {
            HideShowFAB();
        }


        try {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapter = new RecyclerViewClubAdapter(getActivity(), ClubPage.this, PersonalActivity.allClubs);
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
        }

        return view;
    }


    private void HideShowFAB() {
        scroller.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {

            if (scrollY > oldScrollY) {
            }
            if (scrollY < oldScrollY) {
                scrollStatus = false;
            }

            if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                scrollStatus = true;
                fab.hide();
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //nothing to do
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    fab.show();
                } else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    fab.hide();
                }
                if (scrollStatus) {
                    fab.hide();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_CLUB_CREATE) {
                Club result = (Club) data.getExtras().getSerializable("CREATECLUBRESULT");
                Person person1 = SaveSharedPreference.getObject().getUser();
                person1.setClub(result.getId());
                User user = SaveSharedPreference.getObject();
                user.setUser(person1);
                SaveSharedPreference.editObject(user);
                PersonalActivity.allClubs.add(result);
                List<Club> list = new ArrayList<>(PersonalActivity.allClubs);
                ClubPage.adapter.dataChanged(list);
                Toast.makeText(getActivity(), "Клуб создан.", Toast.LENGTH_LONG).show();
            }

        } else {
//            log.error("ERROR: onActivityResult");
        }
    }


}
