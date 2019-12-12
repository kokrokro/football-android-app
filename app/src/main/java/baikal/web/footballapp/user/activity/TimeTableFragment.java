package baikal.web.footballapp.user.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.MankindKeeper;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.Match;
import baikal.web.footballapp.model.MatchPopulate;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.Referee;
import baikal.web.footballapp.model.User;
import baikal.web.footballapp.user.adapter.RVRefereesMatchesAdapter;
import baikal.web.footballapp.user.adapter.RVTimeTableAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimeTableFragment extends Fragment {
    private static final String TAG = "TimeTableFragment: ";
    public final int SUCCESSFUL_EDIT_MATCH = 6123;
    private RVTimeTableAdapter adapter;
    private final List<MatchPopulate> matches = new ArrayList<>();
    private NestedScrollView scroller;
    private final Logger log = LoggerFactory.getLogger(TimeTableFragment.class);
    private LinearLayout layout;
    private List<String> recentReferee = new ArrayList<>();
    private SetRefereesBottomSheet bottomSheet;
    private List<Match> editedMatches ;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        RecyclerView recyclerView;
        RecyclerView recyclerViewSetReferee;
        view = inflater.inflate(R.layout.user_timetable, container, false);
        scroller = view.findViewById(R.id.scrollerTimeTable);
        layout = view.findViewById(R.id.emptyTimetable);
        getActiveMatches("10", "0");
        recyclerViewSetReferee = view.findViewById(R.id.setRefereeRecyclerView);

        recyclerViewSetReferee.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewSetReferee.setNestedScrollingEnabled(false);
        recyclerView = view.findViewById(R.id.recyclerViewTimeTable);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        try {
            adapter = new RVTimeTableAdapter(getActivity(), this, matches);
            recyclerView.setAdapter(adapter);
        } catch (NullPointerException e) {
            log.error(TAG, e);
        }
        if( SaveSharedPreference.getObject().getRecentReferees()!=null) {
            recentReferee = SaveSharedPreference.getObject().getRecentReferees();
            log.error("SavedSharedPreferences non null referees");
        }
        else{
            recentReferee.clear();
            for(Map.Entry<String,Person> entry : MankindKeeper.getInstance().allPerson.entrySet()) {
                Person value = entry.getValue();
                if(recentReferee.size()>3){
                    break;
                }
                recentReferee.add(value.get_id());
            }
        }
        AuthoUser.setSingleReferee.setOnClickListener(v -> {
                log.error("referees" + recentReferee.size());
            editedMatches = new ArrayList<>();
             bottomSheet = new SetRefereesBottomSheet(recentReferee, person -> {
//                 setReferee(person);
                 recyclerView.setVisibility(View.GONE);
                 AuthoUser.setSingleReferee.setVisibility(View.GONE);
                 AuthoUser.saveSingleReferee.setVisibility(View.VISIBLE);
                 AuthoUser.cancelSetSingleReferee.setVisibility(View.VISIBLE);

                 recyclerViewSetReferee.setAdapter( new RVRefereesMatchesAdapter(getActivity(), matches, person,
                         getActivity(),
                         (refereesList, referee, id, isChecked) -> {

                     Log.d("Checked", isChecked+" "+ id);

                     if(!isChecked){
                         for(Referee referee1 : refereesList){
                             if(referee1.getPerson().equals(referee.getPerson()) && referee1.getType().equals(referee.getType())){
                                 refereesList.remove(referee1);
                                 break;
                             }
                         }
                     }else {
                         for (Referee referee1 : refereesList){
                             if(referee1.getType().equals(referee.getType())){
                                 refereesList.remove(referee1);
                                 break;
                             }
                         }
                         refereesList.add(referee);
                     }

//                     for(Referee referee1 : refereesList){
//                         Log.d("}",referee1.getType()+" "+ referee1.getPerson());
//                     }

                     Match newMatch = new Match();
                     newMatch.setReferees(refereesList);
                     newMatch.setId(id);
                     for(Match match : editedMatches){
                         if(match.getId().equals(id)){
                              editedMatches.remove(match);
                               break;
                          }
                     }
                     editedMatches.add(newMatch);
                 }));

                 recyclerViewSetReferee.setVisibility(View.VISIBLE);
                 bottomSheet.dismiss();
//                 recyclerView.setVisibility(View.VISIBLE);

             });
             AuthoUser.saveSingleReferee.setOnClickListener(v1 -> {
//                 Toast.makeText(getContext(), ""+ editedMatches.size(), Toast.LENGTH_SHORT).show();
                 if( editedMatches.size()> 0) {
                     for (int i = 0; i < editedMatches.size(); i++) {
                         Match match = editedMatches.get(i);
                         String id = match.getId();
                         match.setId(null);
                         Controller.getApi().editMatch(id,SaveSharedPreference.getObject().getToken(), match)
                                 .enqueue(new Callback<Match>() {
                                     @Override
                                     public void onResponse(Call<Match> call, Response<Match> response) {

                                     }

                                     @Override
                                     public void onFailure(Call<Match> call, Throwable t) {

                                     }
                                 });
                         for(MatchPopulate matchPopulate : matches){
                             if(matchPopulate.getId().equals(id)){
                                 matchPopulate.setReferees(match.getReferees());
                                 break;
                             }
                         }
                     }
                 }
                 adapter.notifyDataSetChanged();

                 AuthoUser.setSingleReferee.setVisibility(View.VISIBLE);
                 AuthoUser.saveSingleReferee.setVisibility(View.GONE);
                 AuthoUser.cancelSetSingleReferee.setVisibility(View.GONE);
                 recyclerView.setVisibility(View.VISIBLE);
                 recyclerViewSetReferee.setVisibility(View.GONE);
                 adapter.notifyDataSetChanged();


             });
             AuthoUser.cancelSetSingleReferee.setOnClickListener(v1 -> {
                 recyclerView.setVisibility(View.VISIBLE);
                 recyclerViewSetReferee.setVisibility(View.GONE);
                 AuthoUser.setSingleReferee.setVisibility(View.VISIBLE);
                 AuthoUser.saveSingleReferee.setVisibility(View.GONE);
                 AuthoUser.cancelSetSingleReferee.setVisibility(View.GONE);

             });

//            bottomSheet.adapterNotify();
            bottomSheet.show(getFragmentManager(),"BOTTOMSHEET");
        });



        return view;
    }

    @SuppressLint("CheckResult")
    private void getActiveMatches(String limit, String offset) {
        Controller.getApi().getMainRefsLeagues(SaveSharedPreference.getObject().getUser().getId()).enqueue(new Callback<List<League>>() {
            @Override
            public void onResponse(@NonNull Call<List<League>> call, @NonNull Response<List<League>> response) {
                if(response.isSuccessful())
                    if(response.body()!=null)
                        saveData(response.body(), limit, offset);
            }

            @Override
            public void onFailure(@NonNull Call<List<League>> call, @NonNull Throwable t) {
                log.error(t.getMessage());
                layout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void saveData(List<League> leagueList, String limit, String offset) {
        matches.clear();
        StringBuilder str= new StringBuilder();
        for(League l: leagueList)
           for (Match m : l.getMatches())
               if(!m.getPlayed())
                   str.append(",").append(m.getId());

        Controller.getApi().getMatches(str.toString(),null).enqueue(new Callback<List<MatchPopulate>>() {
            @Override
            public void onResponse(@NonNull Call<List<MatchPopulate>> call, @NonNull Response<List<MatchPopulate>> response) {
                if(response.isSuccessful())
                    if(response.body()!=null){
                        matches.addAll(response.body());
                        adapter.notifyDataSetChanged();
                        if (matches.size()==0)
                            layout.setVisibility(View.VISIBLE);
                    }
            }

            @Override
            public void onFailure(@NonNull Call<List<MatchPopulate>> call, @NonNull Throwable t) {
                Log.e(TAG, Objects.requireNonNull(t.getMessage()));
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK)
            return;

        if (requestCode == SUCCESSFUL_EDIT_MATCH) {
            int matchIndx = data.getIntExtra("MatchIndex", -1);
            if (matchIndx != -1) {
                String[] newRefs = data.getStringArrayExtra("refs");
                if (newRefs == null) return;
                Log.d(TAG, newRefs.toString());

                List<Referee> newReferees = new ArrayList<>();
                newReferees.add(new Referee());
                newReferees.add(new Referee());
                newReferees.add(new Referee());
                newReferees.add(new Referee());

                newReferees.get(0).setType("firstReferee");
                newReferees.get(1).setType("secondReferee");
                newReferees.get(2).setType("thirdReferee");
                newReferees.get(3).setType("timekeeper");

                newReferees.get(0).setPerson(newRefs[0]);
                newReferees.get(1).setPerson(newRefs[1]);
                newReferees.get(2).setPerson(newRefs[2]);
                newReferees.get(3).setPerson(newRefs[3]);
                for(Referee referee : newReferees){
                    if(referee.getPerson()!=null && !recentReferee.contains(referee.getPerson())){
                        recentReferee.add(referee.getPerson());
                    }
                }
                for( ;recentReferee.size()>4;){
                    recentReferee.remove(0);
                }
                log.error("edit save shared preferences...");
                User user = SaveSharedPreference.getObject();
                user.setRecentReferees(recentReferee);
                SaveSharedPreference.editObject(user);

                matches.get(matchIndx).setReferees(newReferees);
                adapter.notifyDataSetChanged();
            }
        }
    }

    public void setReferee(Person person) {
        Intent intent = new Intent(getContext(), RefereesMatches.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("REFEREESMATCHES", person);
        bundle.putSerializable("MATCHESREFEREE", (Serializable) matches);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
