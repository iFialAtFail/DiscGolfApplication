package com.example.michael.discgolfapp;

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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Michael on 6/23/2016.
 */
public class PlayerEditorActivity extends Activity {
    Button btnNewPlayer;
    ListView playerListView;
    ArrayAdapter arrayAdapter;
    PlayerStorage playerStorage;
    Context context =  this;
    playerDataAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_editor);

        playerStorage = retrievePlayerStorage();
        if (playerStorage == null){
            playerStorage = new PlayerStorage();
            Toast toast = Toast.makeText(getApplicationContext(),"File Not Found",Toast.LENGTH_LONG);
            toast.show();
        }


        playerListView = (ListView) findViewById(R.id.lvPlayerList);
        if (playerStorage != null && playerStorage.getNumberOfStoredPlayers() > 0) {
            //arrayAdapter = new ArrayAdapter(context,android.R.layout.simple_list_item_1,playerStorage.getPlayerStorage() );
            //playerListView.setAdapter(arrayAdapter);
            adapter = new playerDataAdapter(context, playerStorage.getPlayerStorage());
            playerListView.setAdapter(adapter);
            String playerLength = String.valueOf(playerStorage.getNumberOfStoredPlayers());
            Toast toast = Toast.makeText(getApplicationContext(),playerLength,Toast.LENGTH_LONG);
            toast.show();
        }
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
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainMenuActivity.class));
        finish();

    }

    private void debugPlayerLV(){
        Player mike = new Player("Mike");
        Player drew = new Player("Drew");
        Player brian = new Player("Brian");



        playerStorage.AddPlayerToStorage(mike);
        playerStorage.AddPlayerToStorage(brian);
        playerStorage.AddPlayerToStorage(drew);

        //PlayerStorage ps = new PlayerStorage(playerArray);
    }

    @Override
    protected void onDestroy() {
        savePlayerStorage(playerStorage);
        super.onDestroy();
    }

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
            toast.show();//TODO Throw proper exception//TODO Throw proper exception
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
}
