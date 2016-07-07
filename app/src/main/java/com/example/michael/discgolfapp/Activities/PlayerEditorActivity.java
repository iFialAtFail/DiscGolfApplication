package com.example.michael.discgolfapp.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.michael.discgolfapp.Model.Player;
import com.example.michael.discgolfapp.Model.PlayerStorage;
import com.example.michael.discgolfapp.R;
import com.example.michael.discgolfapp.Adapters.playerDataAdapter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Michael on 6/23/2016.
 */
public class PlayerEditorActivity extends Activity {

    //region Private Fields

    Button btnNewPlayer;
    ListView playerListView;
    PlayerStorage playerStorage;
    Context context =  this;
    playerDataAdapter adapter;

    //endregion

    //region Android Lifecycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_editor);

        setupPlayerStorage();

        playerListView = (ListView) findViewById(R.id.lvPlayerList);
        setupPlayerListView();

        btnNewPlayer = (Button) findViewById(R.id.btnNewPlayer);
        btnNewPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Bundle up object to pass over
                Bundle extras = new Bundle();
                extras.putSerializable("PlayerStorage",playerStorage);

                //Create and add bundle to Intent
                Intent intent = new Intent(getApplicationContext(),AddPlayerMenuActivity.class );
                intent.putExtras(extras);

                startActivity(intent);
            }
        });
    }


    @Override
    protected void onDestroy() {
        savePlayerStorage(playerStorage);
        super.onDestroy();
    }

    //endregion

    //region Overriding normal behaviours

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainMenuActivity.class));
        finish();

    }

    //endregion

    //region Private helper methods

    private void savePlayerStorage(Object myObject){
        try {
            // Write to disk with FileOutputStream
            FileOutputStream fos = getApplicationContext().openFileOutput("playerList.data", Context.MODE_PRIVATE);

            // Write object with ObjectOutputStream
            ObjectOutputStream oos = new
                    ObjectOutputStream (fos);

            // Write object out to disk
            oos.writeObject ( myObject );

            oos.close();
            fos.close();

        } catch(Exception ex){
            ex.printStackTrace();
            Toast toast = Toast.makeText(getApplicationContext(),"File didn't save",Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private PlayerStorage retrievePlayerStorage(){
        PlayerStorage _playerStorage;
        try {
            // Read from disk using FileInputStream
            FileInputStream fis = context.openFileInput("playerList.data");

            // Read object using ObjectInputStream
            ObjectInputStream ois =
                    new ObjectInputStream (fis);

            // Read an object
            Object obj = ois.readObject();

            if (obj instanceof PlayerStorage)
            {
                // Cast object to a Vector
                _playerStorage = (PlayerStorage) obj;
                return _playerStorage;
                // Do something with vector....
            }
        }catch (Exception ex){
            Toast toast = Toast.makeText(context,"Something done messed up",Toast.LENGTH_LONG);
            toast.show();
            ex.printStackTrace();

            return null;
        }
        return null;
    }

    private void setupPlayerListView() {
        if (playerStorage != null && playerStorage.getStoredPlayersCount() > 0) {
            adapter = new playerDataAdapter(context, playerStorage.getPlayerStorageListArray());
            playerListView.setAdapter(adapter);
            String playerLength = String.valueOf(playerStorage.getStoredPlayersCount());
            Toast toast = Toast.makeText(getApplicationContext(),playerLength,Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private void setupPlayerStorage() {
        playerStorage = retrievePlayerStorage();
        if (playerStorage == null){
            playerStorage = new PlayerStorage();
            Toast toast = Toast.makeText(getApplicationContext(),"File Not Found",Toast.LENGTH_LONG);
            toast.show();
        }
    }

    //endregion
}
