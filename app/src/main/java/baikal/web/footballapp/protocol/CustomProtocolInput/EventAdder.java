package baikal.web.footballapp.protocol.CustomProtocolInput;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import baikal.web.footballapp.R;
import baikal.web.footballapp.protocol.CustomProtocolInput.adapters.RVInputEventAdapter;

public class EventAdder extends Fragment {
    private static final String TAG = "EventAdder: ";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_protocol_animated_fragment, container, false);



        ArrayList <InputEventData> inputEventData = new ArrayList<>();
        inputEventData.add(new InputEventData("", R.drawable.ic_goal));
        inputEventData.add(new InputEventData("", R.drawable.ic_red_card));
        inputEventData.add(new InputEventData("", R.drawable.ic_yellow_card));
        inputEventData.add(new InputEventData("", R.drawable.ic_faul));
        inputEventData.add(new InputEventData("", R.drawable.ic_penalty));

        RVInputEventAdapter inputAdapter = new RVInputEventAdapter();


        return view;
    }
}
