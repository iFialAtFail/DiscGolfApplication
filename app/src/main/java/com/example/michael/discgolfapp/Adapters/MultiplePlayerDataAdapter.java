package com.example.michael.discgolfapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.michael.discgolfapp.Model.Player;
import com.example.michael.discgolfapp.R;

import java.util.List;

/**
 * Created by Michael on 7/14/2016.
 */
public class MultiplePlayerDataAdapter extends PlayerDataAdapter {
    Context context;
    public MultiplePlayerDataAdapter(Context context, List<Player> playerStorage) {
        super(context, playerStorage);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.multi_player_listview_row, parent, false);
        Player player = (Player) getItem(position);


        ImageView thumbnail = (ImageView) rowView.findViewById(R.id.imgThumbnail);
        TextView tvNameText = (TextView) rowView.findViewById(R.id.tvPlayerName);

        if (position % 2 == 0) {
            thumbnail.setImageResource(R.mipmap.buzz_blank_100x100);
        }
        else {
            thumbnail.setImageResource(R.mipmap.roadrunnder_blank_100x100);
        }

        tvNameText.setText(player.getName());
        return rowView;
    }
}
