package baikal.web.footballapp.user.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import baikal.web.footballapp.CheckError;
import baikal.web.footballapp.Controller;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.SetImage;
import baikal.web.footballapp.club.activity.ClubPage;
import baikal.web.footballapp.controller.CustomTypefaceSpan;
import baikal.web.footballapp.model.Club;
import baikal.web.footballapp.model.GetLeagueInfo;
import baikal.web.footballapp.model.PendingTeamInvite;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.PersonTeams;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.model.User;
import baikal.web.footballapp.user.adapter.RVOwnCommandAdapter;
import baikal.web.footballapp.user.adapter.RVUserCommandAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class AuthoUser extends Fragment {
    private static final String TAG = "AuthoUser";
    private static final Logger log = LoggerFactory.getLogger(AuthoUser.class);

    private DrawerLayout drawer;
    private Person person;
    public static FloatingActionButton fab;
    //adapters
    public static RVUserCommandAdapter adapterCommand;
    public static RVOwnCommandAdapter adapterOwnCommand;

    private TextView invBadge;
    public static final List<Person> allReferees = new ArrayList<>();
    public static List<PersonTeams> personOngoingLeagues;
    public static List<PersonTeams> personOwnCommand;
    static List<Team> createdTeams = new ArrayList<>();
    public static List<PersonTeams> personCommand;
    public static List<PendingTeamInvite> pendingTeamInvitesList;

    private TextView categoryTitle;
    public TextView textName;
    ImageButton buttonOpenProfile;

    //Menu items
    private final InvitationFragment    firstFragment = new InvitationFragment();
    private final AddTournamentFragment defaultFragment = new AddTournamentFragment();
    private final UserClubs             secondFragment = new UserClubs();
    private final TimeTableFragment     timeTableFragment = new TimeTableFragment();
    private final RefereeFragment       refereeFragment = new RefereeFragment();
    private final MyMatches             myMatches = new MyMatches();
    private final UserCommands          commands = new UserCommands();

    private User user;
    private Menu m;
    private NavigationView nvDrawer;
    private FloatingActionButton fab1;
    private final int REQUEST_CODE_CLUBEDIT = 276;
    private final int REQUEST_CODE_PROFILE_DATA_EDIT = 4214;
    private int clubOldIndex;

    private Activity activity;

    private FragmentManager fragmentManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @SuppressLint("RestrictedApi")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        log.info("INFO: AuthoUser onCreateView");
        View view;
        ImageButton button;
        view = inflater.inflate(R.layout.user_autho, container, false);// Find our drawer view
        drawer = view.findViewById(R.id.drawer_layout);
        nvDrawer = view.findViewById(R.id.nvView);
        nvDrawer.setItemIconTintList(null);
        View view1 = nvDrawer.getHeaderView(0);
        buttonOpenProfile = view1.findViewById(R.id.userProfileOpen);
        textName = view1.findViewById(R.id.navigationName);
        try {
            categoryTitle = view.findViewById(R.id.categoryType);
            user = SaveSharedPreference.getObject();
            person = user.getUser();
//            List<PersonTeams> personTeams = new ArrayList<>();
//            personTeams = person.getParticipation();
            personOngoingLeagues = new ArrayList<>();
            personOwnCommand = new ArrayList<>();
            personCommand = new ArrayList<>();
            pendingTeamInvitesList = new ArrayList<>();
//            List<PendingTeamInvite> pendingTeamInvites = person.getPendingTeamInvites();
            invBadge = (TextView) nvDrawer.getMenu().findItem(R.id.nav_first_fragment).getActionView();
            Controller.getApi().getTeams(PersonalActivity.id).enqueue(new Callback<List<Team>>() {
                @Override
                public void onResponse(@NonNull Call<List<Team>> call, @NonNull Response<List<Team>> response) {
                    if(response.isSuccessful()){
                        if(response.body()!=null){
                            createdTeams.addAll(response.body());
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Team>> call, @NonNull Throwable t) {

                }
            });
//            userGetType();


            try {
                textName.setText(person.getName());
                if (person.getName().equals("")) {
                    textName.setText("Имя");
                }
            } catch (NullPointerException e) {
                textName.setText("Имя");
            }

            SetImage setImage = new SetImage();
            setImage.setImage(getActivity(), buttonOpenProfile, person.getPhoto());
            m = nvDrawer.getMenu();
            for (int i = 0; i < m.size(); i++) {
                MenuItem mi = m.getItem(i);

                //for applying a font to subMenu ...
                SubMenu subMenu = mi.getSubMenu();
                if (subMenu != null && subMenu.size() > 0)
                    for (int j = 0; j < subMenu.size(); j++) {
                        MenuItem subMenuItem = subMenu.getItem(j);
                        applyFontToMenuItem(subMenuItem);
                    }
                applyFontToMenuItem(mi);
            }

        } catch (NullPointerException | IllegalStateException e) {
            log.error("ERROR: ", e);
        }

        fab = view.findViewById(R.id.addCommandButton);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) activity).setSupportActionBar(toolbar);
        button = view.findViewById(R.id.drawerBtn);
        button.setOnClickListener(v -> drawer.openDrawer(GravityCompat.END));


        buttonOpenProfile.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(getActivity(), UserInfo.class);
                startActivityForResult(intent, REQUEST_CODE_PROFILE_DATA_EDIT);
            } catch (Exception e) {
                log.error("ERROR: ", e);
            }

        });

        fab1 = view.findViewById(R.id.buttonEditClub);
        fab1.setVisibility(View.INVISIBLE);

        selectDrawerItem(nvDrawer.getMenu().getItem(0));

        // Setup drawer view
        setupDrawerContent(nvDrawer);
        fab.setVisibility(View.INVISIBLE);

        fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();

        return view;
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(activity.getAssets(), "fonts/manrope_regular.otf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        if (item.getItemId() == android.R.id.home) {
            drawer.openDrawer(GravityCompat.START);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    selectDrawerItem(menuItem);
                    return true;
                });
    }

    @SuppressLint("RestrictedApi")
    private void selectDrawerItem(MenuItem menuItem) {
        FragmentTransaction menuFragmentTransaction = this.getChildFragmentManager().beginTransaction();
        switch (menuItem.getItemId()) {
            case R.id.nav_default_fragment:
                try {
                    setItemColor(1);
                    clearItemColor(1);
                    menuFragmentTransaction.replace(R.id.flContent, defaultFragment, "ONGOINGTOURNAMENT").show(defaultFragment).commit();
                    categoryTitle.setText(activity.getText(R.string.title_tournament));
                    fab.setVisibility(View.INVISIBLE);
                    fab1.setVisibility(View.INVISIBLE);

                } catch (Exception e) {
                    log.error("ERROR: ", e);
                }
                break;
            case R.id.nav_first_fragment:
                try {
                    setItemColor(0);
                    clearItemColor(0);
                    menuFragmentTransaction.replace(R.id.flContent, firstFragment, "INVITATIONFRAGMENT").show(firstFragment).commit();
                    categoryTitle.setText(activity.getText(R.string.invitation));
                    fab.setVisibility(View.INVISIBLE);
                    fab1.setVisibility(View.INVISIBLE);
                } catch (Exception e) {
                    log.error("ERROR: ", e);
                }
                break;
            case R.id.nav_second_fragment:
                try {
                    setItemColor(2);
                    clearItemColor(2);
                    menuFragmentTransaction.replace(R.id.flContent, secondFragment, "CLUBFRAGMENT").show(secondFragment).commit();
                    if (person.getClub() != null) {
                        fab1.setVisibility(View.VISIBLE);
                        Club club = null;
                        Person person = user.getUser();
                        for (Club club1 : PersonalActivity.allClubs) {
                            String str = person.getClub();
                            if (club1.getId().equals(str))
                                club = club1;
                        }
                        final Club finalClub = club;
                        fab1.setOnClickListener(v -> {
                            Intent intent1 = new Intent(getActivity(), UserEditClub.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("AUTHOUSERCLUBINFO", finalClub);
                            clubOldIndex = PersonalActivity.allClubs.indexOf(finalClub);
                            intent1.putExtras(bundle);
                            startActivityForResult(intent1, REQUEST_CODE_CLUBEDIT);
                        });
                    }
                    categoryTitle.setText(activity.getText(R.string.title_club));
                    fab.setVisibility(View.INVISIBLE);
                } catch (Exception e) {
                    log.error("ERROR: ", e);
                }
                break;
            case R.id.nav_command_fragment:
                try {
                    setItemColor(3);
                    clearItemColor(3);
                    menuFragmentTransaction.replace(R.id.flContent, commands).show(commands).commit();
                    categoryTitle.setText(activity.getText(R.string.commands));
                    fab.setVisibility(View.VISIBLE);
                    fab1.setVisibility(View.INVISIBLE);
                } catch (Exception e) {
                    log.error("ERROR: ", e);
                }
                break;
            case R.id.nav_third_fragment:
                try {
                    setItemColor(5);
                    clearItemColor(5);
                    menuFragmentTransaction.replace(R.id.flContent, myMatches, "MYMATCHESFRAGMENT").show(myMatches).commit();
                    categoryTitle.setText(activity.getText(R.string.matches));
                    fab.setVisibility(View.INVISIBLE);
                    fab1.setVisibility(View.INVISIBLE);
                } catch (Exception e) {
                    log.error("ERROR: ", e);
                }
                break;
            case R.id.nav_referee_fragment:
                try {
                    setItemColor(6);
                    clearItemColor(6);
                    menuFragmentTransaction.replace(R.id.flContent, refereeFragment, "REFEREESFRAGMENT").show(refereeFragment).commit();
                    categoryTitle.setText(activity.getText(R.string.referees));
                    fab.setVisibility(View.INVISIBLE);
                    fab1.setVisibility(View.INVISIBLE);
                } catch (Exception e) {
                    log.error("ERROR: ", e);
                }
                break;
            case R.id.nav_timetable_fragment:
                try {
                    setItemColor(4);
                    clearItemColor(4);
                    menuFragmentTransaction.replace(R.id.flContent, timeTableFragment, "TIMETABLEFRAGMENT").show(timeTableFragment).commit();
                    categoryTitle.setText(activity.getText(R.string.tournamentInfoTimetable));
                    fab.setVisibility(View.INVISIBLE);
                    fab1.setVisibility(View.INVISIBLE);
                } catch (Exception e) {
                    log.error("ERROR: ", e);
                }
                break;
            default:
                SaveSharedPreference.setLoggedIn(activity.getApplicationContext(), false);
                SaveSharedPreference.saveObject(null);
//                PersonalActivity.fragmentUser.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                try {
                    Fragment userPage = new UserPage();
                    fragmentManager.beginTransaction()
                            .replace(R.id.pageContainer, userPage)
                            .hide(PersonalActivity.active)
                            .show(userPage)
                            .commit();

                } catch (Exception e) {
                    log.error("AuthoUser: ", e);
                    fragmentManager.beginTransaction().show(PersonalActivity.fragmentUser).commit();
                }
                PersonalActivity.active = PersonalActivity.fragmentUser;
                refreshTournaments();
                break;
        }

        menuItem.setChecked(true);
        drawer.closeDrawers();
    }

    private void clearItemColor(int itemColor) {
        m = nvDrawer.getMenu();
        for (int i = 0; i < m.size(); i++) {
            if (i != itemColor){
                MenuItem mi = m.getItem(i);

                try {
                    SpannableString s = new SpannableString(mi.getTitle());
                    s.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.colorBottomNavigationUnChecked)), 0, s.length(), 0);
                    mi.setTitle(s);
                } catch (Exception e) {
                    log.error(TAG, e);
                }
                //for aapplying a font to subMenu ...
                SubMenu subMenu = mi.getSubMenu();
                if (subMenu != null && subMenu.size() > 0)
                    for (int j = 0; j < subMenu.size(); j++) {
                        MenuItem subMenuItem = subMenu.getItem(j);
                        applyFontToMenuItem(subMenuItem);
                    }

                applyFontToMenuItem(mi);
            }
        }
    }

    @SuppressLint("CheckResult")
    private void refreshTournaments() {
            Controller.getApi().getAllLeagues("10", "0")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(PersonalActivity::saveData
                            ,
                            error ->{
                        CheckError checkError = new CheckError();
                                checkError.checkError(getActivity(), error);
                            }
                    );
        }



    @SuppressLint("CheckResult")
    private void getUserCommands(String leagueId, String teamId, PersonTeams team){

        Controller.getApi().getLeagueInfo(leagueId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getLeagueInfo -> getParticipation(getLeagueInfo, teamId, team)
                        ,
                        error -> {
                            CheckError checkError = new CheckError();
                            checkError.checkError(getActivity(), error);
                        }
                );
    }

    private void getParticipation(GetLeagueInfo getLeagueInfo, String teamId, PersonTeams team) {
        for (Team team1 : getLeagueInfo.getLeagueInfo().getTeams()) {
            if (team1.getId().equals(teamId)) {
                if (team1.getCreator().equals(person.getId())) {
                    if (!personOwnCommand.contains(team)) {
                        personOwnCommand.add(team);
                    }
                } else {
                    if (!personCommand.contains(team)) {
                        personCommand.add(team);
                    }
                }
                break;
            }
        }
    }

    @SuppressLint("SetTextI18n")
    public void SetInvNum(Activity activity, int position) {
        Typeface font = Typeface.createFromAsset(activity.getAssets(), "fonts/manrope_bold.otf");
        invBadge.setTypeface(font);
        invBadge.setGravity(Gravity.CENTER_VERTICAL);
        invBadge.setTextColor(activity.getResources().getColor(R.color.colorAccent));
        if (position > 99) {
            invBadge.setText("99+");
        } else {
            if (position == 0) {
                invBadge.setText(null);
            } else {
                invBadge.setText(String.valueOf(position));
            }
        }
    }

    @Override
    public void onDestroy() {
        log.info("INFO: AuthoUser onDestroy");
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_CLUBEDIT) {
                Club result = (Club) data.getExtras().getSerializable("CREATECLUBRESULT");
                Person person1 = SaveSharedPreference.getObject().getUser();
                person1.setClub(result.getId());
//                SaveSharedPreference.getObject().setUser(person1);
                User user = SaveSharedPreference.getObject();
                user.setUser(person1);
                SaveSharedPreference.editObject(user);
                try {
                    if (PersonalActivity.allClubs.size() == 1) {
                        PersonalActivity.allClubs.clear();
                        PersonalActivity.allClubs.add(result);
                    } else {
                        PersonalActivity.allClubs.set(clubOldIndex, result);
                    }
                }catch (Exception e){}
                List<Club> list = new ArrayList<>(PersonalActivity.allClubs);
                ClubPage.adapter.dataChanged(list);
                FragmentTransaction ft = this.getChildFragmentManager().beginTransaction();
                ft.detach(secondFragment).attach(secondFragment).commit();
                Toast.makeText(getActivity(), "Изменения сохранены", Toast.LENGTH_LONG).show();
            }

            if (requestCode == REQUEST_CODE_PROFILE_DATA_EDIT) {
                user = (User) data.getExtras().getSerializable("newUserData");

                SaveSharedPreference.saveObject(user);
//                buttonOpenProfile
            }
        }
    }

    private void setItemColor(int itemColor) {
            MenuItem mi = m.getItem(itemColor);
            try {
                    SpannableString s = new SpannableString(mi.getTitle());
                    s.setSpan(new ForegroundColorSpan(getActivity().getResources().getColor(R.color.colorAccent)), 0, s.length(), 0);
                    mi.setTitle(s);
            } catch (Exception e) {
                log.error("ERROR: ", e);
            }
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            applyFontToMenuItem(mi);
    }
}
