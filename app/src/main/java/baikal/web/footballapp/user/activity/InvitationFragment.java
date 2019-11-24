package baikal.web.footballapp.user.activity;

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

import baikal.web.footballapp.R;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        try{
//        recyclerView.setAdapter(AuthoUser.adapterInv);
//        if (AuthoUser.pendingTeamInvitesList.size()!=0){
//            linearEmpty.setVisibility(View.GONE);
//        }
//        else {
//            linearNotEmpty.setVisibility(View.GONE);
//        }
        linearEmpty.setVisibility(View.GONE);

        }catch (NullPointerException e){}
        return view;
    }

    @Override
    public void onDestroy() {
        log.info("INFO: InvitationFragment onDestroy");
        super.onDestroy();
    }
}
