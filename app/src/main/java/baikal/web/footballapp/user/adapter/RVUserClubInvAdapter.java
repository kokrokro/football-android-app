package baikal.web.footballapp.user.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import baikal.web.footballapp.R;
import baikal.web.footballapp.user.activity.UserClubs;

public class RVUserClubInvAdapter extends RecyclerView.Adapter<RVUserClubInvAdapter.ViewHolder>{
    private final UserClubs context;
    public RVUserClubInvAdapter(UserClubs context){
        this.context =  context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_club, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
//        holder.textTitle.setText();
//        holder.textCoach.setText();
        holder.buttonOk.setOnClickListener(v -> {
            //post
            int newPosition = holder.getAdapterPosition();
            notifyItemRemoved(newPosition);
        });

        holder.buttonCancel.setOnClickListener(v -> {
            //post
            int newPosition = holder.getAdapterPosition();
            notifyItemRemoved(newPosition);
        });
        Glide.with(context)
                .asBitmap()
                .load(R.drawable.ic_con)
                .apply(RequestOptions
                        .circleCropTransform()
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .priority(Priority.HIGH))
                .into(holder.imageLogo);
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        final TextView textTitle;
        final ImageView imageLogo;
        final TextView textCoach;
        final Button buttonOk;
        final Button buttonCancel;
        ViewHolder(View item) {
            super(item);
            imageLogo = (ImageView) item.findViewById(R.id.userClubInvLogo);
            textTitle = (TextView) item.findViewById(R.id.invClubTitle);
            textCoach = (TextView) item.findViewById(R.id.invClubCoach);
            buttonCancel = (Button) item.findViewById(R.id.invClubButtonCancel);
            buttonOk = (Button) item.findViewById(R.id.invClubButtonOk);
        }
    }
}
