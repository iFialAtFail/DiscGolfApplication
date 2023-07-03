package com.manleysoftware.michael.discgolfapp.data.filerepository;

import android.content.Context;
import android.util.Log;

import com.manleysoftware.michael.discgolfapp.application.PlayerExistsAlreadyException;
import com.manleysoftware.michael.discgolfapp.data.PlayerRepository;
import com.manleysoftware.michael.discgolfapp.domain.Player;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by Michael on 6/27/2016.
 */
public class PlayerFileRepository implements Serializable, PlayerRepository {

    //region Private Fields

    private static final long serialVersionUID = 1L;
    private static final String PLAYER_STORAGE_FILE = "playerList.data";
    private static final String TAG = PlayerFileRepository.class.getSimpleName();
    private List<Player> players;

    //endregion

    //region Constructors

    public PlayerFileRepository(Context context) {
        PlayerRepository repo = loadFromFile(context);
        if (repo != null) {
            players = repo.getAllPlayers();
        } else {
            players = new ArrayList<>();
        }
    }

    public PlayerFileRepository(Player[] arrayOfPlayers) {
        players = new ArrayList<>();
        players.addAll(Arrays.asList(arrayOfPlayers));

    }

    public PlayerFileRepository(ArrayList<Player> listOfPlayers) {
        players.addAll(listOfPlayers);

    }

    //endregion


    //region Public Methods

    @Override
    public void add(Player player, Context context) throws PlayerExistsAlreadyException {
        if (isUniquePlayer(player)) {
            players.add(player);
            save(context);
        } else {
            throw new PlayerExistsAlreadyException();
        }
    }


    @Override
    public void update(Player entity, Context context) throws IllegalStateException {
        Optional<Player> player = players.stream()
                .filter(p -> isSamePlayerName(entity, p))
                .reduce((a, b) -> {
                    throw new IllegalStateException("Multiple elements: " + a + ", " + b);
                });
        if (!player.isPresent()) {
            throw new IllegalStateException();
        }
        save(context);
    }

    private static boolean isSamePlayerName(Player entity, Player p) {
        Log.d(TAG, "isSamePlayerName: " + entity.getName() + ", " + p.getName() + "");
        return p.getName().equals(entity.getName());
    }

    @Override
    public List<Player> getAllPlayers() {
        return players;
    }

    private boolean isUniquePlayer(Player player) {
        //Check to avoid duplicate players
        for (Player p : players) {
            if (isSamePlayerName(p, player)) {
                return false;
            }
        }
        return true;
    }


    @Override
    public void delete(Player player, Context context) {
        players.remove(player);
        save(context);
    }

    @Override
    public Player findByPrimaryKey(Player template) {
        Player retval = null;
        for (Player player : players) {
            if (isSamePlayerName(player, template)) {
                retval = player;
            }
        }
        return retval;
    }

    private boolean save(Context context) {
        try {
            // Write to disk with FileOutputStream
            FileOutputStream fos = context.openFileOutput(PLAYER_STORAGE_FILE, Context.MODE_PRIVATE);

            // Write object with ObjectOutputStream
            ObjectOutputStream oos = new
                    ObjectOutputStream(fos);

            // Write object out to disk
            oos.writeObject(this);

            oos.close();
            fos.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

        return true;
    }

    public static PlayerFileRepository loadFromFile(Context context) {
        PlayerFileRepository retrieve;
        try {
            // Read from disk using FileInputStream
            FileInputStream fis = context.openFileInput(PLAYER_STORAGE_FILE);

            // Read object using ObjectInputStream
            ObjectInputStream ois =
                    new ObjectInputStream(fis);

            // Read an object
            Object obj = ois.readObject();

            if (obj instanceof PlayerFileRepository) {
                retrieve = (PlayerFileRepository) obj;
                return retrieve;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    //endregion
}
