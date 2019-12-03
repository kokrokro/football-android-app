package baikal.web.footballapp.players.activity;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import baikal.web.footballapp.CheckError;
import baikal.web.footballapp.Controller;
import baikal.web.footballapp.MankindKeeper;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.players.adapter.RecyclerViewPlayersAdapter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PlayersPage extends Fragment {
    private static final String TAG = "PlayerPage";
    private final Logger log = LoggerFactory.getLogger(PlayersPage.class);
    private final int limit = 10;
    private final List<String> allPeople = new ArrayList<>();
    public RecyclerViewPlayersAdapter adapter;
    private RecyclerView recyclerView;
    private int offset = 0;
    private SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        allPeople.addAll(MankindKeeper.getInstance().allPlayers.keySet());

        final View view = inflater.inflate(R.layout.page_players, container, false);

        getAllPlayers("10", "0");

        NestedScrollView scroller = view.findViewById(R.id.scrollerPlayersPage);
        searchView = view.findViewById(R.id.searchView);

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/manrope_regular.otf");
        SearchView.SearchAutoComplete theTextArea = searchView.findViewById(R.id.search_src_text);
        theTextArea.setTextColor(getResources().getColor(R.color.colorBottomNavigationUnChecked));
        theTextArea.setTypeface(tf);
        theTextArea.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        searchView.setQueryHint(Html.fromHtml("<font color = #63666F>" + getResources().getString(R.string.search) + "</font>"));
        ImageView icon = searchView.findViewById(androidx.appcompat.R.id.search_button);
        icon.setColorFilter(getResources().getColor(R.color.colorLightGrayForText), PorterDuff.Mode.SRC_ATOP);
        ImageView searchViewClose = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        searchViewClose.setColorFilter(getResources().getColor(R.color.colorLightGrayForText), PorterDuff.Mode.SRC_ATOP);
        scroller.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                offset++;
                int temp = limit * offset;
                if (temp <= allPeople.size()) {
                    String str = String.valueOf(temp);
                    getAllPlayers("10", str);
                }
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchUsers(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnCloseListener(() -> {
            adapter.dataChanged(allPeople);
            return false;
        });

        try {
            recyclerView = view.findViewById(R.id.recyclerViewPlayers);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapter = new RecyclerViewPlayersAdapter(getContext(), (PersonalActivity) getActivity(), allPeople);
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            log.error("ERROR: ", e);
        }

        //Дугар ты упоролся и запутался мы тоже
//        adapter.dataChanged(allPeople);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.player_search, menu);
        searchView.setIconified(true);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView = (SearchView) item.getActionView();
    }

    @SuppressLint("CheckResult")
    private void SearchUsers(String search) {
        Controller.getApi().getAllPersons(search, "50", "0")
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(__ -> showDialog())
                .doOnTerminate(this::hideDialog)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::savePlayers,
                        error -> {
                            CheckError checkError = new CheckError();
                            checkError.checkError(getActivity(), error);
                        }
                );

    }

    private void savePlayers(List<Person> people) {
        List<String> res = new ArrayList<>();
        for (Person p : people)
            if (!MankindKeeper.getInstance().allPlayers.containsKey(p.get_id())) {
                MankindKeeper.getInstance().allPlayers.put(p.get_id(), p);
                res.add(p.getId());
            }
        adapter.dataChanged(res);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void showDialog() {
    }

    private void hideDialog() {
    }


    @SuppressLint("CheckResult")
    private void getAllPlayers(String limit, String offset) {
        CheckError checkError = new CheckError();
        String type = "player";
        Controller.getApi().getAllPersons(null, limit, offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::saveAllPlayers,
                        error -> checkError.checkError(getActivity(), error)
                );
    }

    private void saveAllPlayers(List<Person> peopleList) {
        Log.d(TAG, "saved all players " + peopleList.size());
        for (Person p : peopleList)
            if (!MankindKeeper.getInstance().allPlayers.containsKey(p.get_id())) {
                MankindKeeper.getInstance().allPlayers.put(p.get_id(), p);
                allPeople.add(p.getId());
            }
        adapter.dataChanged(allPeople);
    }
}



