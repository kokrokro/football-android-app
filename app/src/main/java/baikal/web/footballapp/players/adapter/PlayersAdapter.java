package baikal.web.footballapp.players.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;
import java.util.TreeMap;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.DateToString;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Invite;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.Team;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static baikal.web.footballapp.Controller.BASE_URL;

public class PlayersAdapter extends PagedListAdapter<Person, PlayersAdapter.ViewHolder> {
//    private static final String TAG = "PlayersAdapter";
    private final OnItemListener mOnItemListener;
    private OnSwitchListener mOnSwitchListener;

    private TreeMap<String, Invite> accepted = new TreeMap<>();
    private TreeMap<String, Invite> pending = new TreeMap<>();

    private Team team;
    private String teamIds = null;

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

    public PlayersAdapter(OnItemListener mOnItemListener, OnSwitchListener mOnSwitchListener, Team team, List<String> teamIds) {
        super(DIFF_CALLBACK);
        // this is uglyyyy
        this.mOnItemListener = mOnItemListener;
        this.mOnSwitchListener = mOnSwitchListener;
        this.team = team;

        if (teamIds != null) {
            StringBuilder ids = new StringBuilder();
            for (String id : teamIds)
                ids.append(",").append(id);
            this.teamIds = ids.toString();
        }
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

        final SwitchCompat aSwitch;
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

            if (mOnItemListener != null) buttonShow2.setOnClickListener(view -> mOnItemListener.OnClick(person));

            if (mOnSwitchListener != null) {
                aSwitch.setOnClickListener(view -> {
                    if (aSwitch.isChecked()) {
                        aSwitch.setEnabled(false);
                        mOnSwitchListener.OnSwitch(person);
                    }
                });
                aSwitch.setVisibility(View.VISIBLE);
                teamName.setVisibility(View.GONE);

                aSwitch.setEnabled(true);
                aSwitch.setChecked(false);

                Callback<List<Invite>> responseCallbackAC = new Callback<List<Invite>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Invite>> call, @NonNull Response<List<Invite>> response) {
                        if (response.isSuccessful() && response.body()!=null) {
                            if(response.body().size() > 0) {
                                Invite invite = response.body().get(0);

                                accepted.put(person.getId(), invite);
                                teamName.setText(invite.getTeam().getName());
                                aSwitch.setEnabled(false);
                                teamName.setVisibility(View.VISIBLE);
                            }
                            else
                                accepted.put(person.getId(), new Invite());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Invite>> call, @NonNull Throwable t) { }
                };

                Invite acceptedInv = accepted.get(person.getId());
                if (acceptedInv == null)
                    Controller.getApi().getInvites(person.get_id(), teamIds, "accepted").enqueue(responseCallbackAC);
                else {
                    if (acceptedInv.getTeam() != null) {
                        teamName.setText(acceptedInv.getTeam().getName());
                        teamName.setVisibility(View.VISIBLE);
                        aSwitch.setEnabled(false);
                    }
                }

                Callback<List<Invite>> responseCallbackPE = new Callback<List<Invite>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Invite>> call, @NonNull Response<List<Invite>> response) {
                        if (response.isSuccessful() && response.body()!=null) {
                            if(response.body().size() > 0) {
                                Invite invite = response.body().get(0);
                                pending.put(person.getId(), invite);
                                aSwitch.setChecked(true);
                                aSwitch.setEnabled(false);
                            }
                            else
                                pending.put(person.getId(), new Invite());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Invite>> call, @NonNull Throwable t) { }
                };

                Invite pendingInv = pending.get(person.getId());
                if (pendingInv == null)
                    Controller.getApi().getInvites(person.get_id(), team.getId(), "pending").enqueue(responseCallbackPE);
                else {
                    if (pendingInv.getTeam() != null) {
                        aSwitch.setChecked(true);
                        aSwitch.setEnabled(false);
                    }
                }

            } else {
                aSwitch.setVisibility(View.GONE);
                teamName.setVisibility(View.GONE);
            }
        }


    }
}
