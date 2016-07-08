package com.example.michael.discgolfapp.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Michael on 6/27/2016.
 */
public class PlayerStorage implements Serializable {

    //region Private Fields

    private static final long serialVersionUID = 1L;
    private List<Player> playerStorage;

    //endregion

    //region Constructors

    public PlayerStorage(){
        playerStorage = new ArrayList<>();
    }

    public PlayerStorage(Player[] arrayOfPlayers){
        playerStorage.addAll(Arrays.asList(arrayOfPlayers));

    }

    public PlayerStorage(ArrayList<Player> listOfPlayers){
        playerStorage.addAll(listOfPlayers);

    }

    //endregion

    //region Getters and Setters

    public List<Player> getPlayerStorageListArray() {

        return playerStorage;
    }

    public int getStoredPlayersCount(){
        if (!playerStorage.isEmpty()){
            return playerStorage.size();
        }
        return  0;
    }

    //endregion

    //region Public Methods

    public void AddPlayerToStorage(Player player){
        for (Player p : playerStorage){
            if (player.getName().equals(p.getName())){
                return;
            }
        }

        playerStorage.add(player);
    }

    public void DeletePlayerFromStorage(int position){
        playerStorage.remove(position);
    }

    public void DeletePlayerFromStorage(Player player){
        playerStorage.remove(player);
    }

    //endregion
}
