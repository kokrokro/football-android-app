package baikal.web.footballapp.user.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.SignIn;
import baikal.web.footballapp.model.User;
import baikal.web.footballapp.players.activity.PlayersPage;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class UserPage extends Fragment {
    public static boolean auth;
    private static AuthoUser authoUser;
    private final Logger log = LoggerFactory.getLogger(UserPage.class);
    private final int REQUEST_CODE_REGISTRATION = 256;
    private EditText textLogin;
    private EditText textPass;
    private static User user;

    public void resetLoginPassEditText() {
        textLogin.setText("");
        textPass.setText("");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view;
        TextView textReg;
        Button logIn;
        auth = false;
        view = inflater.inflate(R.layout.page_user, container, false);
        textLogin = view.findViewById(R.id.loginLog);
        textPass = view.findViewById(R.id.passwordLog);
        logIn = view.findViewById(R.id.logInButton);
        textReg = view.findViewById(R.id.registration);
        textLogin.getBackground().setColorFilter(getResources().getColor(R.color.colorLightGray), PorterDuff.Mode.SRC_IN);
        textLogin.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                textLogin.getBackground().clearColorFilter();
            } else {
                textLogin.getBackground().setColorFilter(getResources().getColor(R.color.colorLightGray), PorterDuff.Mode.SRC_IN);
            }
        });
        textPass.getBackground().setColorFilter(getResources().getColor(R.color.colorLightGray), PorterDuff.Mode.SRC_IN);
        textPass.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                textPass.getBackground().clearColorFilter();
            } else {
                textPass.getBackground().setColorFilter(getResources().getColor(R.color.colorLightGray), PorterDuff.Mode.SRC_IN);
            }
        });
        logIn.setOnClickListener(v -> SignIn());
        textReg.setOnClickListener(v -> {
            resetLoginPassEditText();
            Intent intent = new Intent(getActivity(), RegistrationUser.class);
            startActivity(intent);
        });
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_REGISTRATION) {
                //pass token
                authoUser = new AuthoUser();
                User user = (User) data.getExtras().getSerializable("PERSONREGINFO");
                Person person = user.getUser();
                PersonalActivity.allPlayers.add(person);
                PersonalActivity.people.add(person);
                PersonalActivity.AllPeople.add(person);
                PlayersPage.adapter.notifyDataSetChanged();
                SaveSharedPreference.setLoggedIn(getActivity().getApplicationContext(), true);
                SaveSharedPreference.saveObject(user);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                authoUser.setFragmentManager(fragmentManager);
                fragmentManager.beginTransaction().add(R.id.pageContainer, authoUser, "AUTHOUSERPAGE").hide(this).show(authoUser).commit();
                PersonalActivity.active = authoUser;
            }
        } else {
            resetLoginPassEditText();
            log.error("ERROR: onActivityResult");
        }
    }


    private void SignIn() {
        String login = textLogin.getText().toString();
        String password = textPass.getText().toString();
        Call<User> call = Controller.getApi().signIn(new SignIn(login, password));

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                log.info("UserPage: check response");
                if (response.isSuccessful()) {
                    log.info("UserPage : response isSuccessful");
                    if (response.body() == null) {
                        log.error("ERROR: body is null");
                    } else {
                        log.info("UserPage: body is not null");
                        //all is ok
                        user = response.body();
                        try {
                            authoUser = new AuthoUser();
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            authoUser.setFragmentManager(fragmentManager);
                            fragmentManager.beginTransaction().add(R.id.pageContainer, authoUser).hide(PersonalActivity.active).show(authoUser).commit();
                            PersonalActivity.active = authoUser;
                            SaveSharedPreference.setLoggedIn(getActivity().getApplicationContext(), true);
                            SaveSharedPreference.saveObject(user);
                            PersonalActivity.id = SaveSharedPreference.getObject().getUser().getId();
                            PersonalActivity.token = user.getToken();

                            resetLoginPassEditText();

                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "Ошибка!", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        String str = "Ошибка! ";
                        str += jsonObject.getString("message");
                        Toast.makeText(getActivity(), str, Toast.LENGTH_LONG).show();
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                //for getting error in network put here Toast, so get the error on network
                log.error("ERROR: SignIn() onResponse ", t);
                Toast.makeText(getActivity(), "Ошибка сервера.", Toast.LENGTH_SHORT).show();
                getActivity().finishAndRemoveTask();
            }
        });

    }



}
