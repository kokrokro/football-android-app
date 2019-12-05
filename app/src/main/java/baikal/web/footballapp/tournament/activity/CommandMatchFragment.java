package baikal.web.footballapp.tournament.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Match;
import baikal.web.footballapp.tournament.adapter.RVCommandMatchAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CommandMatchFragment extends Fragment {
    Logger log = LoggerFactory.getLogger(PersonalActivity.class);

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view;
        RecyclerView recyclerView;
        LinearLayout layout;
        view = inflater.inflate(R.layout.command_info_match, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewCommandMatch);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setVisibility(View.VISIBLE);
        layout = view.findViewById(R.id.teamMatchEmpty);
        try {
        Bundle bundle = getArguments();
        List<Match> matches = (List<Match>) bundle.getSerializable("TEAMSTRUCTUREMATCHES");
        if (matches.size()!=0){
            layout.setVisibility(View.GONE);
        }
        RVCommandMatchAdapter adapter = new RVCommandMatchAdapter(getActivity(), matches);
        recyclerView.setAdapter(adapter);
        }catch (Exception e){}
        return view;
    }
}