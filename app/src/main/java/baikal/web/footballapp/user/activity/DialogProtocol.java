package baikal.web.footballapp.user.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import baikal.web.footballapp.R;

public class DialogProtocol extends DialogFragment {

    private ProtocolListener mListener;
    private int pos = -1;

    public interface ProtocolListener {
        void OnChooseProtocolEvent(int pos);
    }

    DialogProtocol(ProtocolListener mListener) { this.mListener = mListener; }

    private void sendBackResult()
    {
        mListener.OnChooseProtocolEvent(pos);
        dismiss();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.DialogTheme);
        alertDialogBuilder.setTitle("Выберете событие");

        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_view_protocol_event_chooser, null);
        alertDialogBuilder.setView(view);

        ImageButton eventGoal = view.findViewById(R.id.event_goal);
        ImageButton eventYellowCard     = view.findViewById(R.id.event_yellow_card);
        ImageButton eventRedCard        = view.findViewById(R.id.event_red_card);
        ImageButton eventPenaltySuccess = view.findViewById(R.id.event_penalty_success);
        ImageButton eventPenaltyFailure = view.findViewById(R.id.event_penalty_failure);

        eventGoal.          setOnClickListener(v -> {pos = 0; sendBackResult();});
        eventYellowCard.    setOnClickListener(v -> {pos = 1; sendBackResult();});
        eventRedCard.       setOnClickListener(v -> {pos = 2; sendBackResult();});
        eventPenaltySuccess.setOnClickListener(v -> {pos = 3; sendBackResult();});
        eventPenaltyFailure.setOnClickListener(v -> {pos = 4; sendBackResult();});

        return alertDialogBuilder.create();
    }
}
