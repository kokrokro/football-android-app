package baikal.web.footballapp.protocol;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import baikal.web.footballapp.R;

public class RecyclerViewProtocolAdapter extends RecyclerView.Adapter<RecyclerViewProtocolAdapter.ViewHolder> {


    private final ProtocolPage activity;

    public RecyclerViewProtocolAdapter(ProtocolPage activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        final View line;
        final ImageView imageLogo;
        final TextView textTitle;
        final LinearLayout buttonShow;
        ViewHolder(View item) {
            super(item);
            line = item.findViewById(R.id.clubLine);
            imageLogo = item.findViewById(R.id.clubLogo);
            textTitle = item.findViewById(R.id.clubTitle);
            buttonShow = item.findViewById(R.id.clubButtonShow);
        }
    }

}
