package baikal.web.footballapp.user.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Match;

public class ChangePlayersForMatch extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_command_info);
        Intent intent = getIntent();
        LinearLayout editTeam = findViewById(R.id.linLayoutInformationTeam);
        Button button = findViewById(R.id.userCommandPlayerButton);
        button.setVisibility(View.GONE);
        editTeam.setVisibility(View.GONE);

        TextView textView = findViewById(R.id.userCommandPlayersInvText);
        textView.setText("Не участвующие:");
        textView = findViewById(R.id.editTeamTitleToolbar);
        textView.setText("Редактировать состав");

        Match match = (Match) intent.getExtras().getSerializable("MATCH");
        RecyclerView recyclerViewTeam = findViewById(R.id.recyclerViewUserCommandPlayers);
        RecyclerView recyclerViewSpare = findViewById(R.id.recyclerViewUserCommandPlayersInv);

        ImageButton btnClose = findViewById(R.id.userCommandClose);
        btnClose.setOnClickListener(v -> {
            finish();
        });

        ImageButton btnSave = findViewById(R.id.userCommandSave);
        btnSave.setOnClickListener(v -> {

        });
    }
}
