package baikal.web.footballapp.user.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebStorage;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import baikal.web.footballapp.CheckError;
import baikal.web.footballapp.Controller;
import baikal.web.footballapp.MankindKeeper;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.SetImage;
import baikal.web.footballapp.club.activity.ClubPage;
import baikal.web.footballapp.model.Club;
import baikal.web.footballapp.model.Invite;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.PendingTeamInvite;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.PersonTeams;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.model.User;
import baikal.web.footballapp.user.adapter.RVOwnCommandAdapter;
import baikal.web.footballapp.user.adapter.RVUserCommandAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class AuthoUser extends Fragment {
    private static final String TAG = "AuthoUser";
    private static final Logger log = LoggerFactory.getLogger(AuthoUser.class);

    private Person person;
    public static FloatingActionButton fab;
    private FloatingActionButton fab1;
    //adapters
    public static RVUserCommandAdapter adapterCommand;
    public static RVOwnCommandAdapter adapterOwnCommand;

    static final List<Person> allReferees = new ArrayList<>();
    public static List<PersonTeams> personOngoingLeagues;
    public static List<PersonTeams> personOwnCommand;
    static List<Team> createdTeams = new ArrayList<>();
    public static List<Team> personCommand;
    public static List<PendingTeamInvite> pendingTeamInvitesList;

    private TextView categoryTitle;

    //Menu header
    private ImageButton buttonOpenProfile;
    private TextView textName;

    //Menu items
    private RelativeLayout  invitationBtn;
    private RelativeLayout  teamBtn;
    private RelativeLayout  tournamentBtn;

    private LinearLayout    trainerMenu;
    private RelativeLayout  trainerTeamBtn;

    private LinearLayout    refereeMenu;
    private RelativeLayout  refereeTimeTableBtn;
    private RelativeLayout  refereeMatchesBtn;
    private RelativeLayout  refereeTournamentBtn;

    private RelativeLayout  signOutBtn;

    //Menu title holder
    private List<TextView> menuTitles;
    private TextView invitationMT;
    private TextView teamMT;
    private TextView tournamentMT;
    private TextView trainerTeamMT;
    private TextView refTimeTableMT;
    private TextView refMatchesMT;
    private TextView refTournamentsMT;

    //Menu fragments
    private InvitationFragment    invitationFragment = new InvitationFragment();
    private AddTournamentFragment addTournamentFragment = new AddTournamentFragment();
    private UserClubs             userClubFragment = new UserClubs();
    private TimeTableFragment     timeTableFragment = new TimeTableFragment();
    private MyMatches             myMatchesFragment = new MyMatches();
    private UserCommands          commandsFragment = new UserCommands();

    private User user;
    private DrawerLayout mDrawerLayout;

    public static ImageButton setSingleReferee;
    public static ImageButton cancelSetSingleReferee;
    public static ImageButton saveSingleReferee;
    private final int REQUEST_CODE_PROFILE_DATA_EDIT = 4214;

    public PersonalActivity activity;
    private FragmentManager fragmentManager;

    String[] appBarTitles = {"Приглашения", "Команды", "Турниры", "Расписание", "Мои матчи"};

    public AuthoUser (PersonalActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("RestrictedApi")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        log.info("INFO: AuthoUser onCreateView");
        View view = inflater.inflate(R.layout.user_autho, container, false);// Find our drawer view
        setSingleReferee = view.findViewById(R.id.setSingleReferee);
        cancelSetSingleReferee = view.findViewById(R.id.cancelSetSingleReferee);
        saveSingleReferee = view.findViewById(R.id.saveSingleReferee);
        mDrawerLayout = view.findViewById(R.id.drawer_layout);

        ImageButton button = view.findViewById(R.id.drawerBtn);
        button.setOnClickListener(v -> mDrawerLayout.openDrawer(GravityCompat.END));

//**************************************************************************************************
        buttonOpenProfile = view.findViewById(R.id.UA_userProfileOpen);
        textName          = view.findViewById(R.id.UA_userName);
        invitationBtn        = view.findViewById(R.id.UA_inv_button);
        teamBtn              = view.findViewById(R.id.UA_team_button);
        tournamentBtn        = view.findViewById(R.id.UA_tournament_button);
        trainerMenu       = view.findViewById(R.id.UA_trainer_menu);
        trainerTeamBtn       = view.findViewById(R.id.UA_trainer_team_button);
        refereeMenu       = view.findViewById(R.id.UA_referee_menu);
        refereeTimeTableBtn  = view.findViewById(R.id.UA_ref_timeTable_button);
        refereeMatchesBtn    = view.findViewById(R.id.UA_ref_matches_button);
        refereeTournamentBtn = view.findViewById(R.id.UA_ref_tournament_button);
        signOutBtn       = view.findViewById(R.id.UA_quit_button);
//**************************************************************************************************
        invitationMT     = view.findViewById(R.id.UA_inv_TV);
        teamMT           = view.findViewById(R.id.UA_team_TV);
        tournamentMT     = view.findViewById(R.id.UA_tournament_TV);
        trainerTeamMT    = view.findViewById(R.id.UA_trainer_team_TV);
        refTimeTableMT   = view.findViewById(R.id.UA_ref_timeTable_TV);
        refMatchesMT     = view.findViewById(R.id.UA_ref_matches_TV);
        refTournamentsMT = view.findViewById(R.id.UA_ref_tournament_TV);
        menuTitles = new ArrayList<>();
        menuTitles.add(invitationMT    );
        menuTitles.add(teamMT          );
        menuTitles.add(tournamentMT    );
        menuTitles.add(trainerTeamMT   );
        menuTitles.add(refTimeTableMT  );
        menuTitles.add(refMatchesMT    );
        menuTitles.add(refTournamentsMT);
//**************************************************************************************************

        categoryTitle = view.findViewById(R.id.categoryType);
        fab = view.findViewById(R.id.addCommandButton);
        fab1 = view.findViewById(R.id.buttonEditClub);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        personOngoingLeagues = new ArrayList<>();
        personOwnCommand = new ArrayList<>();
        personCommand = new ArrayList<>();
        pendingTeamInvitesList = new ArrayList<>();

        fab.setVisibility(View.INVISIBLE);
        fab1.setVisibility(View.INVISIBLE);
        fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        setMenuItemListeners();
        showFragment(invitationFragment,   invitationMT, false, false, 0);
        openRefereeMenu();

        try {
            user = SaveSharedPreference.getObject();
            person = user.getUser();
            if (person != null && person.getId() != null) {
                textName.setText(person.getName());
                (new SetImage()).setImage(getActivity(), buttonOpenProfile, person.getPhoto());

                Controller.getApi().getUsersTeams(person.get_id()).enqueue(new Callback<List<Invite>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Invite>> call, @NonNull Response<List<Invite>> response) {
                        if (response.isSuccessful())
                            if (response.body() != null)
                                for (Invite invite : response.body())
                                    personCommand.add(invite.getTeam());
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Invite>> call, @NonNull Throwable t) {

                    }
                });
                Controller.getApi().getTeams(person.get_id()).enqueue(new Callback<List<Team>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Team>> call, @NonNull Response<List<Team>> response) {
                        if (response.isSuccessful())
                            if (response.body() != null && response.body().size() > 0) {
                                createdTeams.clear();
                                createdTeams.addAll(response.body());
                                openTrainerMenu();
                            }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Team>> call, @NonNull Throwable t) {

                    }
                });

            }
        } catch (Exception e) {
            log.error("ERROR: ", e);
            Log.e("ERROR: ", e.toString());
        }
        return view;
    }

    private void setMenuItemListeners()
    {
        buttonOpenProfile.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(getActivity(), UserInfo.class);
                startActivityForResult(intent, REQUEST_CODE_PROFILE_DATA_EDIT);
            } catch (Exception e) {
                log.error("ERROR: ", e);
            }
        });
                    //{"Приглашения", "Команды", "Турниры", "Расписание", "Мои матчи"};
        invitationBtn.setOnClickListener(v->showFragment(invitationFragment,   invitationMT, false, false, 0));
        teamBtn.setOnClickListener      (v->showFragment(commandsFragment,     teamMT,       false, true, 1));
        tournamentBtn.setOnClickListener(v->showFragment(addTournamentFragment, tournamentMT,false, false, 2));

        trainerTeamBtn.setOnClickListener (v->showFragment(commandsFragment, trainerTeamMT, false, true, 1));

        refereeTimeTableBtn.setOnClickListener  (v->showFragment(timeTableFragment, refTimeTableMT, true, false,3 ));
        refereeMatchesBtn.setOnClickListener    (v->showFragment(myMatchesFragment, refMatchesMT,   false, false, 4));
        refereeTournamentBtn.setOnClickListener (v->showFragment(addTournamentFragment, refTournamentsMT, false, false, 2));

        signOutBtn.setOnClickListener(v->{
            WebStorage.getInstance().deleteAllData();
            SaveSharedPreference.setLoggedIn(activity.getApplicationContext(), false);
            SaveSharedPreference.saveObject(null);
//                PersonalActivity.fragmentUser.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            setSingleReferee.setVisibility(View.GONE);
            cancelSetSingleReferee.setVisibility(View.GONE);
            saveSingleReferee.setVisibility(View.GONE);
            try {
                Fragment userPage = new UserPage(activity);
                fragmentManager.beginTransaction()
                        .replace(R.id.pageContainer, userPage)
                        .hide(activity.getActive())
                        .show(userPage)
                        .commit();

            } catch (Exception e) {
                log.error("AuthoUser: ", e);
                activity.setFragmentUser(new UserPage(activity));
                fragmentManager.beginTransaction().show(activity.getFragmentUser()).commit();
            }
            activity.setActive(activity.getFragmentUser());
            refreshTournaments();
        });
    }

    @SuppressLint("RestrictedApi")
    private void showFragment (Fragment fragmentToShow, TextView textView,
                               boolean isSingleRefVisible, boolean isFabV, int pos) {
        categoryTitle.setText(appBarTitles[pos]);
        try {
            setItemColor(textView);
            cancelSetSingleReferee.setVisibility(View.GONE);
            saveSingleReferee.setVisibility(View.GONE);
            setSingleReferee.setVisibility(isSingleRefVisible ? View.VISIBLE : View.GONE);
            fab.setVisibility(isFabV ? View.VISIBLE : View.GONE);
            FragmentTransaction menuFragmentTransaction = this.getChildFragmentManager().beginTransaction();
            menuFragmentTransaction.replace(R.id.flContent, fragmentToShow).show(fragmentToShow).commit();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            log.error(TAG, e);
        }
        mDrawerLayout.closeDrawers();
    }

    private void setItemColor(TextView textView) {
        for (TextView tv: menuTitles)
            tv.setTextColor(activity.getResources().getColor(R.color.colorBottomNavigationUnChecked));
        textView.setTextColor(activity.getResources().getColor(R.color.colorAccent));
    }

    @SuppressLint("CheckResult")
    private void refreshTournaments() {
        Controller.getApi().getAllLeagues("10", "0").enqueue(new Callback<List<League>>() {
            @Override
            public void onResponse(@NonNull Call<List<League>> call, @NonNull Response<List<League>> response) {
                if (response.body() != null)
                    for (League l: response.body())
                        if (!MankindKeeper.getInstance().allLeagues.contains(l))
                            MankindKeeper.getInstance().allLeagues.add(l);
            }

            @Override
            public void onFailure(@NonNull Call<List<League>> call, @NonNull Throwable t) {
                CheckError checkError = new CheckError();
                try {
                    checkError.checkError(getActivity(), t);
                } catch (Exception ignored) { }

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            int REQUEST_CODE_CLUBEDIT = 276;
            if (requestCode == REQUEST_CODE_CLUBEDIT) {
                Club result = (Club) Objects.requireNonNull(data.getExtras()).getSerializable("CREATECLUBRESULT");
                Person person1 = SaveSharedPreference.getObject().getUser();
                person1.setClub(Objects.requireNonNull(result).getId());
//                SaveSharedPreference.getObject().setUser(person1);
                User user = SaveSharedPreference.getObject();
                user.setUser(person1);
                SaveSharedPreference.editObject(user);
                try {
                    if (MankindKeeper.getInstance().allClubs.size() == 1) {
                        MankindKeeper.getInstance().allClubs.clear();
                        MankindKeeper.getInstance().allClubs.add(result);
                    } else
                        MankindKeeper.getInstance().updateClub(result);
                } catch (Exception ignored){}
                List<Club> list = new ArrayList<>(MankindKeeper.getInstance().allClubs);
                ClubPage.adapter.dataChanged(list);
                FragmentTransaction ft = this.getChildFragmentManager().beginTransaction();
                ft.detach(userClubFragment).attach(userClubFragment).commit();
                Toast.makeText(getActivity(), "Изменения сохранены", Toast.LENGTH_LONG).show();
            }

            if (requestCode == REQUEST_CODE_PROFILE_DATA_EDIT) {
                person = (Person) Objects.requireNonNull(data.getExtras()).getSerializable("newUserData");
                user.setUser(person);
                SaveSharedPreference.saveObject(user);

                MankindKeeper.getInstance().addPerson(person);
            }
        }
    }

    @SuppressLint("SetTextI18n")
    public void SetInvNum(Activity activity, int position) {

    }

    private void openTrainerMenu()
    {
        trainerMenu.setVisibility(View.VISIBLE);
        teamBtn.setVisibility(View.GONE);
    }

    private void openRefereeMenu ()
    {
        refereeMenu.setVisibility(View.VISIBLE);
        tournamentBtn.setVisibility(View.GONE);
    }
}
