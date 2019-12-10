package baikal.web.footballapp.tournament.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import baikal.web.footballapp.MankindKeeper;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.PersonStats;
import baikal.web.footballapp.model.Player;
import baikal.web.footballapp.tournament.activity.TournamentPlayersFragment;

import java.util.List;


public class RVTournamentPlayersAdapter extends RecyclerView.Adapter<RVTournamentPlayersAdapter.ViewHolder>{
    private static final String TAG = "TournamentPlayersAdap";
    private final List<Player> players;
    private final List<PersonStats> personStats;
    public RVTournamentPlayersAdapter(TournamentPlayersFragment context, List<Player> players, List<PersonStats> personStats){
        this.players = players;
        this.personStats = personStats;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tournament_player, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PersonStats stats = personStats.get(position);
        Person player = null;
        try {
            Log.d(TAG, players.get(position).getId());
//            Log.d(TAG, String.valueOf(MankindKeeper.getInstance().allPerson.containsKey(players.get(position).getId())));
            player = MankindKeeper.getInstance().getPersonById(players.get(position).getId());
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        String str;

        try {
            if (player==null){
                player = new Person();
                player.setName("Удален");
                player.setSurname("");
                player.setLastname("");
            }
            str = player.getSurnameWithInitials();
            holder.textName.setText(str);
            int count = players.get(position).getMatches();
            str = String.valueOf(count);
            holder.textPoint1.setText(str);
            count = players.get(position).getGoals();
            str = String.valueOf(count);
            holder.textPoint2.setText(str);
            count = players.get(position).getYellowCards();
//            count = players.get(position).getActiveYellowCards();
            str = String.valueOf(count);
            holder.textPoint3.setText(str);
//            count = players.get(position).getActiveDisquals();
            count = players.get(position).getRedCards();
            str = String.valueOf(count);
            holder.textPoint4.setText(str);
        }catch (NullPointerException e){
            int count = position +1;
            str = String.valueOf(count);
            holder.textPoint1.setText(str);
            str = String.valueOf(count);
            holder.textPoint2.setText(str);
            str = String.valueOf(count);
            holder.textPoint3.setText(str);
            str = String.valueOf(count);
            holder.textPoint4.setText(str);
        }
//        Club club = null;
//        for (Club club1 : PersonalActivity.allClubs){
//            try{
//                if (club1.getId().equals(clubs.get(position))){
//                    club = club1;
//                }
//            }catch (IndexOutOfBoundsException e){break;}
//        }
//        SetImage setImage = new SetImage();
//        assert club != null;
//        try {
//            setImage.setImage(holder.image.getContext(), holder.image, club.getLogo());
//        }catch (NullPointerException e){}


    }

    @Override
    public int getItemCount() {
        return personStats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        final TextView textName;
        final TextView textPoint1;
        final TextView textPoint2;
        final TextView textPoint3;
        final TextView textPoint4;
        final ImageView image;

        ViewHolder(View item) {
            super(item);
            textName = item.findViewById(R.id.tournamentPlayer);
            textPoint1 = item.findViewById(R.id.tournamentPlayerPoint1);
            textPoint2 = item.findViewById(R.id.tournamentPlayerPoint2);
            textPoint3 = item.findViewById(R.id.tournamentPlayerPoint3);
            textPoint4 = item.findViewById(R.id.tournamentPlayerPoint4);
            image = item.findViewById(R.id.tournamentPlayerCommandLogo);
        }
    }
    public void dataChanged(List<Player> allPlayers1){
        players.clear();
        players.addAll(allPlayers1);
        notifyDataSetChanged();
    }
}
