package com.example.michael.discgolfapp.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michael.discgolfapp.Adapters.PlayerDataAdapter;
import com.example.michael.discgolfapp.Model.Course;
import com.example.michael.discgolfapp.Model.Player;
import com.example.michael.discgolfapp.Model.PlayerStorage;
import com.example.michael.discgolfapp.R;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Michael on 7/10/2016.
 */
public class PlayerPickerActivity extends Activity {

    private static final int PLAYER_PICKER_INTENT = 3;

    PlayerDataAdapter adapter;
    PlayerStorage playerStorage;
    Context context = this;
    ListView lvPlayerList;
    Button btnNewPlayer;
    TextView tvChoosePlayersLable;
    Course selectedCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_picker_layout);
        tvChoosePlayersLable = (TextView) findViewById(R.id.tvChooseCoursesLable); //Change text to be meant for players.
        tvChoosePlayersLable.setText("Please choose your player.");
        setupPlayerStorage();
        tryCourseRetrieval();

        lvPlayerList = (ListView) findViewById(R.id.lvCourseList);
        setupPlayersListView();
        lvPlayerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Player player = (Player)adapter.getItem(position);
                if (player == null){
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable("Player",player);
                bundle.putSerializable("Course",selectedCourse);
                Intent intent = new Intent(getApplicationContext(),RuntimeGameActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
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
                                savePlayerStorage(playerStorage);
                                onCreate(null);
                            }
                        });
                AlertDialog art = aat.create();

                art.show();
                return true;
            }
        });
        btnNewPlayer = (Button) findViewById(R.id.btnNewCourse);
        btnNewPlayer.setText("Create New Player");
        btnNewPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddPlayerMenuActivity.class);
                Bundle bundle = new Bundle();

                bundle.putInt("PlayerKey",PLAYER_PICKER_INTENT);
                bundle.putSerializable("PlayerStorage", playerStorage);
                bundle.putSerializable("Course", selectedCourse);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    /*
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainMenuActivity.class));
        finish();
    }
    */

    private void setupPlayersListView(){
        if (playerStorage != null && playerStorage.getStoredPlayersCount() > 0){
            adapter = new PlayerDataAdapter(context, playerStorage.getPlayerStorageListArray());
            lvPlayerList.setAdapter(adapter);
        }
    }

    private void setupPlayerStorage() {
        playerStorage = retrievePlayersStorage();
        if (playerStorage == null){
            playerStorage = new PlayerStorage();
            Toast toast = Toast.makeText(getApplicationContext(),"File Not Found",Toast.LENGTH_LONG);
            toast.show();
        }
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
            toast.show();
        }
    }

    private PlayerStorage retrievePlayersStorage(){
        PlayerStorage playerStorage;
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
                playerStorage = (PlayerStorage) obj;
                return playerStorage;
            }

        }catch (Exception ex){
            Toast toast = Toast.makeText(context,"Something done messed up",Toast.LENGTH_LONG);
            toast.show();
            ex.printStackTrace();

            return null;
        }
        return null;
    }

    private void tryCourseRetrieval(){
        Bundle b = this.getIntent().getExtras();
        if (b != null){
            selectedCourse = (Course)b.getSerializable("Course");
        }
    }
}
