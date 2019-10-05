package baikal.web.footballapp.user.adapter;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import baikal.web.footballapp.CheckName;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SetImage;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.Player;
import baikal.web.footballapp.user.activity.StructureCommand1;

import java.util.List;

public class RVProtocolFirstCommandAdapter extends RecyclerView.Adapter<RVProtocolFirstCommandAdapter.ViewHolder>{
    private final StructureCommand1 context;
    private final List<String> listId;
    private final List<Player> players;
    private final List<Person> personList;
    public RVProtocolFirstCommandAdapter(Activity context, List<String> listId, List<Player> players, List<Person> personList){
        this.context = (StructureCommand1) context;
        this.listId = listId;
        this.players = players;
        this.personList = personList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_first_command, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String str;
        try {
            str = String.valueOf(players.get(position).getNumber());
        }catch (NullPointerException e){
            str = "-";
        }

        holder.textNum.setText(str);
        CheckName checkName = new CheckName();
        SetImage setImage = new SetImage();
        Person person;
        try {
            person = personList.get(position);
        }catch (NullPointerException | IndexOutOfBoundsException e){
            person = new Person();
            person.setSurname("Удален");
            person.setName("");
            person.setLastname("");
        }
        setImage.setImage(context, holder.image, person.getPhoto());
        str = checkName.check(person.getSurname(), person.getName(), person.getLastname());
        holder.textName.setText(str);
        if (!listId.contains(person.getId())){
            holder.textName.setTextColor(ContextCompat.getColor(context, R.color.colorLightGrayForText));
            holder.textNum.setTextColor(ContextCompat.getColor(context, R.color.colorLightGrayForText));
        }
        if (position==(players.size()-1)){
            holder.line.setVisibility(View.INVISIBLE);
        }
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
        ViewHolder(View item) {
            super(item);
            textNum = item.findViewById(R.id.playerCommandFirstNum);
            textName = item.findViewById(R.id.playerCommandFirstName);
            image = item.findViewById(R.id.playerCommandFirstLogo);
            line = item.findViewById(R.id.playerCommandFirstLine);
        }
    }
}
