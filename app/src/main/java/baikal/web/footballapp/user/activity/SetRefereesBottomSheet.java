package baikal.web.footballapp.user.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

import baikal.web.footballapp.CheckError;
import baikal.web.footballapp.Controller;
import baikal.web.footballapp.MankindKeeper;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.user.adapter.RecentRefereeAdapter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SetRefereesBottomSheet extends BottomSheetDialogFragment {
    private List<String> referees;
    private RecyclerView recyclerView;
    private List<String> initialReferees;
    private RecentRefereeAdapter adapter;
   private final   Listener listener;
   private SearchView searchView;

    public SetRefereesBottomSheet(List<String> referees, Listener listener) {
        this.referees = referees;
        this.listener = listener;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,@Nullable Bundle savedInstance){
        View view = inflater.inflate(R.layout.set_referees_bottom_sheet,container, false);
        setStyle(DialogFragment.STYLE_NORMAL,R.style.BottomSheetDialogStyle);
        initialReferees = new ArrayList<>();
        initialReferees.addAll(referees);
        recyclerView = view.findViewById(R.id.setRefereeRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecentRefereeAdapter(referees, getContext(), person -> {
            if(listener==null){
                Log.d("LISTENER", "NULL PO");
            }
            listener.onClick(person);
        });
        recyclerView.setAdapter(adapter);
        searchView = view.findViewById(R.id.bottomSheetSearchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @SuppressLint("CheckResult")
            @Override
            public boolean onQueryTextChange(String newText) {

                if(newText.isEmpty()){
                    referees.clear();
                    referees.addAll(initialReferees);
                    adapter.notifyDataSetChanged();
                    return false;
                }
                Controller.getApi().getAllPersons(newText, "4","0")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(people -> {
                            referees.clear();
                            for(Person person : people) {
                                referees.add(person.getId());
                                MankindKeeper.getInstance().addPerson(person);
                            }
                            adapter.notifyDataSetChanged();
                                }
                                ,
                                error -> {
                                    CheckError checkError = new CheckError();
                                    checkError.checkError(getActivity(), error);
                                }
                        );
                return false;
            }
        });

        return view;
    }
    public interface Listener{
        void onClick(Person person);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogStyle);
        dialog.setOnShowListener(dialog1 -> new Handler().postDelayed(() -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog1;
            FrameLayout bottomSheet = d.findViewById(R.id.design_bottom_sheet);
            BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        },0));
        return dialog;
    }
}

