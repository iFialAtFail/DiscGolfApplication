package com.example.michael.discgolfapp;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.michael.discgolfapp.Model.Player;
import com.example.michael.discgolfapp.Model.PlayerStorage;

import java.util.List;

/**
 * Created by Michael on 7/6/2016.
 */
public class playerDataAdapter extends BaseAdapter {

    private List<Player> playerList;
    private Context context;
    private LayoutInflater inflater;

    public playerDataAdapter(Context context, List<Player> playerStorage) {
        playerList = playerStorage;
        this.context = context;
    }

    @Override
    public int getCount() {
        return playerList.size();
    }

    @Override
    public Object getItem(int position) {
        return playerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.player_listview_row, parent, false);
        Player player = (Player) getItem(position);

        ImageView thumbnail = (ImageView) rowView.findViewById(R.id.imgThumbnail);
        TextView tvNameText = (TextView) rowView.findViewById(R.id.tvPlayerName);

        if (position % 2 == 0) {
            thumbnail.setImageResource(R.mipmap.buzz_blank_100x100);
        }
        else {
            thumbnail.setImageResource(R.mipmap.roadrunnder_blank_100x100);
        }

        tvNameText.setText(player.getPlayerName());
        return rowView;
    }
}
