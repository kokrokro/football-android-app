package baikal.web.footballapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
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
import androidx.lifecycle.ViewModelProviders;

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import baikal.web.footballapp.club.activity.ClubPage;
import baikal.web.footballapp.controller.CustomTypefaceSpan;
import baikal.web.footballapp.home.activity.MainPage;
import baikal.web.footballapp.model.Advertisings;
import baikal.web.footballapp.model.Club;
import baikal.web.footballapp.model.Clubs;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.People;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.Region;
import baikal.web.footballapp.model.Tournaments;
import baikal.web.footballapp.model.Tourney;
import baikal.web.footballapp.players.activity.PlayersPage;
import baikal.web.footballapp.tournament.activity.TournamentPage;
import baikal.web.footballapp.user.activity.AuthoUser;
import baikal.web.footballapp.user.activity.UserPage;
import baikal.web.footballapp.viewmodel.MainViewModel;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

public class PersonalActivity extends AppCompatActivity {
    public static final List<Person> allPlayers = new ArrayList<>();
    public static final List<Person> people = new ArrayList<>();
    public static final List<Person> AllPeople = new ArrayList<>();

    public static List<Tourney> allTourneys = new ArrayList<>();
    public static List<League> tournaments = new ArrayList<>();
    public static List<Region> regions = new ArrayList<>();
    public static List<Club> allClubs = new ArrayList<>();

    private static final String MAIN = "MAIN_PAGE";
    private static final String TOURNAMENT = "TOURNAMENT_PAGE";
    private static final String CLUB = "CLUB_PAGE";
    private static final String PLAYERS = "PLAYERS_PAGE";
    private static final String USER = "USER_PAGE";

    public static MainPage       fragmentMain = new MainPage();
    public static TournamentPage fragmentTournament = new TournamentPage();
    public static ClubPage       fragmentClub = new ClubPage();
    public static PlayersPage    fragmentPlayers = new PlayersPage();
    public static AuthoUser      authoUser = new AuthoUser();
    public static UserPage       fragmentUser = new UserPage();

    public static Fragment active = fragmentMain;

    private static BottomNavigationView bottomNavigationView;
    private final Logger log = LoggerFactory.getLogger(PersonalActivity.class);


    private final FragmentManager fragmentManager = this.getSupportFragmentManager();

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
                            fragmentManager.beginTransaction().replace(R.id.pageContainer, active).addToBackStack(MAIN).commit();
                            return true;
                        case R.id.navigation_tournament:
                            active = fragmentTournament = new TournamentPage();
                            fragmentManager.beginTransaction().replace(R.id.pageContainer, active).addToBackStack(TOURNAMENT).commit();
                            return true;
                        case R.id.navigation_club:
                            active = fragmentClub = new ClubPage();
                            fragmentManager.beginTransaction().replace(R.id.pageContainer, active).addToBackStack(CLUB).commit();
                            return true;
                        case R.id.navigation_players:
                            active = fragmentPlayers = new PlayersPage();
                            fragmentManager.beginTransaction().replace(R.id.pageContainer, active).addToBackStack(PLAYERS).commit();
                            return true;
                        case R.id.navigation_user:
                            status = SaveSharedPreference.getLoggedStatus(getApplicationContext());
                            if (status) {
                                id = SaveSharedPreference.getObject().getUser().getId();

                                active = authoUser = new AuthoUser();
                                authoUser.setFragmentManager(fragmentManager);
                                fragmentManager.beginTransaction().replace(R.id.pageContainer, authoUser).addToBackStack(USER).commit();
                            } else {
                                log.error("НЕ ЗАРЕГАН");
                                fragmentManager.beginTransaction().replace(R.id.pageContainer, new UserPage()).addToBackStack(USER).commit();
                                active = fragmentUser;
                            }
                            return true;
                    }
                    return false;
                }
            };

    public static void saveData(List<League> tournaments1) {
        tournaments.clear();
        tournaments.addAll(tournaments1);
//        TournamentPage.adapter.dataChanged(tournaments1.getLeagues());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAnimation();
        setContentView(R.layout.activity_personal);
        status = SaveSharedPreference.getLoggedStatus(getApplicationContext());
        //     FragmentManager.enableDebugLogging(true);
        if (status) {
            //log.debug(SaveSharedPreference.getObject().getToken());
            id = SaveSharedPreference.getObject().getUser().getId();
            token = SaveSharedPreference.getObject().getToken();
        }
        ProgressDialog mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Загрузка...");

        checkConnection();
        checkConnectionSingle();

        try {
            bottomNavigationView = findViewById(R.id.bottom_navigation_view);
            //            BottomNavigationViewHelper helper = new BottomNavigationViewHelper();
//            BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        } catch (Exception e) {
            log.error("ERROR: ", e);
        }

        fragmentManager.beginTransaction().setReorderingAllowed(true)
                .add(R.id.pageContainer, fragmentMain, "1").commit();

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
        single
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isConnectedToInternet -> GetAdvertising("1", "0"));
    }

    @SuppressLint("CheckResult")
    private void GetAdvertising(String limit, String offset) {
        Controller.getApi().getAdvertising(limit, offset)
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

    @SuppressLint("CheckResult")
    private void checkConnection() {
        ReactiveNetwork
                .observeNetworkConnectivity(getApplicationContext())
                .flatMapSingle(connectivity -> ReactiveNetwork.checkInternetConnectivity())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isConnected -> {
                    // isConnected can be true or false
                    if (isConnected) {
                        showSnack();
                    } else {
                        String str = "Отсутствует интернет соединение";
                        if(App.wasInBackground)
                            showToast(str);
                    }
                });
    }

    private void showSnack() {
        //all tournaments
        GetAllTournaments();
        //all players
        GetAllPlayers();
        //all clubs
        GetAllClubs();
        GetAllRegions();
        if (SaveSharedPreference.getLoggedStatus(getApplicationContext())) {
            log.error("REFRESH USER");
            RefreshUser();
        }
    }

    private void GetAllRegions() {
        Controller.getApi().getRegions().enqueue(new Callback<List<Region>>() {
            @Override
            public void onResponse(Call<List<Region>> call, Response<List<Region>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        regions.clear();
                        regions.addAll(response.body());
                    }
                }}

            @Override
            public void onFailure(Call<List<Region>> call, Throwable t) {

            }
        });
    }

    @SuppressLint("CheckResult")
    private void RefreshUser() {
        Controller.getApi().refreshUser(SaveSharedPreference.getObject().getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .repeatWhen(completed -> completed.delay(5, TimeUnit.MINUTES))
                .subscribe(SaveSharedPreference::editObject
                        ,
                        this::getError
                );
    }

    @SuppressLint("CheckResult")
    private void GetAllTournaments() {
        tournaments = new ArrayList<>();
        Controller.getApi().getAllLeagues("32575", "0")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                //.repeatWhen(completed -> completed.delay(5, TimeUnit.MINUTES))
                .subscribe(PersonalActivity::saveData
                        ,
                        this::getError
                );
        allTourneys = new ArrayList<>();
        Controller.getApi().getAllTourneys().enqueue(new Callback<List<Tourney>>() {
            @Override
            public void onResponse(Call<List<Tourney>> call, Response<List<Tourney>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        allTourneys.clear();
                        allTourneys.addAll(response.body());
                    }
                }

            }
            @Override
            public void onFailure(Call<List<Tourney>> call, Throwable t) {
                Log.e("ERROR: ", t.getMessage());
            }
        });

    }

    private void getError(Throwable error) {
        String str;
        try {

            if (error instanceof HttpException) {
                HttpException exception = (HttpException) error;
                switch (exception.code()) {
                    case 408:
                        str = "Истекло время ожидания, попробуйте позже";
                        break;
                    case 500:
                        str = "Неполадки на сервере. Попробуйте позже";
                        break;
                    case 522:
                        str = "Отсутствует соединение";
                    case 410:
                        str = "Wrong api request";
                    default:
                        break;
                }
            }
            if (error instanceof SocketTimeoutException) {
                str = "Неполадки на сервере. Попробуйте позже.";
                if(App.wasInBackground)
                    Toast.makeText(PersonalActivity.this, str, Toast.LENGTH_SHORT).show();
            }
            if (error instanceof ConnectException) {
                str = "Отсутствует соединение.";
                if(App.wasInBackground)
                    Toast.makeText(PersonalActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        } catch (ClassCastException n) {
            str = "Неполадки на сервере. Попробуйте позже.";
            if(App.wasInBackground)
                Toast.makeText(PersonalActivity.this, str, Toast.LENGTH_SHORT).show();
        }


    }

    private void savePlayers(List<Person> people1) {
        people.clear();
        AllPeople.clear();
        allPlayers.clear();
        Log.d("Persons count", String.valueOf(people1.size()));
        allPlayers.addAll(people1);
        people.addAll(allPlayers);
        AllPeople.addAll(allPlayers);
//        PlayersPage.adapter.notifyDataSetChanged();
//        PlayersPage.adapter.dataChanged(people1.getPeople());
    }

    @SuppressLint("CheckResult")
    private void GetAllPlayers() {
        String type = "player";
        Controller.getApi().getAllPersons( null, "20", "0")
                .subscribeOn(Schedulers.io())
//                .doOnSubscribe(__ -> showDialog())
//                .doOnTerminate(__ ->hideDialog())
//                .retryWhen(retryHandler -> retryHandler.flatMap(nothing -> retrySubject.asObservable()))
                .retryWhen(throwableObservable -> throwableObservable.take(3).delay(30, TimeUnit.SECONDS))
                .observeOn(AndroidSchedulers.mainThread())
                .repeatWhen(completed -> completed.delay(5, TimeUnit.MINUTES))
                .subscribe(this::savePlayers,
                        this::getError
                );
    }


    @SuppressLint("CheckResult")
    private void GetAllClubs() {
        allClubs = new ArrayList<>();
        Controller.getApi().getAllClubs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .repeatWhen(completed -> completed.delay(5, TimeUnit.MINUTES))
                .subscribe(this::saveClubs,
                        this::getError
                );
    }

    private void saveClubs(Clubs clubs) {
        allClubs.clear();
        allClubs.addAll(clubs.getClubs());
        ClubPage.adapter.dataChanged(clubs.getClubs());
//        ClubPage.adapter.notifyDataSetChanged();
    }

    private void showToast(String str) {
        this.runOnUiThread(() -> Toast.makeText(PersonalActivity.this, str, Toast.LENGTH_SHORT).show());
    }

    public void setAnimation() {
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.LEFT);
        slide.setDuration(400);
        slide.setInterpolator(new DecelerateInterpolator());
        getWindow().setExitTransition(slide);
        getWindow().setEnterTransition(slide);
    }
}

