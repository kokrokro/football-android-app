package baikal.web.footballapp.user.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import baikal.web.footballapp.App;
import baikal.web.footballapp.CheckName;
import baikal.web.footballapp.Controller;
import baikal.web.footballapp.DateToString;
import baikal.web.footballapp.MankindKeeper;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.model.Invite;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.PersonTeams;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.model.Tourney;
import baikal.web.footballapp.model.User;
import baikal.web.footballapp.user.activity.InvitationFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RVInvitationAdapter extends RecyclerView.Adapter<RVInvitationAdapter.ViewHolder> {
    private final Logger log = LoggerFactory.getLogger(InvitationFragment.class);
    private final List<Invite> list;

    public RVInvitationAdapter(List<Invite> list) {
//    public RVInvitationAdapter(Activity activity,  List<PendingTeamInvite> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.invitation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        String str;
        Invite pendingTeamInvite = list.get(position);
        League league = null;
        Person person;
        Tourney tourney = null;
        Team team = pendingTeamInvite.getTeam();
        for (League tournament : MankindKeeper.getInstance().allLeagues) {
            if (tournament.getId().equals(team.getLeague())) {
                league = tournament;
                String tourneyId = league.getTourney();
                for(Tourney tr : MankindKeeper.getInstance().allTourneys){
                    if(tr.getId().equals(tourneyId)){
                        tourney = tr;
                    }
                }
                break;
            }
        }

        person = MankindKeeper.getInstance().getPersonById(team.getTrainer());

        if (tourney != null) {
            str = tourney.getName() + ". " + league.getName();
            holder.textTournamentTitle.setText(str);
            str = DateToString.ChangeDate(league.getBeginDate()) + "-" + DateToString.ChangeDate(league.getEndDate());
            holder.textTournamentDate.setText(str);
            str = "Тренер: ";
            CheckName checkName = new CheckName();

            try {
                str += checkName.check(person.getSurname(), person.getName(), person.getLastname());
            } catch (Exception ignored) { str = ""; }

            holder.textCoach.setText(str);

            str = "Команда: ";
            str += team.getName();
            holder.textCommand.setText(str);
        }

        final League finalLeague = league;
        final Team finalTeam = team;
        holder.buttonOk.setOnClickListener(v -> {
            //post
            String status = "Accepted";
            AcceptRequest(pendingTeamInvite.get_id(), finalTeam.getId(), status, finalLeague, position);
            list.remove(position);
            notifyDataSetChanged();
        });

        holder.buttonCancel.setOnClickListener(v -> {
            //post
            Controller.getApi().rejectInv(pendingTeamInvite.get_id(),SaveSharedPreference.getObject().getToken()).enqueue(new Callback<Invite>() {
                @Override
                public void onResponse(@NonNull Call<Invite> call, @NonNull Response<Invite> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(App.getAppContext(), "Вы отклонили приглашение", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Invite> call, @NonNull Throwable t) {
                    Toast.makeText(App.getAppContext(), "Не удалось отклонить приглашение", Toast.LENGTH_LONG).show();
                }
            });
            list.remove(position);
            notifyDataSetChanged();

        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textTournamentTitle;
        final TextView textTournamentDate;
        final TextView textCommand;
        final TextView textCoach;
        final Button buttonCancel;
        final Button buttonOk;

        ViewHolder(View item) {
            super(item);
            textTournamentTitle = item.findViewById(R.id.invTournamentTitle);
            textTournamentDate = item.findViewById(R.id.invTournamentDate);
            textCommand = item.findViewById(R.id.invTournamentCommandTitle);
            textCoach = item.findViewById(R.id.invTournamentCoach);
            buttonCancel = item.findViewById(R.id.invTournamentButtonCancel);
            buttonOk = item.findViewById(R.id.invTournamentButtonOk);
        }
    }

    private void AcceptRequest(String invite, final String team, final String status, final League league1, final int position) {
//        Call<ServerResponse> call = Controller.getApi().playerInv(token, map);
        Call<User> call = Controller.getApi().playerInv(invite, SaveSharedPreference.getObject().getToken());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        User user = response.body();
                        SaveSharedPreference.saveObject(user);
//                        String result = response.body().getMessage();
//                        if (status.equals("Accepted") && result.equals("ok")) {
                        if (status.equals("Accepted")) {
                            Toast.makeText(App.getAppContext(), "Вы приняли приглашение", Toast.LENGTH_LONG).show();
//                            AuthoUser.pendingTeamInvitesList.remove(position);
                            PersonTeams personTeams = new PersonTeams();
                            personTeams.setLeague(league1.getId());
                            personTeams.setTeam(team);
                        }
                        if (status.equals("Rejected")) {
//                            AuthoUser.pendingTeamInvitesList.remove(position);
                        }


                        for (int i = 0; i< MankindKeeper.getInstance().allLeagues.size(); i++){
                            if (league1.getId().equals(MankindKeeper.getInstance().allLeagues.get(i).getId())){
                                MankindKeeper.getInstance().allLeagues.set(i, league1);
                            }
                        }

                        notifyDataSetChanged();
//                        if (AuthoUser.pendingTeamInvitesList.size() == 0) {
//                            LayoutInflater layoutInflater = (LayoutInflater)
//                                    activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                            View newRow = layoutInflater.inflate(R.layout.user_invitations, null,   false);
//                            LinearLayout layout = newRow.findViewById(R.id.emptyInv);
//                            layout.setVisibility(View.VISIBLE);
//                        }
                    }
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        String str = "Ошибка! ";
                        str += jsonObject.getString("message");
                        Toast.makeText(App.getAppContext(), str, Toast.LENGTH_LONG).show();
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }

            }


            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                log.error("ERROR: onResponse", t);
                Toast.makeText(App.getAppContext(), "Ошибка", Toast.LENGTH_LONG).show();
            }
        });
    }
}
