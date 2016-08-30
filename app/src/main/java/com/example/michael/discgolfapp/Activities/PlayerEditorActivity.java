package com.example.michael.discgolfapp.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.michael.discgolfapp.Model.PlayerStorage;
import com.example.michael.discgolfapp.R;
import com.example.michael.discgolfapp.Adapters.PlayerDataAdapter;

/**
 * Created by Michael on 6/23/2016.
 */
public class PlayerEditorActivity extends AppCompatActivity {

    //region Private Fields

    Button btnNewPlayer;
    ListView lvPlayerList;
    PlayerStorage playerStorage;
    Context context =  this;
    PlayerDataAdapter adapter;

    //endregion

    //region Android Lifecycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_editor);

        setupPlayerStorage();

        lvPlayerList = (ListView) findViewById(R.id.lvPlayerList);

        setupPlayerListView();

        lvPlayerList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           final int position, long id) {
                AlertDialog.Builder aat = new AlertDialog.Builder(context);
                aat.setTitle("Delete?")
                        .setMessage("Are you sure you want to delete "+parent.getItemAtPosition(position).toString()+"?")
                        .setCancelable(true)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                dialog.cancel();
                            }

                        })
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                //Make the change
                                playerStorage.DeletePlayerFromStorage(position);

                                //Commit the change to persistant memory
                                playerStorage.SaveToFile(context);
                                onCreate(null);
                            }
                        });
                AlertDialog art = aat.create();

                art.show();
                return true;

            }

        });

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
        playerStorage.SaveToFile(context);
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

    private void setupPlayerListView() {
        if (playerStorage != null && playerStorage.getStoredPlayersCount() > 0) {
            adapter = new PlayerDataAdapter(context, playerStorage.getPlayerStorageListArray());
            lvPlayerList.setAdapter(adapter);
            String playerLength = String.valueOf(playerStorage.getStoredPlayersCount());
            Toast toast = Toast.makeText(getApplicationContext(),playerLength,Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private void setupPlayerStorage() {
        playerStorage = PlayerStorage.LoadPlayerStorage(context);
        if (playerStorage == null){
            playerStorage = new PlayerStorage();
            Toast toast = Toast.makeText(getApplicationContext(),"File Not Found",Toast.LENGTH_LONG);
            toast.show();
        }
    }

    //endregion
}
