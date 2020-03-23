package baikal.web.footballapp.user.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import baikal.web.footballapp.R;
import baikal.web.footballapp.players.activity.PlayersPage;

public class ChooseTrainer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_players);
        PlayersPage playersPage = new PlayersPage(null, null, person -> {
            Intent data = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("PERSON", person);
            data.putExtras(bundle);
            setResult(RESULT_OK, data);
            finish();
        }, null);

        getSupportFragmentManager().beginTransaction().replace(R.id.PP_personToChoose, playersPage).commit();
    }
}



