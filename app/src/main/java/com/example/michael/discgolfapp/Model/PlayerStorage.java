package com.example.michael.discgolfapp.Model;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
    private static final String PLAYER_STORAGE_FILE = "playerList.data";
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

    public boolean SaveToFile(Context context){
        try {
            // Write to disk with FileOutputStream
            FileOutputStream fos = context.openFileOutput(PLAYER_STORAGE_FILE, Context.MODE_PRIVATE);

            // Write object with ObjectOutputStream
            ObjectOutputStream oos = new
                    ObjectOutputStream (fos);

            // Write object out to disk
            oos.writeObject ( this );

            oos.close();
            fos.close();

        } catch(Exception ex){
            ex.printStackTrace();
            return false;
        }

        return true;
    }

    public static PlayerStorage LoadPlayerStorage(Context context){
        PlayerStorage retrieve;
        try {
            // Read from disk using FileInputStream
            FileInputStream fis = context.openFileInput(PLAYER_STORAGE_FILE);

            // Read object using ObjectInputStream
            ObjectInputStream ois =
                    new ObjectInputStream (fis);

            // Read an object
            Object obj = ois.readObject();

            if (obj instanceof PlayerStorage)
            {
                retrieve = (PlayerStorage) obj;
                return retrieve;
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    //endregion
}
