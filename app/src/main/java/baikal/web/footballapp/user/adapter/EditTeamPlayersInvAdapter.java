package baikal.web.footballapp.user.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import baikal.web.footballapp.App;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SetImage;
import baikal.web.footballapp.model.Invite;
import baikal.web.footballapp.model.Person;

public class EditTeamPlayersInvAdapter extends RecyclerView.Adapter<EditTeamPlayersInvAdapter.ViewHolder>{
    private final List<Invite> invites;
    private final OnInvitationCancel listener;

    public interface OnInvitationCancel {
        void onClick(Invite invite);
    }

    public EditTeamPlayersInvAdapter(List<Invite> invites, OnInvitationCancel listener){
        this.invites = invites;
        this.listener = listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.command_player_inv
                , parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.textNum.setText(String.valueOf(position + 1));
        if (position == (invites.size() - 1))
            holder.line.setVisibility(View.INVISIBLE);

        Invite invite = invites.get(position);
        if (invite != null)
            holder.bindTo(invite);
    }

    @Override
    public int getItemCount() {
        return invites.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textNum;
        final ImageView image;
        final TextView textName;
        final ImageButton buttonDelete;
        final View line;

        ViewHolder(View item) {
            super(item);
            textNum = item.findViewById(R.id.userCommandPlayerInvTextNum);
            image = item.findViewById(R.id.userCommandPlayerInvLogo);
            textName = item.findViewById(R.id.userCommandPlayerInvName);
            buttonDelete = item.findViewById(R.id.userCommandInvPlayerDelete);
            line = item.findViewById(R.id.userCommandPlayerInvLine);
        }

        void bindTo (Invite invite) {
            Person person = invite.getPerson();
            SetImage.setImage(App.getAppContext(), image, person.getPhoto());
            textName.setText(person.getSurnameAndName());

            buttonDelete.setOnClickListener(v ->
                listener.onClick(invite)
            );
        }
    }
}
