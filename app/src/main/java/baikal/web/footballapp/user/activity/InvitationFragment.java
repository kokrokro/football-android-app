package baikal.web.footballapp.user.activity;

import android.graphics.LightingColorFilter;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Invite;
import baikal.web.footballapp.user.adapter.RVInvitationAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class InvitationFragment extends Fragment {
    private final Logger log = LoggerFactory.getLogger(InvitationFragment.class);
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        RecyclerView recyclerView;
        LinearLayout linearEmpty;
        LinearLayout linearNotEmpty;
        view = inflater.inflate(R.layout.user_invitations, container, false);
        linearEmpty = view.findViewById(R.id.emptyInv);
        linearNotEmpty = view.findViewById(R.id.notEmptyInv);
        recyclerView = view.findViewById(R.id.recyclerViewUserInvitation);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RVInvitationAdapter adapter;
        List<Invite> invites = new ArrayList<>();
        adapter = new RVInvitationAdapter(getActivity(),getContext(),invites);
        Controller.getApi().getInvites(PersonalActivity.id,null,"pending").enqueue(new Callback<List<Invite>>() {
            @Override
            public void onResponse(Call<List<Invite>> call, Response<List<Invite>> response) {
                if(response.isSuccessful()){
                    if(response.body()!=null){
                        invites.clear();
                        invites.addAll(response.body());
                        if(invites.size()>0){
                            linearEmpty.setVisibility(View.GONE);
                        }
                        recyclerView.setAdapter(adapter);
                        log.error(""+invites.size());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Invite>> call, Throwable t) {
                log.error(t.getMessage());
            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        log.info("INFO: InvitationFragment onDestroy");
        super.onDestroy();
    }
}
