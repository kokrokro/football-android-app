package baikal.web.footballapp.user.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import baikal.web.footballapp.MankindKeeper;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.SetImage;
import baikal.web.footballapp.model.Club;
import baikal.web.footballapp.model.Person;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class UserClubs extends Fragment {
    private static final String TAG = "UserClubs";
    private final Logger log = LoggerFactory.getLogger(UserClubs.class);
    private FloatingActionButton fab;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fab = Objects.requireNonNull(getActivity()).findViewById(R.id.buttonEditClub);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        TextView textDesc;
        TextView textTitle;
        ImageView imageLogo;
        Club club;
        LinearLayout linearLayout;
        LinearLayout linearLayoutClubInfo;
        view = inflater.inflate(R.layout.user_clubs, container, false);
        linearLayout = view.findViewById(R.id.emptyClub);
        linearLayoutClubInfo = view.findViewById(R.id.userClub);
        textDesc = view.findViewById(R.id.userClubInfoDesc);
        textTitle = view.findViewById(R.id.userClubInfoTitle);
        imageLogo = view.findViewById(R.id.userClubInfoLogo);
        club = null;
        try {
            Person person = SaveSharedPreference.getObject().getUser();
            String str = person.getClub();
            for (Club club1 : MankindKeeper.getInstance().allClubs) {
                if (club1.getId().equals(str)) {
                    club = club1;
                }
            }

            if (club != null) {
                linearLayout.setVisibility(View.GONE);
                (new SetImage()).setImage(getContext(), imageLogo, club.getLogo());

                textTitle.setText(club.getName());
                textDesc.setText(club.getInfo());
            } else {
                linearLayoutClubInfo.setVisibility(View.GONE);
            }
        } catch (NullPointerException e) {
            log.error(TAG, e);
        }

        return view;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onDestroy() {
        log.info("INFO: UserClubs onDestroy");
        fab.setVisibility(View.INVISIBLE);
        super.onDestroy();
    }
}
