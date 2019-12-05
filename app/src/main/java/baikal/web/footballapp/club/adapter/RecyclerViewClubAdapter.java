package baikal.web.footballapp.club.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import baikal.web.footballapp.FullScreenImage;
import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SetImage;
import baikal.web.footballapp.club.activity.Club;
import baikal.web.footballapp.club.activity.ClubPage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static baikal.web.footballapp.Controller.BASE_URL;

public class RecyclerViewClubAdapter extends RecyclerView.Adapter<RecyclerViewClubAdapter.ViewHolder>{
    private final Logger log = LoggerFactory.getLogger(PersonalActivity.class);
    private final List<baikal.web.footballapp.model.Club> allClubs;
    private final ClubPage context;
    private final PersonalActivity activity;
    public RecyclerViewClubAdapter(Activity activity, ClubPage context,  List<baikal.web.footballapp.model.Club> allClubs){
        this.allClubs = allClubs;
        this.activity = (PersonalActivity) activity;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.club, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        try {
            String uriPic = BASE_URL;
            String str = allClubs.get(position).getName();
            holder.textTitle.setText(str);

            (new SetImage()).setImage(activity, holder.imageLogo, allClubs.get(position).getLogo());

            uriPic += "/" + allClubs.get(position).getLogo();
            log.info("INFO: url " + uriPic);

            final String finalUriPic = uriPic;
            holder.imageLogo.setOnClickListener(v -> {
                if (finalUriPic.contains(".jpg") || finalUriPic.contains(".jpeg") || finalUriPic.contains(".png")) {
                    Intent intent = new Intent(activity, FullScreenImage.class);
                    intent.putExtra("player_photo", finalUriPic);
                    context.startActivity(intent);
                }
            });

            holder.buttonShow.setOnClickListener(v -> {
                Club club = new Club();
                Bundle bundle = new Bundle();
                baikal.web.footballapp.model.Club club1 = allClubs.get(position);
                bundle.putSerializable("CLUBINFO", club1);
                club.setArguments(bundle);
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                fragmentManager.beginTransaction().add(R.id.pageContainer, club).hide(activity.getActive()).show(club).commit();
                activity.setActive(club);
            });
            if (position == (allClubs.size() - 1)){
                holder.line.setVisibility(View.INVISIBLE);
            }
        } catch (Exception ignored) {}

    }

    @Override
    public int getItemCount() {
        return allClubs.size();
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

    public void dataChanged(List<baikal.web.footballapp.model.Club> allPlayers1){
        allClubs.clear();
        allClubs.addAll(allPlayers1);
        notifyDataSetChanged();
    }
}
