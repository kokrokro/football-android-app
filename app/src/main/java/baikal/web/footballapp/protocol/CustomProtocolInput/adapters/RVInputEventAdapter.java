package baikal.web.footballapp.protocol.CustomProtocolInput.adapters;

import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import baikal.web.footballapp.R;
import baikal.web.footballapp.protocol.CustomProtocolInput.InputEventData;

public class RVInputEventAdapter extends RecyclerView.Adapter<RVInputEventAdapter.ViewHolder> {

    private static final String TAG = "RVInputEventAdapter: ";
    ArrayList<InputEventData> protocolEvents;

    public RVInputEventAdapter (ArrayList<InputEventData> protocolEvents) {
        this.protocolEvents = protocolEvents;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_protocol_event_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.protocolEvent.setImageResource(protocolEvents.get(position).getEventIconId());
        Log.d(TAG, String.valueOf(position) + " ====================================");
    }

    @Override
    public int getItemCount() {
        return protocolEvents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton protocolEvent;
        ViewHolder (View item) {
            super(item);
            protocolEvent = item.findViewById(R.id.editProtocolEvent);
        }
    }
}
