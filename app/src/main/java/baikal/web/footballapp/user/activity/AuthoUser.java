package baikal.web.footballapp.user.activity;

import android.annotation.SuppressLint;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.MankindKeeper;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.SetImage;
import baikal.web.footballapp.club.activity.ClubPage;
import baikal.web.footballapp.model.Club;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.model.Tourney;
import baikal.web.footballapp.model.User;
import baikal.web.footballapp.user.activity.UserTeams.UserCommands;
import baikal.web.footballapp.user.adapter.TourneyListInMenuAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class AuthoUser extends Fragment {
    private static final String TAG = "AuthoUser";
    private static final Logger log = LoggerFactory.getLogger(AuthoUser.class);
    private static final int REQUEST_CODE_PROFILE_DATA_EDIT = 4214;

    private Person person;
    public FloatingActionButton fab;

    static final List<Person> allReferees = new ArrayList<>();

    private TextView categoryTitle;

    //Menu header
    private ImageButton buttonOpenProfile;

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
    private InvitationFragment    invitationFragment    ;
    private AddTournamentFragment addTournamentFragment ;
    private UserClubs             userClubFragment      ;
    private TimeTableFragment     timeTableFragment     ;
    private MyMatches             myMatchesFragment     ;
    private UserCommands          commandsFragment      ;

    private Fragment activeAU;

    private User user;
    private DrawerLayout mDrawerLayout;

    ImageButton setSingleReferee;
    ImageButton cancelSetSingleReferee;
    ImageButton saveSingleReferee;

    private List<Tourney> createdTourneys = new ArrayList<>();

    public PersonalActivity activity;
    private FragmentManager fragmentManager;

    private String[] appBarTitles = {"Приглашения", "Команды", "Турниры", "Расписание", "Мои матчи"};

    public AuthoUser (PersonalActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        invitationFragment    = new InvitationFragment();
        addTournamentFragment = new AddTournamentFragment();
        userClubFragment      = new UserClubs();
        timeTableFragment     = new TimeTableFragment(this);
        myMatchesFragment     = new MyMatches();
        commandsFragment      = new UserCommands();

        activeAU = invitationFragment;
    }

    @SuppressLint("RestrictedApi")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.user_autho, container, false);// Find our drawer view
        setSingleReferee = view.findViewById(R.id.setSingleReferee);
        cancelSetSingleReferee = view.findViewById(R.id.cancelSetSingleReferee);
        saveSingleReferee = view.findViewById(R.id.saveSingleReferee);
        mDrawerLayout = view.findViewById(R.id.drawer_layout);

        ImageButton button = view.findViewById(R.id.drawerBtn);
        button.setOnClickListener(v -> mDrawerLayout.openDrawer(GravityCompat.END));

//**************************************************************************************************
        buttonOpenProfile = view.findViewById(R.id.UA_userProfileOpen);
        TextView textName = view.findViewById(R.id.UA_userName);
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
        RecyclerView rvCreatedTeamList = view.findViewById(R.id.UA_teamList);
        RecyclerView rvCreatedTourneyList = view.findViewById(R.id.UA_tournamentList);

        rvCreatedTeamList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCreatedTourneyList.setLayoutManager(new LinearLayoutManager(getContext()));

        TourneyListInMenuAdapter tourneyListInMenuAdapter = new TourneyListInMenuAdapter(createdTourneys, this::openTourneyFragment);
        rvCreatedTourneyList.setAdapter(tourneyListInMenuAdapter);
//**************************************************************************************************

        categoryTitle = view.findViewById(R.id.categoryType);
        fab = view.findViewById(R.id.addCommandButton);
//        FloatingActionButton fab1 = view.findViewById(R.id.buttonEditClub);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);

        fab.setVisibility(View.INVISIBLE);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), NewCommand.class);
            startActivityForResult(intent, UserCommands.REQUEST_CODE_NEW_COMMAND);
        });

        fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        setMenuItemListeners();

        showFragment(activeAU,   invitationMT, false, false, 0);

        try {
            user = SaveSharedPreference.getObject();
            person = user.getUser();
            if (person != null && person.getId() != null) {
                textName.setText(person.getName());
                SetImage.setImage(getActivity(), buttonOpenProfile, person.getPhoto());

                Controller.getApi().getTeams(person.get_id()).enqueue(new Callback<List<Team>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Team>> call, @NonNull Response<List<Team>> response) {
                        if (response.isSuccessful())
                            if (response.body() != null && response.body().size() > 0) {
                                openTrainerMenu();
                            }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Team>> call, @NonNull Throwable t) { }
                });

                Controller.getApi().getMainRefsLeague(person.getId()).enqueue(new Callback<List<League>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<League>> call, @NonNull Response<List<League>> response) {
                        if (response.body() != null && response.isSuccessful() && response.body().size() > 0)
                            openRefereeMenu();
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<League>> call, @NonNull Throwable t) { }
                });

                Controller.getApi().getTourneysByCreator(person.getId()).enqueue(new Callback<List<Tourney>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Tourney>> call, @NonNull Response<List<Tourney>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().size() == 0) {
                                rvCreatedTourneyList.setVisibility(View.GONE);
                                return;
                            }
                            Log.d(TAG, "tourneys count: " + response.body().size());
                            createdTourneys.clear();
                            createdTourneys.addAll(response.body());
                            rvCreatedTourneyList.setVisibility(View.VISIBLE);
                            tourneyListInMenuAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Tourney>> call, @NonNull Throwable t) { }
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
        invitationBtn.setOnClickListener(v->showFragment(invitationFragment,    invitationMT,false, false,0));
        teamBtn.setOnClickListener      (v->showFragment(commandsFragment,      teamMT,      false, true, 1));
        tournamentBtn.setOnClickListener(v->showFragment(addTournamentFragment, tournamentMT,false, false,2));

        trainerTeamBtn.setOnClickListener (v->showFragment(commandsFragment, trainerTeamMT, false, true, 1));

        refereeTimeTableBtn.setOnClickListener (v->showFragment(timeTableFragment,     refTimeTableMT,  true,  false,3 ));
        refereeMatchesBtn.setOnClickListener   (v->showFragment(myMatchesFragment,     refMatchesMT,    false, false,4));
        refereeTournamentBtn.setOnClickListener(v->showFragment(addTournamentFragment, refTournamentsMT,false, false,2));

        signOutBtn.setOnClickListener(v->{
            WebStorage.getInstance().deleteAllData();
            SaveSharedPreference.setLoggedIn(activity.getApplicationContext(), false);
            SaveSharedPreference.saveObject(null);
            activity.EndRefreshToken();
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
                Log.e("AuthoUser: ", e.toString());
                activity.setFragmentUser(new UserPage(activity));
                fragmentManager.beginTransaction().show(activity.getFragmentUser()).commit();
            }
        });
    }

    @SuppressLint("RestrictedApi")
    private void showFragment (Fragment fragmentToShow, TextView textView,
                               boolean isSingleRefVisible, boolean isFabV, int pos) {
        Log.d("AuthoUser", fragmentToShow.toString() + " " + textView.getText());
        activeAU = fragmentToShow;
        categoryTitle.setText(appBarTitles[pos]);
        try {
            setItemColor(textView);
            cancelSetSingleReferee.setVisibility(View.GONE);
            saveSingleReferee.setVisibility(View.GONE);
            setSingleReferee.setVisibility(isSingleRefVisible ? View.VISIBLE : View.GONE);
            fab.setVisibility(isFabV ? View.VISIBLE : View.GONE);
            FragmentTransaction menuFragmentTransaction = this.getChildFragmentManager().beginTransaction();
            menuFragmentTransaction.replace(R.id.flContent, fragmentToShow).commit();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            int REQUEST_CODE_CLUB_EDIT = 276;
            if (requestCode == REQUEST_CODE_CLUB_EDIT) {
                Club result = (Club) Objects.requireNonNull(data.getExtras()).getSerializable("CREATECLUBRESULT");
                Person person1 = SaveSharedPreference.getObject().getUser();
                person1.setClub(Objects.requireNonNull(result).getId());
//                SaveSharedPreference.getObject().setUser(person1);
                User user = SaveSharedPreference.getObject();
                user.setUser(person1);
                SaveSharedPreference.saveObject(user);
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

    private void openTourneyFragment (Tourney tourney) {
        addTournamentFragment = new AddTournamentFragment(tourney.getId());
        showFragment(addTournamentFragment, tournamentMT,false, false,2);
    }
}
