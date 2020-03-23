package baikal.web.footballapp.user.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import baikal.web.footballapp.App;
import baikal.web.footballapp.Controller;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.ResponseInvite;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.players.activity.PlayersPage;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayerAddToTeam extends AppCompatActivity {
    private static final String TAG = "PlayerAddToTeam";
//    private Logger log = LoggerFactory.getLogger(PlayerAddToTeam.class);

    private Team team;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_add_to_team);

        team = (Team) getIntent().getExtras().getSerializable("ADD_PLAYER_TO_USER_TEAM");
        List<String> teamIds = getIntent().getExtras().getStringArrayList("TEAM_IDS_FOR_THAT_LEAGUE");

        PlayersPage playersPage = new PlayersPage(team, teamIds, null, this::addPerson);

        getSupportFragmentManager().beginTransaction().replace(R.id.PATT_personToChoose, playersPage).commit();
    }

    void addPerson (Person person) {
        Map<String, RequestBody> map = new HashMap<>();

        map.put("person", RequestBody.create(MediaType.parse("text/plain"), person.get_id()));
        map.put("team"  , RequestBody.create(MediaType.parse("text/plain"), team.getId()));

        Callback<ResponseInvite> responseCallback = new Callback<ResponseInvite>() {
            @Override
            public void onResponse(@NonNull Call<ResponseInvite> call, @NonNull Response<ResponseInvite> response) {
                if (response.isSuccessful() && response.body() != null)
                    Toast.makeText(App.getAppContext(), "Приглашение в команду отпралено ...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<ResponseInvite> call, @NonNull Throwable t) {
                Log.e(TAG, t.getMessage());
                Toast.makeText(App.getAppContext(), "Не удалось отправить приглашение ...", Toast.LENGTH_SHORT).show();
            }
        };

        Controller.getApi().addPlayerToTeam(SaveSharedPreference.getObject().getToken(), map).enqueue(responseCallback);
    }
}
