package baikal.web.footballapp.user.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import baikal.web.footballapp.CheckError;
import baikal.web.footballapp.Controller;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.ActiveMatch;
import baikal.web.footballapp.model.ActiveMatches;
import baikal.web.footballapp.user.adapter.RVTimeTableAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TimeTableFragment extends Fragment {
    private RVTimeTableAdapter adapter;
    private final List<ActiveMatch> matches = new ArrayList<>();
    private NestedScrollView scroller;
    private int count = 0;
    private final int limit = 10;
    private int offset = 0;
    private final Logger log = LoggerFactory.getLogger(TimeTableFragment.class);
    private LinearLayout layout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        RecyclerView recyclerView;
        view = inflater.inflate(R.layout.user_timetable, container, false);
        scroller = view.findViewById(R.id.scrollerTimeTable);
        layout = view.findViewById(R.id.emptyTimetable);
        getActiveMatches("10", "0");
        recyclerView = view.findViewById(R.id.recyclerViewTimeTable);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        try {
            adapter = new RVTimeTableAdapter(getActivity(), this, matches);
            recyclerView.setAdapter(adapter);

        } catch (NullPointerException e) {
            log.error("ERROR: ", e);
        }
        scroller.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                offset++;
                int temp = limit*offset;
                if (temp<=count) {
                    String str = String.valueOf(temp);
                    getActiveMatches("10", str);
                }
            }
        });

        return view;
    }

    @SuppressLint("CheckResult")
    private void getActiveMatches(String limit, String offset) {
//        Controller.getApi().getActiveMatches(limit, offset, false)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(this::saveData
//                        ,
//                        error -> {
//                            layout.setVisibility(View.VISIBLE);
//                            CheckError checkError = new CheckError();
//                            checkError.checkError(getActivity(), error);
//                        }
//                );
    }

    private void saveData(ActiveMatches matches1) {
        count = matches1.getCount();
        List<ActiveMatch> result;
        result = matches1.getMatches();
        if (result.size() != 0) {
            matches.addAll(matches.size(), result);
            List<ActiveMatch> list = new ArrayList<>(matches);
            adapter.dataChanged(list);
            layout.setVisibility(View.GONE);

        }
        if (matches.size()==0){
            layout.setVisibility(View.VISIBLE);
        }
//        else {
//            layout.setVisibility(View.VISIBLE);
//        }
    }


}
