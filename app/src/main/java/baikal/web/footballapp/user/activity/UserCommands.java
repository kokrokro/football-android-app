package baikal.web.footballapp.user.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.user.adapter.RVOwnCommandAdapter;
import baikal.web.footballapp.user.adapter.RVUserCommandAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class UserCommands extends Fragment {
    private final Logger log = LoggerFactory.getLogger(UserCommands.class);
    private final int REQUEST_CODE_NEWCOMMAND = 286;
    private int num;
    private boolean scrollStatus;
    private View line;
    private TextView textView;
    private TextView textView2;
    private LinearLayout linearLayoutTrainerTeams;
    private TextView textTrainerTeams;
    private View lineTrainer;
    private RecyclerView trainerRecyclerView;
    private static List<Team> trainerTeams = new ArrayList<>();
    private static List<Team> teams = new ArrayList<>();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        log.info("INFO: UserCommands onCreate");
    }

    public static UserCommands newInstance() {
        return new UserCommands();
    }
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        log.info("INFO: onCreateView onCreate");
//        final FloatingActionButton fab;
        RecyclerView recyclerView;
        RecyclerView recyclerView2;
        NestedScrollView scroller;
        LinearLayout linearLayout;
        LinearLayout linear;
        view = inflater.inflate(R.layout.user_commands, container, false);
        LinearLayout linearOwnCommand = view.findViewById(R.id.ownCommands);
        LinearLayout linearUserCommand = view.findViewById(R.id.userCommands);
        line = view.findViewById(R.id.userCommandsLine);
        textView = view.findViewById(R.id.userCommandsText);
        textView2 = view.findViewById(R.id.userCommandsText2);
        linearLayoutTrainerTeams = view.findViewById(R.id.userCommandsTrainer);
        textTrainerTeams = view.findViewById(R.id.userCommandsTex3);
        lineTrainer  = view.findViewById(R.id.userCommandsLine2);
        teams = AuthoUser.createdTeams;


        for(Team team : PersonalActivity.allTeams){
            if(team.getTrainer().equals(PersonalActivity.id)){
                trainerTeams.add(team);
            }
        }
        if (teams.size()==0){
            linearOwnCommand.setVisibility(View.GONE);
        }
        else {
            textView2.setVisibility(View.VISIBLE);
        }
        if (AuthoUser.personCommand.size()==0){
            linearUserCommand.setVisibility(View.GONE);
        }
        else {
            textView.setVisibility(View.VISIBLE);
        }
        if(trainerTeams.size()==0){
            linearLayoutTrainerTeams.setVisibility(View.GONE);
        }
        else {
            textTrainerTeams.setVisibility(View.VISIBLE);
        }



        if(teams.size()!=0 && (AuthoUser.personCommand.size()!=0 || trainerTeams.size()!=0)){
            line.setVisibility(View.VISIBLE);
        }
        if((teams.size()==0 || AuthoUser.personCommand.size()!=0) && trainerTeams.size()!=0){
            lineTrainer.setVisibility(View.VISIBLE);
        }




        linearLayout = view.findViewById(R.id.emptyCommand);
        if(teams.size()==0 && AuthoUser.personCommand.size()==0 && trainerTeams.size()==0){
            linearLayout.setVisibility(View.VISIBLE);
        }
        else {
            linearLayout.setVisibility(View.GONE);
        }

        scroller = view.findViewById(R.id.userCommandScroll);
        recyclerView = view.findViewById(R.id.recyclerViewUserCommand);
//        recyclerView.setAdapter(AuthoUser.adapterCommand);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RVOwnCommandAdapter adapter = new RVOwnCommandAdapter(getActivity(),teams);

        RVUserCommandAdapter adapter1 = new RVUserCommandAdapter(getActivity(),AuthoUser.personCommand, (t,l) ->{});
        recyclerView.setAdapter(adapter1);
        recyclerView2 = view.findViewById(R.id.recyclerViewOwnCommand);
        recyclerView2.setAdapter(adapter);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
        trainerRecyclerView = view.findViewById(R.id.recyclerViewUserCommandTrainer);
        trainerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        RVUserCommandAdapter adapter2 = new RVUserCommandAdapter(getActivity(),trainerTeams, (t,l) ->{
            Intent intent = new Intent(getActivity(), UserCommandInfo.class);
            Bundle bundle = new Bundle();
            Bundle bundle1 = new Bundle();
            bundle.putSerializable("COMMANDEDIT", t);
            bundle1.putSerializable("COMMANDEDITLEAGUE", l);
            intent.putExtras( bundle);
            intent.putExtras( bundle1);
            getActivity().startActivity(intent);
        });
        trainerRecyclerView.setAdapter(adapter2);
        scrollStatus = false;

        scroller.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY > oldScrollY) {}
            if (scrollY < oldScrollY) {
                scrollStatus = false;
            }
            if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                scrollStatus = true;
                AuthoUser.fab.hide();
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
                    AuthoUser.fab.show();
                } else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    AuthoUser.fab.hide();
                }
                if (scrollStatus) {
                    AuthoUser.fab.hide();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        recyclerView2.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//nothing to do
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    AuthoUser.fab.show();
                } else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    AuthoUser.fab.hide();
                }
                if (scrollStatus) {
                    AuthoUser.fab.hide();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
//
        AuthoUser.fab.setOnClickListener(v -> {
            //add command
//                num = AuthoUser.pendingTeamInvitesList.size();
            num = AuthoUser.personOwnCommand.size();
            Intent intent = new Intent(getActivity(), NewCommand.class);
            startActivityForResult(intent, REQUEST_CODE_NEWCOMMAND);
        });
//
//        if (AuthoUser.personOngoingLeagues.size()!=0){
//            linear.setVisibility(View.GONE);
//        }
//        else {
//            linearLayout.setVisibility(View.GONE);
//        }
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_NEWCOMMAND) {
                if (num==0){
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//                    FragmentTransaction ft = this.getChildFragmentManager().beginTransaction();
                    try {
                        ft.detach(this).attach(this).commit();
                    }catch (Exception e ){

                    }

                }
                else{
                    AuthoUser.adapterCommand.notifyDataSetChanged();
                    Toast.makeText(getContext(), "Команда добавлена", Toast.LENGTH_LONG).show();
                }

                if (AuthoUser.personOwnCommand.size()!=0
                        && AuthoUser.personCommand.size()!=0){
                    line.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                    textView2.setVisibility(View.VISIBLE);
                }
            }
        } else {
            log.error("ERROR: onActivityResult");
        }
    }

    @Override
    public void onDestroy() {
        log.info("INFO: UserCommands onDestroy");
        super.onDestroy();
    }
}
