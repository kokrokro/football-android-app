package baikal.web.footballapp.user.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import baikal.web.footballapp.CheckName;
import baikal.web.footballapp.Controller;
import baikal.web.footballapp.DateToString;
import baikal.web.footballapp.FullScreenImage;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.model.Club;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.PendingTeamInvite;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.PersonTeams;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.model.User;
import baikal.web.footballapp.user.activity.AuthoUser;
import baikal.web.footballapp.user.activity.InvitationFragment;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static baikal.web.footballapp.Controller.BASE_URL;
import static baikal.web.footballapp.user.activity.AuthoUser.SetInvNum;

public class RVInvitationAdapter extends RecyclerView.Adapter<RVInvitationAdapter.ViewHolder> {
    private final Logger log = LoggerFactory.getLogger(InvitationFragment.class);
    private final InvitationFragment context;
    private final PersonalActivity activity;
    private final List<PendingTeamInvite> list;

    public RVInvitationAdapter(Activity activity, InvitationFragment context, List<PendingTeamInvite> list) {
//    public RVInvitationAdapter(Activity activity,  List<PendingTeamInvite> list) {
        this.activity = (PersonalActivity) activity;
        this.list = list;
        this.context = context;
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
        String uriPic = BASE_URL;
        PendingTeamInvite pendingTeamInvite = list.get(position);
        League league = null;
        Person person = null;
        Club club = null;
        for (League tournament : PersonalActivity.tournaments) {
            if (tournament.getId().equals(pendingTeamInvite.getLeague())) {
                league = tournament;
                break;
            }
        }
        Team team = null;
        for (Team teams : league.getTeams()) {
            if (teams.getId().equals(pendingTeamInvite.getTeam())) {
                team = teams;
                for (Person people : PersonalActivity.people) {
                    if (team.getCreator().equals(people.getId())) {
                        person = people;
                        break;
                    }

                }
                for (Club clubs : PersonalActivity.allClubs) {
                    if (team.getClub().equals(clubs.getId())) {
                        club = clubs;
                        break;
                    }

                }
                break;
            }
        }

        str = league.getTourney() + ". " + league.getName();
        holder.textTournamentTitle.setText(str);
        DateToString dateToString = new DateToString();
        str = dateToString.ChangeDate(league.getBeginDate()) + "-" + dateToString.ChangeDate(league.getEndDate());
        holder.textTournamentDate.setText(str);
        str = "Тренер: ";
        CheckName checkName = new CheckName();
        str += checkName.check(person.getSurname(), person.getName(), person.getLastname());
        holder.textCoach.setText(str);

        str = "Команда: ";
        str += team.getName();
        holder.textCommand.setText(str);

        uriPic += "/" + club.getLogo();
        try {
            URL url = new URL(uriPic);
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.optionalCircleCrop();
            requestOptions.format(DecodeFormat.PREFER_ARGB_8888);
            requestOptions.error(R.drawable.ic_logo2);
            requestOptions.override(500, 500);
            requestOptions.priority(Priority.HIGH);
//            Glide.with(context)
            Glide.with(holder.image.getContext())
                    .asBitmap()
                    .load(url)
                    .apply(requestOptions)
                    .into(holder.image);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        final String finalUriPic = uriPic;
        holder.image.setOnClickListener(v -> {
            if (finalUriPic.contains(".jpg") || finalUriPic.contains(".jpeg") || finalUriPic.contains(".png")) {
                Intent intent = new Intent(activity, FullScreenImage.class);
//                    Intent intent = new Intent(context, FullScreenImage.class);
                intent.putExtra("player_photo", finalUriPic);
                activity.startActivity(intent);
//                    context.startActivity(intent);
            }
        });

        final League finalLeague = league;
        final Team finalTeam = team;
        holder.buttonOk.setOnClickListener(v -> {
            //post
            String status = "Accepted";
            AcceptRequest(SaveSharedPreference.getObject().getToken(), finalLeague.getId(), finalTeam.getId(), status, finalLeague, position);
        });

        holder.buttonCancel.setOnClickListener(v -> {
            //post
            String status = "Rejected";
            AcceptRequest(SaveSharedPreference.getObject().getToken(), finalLeague.getId(), finalTeam.getId(), status, finalLeague, position);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView image;
        final TextView textTournamentTitle;
        final TextView textTournamentDate;
        final TextView textCommand;
        final TextView textCoach;
        final Button buttonCancel;
        final Button buttonOk;

        ViewHolder(View item) {
            super(item);
            image = item.findViewById(R.id.userTournamentInvLogo);
            textTournamentTitle = item.findViewById(R.id.invTournamentTitle);
            textTournamentDate = item.findViewById(R.id.invTournamentDate);
            textCommand = item.findViewById(R.id.invTournamentCommandTitle);
            textCoach = item.findViewById(R.id.invTournamentCoach);
            buttonCancel = item.findViewById(R.id.invTournamentButtonCancel);
            buttonOk = item.findViewById(R.id.invTournamentButtonOk);
        }
    }


    private void AcceptRequest(String token, String league, final String team, final String status, final League league1, final int position) {
        Map<String, RequestBody> map = new HashMap<>();
        RequestBody request = RequestBody.create(MediaType.parse("text/plain"), league);
        map.put("_id", request);
        request = RequestBody.create(MediaType.parse("text/plain"), team);
        map.put("teamId", request);
        request = RequestBody.create(MediaType.parse("text/plain"), status);
        map.put("status", request);
//        Call<ServerResponse> call = Controller.getApi().playerInv(token, map);
        Call<User> call = Controller.getApi().playerInv(token, map);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        User user = response.body();
                        SaveSharedPreference.editObject(user);
//                        String result = response.body().getMessage();
//                        if (status.equals("Accepted") && result.equals("ok")) {
                        if (status.equals("Accepted")) {
                            Toast.makeText(activity, "Вы приняли приглашение", Toast.LENGTH_LONG).show();
                            AuthoUser.pendingTeamInvitesList.remove(position);
                            SetInvNum(activity, (list.size()));
                            PersonTeams personTeams = new PersonTeams();
                            personTeams.setLeague(league1.getId());
                            personTeams.setTeam(team);
                            int size1 = AuthoUser.personOwnCommand.size();
                            int size2 = AuthoUser.personCommand.size();
                            AuthoUser.personCommand.add(personTeams);
                            if (AuthoUser.personOwnCommand.size() != 0
                                    && AuthoUser.personCommand.size() != 0) {
                                LayoutInflater factory = activity.getLayoutInflater();
                                final View textEntryView = factory.inflate(R.layout.user_commands, null);
                                View line = textEntryView.findViewById(R.id.userCommandsLine);
                                line.setVisibility(View.VISIBLE);
                                textEntryView.setVisibility(View.VISIBLE);
                            }
                            if (size1 == 0
                                    && size2 == 0) {
                                LayoutInflater factory = activity.getLayoutInflater();
                                final View textEntryView = factory.inflate(R.layout.user_commands, null);
                                LinearLayout linearLayout = textEntryView.findViewById(R.id.emptyCommand);
                                TextView textView = textEntryView.findViewById(R.id.userCommandsText);
                                linearLayout.setVisibility(View.GONE);
                                textView.setVisibility(View.VISIBLE);
                                AuthoUser.adapterCommand.notifyDataSetChanged();
                            } else {
                                AuthoUser.adapterCommand.notifyDataSetChanged();
                            }
                        }
                        if (status.equals("Rejected")) {
                            AuthoUser.pendingTeamInvitesList.remove(position);
                            SetInvNum(activity, (list.size()));
                        }

//                        Team teamLeague = null;
//                        for (Team team1: league1.getTeams()){
//                            if (team1.getId().equals(team)){
//                                teamLeague = team1;
//                                break;
//                            }
//                        }
//                        league1.getTeams().remove(teamLeague);
//                        league1.getTeams().add(teamLeague);

                        for (int i = 0; i< PersonalActivity.tournaments.size(); i++){
                            if (league1.getId().equals(PersonalActivity.tournaments.get(i).getId())){
                                PersonalActivity.tournaments.set(i, league1);
                            }
                        }


                        notifyDataSetChanged();
                        if (AuthoUser.pendingTeamInvitesList.size() == 0) {
                            LayoutInflater layoutInflater = (LayoutInflater)
                                    activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View newRow = layoutInflater.inflate(R.layout.user_invitations, null,   false);
                            LinearLayout layout = newRow.findViewById(R.id.emptyInv);
                            layout.setVisibility(View.VISIBLE);
                        }




                    }
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        String str = "Ошибка! ";
                        str += jsonObject.getString("message");
                        Toast.makeText(context.getContext(), str, Toast.LENGTH_LONG).show();
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }


            @Override
            public void onFailure(Call<User> call, Throwable t) {
                log.error("ERROR: onResponse", t);
                Toast.makeText(activity, "Ошибка", Toast.LENGTH_LONG).show();
            }
        });
    }
}
