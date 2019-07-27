package com.manleysoftware.michael.discgolfapp.data;

import android.content.Context;

import com.manleysoftware.michael.discgolfapp.Application.PlayerExistsAlreadyException;
import com.manleysoftware.michael.discgolfapp.data.Model.Player;

import java.util.List;

public interface PlayerRepository {
    List<Player> getPlayers();

    void addPlayer(Player player) throws PlayerExistsAlreadyException;

    void removePlayer(Player player);

    boolean Save(Context context);
}
