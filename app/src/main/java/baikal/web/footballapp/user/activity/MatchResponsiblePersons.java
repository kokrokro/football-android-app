package baikal.web.footballapp.user.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import baikal.web.footballapp.MankindKeeper;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SetImage;
import baikal.web.footballapp.model.Person;

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

        try {
            ArrayList<CharSequence> referees =  Objects.requireNonNull(getIntent().getExtras()).getCharSequenceArrayList("CONFIRMPROTOCOLREFEREES");

            List<Person> personList = new ArrayList<>();
            for (CharSequence referee : Objects.requireNonNull(referees))
                personList.add(MankindKeeper.getInstance().getPersonById(referee.toString()));

            setRefereesView(personList.get(0), textReferee1, imageReferee1);
            setRefereesView(personList.get(1), textReferee2, imageReferee2);
            setRefereesView(personList.get(2), textReferee3, imageReferee3);
            setRefereesView(personList.get(3), textReferee4, imageReferee4);

        } catch (Exception e) {
            Log.d("MatchRespos: ", e.toString());
        }
        buttonBack.setOnClickListener(v -> finish());
    }

    void setRefereesView (Person person, TextView textView, ImageView imageView)
    {
        textView.setText(person.getSurnameNameLastName());
        SetImage.setImage(this, imageView, person.getPhoto());
    }
}
