package com.manleysoftware.michael.discgolfapp.data;

import com.manleysoftware.michael.discgolfapp.domain.Player;

import java.util.List;

public interface PlayerRepository extends Repository<Player>{
    List<Player> getAllPlayers();
}
