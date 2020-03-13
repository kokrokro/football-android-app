package baikal.web.footballapp.user.activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.MankindKeeper;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.SignIn;
import baikal.web.footballapp.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class UserPage extends Fragment {
    private static final String TAG = "UserPage: ";
    static boolean auth;
    private AuthoUser authoUser;
    private final Logger log = LoggerFactory.getLogger(UserPage.class);
    private static final int REQUEST_CODE_REGISTRATION = 256;
    private EditText textLogin;
    private EditText textPass;
    private static User user;

    private PersonalActivity activity;

    public UserPage(PersonalActivity activity) {
        this.activity = activity;
    }

    private void resetLoginPassEditText() {
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
                authoUser = new AuthoUser(activity);
                User user = (User) Objects.requireNonNull(data.getExtras()).getSerializable("PERSONREGINFO");
                Person person = Objects.requireNonNull(user).getUser();
                MankindKeeper.getInstance().allPerson.put(person.get_id(), person);
                SaveSharedPreference.setLoggedIn(Objects.requireNonNull(getActivity()).getApplicationContext(), true);
                SaveSharedPreference.saveObject(user);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().add(R.id.pageContainer, authoUser, "AUTHOUSERPAGE").hide(this).show(authoUser).commit();
                activity.setActive(authoUser);
                activity.StartRefreshToken();
            }
        } else {
            log.error("ERROR: onActivityResult");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "On Destroy...");
    }

    private void SignIn() {
        String login = textLogin.getText().toString();
        String password = textPass.getText().toString();
        Call<User> call = Controller.getApi().signIn(new SignIn(login, password));
        log.info("INFO: load and parse json-file");
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                log.info("INFO: check response");
                if (response.isSuccessful()) {
                    log.info("INFO: response isSuccessful");
                    if (response.body() == null) {
                        log.error("ERROR: body is null");
                    } else {
                        //all is ok
                        user = response.body();
                        try {
                            authoUser = new AuthoUser(activity);
                            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                            fragmentManager.beginTransaction()
                                    .add(R.id.pageContainer, authoUser)
                                    .hide(authoUser.activity.getActive())
                                    .show(authoUser)
                                    .commit();
                            authoUser.activity.setActive(authoUser);
                            SaveSharedPreference.setLoggedIn(getActivity().getApplicationContext(), true);
                            SaveSharedPreference.saveObject(user);
                            activity.StartRefreshToken();
                            resetLoginPassEditText();

                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "Ошибка!", Toast.LENGTH_LONG).show();
                            log.error(TAG, e);
                        }
                    }
                }
                else {
                    try {
                        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.errorBody()).string());
                        String str = "Ошибка! ";
                        str += jsonObject.getString("message");
                        Toast.makeText(getActivity(), str, Toast.LENGTH_LONG).show();
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        log.error(TAG, e);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                //for getting error in network put here Toast, so get the error on network
                log.error("ERROR: SignIn() onResponse ", t);
                Toast.makeText(getActivity(), "Ошибка сервера.", Toast.LENGTH_SHORT).show();
//                try {
//                    Objects.requireNonNull(getActivity()).finishAndRemoveTask();
//                } catch (Exception e) {
//                    log.error(TAG, e);
//                }
            }
        });

    }
}
