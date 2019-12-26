package baikal.web.footballapp.user.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.MankindKeeper;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SetImage;
import baikal.web.footballapp.model.MatchPopulate;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.Player;
import baikal.web.footballapp.user.activity.StructureCommand1;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RVWorkProtocolEditTeamAdapter extends RecyclerView.Adapter<RVWorkProtocolEditTeamAdapter.ViewHolder>{
    private final StructureCommand1 context;
    private final List<Player> players;
    private final MatchPopulate match;
    private boolean isEditable;

    public RVWorkProtocolEditTeamAdapter(Activity context, List<Player> players, MatchPopulate match, boolean isEditable){
        this.context = (StructureCommand1) context;
        this.players = players;
        this.match = match;
        this.isEditable = isEditable;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_first_command, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Player player = players.get(position);

        if (MankindKeeper.getInstance().getPersonById(player.getPerson()) == null) {
            getPerson(player.getPerson());
            return;
        }

        Person person = MankindKeeper.getInstance().getPersonById(player.getPerson());
        SetImage.setImage(context, holder.image, person.getPhoto());
        holder.textName.setText(person.getSurnameAndName());
        holder.textNum.setText(player.getNumber().toString());

        if (!match.getPlayersList().contains(person.getId())){
            holder.textName.setTextColor(ContextCompat.getColor(context, R.color.colorLightGrayForText));
            holder.textNum.setTextColor(ContextCompat.getColor(context, R.color.colorLightGrayForText));
            holder.aSwitch.setChecked(false);
        } else {
            holder.textName.setTextColor(ContextCompat.getColor(context, R.color.colorBottomNavigationUnChecked));
            holder.textNum.setTextColor(ContextCompat.getColor(context, R.color.colorBottomNavigationUnChecked));
            holder.aSwitch.setChecked(true);
        }

        holder.aSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                holder.textName.setTextColor(ContextCompat.getColor(context, R.color.colorBottomNavigationUnChecked));
                holder.textNum.setTextColor(ContextCompat.getColor(context, R.color.colorBottomNavigationUnChecked));
                match.getPlayersList().add(player.getPerson());
            } else {
                holder.textName.setTextColor(ContextCompat.getColor(context, R.color.colorLightGrayForText));
                holder.textNum.setTextColor(ContextCompat.getColor(context, R.color.colorLightGrayForText));
                try {
                    match.getPlayersList().remove(player.getPerson());
                } catch (Exception ignored) {
                }
            }
        });

        holder.aSwitch.setEnabled(isEditable);

        if (position==(players.size()-1))
            holder.line.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textNum;
        final TextView textName;
        final ImageView image;
        final View line;
        final Switch aSwitch;

        ViewHolder(View item) {
            super(item);
            textNum = item.findViewById(R.id.playerCommandFirstNum);
            textName = item.findViewById(R.id.playerCommandFirstName);
            image = item.findViewById(R.id.playerCommandFirstLogo);
            line = item.findViewById(R.id.playerCommandFirstLine);
            aSwitch = item.findViewById(R.id.PFC_switch);
        }
    }

    private void getPerson (String personId)
    {
        Controller.getApi().getPerson(personId).enqueue(new Callback<List<Person>>() {
            @Override
            public void onResponse(@NonNull Call<List<Person>> call, @NonNull Response<List<Person>> response) {
                if (response.body() != null && response.body().size() > 0) {
                    Person person = response.body().get(0);
                    MankindKeeper.getInstance().addPerson(person);
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Person>> call, @NonNull Throwable t) {

            }
        });
    }
}
