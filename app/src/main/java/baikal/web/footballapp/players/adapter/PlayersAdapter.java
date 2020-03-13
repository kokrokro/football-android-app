package baikal.web.footballapp.players.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;

import baikal.web.footballapp.DateToString;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Person;

import static baikal.web.footballapp.Controller.BASE_URL;

public class PlayersAdapter extends PagedListAdapter<Person, PlayersAdapter.ViewHolder> {
    private static final String TAG = "PlayersAdapter";
    private final OnItemListener mOnItemListener;
    private OnSwitchListener mOnSwitchListener = null;
    private boolean isInvitationView = false;

    public interface OnSwitchListener {
        void OnSwitch(Person person);
    }

    public interface OnItemListener {
        void OnClick(Person person);
    }

    private static final DiffUtil.ItemCallback<Person> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Person>() {
                @Override
                public boolean areItemsTheSame(
                        @NonNull Person oldPerson, @NonNull Person newPerson) {
                    // Person properties may have changed if reloaded from the DB, but ID is fixed
                    return oldPerson.getId().equals(newPerson.getId());
                }

                @Override
                public boolean areContentsTheSame(
                        @NonNull Person oldPerson, @NonNull Person newPerson) {
                    // NOTE: if you use equals, your object must properly override Object#equals()
                    // Incorrectly returning false here will result in too many animations.
                    return oldPerson.getId().equals(newPerson.getId());
                }
            };

    public PlayersAdapter(OnItemListener mOnItemListener) {
        super(DIFF_CALLBACK);
        // this is uglyyyy
        this.mOnItemListener = mOnItemListener;
    }

    public void setViewForInvites (OnSwitchListener onSwitchListener) {
        isInvitationView = true;
        mOnSwitchListener = onSwitchListener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlayersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.player, parent, false);
        return new PlayersAdapter.ViewHolder(view, parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Person player = getItem(position);

        if (player != null)
            holder.bindTo(player);
    }

    // да, это дубликат кода ViewHolder
    // надо его выносить т.к. используется уже в 3-ёх местах
    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageLogo;
        final LinearLayout buttonShow2;
        final TextView textName;
        final TextView textDOB;
        final View line;

        final Switch aSwitch;
        final TextView teamName;

        final Context context;

        public ViewHolder(View item, Context context) {
            super(item);
            line = item.findViewById(R.id.playersLine);
            imageLogo = item.findViewById(R.id.playerInfoLogo);
            buttonShow2 = item.findViewById(R.id.playerInfoButtonShow2);
            textName = item.findViewById(R.id.playerInfoName);
            textDOB = item.findViewById(R.id.playerInfoDOB);

            aSwitch = item.findViewById(R.id.P_switch);
            teamName = item.findViewById(R.id.P_team);

            this.context = context;
        }

        void bindTo(@NonNull Person person) {
            String name = person.getNameWithSurname();
            textName.setText(name);
            textDOB.setText(DateToString.stringDate(person.getBirthdate()));

            RequestOptions requestOptions = new RequestOptions()
                    .optionalCircleCrop()
                    .error(R.drawable.ic_logo2)
                    .override(500, 500)
                    .priority(Priority.NORMAL);


            if (person.getPhoto() != null) {
                Uri uri = Uri.parse(BASE_URL + "/" + person.getPhoto());

                Glide.with(context)
                        .asBitmap()
                        .load(uri)
                        .apply(requestOptions)
                        .into(imageLogo);
            }

            buttonShow2.setOnClickListener(view -> mOnItemListener.OnClick(person));

            if (isInvitationView) {
                aSwitch.setVisibility(View.VISIBLE);
                teamName.setVisibility(View.VISIBLE);
            }
        }


    }
}
