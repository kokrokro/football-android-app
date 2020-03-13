package baikal.web.footballapp.user.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.model.Invite;
import baikal.web.footballapp.user.adapter.RVInvitationAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvitationFragment extends Fragment {
    private final Logger log = LoggerFactory.getLogger(InvitationFragment.class);
    private AuthoUser authoUser;
    private List<Invite> invites = new ArrayList<>();
    private LinearLayout linearEmpty;
    private RVInvitationAdapter adapter;

    InvitationFragment (AuthoUser authoUser) {
        this.authoUser = authoUser;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        RecyclerView recyclerView;

        view = inflater.inflate(R.layout.user_invitations, container, false);
        linearEmpty = view.findViewById(R.id.emptyInv);
        view.findViewById(R.id.notEmptyInv);
        recyclerView = view.findViewById(R.id.recyclerViewUserInvitation);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new RVInvitationAdapter(getActivity(),getContext(), authoUser, invites);
        recyclerView.setAdapter(adapter);

        loadData();

        return view;
    }

    private void loadData ()
    {
        Controller.getApi().getInvites(SaveSharedPreference.getObject().getUser().getId(),null,"pending").enqueue(new Callback<List<Invite>>() {
            @Override
            public void onResponse(@NonNull Call<List<Invite>> call, @NonNull Response<List<Invite>> response) {
                if(response.isSuccessful()){
                    if(response.body()!=null) {
                        invites.clear();
                        invites.addAll(response.body());
                        if(invites.size()>0)
                            linearEmpty.setVisibility(View.GONE);

                        adapter.notifyDataSetChanged();
                        log.error(""+invites.size());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Invite>> call, @NonNull Throwable t) {
                log.error(t.getMessage());
            }
        });
    }

    @Override
    public void onDestroy() {
        log.info("INFO: InvitationFragment onDestroy");
        super.onDestroy();
    }
}
