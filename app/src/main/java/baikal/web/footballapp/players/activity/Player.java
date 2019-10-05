package baikal.web.footballapp.players.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import baikal.web.footballapp.CheckName;
import baikal.web.footballapp.DateToString;
import baikal.web.footballapp.FullScreenImage;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SetImage;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.players.adapter.RVPlayersTournamentAdapter;
import baikal.web.footballapp.user.activity.AuthoUser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static android.app.Activity.RESULT_OK;
import static baikal.web.footballapp.Controller.BASE_URL;

public class Player extends Fragment {
    boolean scrollStatus;
    final Logger log = LoggerFactory.getLogger(Player.class);
    final int REQUEST_CODE_PLAYERINV = 286;
    Person person;
    public static FloatingActionButton fab;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view;
        ImageView imageLogo;
        TextView textName;
        TextView textDOB;
        RecyclerView recyclerView;
        NestedScrollView scroller;
        RelativeLayout layout;
        view = inflater.inflate(R.layout.player_info, container, false);
        imageLogo = view.findViewById(R.id.playerImage);
        textName = view.findViewById(R.id.playerName);
        textDOB = view.findViewById(R.id.playerDOB);
        layout = view.findViewById(R.id.layoutPlayerInfoLeagues);
        fab = view.findViewById(R.id.addPlayerButton);
        scroller = view.findViewById(R.id.playerInfoScroll);
        scrollStatus = false;



        try {
            Bundle bundle = this.getArguments();
            person = (Person) bundle.getSerializable("PLAYERINFO");
            String str;
            CheckName checkName = new CheckName();
            str = checkName.check(person.getSurname(), person.getName(), person.getLastname());
            textName.setText(str);
            SetImage setImage = new SetImage();
            setImage.setImage(getActivity(), imageLogo, person.getPhoto());
            String uriPic = BASE_URL;
            try {
                uriPic += "/" + person.getPhoto();
            } catch (Exception e) {
            }

            fab.setOnClickListener(v -> {
                try {
                    if (AuthoUser.personOngoingLeagues.size()==0){
                        Toast.makeText(getActivity(), "Ошибка! У вас нет ни одной команды", Toast.LENGTH_LONG).show();
                    }
                    else {
                            Intent intent = new Intent(getActivity(), PlayerInv.class);
                            Bundle bundle1 = new Bundle();
                            bundle1.putSerializable("PLAYERINV", person);
                            intent.putExtras(bundle1);
                            startActivityForResult(intent, REQUEST_CODE_PLAYERINV);

                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Необходимо авторизоваться", Toast.LENGTH_SHORT).show();
                }

            });

            scroller.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {

                if (scrollY < oldScrollY) {
                    scrollStatus = false;
                }

                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    scrollStatus = true;
                    fab.hide();
                }
            });

            final String finalUriPic = uriPic;
            imageLogo.setOnClickListener(v -> {
                if (finalUriPic.contains(".jpg") || finalUriPic.contains(".jpeg") || finalUriPic.contains(".png")) {
                    Intent intent = new Intent(getActivity(), FullScreenImage.class);
                    intent.putExtra("player_photo", finalUriPic);
                    startActivity(intent);
                }
            });

            String DOB = person.getBirthdate();
            DateToString dateToString = new DateToString();
            textDOB.setText(dateToString.ChangeDate(DOB));
            recyclerView = view.findViewById(R.id.recyclerViewPlayerTournaments);
            recyclerView.setNestedScrollingEnabled(false);
            try {
                if (person.getPastLeagues().size()==0){
                    layout.setVisibility(View.GONE);
                }
                else {
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }catch (NullPointerException r){
                layout.setVisibility(View.GONE);
            }

            RVPlayersTournamentAdapter adapter = new RVPlayersTournamentAdapter(this, person.getPastLeagues());
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//nothing to do
                }

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        fab.show();
                    } else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                        fab.hide();
                    }
                    if (scrollStatus) {
                        fab.hide();
                    }
                    super.onScrollStateChanged(recyclerView, newState);
                }
            });
        } catch (Exception E) {
            log.error("ERROR: " + E);
        }

        fab.show();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_PLAYERINV) {
                Toast.makeText(getContext(), "Вы отправили игроку приглашение", Toast.LENGTH_LONG).show();
            }
        } else {
            log.error("ERROR: onActivityResult");
        }
    }

}
