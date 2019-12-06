package baikal.web.footballapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import baikal.web.footballapp.club.activity.ClubPage;
import baikal.web.footballapp.controller.CustomTypefaceSpan;
import baikal.web.footballapp.home.activity.MainPage;
import baikal.web.footballapp.model.Advertisings;
import baikal.web.footballapp.model.Club;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.Region;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.model.TeamStats;
import baikal.web.footballapp.model.Tourney;
import baikal.web.footballapp.players.activity.PlayersPage;
import baikal.web.footballapp.tournament.activity.TournamentPage;
import baikal.web.footballapp.user.activity.AuthoUser;
import baikal.web.footballapp.user.activity.UserPage;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

public class PersonalActivity extends AppCompatActivity {
    private static final String TAG = "PersonalActivity";
    private final Logger log = LoggerFactory.getLogger(PersonalActivity.class);

    private static final String MAIN    = "MAIN_PAGE";
    private static final String TOURNAMENT = "TOURNAMENT_PAGE";
    private static final String CLUB    = "CLUB_PAGE";
    private static final String PLAYERS = "PLAYERS_PAGE";
    private static final String USER    = "USER_PAGE";

    private AuthoUser authoUser         = new AuthoUser(this);
    private Fragment fragmentUser       = new UserPage(this);
    private Fragment fragmentMain       = new MainPage();
    private Fragment fragmentTournament;
    private Fragment fragmentClub       = new ClubPage();
    private Fragment fragmentPlayers    = new PlayersPage();
    private Fragment active = fragmentMain;

    private BottomNavigationView bottomNavigationView;
    private final FragmentManager fragmentManager = this.getSupportFragmentManager();
    public static List<Region> regions = new ArrayList<>();
    public static List<TeamStats> teamStats = new ArrayList<>();
    public static List<Team> allTeams = new ArrayList<>();
    public static String id ;
    public static String token;
    public static boolean status;

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            active = fragmentMain = new MainPage();
                            beginTransaction(fragmentManager, R.id.pageContainer, active, MAIN);
                            return true;
                        case R.id.navigation_tournament:
                            fragmentManager.beginTransaction().replace(R.id.pageContainer, new TournamentPage(PersonalActivity.this)).addToBackStack(TOURNAMENT).commit();
                            active = fragmentTournament;
                            return true;
                        case R.id.navigation_club:
                            fragmentManager.beginTransaction().replace(R.id.pageContainer, new ClubPage()).addToBackStack(CLUB).commit();
                            active = fragmentClub;
                            return true;
                        case R.id.navigation_players:
                            fragmentManager.beginTransaction().replace(R.id.pageContainer, new PlayersPage()).addToBackStack(PLAYERS).commit();
                            active = fragmentPlayers;
                            return true;
                        case R.id.navigation_user:

                            boolean status = SaveSharedPreference.getLoggedStatus(getApplicationContext());
                            if (status) {
                                log.debug("ЗАРЕГАН");
                                log.debug(SaveSharedPreference.getObject().getToken());
                                Log.d(TAG, "User ID: " + SaveSharedPreference.getObject().getUser().getId());

                                fragmentManager.beginTransaction().replace(R.id.pageContainer, new AuthoUser(PersonalActivity.this)).addToBackStack(USER).commit();
                                active = authoUser;
                            } else {
                                log.error("НЕ ЗАРЕГАН");
                                fragmentManager.beginTransaction().replace(R.id.pageContainer, new UserPage(PersonalActivity.this)).addToBackStack(USER).commit();
                                active = fragmentUser;
                            }
                            return true;
                    }
                    return false;
                }
            };

    private void beginTransaction (FragmentManager fragmentManager, int resourceId, Fragment fragment, String backStack) {
        fragmentManager.beginTransaction().replace(resourceId, fragment).addToBackStack(backStack).commit();
    }

    public PersonalActivity() {
        fragmentTournament = new TournamentPage(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAnimation();
        setContentView(R.layout.activity_personal);
        ProgressDialog mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Загрузка...");

//        checkConnection();
//        checkConnectionSingle();
        showSnack();
        checkConnectionSingle();

        try {
            bottomNavigationView = findViewById(R.id.bottom_navigation_view);
//                        BottomNavigationViewHelper helper = new BottomNavigationViewHelper();
//            BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        } catch (Exception e) {
            log.error("ERROR: ", e);
        }
//        fragmentManager.beginTransaction().add(R.id.pageContainer, fragmentHome, "1").commit();


        fragmentManager.beginTransaction().setReorderingAllowed(true)
                .add(R.id.pageContainer, fragmentMain, "1")
                .commit();

        // bottomNavigationView.getChildAt(0);
        //set font
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/manrope_regular.otf");
        CustomTypefaceSpan typefaceSpan = new CustomTypefaceSpan("", tf);
        for (int i = 0; i < bottomNavigationView.getMenu().size(); i++) {
            MenuItem menuItem = bottomNavigationView.getMenu().getItem(i);
            SpannableStringBuilder spannableTitle = new SpannableStringBuilder(menuItem.getTitle());
            spannableTitle.setSpan(typefaceSpan, 0, spannableTitle.length(), 0);
            menuItem.setTitle(spannableTitle);
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        int count = getSupportFragmentManager().getBackStackEntryCount();
        Log.d("PersonalActivity", "back stack entry count " + count);

        if (count > 0) {
            FragmentManager.BackStackEntry backStackEntry = getSupportFragmentManager().getBackStackEntryAt(count - 1);
            String transactionName = backStackEntry.getName();

            if (transactionName != null) {
                int itemNum = 0;
                switch (transactionName) {
                    case MAIN:
                        itemNum = 0;
                        break;
                    case TOURNAMENT:
                        itemNum = 1;
                        break;
                    case CLUB:
                        itemNum = 2;
                        break;
                    case PLAYERS:
                        itemNum = 3;
                        break;
                    case USER:
                        itemNum = 4;
                        break;
                }
                bottomNavigationView.getMenu().getItem(itemNum).setChecked(true);
            }
        } else if (count == 0) {
            bottomNavigationView.getMenu().getItem(0).setChecked(true);
        }

    }

    @SuppressLint("CheckResult")
    private void checkConnectionSingle() {
        Single<Boolean> single = ReactiveNetwork.checkInternetConnectivity();
        //noinspection ResultOfMethodCallIgnored
        single
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isConnectedToInternet -> GetAdvertising());
    }

    @SuppressLint("CheckResult")
    private void GetAdvertising() {
        //noinspection ResultOfMethodCallIgnored
        Controller.getApi().getAdvertising("1", "0")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::showAds
                        ,
                        error -> {
                            CheckError checkError = new CheckError();
                            checkError.checkError(this, error);
                        }
                );
    }

    private void showAds(Advertisings news) {
        if (news.getAds().size() != 0) {
            AdvertisingFragment dialogFragment = AdvertisingFragment.newInstance();
            Bundle args = new Bundle();
            args.putSerializable("ADVERTISING", (Serializable) news.getAds());
            dialogFragment.setArguments(args);
            dialogFragment.show(this.getSupportFragmentManager(), "dialogFragment");
        }
    }

    private void showSnack() {
        //all allLeagues
        GetAllTournaments();
        //all players
        GetAllPlayers();
        //all clubs
//        GetAllClubs();
        GetAllRegions();
        getAllTeams();
        if (SaveSharedPreference.getLoggedStatus(getApplicationContext())) {
            log.error("REFRESH USER");
            RefreshUser();
        }
    }
    private void getAllMatches(){

    }

    private void getAllTeams(){
        Controller.getApi().getTeams(null).enqueue(new Callback<List<Team>>() {
            @Override
            public void onResponse(Call<List<Team>> call, Response<List<Team>> response) {
                if(response.isSuccessful()){
                    if(response.body()!=null){
                        allTeams.clear();
                        allTeams.addAll(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Team>> call, Throwable t) {

            }
        });
    }
    private void GetAllRegions() {
        Controller.getApi().getRegions().enqueue(new Callback<List<Region>>() {
            @Override
            public void onResponse(@NonNull Call<List<Region>> call, @NonNull Response<List<Region>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        MankindKeeper.getInstance().regions.clear();
                        MankindKeeper.getInstance().regions.addAll(response.body());
                    }
                }}

            @Override
            public void onFailure(@NonNull Call<List<Region>> call, @NonNull Throwable t) {

            }
        });
    }
    @SuppressLint("CheckResult")
    private void getAllTeamStats(){
        Controller.getApi().getTeamStats(null).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread())
                .subscribe(stats->{
                    teamStats.clear();
                    teamStats.addAll(stats);
                },error -> {
                    CheckError checkError = new CheckError();
                    checkError.checkError(this, error);
                });



    }
    @SuppressLint("CheckResult")
    private void RefreshUser() {
        //noinspection ResultOfMethodCallIgnored
        Controller.getApi().refreshUser(SaveSharedPreference.getObject().getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .repeatWhen(completed -> completed.delay(5, TimeUnit.MINUTES))
                .subscribe(SaveSharedPreference::editObject,
                        error -> (new CheckError()).toastError(PersonalActivity.this, error)
                );
    }

    private void GetAllTournaments() {
        Controller.getApi().getAllLeagues("25", "0").enqueue(new Callback<List<League>>() {
            @Override
            public void onResponse(@NonNull Call<List<League>> call, @NonNull Response<List<League>> response) {
                if (response.body() != null)
                    for (League l: response.body())
                        if (!MankindKeeper.getInstance().allLeagues.contains(l))
                            MankindKeeper.getInstance().allLeagues.add(l);
            }

            @Override
            public void onFailure(@NonNull Call<List<League>> call, @NonNull Throwable t) {
                (new CheckError()).toastError(PersonalActivity.this, t);
            }
        });

        Controller.getApi().getAllTourneys().enqueue(new Callback<List<Tourney>>() {
            @Override
            public void onResponse(@NonNull Call<List<Tourney>> call, @NonNull Response<List<Tourney>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        MankindKeeper.getInstance().allTourneys.clear();
                        MankindKeeper.getInstance().allTourneys.addAll(response.body());
                    }
                }

            }
            @Override
            public void onFailure(@NonNull Call<List<Tourney>> call, @NonNull Throwable t) {
                Log.e("ERROR: ", Objects.requireNonNull(t.getMessage()));
            }
        });

    }

    private void savePlayers(List<Person> people1) {
        for (Person p: people1)
            if (!MankindKeeper.getInstance().allPlayers.containsKey(p.get_id()))
                MankindKeeper.getInstance().allPlayers.put(p.get_id(), p);
    }

    @SuppressLint("CheckResult")
    private void GetAllPlayers() {
        //noinspection ResultOfMethodCallIgnored
        Controller.getApi().getAllPersons( null, "25", "0")
                .subscribeOn(Schedulers.io())
                .retryWhen(throwableObservable -> throwableObservable.take(3).delay(30, TimeUnit.SECONDS))
                .observeOn(AndroidSchedulers.mainThread())
                .repeatWhen(completed -> completed.delay(5, TimeUnit.MINUTES))
                .subscribe(this::savePlayers,
                        (error) -> (new CheckError()).toastError(PersonalActivity.this, error)
                );
    }

    private void GetAllClubs() {
        Controller.getApi().getAllClubs().enqueue(new Callback<List<Club>>() {
            @Override
            public void onResponse(@NonNull Call<List<Club>> call, @NonNull Response<List<Club>> response) {
                MankindKeeper.getInstance().allClubs.clear();
                if (response.body() != null)
                    MankindKeeper.getInstance().allClubs.addAll(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<Club>> call, @NonNull Throwable t) {
                (new CheckError()).toastError(PersonalActivity.this, t);
            }
        });
    }

    public void setAnimation() {
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.START);
        slide.setDuration(400);
        slide.setInterpolator(new DecelerateInterpolator());
        getWindow().setExitTransition(slide);
        getWindow().setEnterTransition(slide);
    }

    public Fragment getActive() {
        return active;
    }

    public void setActive(Fragment active) {
        this.active = active;
    }

    public Fragment getFragmentUser() {
        return fragmentUser;
    }

    public void setFragmentUser(Fragment fragmentUser) {
        this.fragmentUser = fragmentUser;
    }
}

