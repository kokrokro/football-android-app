package baikal.web.footballapp.players.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.FullScreenImage;
import baikal.web.footballapp.MankindKeeper;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.model.Club;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.PersonTeams;
import baikal.web.footballapp.model.Player;
import baikal.web.footballapp.model.ResponseInvite;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.model.User;
import baikal.web.footballapp.players.activity.PlayerInv;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static baikal.web.footballapp.Controller.BASE_URL;

public class RVPlayerInvAdapter extends RecyclerView.Adapter<RVPlayerInvAdapter.ViewHolder> {
    private final Logger log = LoggerFactory.getLogger(PlayerInv.class);
    private final PlayerInv context;
    private final List<PersonTeams> leagues;

    public RVPlayerInvAdapter(PlayerInv context, List<PersonTeams> leagues){
        this.context =  context;
        this.leagues = leagues;
    }
    @NonNull
    @Override
    public RVPlayerInvAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_inv_command, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVPlayerInvAdapter.ViewHolder holder, int position) {
        PersonTeams personTeams = leagues.get(position);
//        final League league = personTeams.getLeague();
        League league = null;
        for (League league1 : MankindKeeper.getInstance().allLeagues){
            if (league1.getId().equals(personTeams.getLeague())){
                league = league1;
                break;
            }
        }
        String uriPic = BASE_URL;
        final String teamId = personTeams.getTeam();
        Team teamLeague = null;
        for (Team team: league.getTeams()){
            if (team.getId().equals(teamId) ){
                teamLeague = team;
            }
        }
        Club clubLeague = null;
        for (Club club : MankindKeeper.getInstance().allClubs){
            if (club.getId().equals(teamLeague.getClub()) ){
                clubLeague = club;
                break;
            }
        }

        RequestOptions requestOptions = RequestOptions
                .errorOf(R.drawable.ic_logo2)
                .optionalCircleCrop()
                .format(DecodeFormat.PREFER_ARGB_8888)
                .override(500, 500) // resizing
                .priority(Priority.HIGH);
        try {
            uriPic += "/" + clubLeague.getLogo();
            URL url = new URL(uriPic);
            Glide.with(context)
                    .asBitmap()
                    .load(url)
                    .apply(requestOptions)
                    .into(holder.image);

            final String finalUriPic = uriPic;
            holder.image.setOnClickListener(v -> {
                if (finalUriPic.contains(".jpg") || finalUriPic.contains(".jpeg") || finalUriPic.contains(".png")) {
                    Intent intent = new Intent(context, FullScreenImage.class);
                    intent.putExtra("player_photo", finalUriPic);
                    context.startActivity(intent);
                }
            });
        } catch (Exception e) {
            Glide.with(context)
                    .asBitmap()
                    .load(R.drawable.ic_logo2)
                    .apply(requestOptions)
                    .into(holder.image);
        }
        String str = teamLeague.getName();
        holder.textTitle.setText(str);
        if (position == (leagues.size() - 1)){
            holder.line
                    .setVisibility(View.INVISIBLE);
        }
        final Team finalTeamLeague = teamLeague;
        League finalLeague = league;
        holder.button.setOnClickListener(v -> addPlayer(PlayerInv.personId, finalLeague, finalTeamLeague));
    }

    @Override
    public int getItemCount() {
        return leagues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
//        View card;
final ImageButton button;
        final ImageView image;
        final TextView textTitle;
        final View line;
        public ViewHolder(View item) {
            super(item);
            image = item.findViewById(R.id.playerInvLogo);
            textTitle = item.findViewById(R.id.playerInvTitle);
//            card = (View) item.findViewById(R.id.playerInvCard);
            button = item.findViewById(R.id.playerInvButton);
            line = item.findViewById(R.id.playerInvLine);
        }
    }


    public void addPlayer(final String personId, League league, final Team team){
//        List<PersonTeams> list = AuthoUser.personOngoingLeagues;
//        Team teamLeague = null;
//        League league1 = null;
//        for (PersonTeams personTeams: list){
//            League league = personTeams.getLeague();
//            String teamId = personTeams.getTeam();
//            for (Team team: league.getTeams()){
//                Person person = AuthoUser.web.getUser();
//                if (team.getId().equals(teamId) && team.getCreator().equals(person.getId())){
//                    teamLeague = team;
//                    league1 = league;
//                }
//            }
//        }
        Map<String, RequestBody> map = new HashMap<>();
        RequestBody request = RequestBody.create(MediaType.parse("text/plain"), personId);  map.put("playerId", request);
        request = RequestBody.create(MediaType.parse("text/plain"), league.getId());        map.put("_id", request);
        request = RequestBody.create(MediaType.parse("text/plain"), team.getId());          map.put("teamId", request);

        Call<ResponseInvite> call = Controller.getApi().addPlayerToTeam(SaveSharedPreference.getObject().getToken(), map);

        final League finalLeague = league;
        call.enqueue(new Callback<ResponseInvite>() {
            @Override
            public void onResponse(Call<ResponseInvite> call, Response<ResponseInvite> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null){
                        PersonTeams personTeams = new PersonTeams();
//                        personTeams.setId("000");
                        personTeams.setLeague(finalLeague.getId());
                        personTeams.setTeam(team.getId());


//                        for (PersonTeams teams : AuthoUser.personOwnCommand){
//                            if (teams.getTeam().equals(team.getId())){
//                                personTeams = teams;
//                                break;
//                            }
//                        }
                        Player player = new Player();
//                        player.setId("000");
                        player.setPlayerId(personId);
                        player.setInviteStatus("Pending");
                        team.getPlayers().add(player);
                        PersonTeams personTeams2 = new PersonTeams();
                        personTeams2.setId(personTeams.getId());
                        personTeams2.setLeague(finalLeague.getId());
                        personTeams2.setTeam(team.getId());
//                        AuthoUser.personOwnCommand.remove(personTeams);
//                        AuthoUser.personOwnCommand.add(personTeams2);


                        User user = SaveSharedPreference.getObject();
                        Person person = user.getUser();
                        List<PersonTeams> list = new ArrayList<>(person.getParticipation());
                        for (int i=0; i<list.size(); i++){
                            if (list.get(i).getLeague().equals(personTeams.getLeague())){
                                list.set(i, personTeams2);
                            }
                        }
                        person.setParticipation(list);
                        user.setUser(person);
                        SaveSharedPreference.saveObject(user);

//                        for (int i=0; i<AuthoUser.personOwnCommand.size(); i++){
//                            if (AuthoUser.personOwnCommand.get(i).getLeague().equals(personTeams.getLeague())){
//                                AuthoUser.personOwnCommand.set(i, personTeams2);
//                            }
//                        }


//                        List<PersonTeams> result = new ArrayList<>(AuthoUser.personOwnCommand);
                        Intent intent = new Intent();
                        context.setResult(RESULT_OK, intent);
                        context.finish();
                    }
                }
                else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        String str = "Ошибка! ";
                        str += jsonObject.getString("message");
                        Toast.makeText(context, str, Toast.LENGTH_LONG).show();
                        context.finish();
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseInvite> call, Throwable t) {
                log.error("ERROR: onResponse");
                Toast.makeText(context, "Ошибка сервера.", Toast.LENGTH_LONG).show();
                context.finish();
            }
        });
    }
}
