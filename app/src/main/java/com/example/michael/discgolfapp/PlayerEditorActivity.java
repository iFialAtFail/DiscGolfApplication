package com.example.michael.discgolfapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.michael.discgolfapp.Model.Player;
import com.example.michael.discgolfapp.Model.PlayerStorage;

/**
 * Created by Michael on 6/23/2016.
 */
public class PlayerEditorActivity extends Activity {
    Button btnNewPlayer;
    ListView playerListView;
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_editor);

        //debug
        debugPlayerLV();
        //debug

        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, PlayerStorage.getPlayerStorage());
        playerListView = (ListView) findViewById(R.id.lvPlayerList);
        if (PlayerStorage.getPlayerStorage() != null) {
            playerListView.setAdapter(arrayAdapter);
        }
        btnNewPlayer = (Button) findViewById(R.id.btnNewPlayer);
        btnNewPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AddPlayerMenuActivity.class );
                startActivity(intent);
            }
        });


    }

    private void debugPlayerLV(){
        Player mike = new Player("Mike");
        Player drew = new Player("Drew");
        Player brian = new Player("Brian");

        PlayerStorage.AddPlayerToStorage(mike);
        PlayerStorage.AddPlayerToStorage(brian);
        PlayerStorage.AddPlayerToStorage(drew);

        //PlayerStorage ps = new PlayerStorage(playerArray);
    }
}
