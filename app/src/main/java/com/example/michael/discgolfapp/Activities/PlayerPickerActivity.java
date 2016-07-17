package com.example.michael.discgolfapp.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michael.discgolfapp.Adapters.MultiplePlayerDataAdapter;
import com.example.michael.discgolfapp.Adapters.PlayerDataAdapter;
import com.example.michael.discgolfapp.Model.Course;
import com.example.michael.discgolfapp.Model.Player;
import com.example.michael.discgolfapp.Model.PlayerStorage;
import com.example.michael.discgolfapp.R;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 7/10/2016.
 */
public class PlayerPickerActivity extends Activity {

    private static final int PLAYER_PICKER_INTENT = 3;

    MultiplePlayerDataAdapter adapter;
    PlayerStorage playerStorage;
    Context context = this;
    ListView lvPlayerList;
    ArrayList<Player> playersPlaying;
    Button btnStartGame;


    Course selectedCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_picker_layout);


        setupPlayerStorage();
        tryCourseRetrieval();

        btnStartGame = (Button) findViewById(R.id.btnStartTheGame);
        lvPlayerList = (ListView) findViewById(R.id.lvPlayerList);

        setupPlayersListView();
        lvPlayerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckedTextView check = (CheckedTextView)view.findViewById(R.id.tvPlayerName);
                check.toggle();

            }
        });



        lvPlayerList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
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
    }


    //region Button Handling

    public void onNewGameClicked(View v){
        playersPlaying = new ArrayList<>();

        SparseBooleanArray sparseBooleanArray = lvPlayerList.getCheckedItemPositions();

        for (int i = 0; i < playerStorage.getStoredPlayersCount(); i++){
            if (sparseBooleanArray.get(i)){
                playersPlaying.add((Player)lvPlayerList.getItemAtPosition(i));
            }
        }

        if (playersPlaying.isEmpty()) {
            Toast.makeText(getApplicationContext(),"No players chosen, please select a player and try again.",Toast.LENGTH_LONG).show();
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable("Course",selectedCourse);
        bundle.putSerializable("Players", playersPlaying);

        Intent intent = new Intent(getApplicationContext(),RuntimeGameActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void onNewPlayerClicked(View v){
        Intent intent = new Intent(getApplicationContext(), AddPlayerMenuActivity.class);
        Bundle bundle = new Bundle();

        bundle.putInt("PlayerKey",PLAYER_PICKER_INTENT);
        bundle.putSerializable("PlayerStorage", playerStorage);
        bundle.putSerializable("Course", selectedCourse);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //endregion

    //region Private Helper Methods

    private void setupPlayersListView(){
        if (playerStorage != null && playerStorage.getStoredPlayersCount() > 0){
            lvPlayerList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            adapter = new MultiplePlayerDataAdapter(context, playerStorage.getPlayerStorageListArray());
            lvPlayerList.setAdapter(adapter);
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




    private void tryCourseRetrieval(){
        Bundle b = this.getIntent().getExtras();
        if (b != null){
            selectedCourse = (Course)b.getSerializable("Course");
        }
    }

    //endregion
}
