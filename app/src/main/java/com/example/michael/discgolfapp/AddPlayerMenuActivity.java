package com.example.michael.discgolfapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.michael.discgolfapp.Model.Player;
import com.example.michael.discgolfapp.Model.PlayerStorage;
import com.google.gson.Gson;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Michael on 6/23/2016.
 */
public class AddPlayerMenuActivity extends Activity {
    EditText etName;
    Button btnSavePlayer;
    PlayerStorage playerStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_player_menu_layout);

        Bundle b = this.getIntent().getExtras();
        if (b != null){
            playerStorage = (PlayerStorage) b.getSerializable("PlayerStorage");
        }

        etName = (EditText) findViewById(R.id.etName);
        btnSavePlayer = (Button) findViewById(R.id.btnSavePlayer);
        btnSavePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                if ( validName(name) ){
                    Player p = new Player(name);
                    playerStorage.AddPlayerToStorage(p);
                    savePlayerStorage(playerStorage);

                    Intent intent = new Intent(getApplicationContext(),PlayerEditorActivity.class);
                    startActivity(intent);
                }

                else{
                    Toast toast = Toast.makeText(getApplicationContext(),"Invalid input or duplicate user. Please try again.", Toast.LENGTH_LONG);
                    toast.show();
                }



            }
        });
    }



    private boolean validName(String nameInput){
        if (playerStorage.getNumberOfStoredPlayers() > 0) {
            for (Player player : playerStorage.getPlayerStorage()) {
                if (nameInput.equals(player.getPlayerName())) {
                    return false;
                }
            }
        }

        char[] charArray = nameInput.toCharArray();

        for(char letter : charArray)
        {
            if (Character.isLetterOrDigit(letter) || letter == ' ')
            {
                //Let it keep iterating through the characters.
            }
            else { return false; }
        }
        return true;
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
            toast.show();//TODO Throw proper exception
        }
    }
}
