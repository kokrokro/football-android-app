package baikal.web.footballapp.players.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.os.ConfigurationCompat;
import androidx.fragment.app.FragmentManager;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.internal.bind.util.ISO8601Utils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import baikal.web.footballapp.R;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.players.activity.Player;

import static baikal.web.footballapp.Controller.BASE_URL;

public class PlayersAdapter extends PagedListAdapter<Person, PlayersAdapter.ViewHolder> {
    private static final String TAG = "PlayersAdapter";
    final FragmentManager fragmentManager; // this is uglyyyy

    public static final DiffUtil.ItemCallback<Person> DIFF_CALLBACK =
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

    public PlayersAdapter(FragmentManager fragmentManager) {
        super(DIFF_CALLBACK);
        this.fragmentManager = fragmentManager;
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

        if (player != null) {
            holder.bindTo(player);
        }
    }

    // да, это дубликат кода ViewHolder
    // надо его выносить т.к. используется уже в 3-ёх местах
    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageLogo;
        final LinearLayout buttonShow2;
        final TextView textName;
        final TextView textDOB;
        final View line;

        final Context context;

        public ViewHolder(View item, Context context) {
            super(item);
            line = item.findViewById(R.id.playersLine);
            imageLogo = item.findViewById(R.id.playerInfoLogo);
            buttonShow2 = item.findViewById(R.id.playerInfoButtonShow2);
            textName = item.findViewById(R.id.playerInfoName);
            textDOB = item.findViewById(R.id.playerInfoDOB);
            this.context = context;
        }

        void bindTo(@NonNull Person person) {
            String name = person.getNameWithSurname();
            textName.setText(name);

            Locale locale = ConfigurationCompat.getLocales(Resources.getSystem().getConfiguration()).get(0);

            String formattedBirthDateStr = "";
            SimpleDateFormat outDateFormat = new SimpleDateFormat("dd.MM.yyyy", locale);

            try {
                Date birthDate = ISO8601Utils.parse(person.getBirthdate(), new ParsePosition(0));
                formattedBirthDateStr = outDateFormat.format(birthDate);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            textDOB.setText(formattedBirthDateStr);


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

            buttonShow2.setOnClickListener(view -> {
                Player playerFragment = new Player();
                Bundle bundle = new Bundle();
                bundle.putSerializable("PLAYERINFO", person);

                playerFragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.pageContainer, playerFragment).addToBackStack(null).commit();
            });

        }


    }
}
