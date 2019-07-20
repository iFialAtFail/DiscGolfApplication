package com.manleysoftware.michael.discgolfapp.data;

import android.content.Context;

import com.manleysoftware.michael.discgolfapp.Model.Player;

import java.util.List;

public interface PlayerRepository {
    List<Player> getPlayers();

    void addPlayer(Player player);

    void removePlayer(Player player);

    boolean Save(Context context);
}
