package baikal.web.footballapp.user.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.user.adapter.RecentRefereeAdapter;

public class SetRefereesBottomSheet extends BottomSheetDialogFragment {
    private List<String> referees;
    private RecyclerView recyclerView;
    private RecentRefereeAdapter adapter;
   private final   Listener listener;

    public SetRefereesBottomSheet(List<String> referees, Listener listener) {
        this.referees = referees;
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,@Nullable Bundle savedInstance){
        View view = inflater.inflate(R.layout.set_referees_bottom_sheet,container, false);
        recyclerView = view.findViewById(R.id.setRefereeRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecentRefereeAdapter(referees, getContext(), person -> {
            if(listener==null){
                Log.d("LISTENER", "NULL PO");
            }
            listener.onClick(person);
        });
        recyclerView.setAdapter(adapter);
        return view;
    }
    public interface Listener{
        void onClick(Person person);
    }
}
