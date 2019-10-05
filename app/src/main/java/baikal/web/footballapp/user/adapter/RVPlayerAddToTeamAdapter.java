package baikal.web.footballapp.user.adapter;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.PersonTeams;
import baikal.web.footballapp.model.ServerResponse;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.user.activity.AuthoUser;
import baikal.web.footballapp.user.activity.PlayerAddToTeam;
import baikal.web.footballapp.user.activity.UserCommandInfo;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static baikal.web.footballapp.Controller.BASE_URL;

public class RVPlayerAddToTeamAdapter extends RecyclerView.Adapter<RVPlayerAddToTeamAdapter.ViewHolder> {
    private final Logger log = LoggerFactory.getLogger(PlayerAddToTeam.class);
    private final List<Person> allPlayers;
    private final League league;
    private final PlayerAddToTeam context;
    private final Team team;

    public RVPlayerAddToTeamAdapter(Activity context, List<Person> allPlayers, Team team, League league) {
        this.allPlayers = allPlayers;
        this.context = (PlayerAddToTeam) context;
        this.team = team;
        this.league = league;
    }

    @NonNull
    @Override
    public RVPlayerAddToTeamAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_add, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVPlayerAddToTeamAdapter.ViewHolder holder, final int position) {
        try {
            String uriPic = BASE_URL;
            String DOB = allPlayers.get(position).getBirthdate();
            DateToString dateToString = new DateToString();
            holder.textDOB.setText(dateToString.ChangeDate(DOB));
            String str;
            CheckName checkName = new CheckName();
            str = checkName.check(allPlayers.get(position).getSurname(), allPlayers.get(position).getName(), allPlayers.get(position).getLastname());
            holder.textName.setText(str);
            try {

                uriPic += "/" + allPlayers.get(position).getPhoto();
                URL url = new URL(uriPic);
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.optionalCircleCrop();
                requestOptions.format(DecodeFormat.PREFER_ARGB_8888);
                requestOptions.error(R.drawable.ic_logo2);
                requestOptions.override(500, 500);
                requestOptions.priority(Priority.HIGH);
                Glide.with(context)
                        .asBitmap()
                        .load(url)
                        .apply(requestOptions)
                        .into(holder.imageLogo);
                final String finalUriPic = uriPic;
                holder.imageLogo.setOnClickListener(v -> {
                    if (finalUriPic.contains(".jpg") || finalUriPic.contains(".jpeg") || finalUriPic.contains(".png")) {
                        Intent intent = new Intent(context, FullScreenImage.class);
                        intent.putExtra("player_photo", finalUriPic);
                        context.startActivity(intent);
                    }

                });
            } catch (MalformedURLException e) {
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.optionalCircleCrop();
                requestOptions.format(DecodeFormat.PREFER_ARGB_8888);
                requestOptions.error(R.drawable.ic_logo2);
                requestOptions.override(500, 500);
                requestOptions.priority(Priority.HIGH);
                Glide.with(context)
                        .asBitmap()
                        .load(R.drawable.ic_logo2)
                        .apply(requestOptions)
                        .into(holder.imageLogo);
            }

            holder.button.setOnClickListener(v -> addPlayer(allPlayers.get(position).getId(), league, team));
            if (position == (allPlayers.size() - 1)) {
                holder.line.setVisibility(View.INVISIBLE);
            }


        } catch (Exception e) {
        }

    }

    @Override
    public int getItemCount() {
        return allPlayers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageLogo;
        final ImageButton button;
        final TextView textName;
        final TextView textDOB;
        final View line;

        ViewHolder(View item) {
            super(item);
            line = item.findViewById(R.id.playersAddToTeamLine);
            imageLogo = item.findViewById(R.id.playerAddToTeamLogo);
            button = item.findViewById(R.id.playerAddToTeamButton);
            textName = item.findViewById(R.id.playerAddToTeamName);
            textDOB = item.findViewById(R.id.playerAddToTeamDOB);
        }
    }

    private void addPlayer(final String personId, League league, final Team team) {
        Map<String, RequestBody> map = new HashMap<>();
        RequestBody request = RequestBody.create(MediaType.parse("text/plain"), personId);
        map.put("playerId", request);
        request = RequestBody.create(MediaType.parse("text/plain"), league.getId());
        map.put("_id", request);
        request = RequestBody.create(MediaType.parse("text/plain"), team.getId());
        map.put("teamId", request);
        Call<ServerResponse> call = Controller.getApi().addPlayerToTeam(SaveSharedPreference.getObject().getToken(), map);
        log.info("INFO: load and parse json-file");
        final League finalLeague = league;
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                log.info("INFO: check response");
                if (response.isSuccessful()) {
                    log.info("INFO: response isSuccessful");
                    if (response.body() == null) {
                        log.error("ERROR: body is null");
                    } else {
                        PersonTeams personTeams = new PersonTeams();
                        personTeams.setLeague(finalLeague.getId());
                        personTeams.setTeam(team.getId());
                        AuthoUser.personOngoingLeagues.add(personTeams);


                        for (PersonTeams teams : AuthoUser.personOwnCommand) {
                            if (teams.getTeam().equals(team.getId())) {
                                personTeams = teams;
                                break;
                            }
                        }
                        baikal.web.footballapp.model.Player player = new baikal.web.footballapp.model.Player();
//                        player.setId("000");
                        player.setPlayerId(personId);
                        player.setInviteStatus("Pending");
                        List<baikal.web.footballapp.model.Player> playerList = team.getPlayers();
                        playerList.add(player);
                        team.setPlayers(playerList);
                        PersonTeams personTeams2 = new PersonTeams();
                        personTeams2.setId(personTeams.getId());
                        personTeams2.setLeague(finalLeague.getId());
                        personTeams2.setTeam(team.getId());
//                        AuthoUser.personOwnCommand.remove(personTeams);

//                        for (League league1 : PersonalActivity.tournaments){
//                            if (league1.getId().equals(personTeams2.getLeague())){
//                                Team teamCount = null;
//                                for (Team team1: league1.getTeams()){
//                                    if (team1.getId().equals(team.getId())){
//                                        teamCount = team1;
//                                    }
//                                }
//                            }
//                        }

                        for (int i=0; i<AuthoUser.personOwnCommand.size(); i++){
                            if (AuthoUser.personOwnCommand.get(i).getLeague().equals(personTeams2.getLeague())){
                                AuthoUser.personOwnCommand.set(i, personTeams2);
                            }
                        }

                        UserCommandInfo.playersInv.add(player);
//                        UserCommandInfo.adapterInv.notifyDataSetChanged();
                        List<baikal.web.footballapp.model.Player> players = new ArrayList<>(UserCommandInfo.playersInv);
                        UserCommandInfo.adapterInv.dataChanged(players);
                        List<PersonTeams> result = new ArrayList<>(AuthoUser.personOwnCommand);
                        AuthoUser.adapterOwnCommand.dataChanged(result);
                        Toast.makeText(context, "Вы отправили игроку приглашение", Toast.LENGTH_LONG).show();

                    }
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        String str = "Ошибка! ";
                        str += jsonObject.getString("message");
                        Toast.makeText(context, str, Toast.LENGTH_LONG).show();
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ServerResponse> call, @NonNull Throwable t) {
                log.error("ERROR: onResponse");
                Toast.makeText(context, "Ошибка сервера.", Toast.LENGTH_LONG).show();
                context.finish();
            }
        });
    }

    public void dataChanged(List<Person> allPlayers1) {
        allPlayers.clear();
        allPlayers.addAll(allPlayers1);
        notifyDataSetChanged();
    }
}
