package baikal.web.footballapp.protocol.CustomProtocolInput.adapters;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import baikal.web.footballapp.R;

public class RVInputEventAdapter extends RecyclerView.Adapter<RVInputEventAdapter.ViewHolder> {

    private int[] eventIcons;

    public RVInputEventAdapter ()
    {
        eventIcons = new int[5];
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_protocol_event_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.protocolEvent.setImageResource(eventIcons[position]);
    }

    @Override
    public int getItemCount() {
        return eventIcons.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton protocolEvent;
        ViewHolder (View item) {
            super(item);
            protocolEvent = item.findViewById(R.id.editProtocolEvent);
        }
    }
}
