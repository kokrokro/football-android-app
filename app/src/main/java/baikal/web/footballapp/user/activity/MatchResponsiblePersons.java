package baikal.web.footballapp.user.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import baikal.web.footballapp.CheckName;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SetImage;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.Referee;
import baikal.web.footballapp.tournament.activity.TournamentPage;

import java.util.ArrayList;
import java.util.List;

public class MatchResponsiblePersons extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ImageButton buttonBack;
        ImageView imageInspector;
        ImageView imageReferee1;
        ImageView imageReferee2;
        ImageView imageReferee3;
        ImageView imageReferee4;
        TextView textInspector;
        TextView textReferee1;
        TextView textReferee2;
        TextView textReferee3;
        TextView textReferee4;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match_responsible_persons);
        buttonBack = findViewById(R.id.matchResponsiblePersonsBack);
        imageInspector = findViewById(R.id.inspectorLogo);
        imageReferee1 = findViewById(R.id.referee1Logo);
        imageReferee2 = findViewById(R.id.referee2Logo);
        imageReferee3 = findViewById(R.id.referee3Logo);
        imageReferee4 = findViewById(R.id.referee4Logo);
        textInspector = findViewById(R.id.inspectorName);
        textReferee1 = findViewById(R.id.referee1Name);
        textReferee2 = findViewById(R.id.referee2Name);
        textReferee3 = findViewById(R.id.referee3Name);
        textReferee4 = findViewById(R.id.referee4Name);
        String str;
        try {
            CheckName checkName = new CheckName();
            SetImage setImage = new SetImage();
            List<Referee> referees = (List<Referee>) getIntent().getExtras().getSerializable("CONFIRMPROTOCOLREFEREES");
            List<Person> personList = new ArrayList<>();
            for (Referee referee : referees) {
                for (Person person : TournamentPage.referees) {
                    if (referee.getPerson().equals(person.getId())) {
                        personList.add(person);
                    }
                }
            }
            for (int i = 0; i < referees.size(); i++) {
                switch (referees.get(i).getType()) {
                    case "Инспектор":
                        str = checkName.check(personList.get(i).getSurname(), personList.get(i).getName(), personList.get(i).getLastname());
                        textInspector.setText(str);
                        setImage.setImage(this, imageInspector, personList.get(i).getPhoto());
                        break;
                    case "1 судья":
                        str = checkName.check(personList.get(i).getSurname(), personList.get(i).getName(), personList.get(i).getLastname());
                        textReferee1.setText(str);
                        setImage.setImage(this, imageReferee1, personList.get(i).getPhoto());
                        break;
                    case "2 судья":
                        str = checkName.check(personList.get(i).getSurname(), personList.get(i).getName(), personList.get(i).getLastname());
                        textReferee2.setText(str);
                        setImage.setImage(this, imageReferee2, personList.get(i).getPhoto());
                        break;
                    case "3 судья":
                        str = checkName.check(personList.get(i).getSurname(), personList.get(i).getName(), personList.get(i).getLastname());
                        textReferee3.setText(str);
                        setImage.setImage(this, imageReferee3, personList.get(i).getPhoto());
                        break;
                    case "хронометрист":
                        str = checkName.check(personList.get(i).getSurname(), personList.get(i).getName(), personList.get(i).getLastname());
                        textReferee4.setText(str);
                        setImage.setImage(this, imageReferee4, personList.get(i).getPhoto());
                        break;
                    default:
                        break;
                }
            }
        } catch (NullPointerException e) {
        }
        buttonBack.setOnClickListener(v -> finish());
    }
}
