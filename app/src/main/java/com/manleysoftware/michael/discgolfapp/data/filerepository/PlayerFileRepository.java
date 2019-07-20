package com.manleysoftware.michael.discgolfapp.data.filerepository;

import android.content.Context;

import com.manleysoftware.michael.discgolfapp.data.Model.Player;
import com.manleysoftware.michael.discgolfapp.data.PlayerRepository;

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
public class PlayerFileRepository implements Serializable, PlayerRepository {

    //region Private Fields

    private static final long serialVersionUID = 1L;
    private static final String PLAYER_STORAGE_FILE = "playerList.data";
    private List<Player> players;

    //endregion

    //region Constructors

    public PlayerFileRepository(Context context){
        PlayerRepository repo = loadFromFile(context);
        if (repo != null){
            players = repo.getPlayers();
        } else {
            players = new ArrayList<>();
        }
    }

    public PlayerFileRepository(Player[] arrayOfPlayers){
        players = new ArrayList<>();
        players.addAll(Arrays.asList(arrayOfPlayers));

    }

    public PlayerFileRepository(ArrayList<Player> listOfPlayers){
        players.addAll(listOfPlayers);

    }

    //endregion

    //region Getters and Setters

    @Override
    public List<Player> getPlayers() {

        return players;
    }


    //endregion

    //region Public Methods

    @Override
    public void addPlayer(Player player){
        if (isUniquePlayer(player)){
            players.add(player);
        }
    }

    private boolean isUniquePlayer(Player player) {
        //Check to avoid duplicate players
        for (Player p : players){
            if (player.getName().equals(p.getName())){
                return false;
            }
        }
        return true;
    }

    @Override
    public void removePlayer(Player player){
        players.remove(player);
    }

    @Override
    public boolean Save(Context context){
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

    public static PlayerFileRepository loadFromFile(Context context){
        PlayerFileRepository retrieve;
        try {
            // Read from disk using FileInputStream
            FileInputStream fis = context.openFileInput(PLAYER_STORAGE_FILE);

            // Read object using ObjectInputStream
            ObjectInputStream ois =
                    new ObjectInputStream (fis);

            // Read an object
            Object obj = ois.readObject();

            if (obj instanceof PlayerFileRepository)
            {
                retrieve = (PlayerFileRepository) obj;
                return retrieve;
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    //endregion
}
