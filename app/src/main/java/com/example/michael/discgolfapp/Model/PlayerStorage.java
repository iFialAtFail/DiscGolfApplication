package com.example.michael.discgolfapp.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Michael on 6/27/2016.
 */
public class PlayerStorage implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<Player> playerStorage;


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

    public List<Player> getPlayerStorage() {
        return playerStorage;
    }



    public int getNumberOfStoredPlayers(){
        if (!playerStorage.isEmpty()){
            return playerStorage.size();
        }
        return  0;
    }



    public void AddPlayerToStorage(Player player){
        for (Player p : playerStorage){
            if (player.getPlayerName() == p.getPlayerName()){
                return;
            }
        }

        playerStorage.add(player);

    }
}
