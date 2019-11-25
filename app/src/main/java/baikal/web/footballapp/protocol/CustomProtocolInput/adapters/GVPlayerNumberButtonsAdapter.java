package baikal.web.footballapp.protocol.CustomProtocolInput.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Comparator;

import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Player;

public class GVPlayerNumberButtonsAdapter extends BaseAdapter {

    private static final String TAG = "GV Num Btn Adapter: ";
    private ArrayList<Player> players;
    private Context context;
    private LayoutInflater layoutInflater;
    private Comparator<Player> comparator;

    public GVPlayerNumberButtonsAdapter (ArrayList<Player> players, Context context, LayoutInflater layoutInflater)
    {
        this.players = players;
        this.context = context;
        this.layoutInflater = layoutInflater;
        comparator = (o1, o2) -> {
            if (o1.getNumber() == null) return -1;
            if (o2.getNumber() == null) return  1;
            return o1.getNumber().compareTo(o2.getNumber());
        };
    }

    @Override
    public int getCount() {
        return players != null ? players.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return players != null ? players.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = layoutInflater.inflate(R.layout.edit_protocol_player_number_item, null);

        if (players == null)
            return convertView;

        Button playerNumButton = convertView.findViewById(R.id.playerNumberButton);
        playerNumButton.setText(players.get(position).getNumber());

        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
//        Collections.sort(players, comparator);
    }
}
