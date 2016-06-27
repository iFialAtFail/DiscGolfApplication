package com.example.michael.discgolfapp.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 6/27/2016.
 */
public class PlayerStorage implements Serializable {
    List<Player> playerStorage;

    //region Constructors

    public PlayerStorage(){
        playerStorage = new ArrayList<Player>();
    }

    public PlayerStorage(Player[] arrayOfPlayers){
        playerStorage = new ArrayList<Player>();

        for (Player player : arrayOfPlayers){
            playerStorage.add(player);
        }
    }

    public PlayerStorage(ArrayList<Player> listOfPlayers){
        playerStorage = new ArrayList<Player>(listOfPlayers);
    }

    //endregion

    public List<Player> getPlayerStorage() {
        return playerStorage;
    }

    public void setPlayerStorage(List<Player> playerStorage) {
        this.playerStorage = playerStorage;
    }

    public int getNumberOfStoredPlayers(){
        if (!playerStorage.isEmpty()){
            return playerStorage.size();
        }
        return  0;
    }
}
