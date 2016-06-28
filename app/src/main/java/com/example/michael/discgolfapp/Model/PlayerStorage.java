package com.example.michael.discgolfapp.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Michael on 6/27/2016.
 */
public class PlayerStorage implements Serializable {
    private static List<Player> playerStorage = new ArrayList<>();

    //region Constructors

    public PlayerStorage(Player[] arrayOfPlayers){
        playerStorage.addAll(Arrays.asList(arrayOfPlayers));
    }

    public PlayerStorage(ArrayList<Player> listOfPlayers){
        playerStorage.addAll(listOfPlayers);
    }

    //endregion

    public static List<Player> getPlayerStorage() {
        return playerStorage;
    }



    public static int getNumberOfStoredPlayers(){
        if (!playerStorage.isEmpty()){
            return playerStorage.size();
        }
        return  0;
    }

    public static void AddPlayerToStorage(Player player){
        for (Player p : playerStorage){
            if (player.getPlayerName() == p.getPlayerName()){
                return;
            }

        }
        playerStorage.add(player);
    }
}
