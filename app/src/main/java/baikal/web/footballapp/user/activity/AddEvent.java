package baikal.web.footballapp.user.activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Event;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.user.adapter.RVEventsIconAdapter;
import baikal.web.footballapp.user.adapter.SpinnerRefereeAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddEvent extends AppCompatActivity {
    //    EditText textMinutes;
    private EditText textHalf;
    private int eventPosition = 100;
    private final Logger log = LoggerFactory.getLogger(AddEvent.class);
    public static List<Boolean> list2;
    private Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);
        List<String> list;
        Spinner spinner;
        ImageButton buttonClose;
        ImageButton buttonSave;
        RecyclerView recyclerView;
        LinearLayoutManager layoutManager;
        list = new ArrayList<>();
        list2 = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerViewEvents);
//        textMinutes = (EditText) findViewById(R.id.editProtocolEventMinutes);
        textHalf = findViewById(R.id.editProtocolHalf);
        buttonClose = findViewById(R.id.addEventClose);
        buttonSave = findViewById(R.id.addEventSave);
        spinner = findViewById(R.id.editProtocolSpinnerPlayers);
        list.add("Гол");
        list.add("ЖК");
        list.add("КК");
        list.add("Фол");
        list.add("Автогол");
        list.add("Пенальти");
        for (String str : list) {
            list2.add(false);
        }
//        textMinutes.getBackground().setColorFilter(getResources().getColor(R.color.colorLightGray), PorterDuff.Mode.SRC_IN);
//        textMinutes.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    textMinutes.getBackground().clearColorFilter();
//                } else {
//                    textMinutes.getBackground().setColorFilter(getResources().getColor(R.color.colorLightGray), PorterDuff.Mode.SRC_IN);
//                }
//            }
//        });
        textHalf.getBackground().setColorFilter(getResources().getColor(R.color.colorLightGray), PorterDuff.Mode.SRC_IN);
        textHalf.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                textHalf.getBackground().clearColorFilter();
            } else {
                textHalf.getBackground().setColorFilter(getResources().getColor(R.color.colorLightGray), PorterDuff.Mode.SRC_IN);
            }
        });

        buttonClose.setOnClickListener(v -> finish());


        RVEventsIconAdapter adapter = new RVEventsIconAdapter(this, list, list2, position -> eventPosition = position);
        recyclerView.setAdapter(adapter);
        layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        try {
            final Event event = new Event();
            Intent arguments = getIntent();
            final List<String> countPlayers = Objects.requireNonNull(arguments.getExtras()).getStringArrayList("PROTOCOLCOUNTPLAYERS");
            List<Person> people = new ArrayList<>();
            for (Person person : PersonalActivity.AllPeople) {
                if (countPlayers.contains(person.getId())) {
                    people.add(person);
                }
            }
            SpinnerRefereeAdapter adapterSpinner = new SpinnerRefereeAdapter(this, R.layout.spinner_item, people);
            spinner.setAdapter(adapterSpinner);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    person = (Person) parent.getItemAtPosition(pos);
                    log.error(person.getId());
                    event.setPlayer(person.getId());
                }

                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            buttonSave.setOnClickListener(v -> {
                String str = textHalf.getText().toString();

                if (!str.equals("")) {
                    if (eventPosition == 100) {
                        Toast.makeText(AddEvent.this, "Выберите событие.", Toast.LENGTH_SHORT).show();
                    } else {
                        str += " тайм";
                        event.setTime(str);
                        switch (eventPosition) {
                            case 0:
                                str = "goal";
                                break;
                            case 1:
                                str = "yellowCard";
                                break;
                            case 2:
                                str = "redCard";
                                break;
                            case 3:
                                str = "foul";
                                break;
                            case 4:
                                str = "autoGoal";
                                break;
                            case 5:
                                str = "penalty";
                                break;
                        }
                        event.setEventType(str);
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("ADDEVENT", event);
                        bundle.putSerializable("ADDEVENTPERSON", person);
                        intent.putExtras(bundle);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                } else {
                    Toast.makeText(AddEvent.this, "Укажите тайм", Toast.LENGTH_SHORT).show();
                }

            });

        } catch (Exception e) {
        }
    }
}
