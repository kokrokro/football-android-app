package baikal.web.footballapp.user.activity;

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

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.model.Club;
import baikal.web.footballapp.model.Person;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

import static baikal.web.footballapp.Controller.BASE_URL;

public class UserClubs extends Fragment {
    private final Logger log = LoggerFactory.getLogger(UserClubs.class);
    private String uriPic;
    private FloatingActionButton fab;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fab = getActivity().findViewById(R.id.buttonEditClub);
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
            for (Club club1 : PersonalActivity.allClubs) {
                if (club1.getId().equals(str)) {
                    club = club1;
                }
            }

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.optionalCircleCrop();
            requestOptions.format(DecodeFormat.PREFER_ARGB_8888);
            RequestOptions.errorOf(R.drawable.ic_logo2);
            requestOptions.override(500, 500); // resizing
            requestOptions.priority(Priority.HIGH);

            if (club != null) {
                linearLayout.setVisibility(View.GONE);
                str = club.getName();
                textTitle.setText(str);
                str = club.getInfo();
                textDesc.setText(str);
                uriPic = BASE_URL;

                try {
                    uriPic += "/" + club.getLogo();
                    URL url = new URL(uriPic);
                    Glide.with(this)
                            .asBitmap()
                            .load(url)
                            .apply(requestOptions)
                            .into(imageLogo);
                } catch (Exception e) {
                    Glide.with(this)
                            .asBitmap()
                            .load(R.drawable.ic_logo2)
                            .apply(requestOptions)
                            .into(imageLogo);
                }
            } else {
                linearLayoutClubInfo.setVisibility(View.GONE);
            }
        } catch (NullPointerException e) {
        }

        return view;
    }

    @Override
    public void onDestroy() {
        log.info("INFO: UserClubs onDestroy");
        fab.setVisibility(View.INVISIBLE);
        super.onDestroy();
    }
}
