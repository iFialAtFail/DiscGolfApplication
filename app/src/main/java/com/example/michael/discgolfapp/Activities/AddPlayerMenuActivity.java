package com.example.michael.discgolfapp.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.michael.discgolfapp.Model.Course;
import com.example.michael.discgolfapp.Model.Player;
import com.example.michael.discgolfapp.Model.PlayerStorage;
import com.example.michael.discgolfapp.R;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Michael on 6/23/2016.
 */
public class AddPlayerMenuActivity extends AppCompatActivity {

    //region Private Fields

    private EditText etName;
    private Button btnSavePlayer;
    private PlayerStorage playerStorage;
    private Course course;

    //endregion

    //region Android Product Lifecycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_player_menu_layout);

        tryRestorePlayerStorageObj(); //using serializable and bundle
        tryRestoreCourseObject();

        etName = (EditText) findViewById(R.id.etName);
        btnSavePlayer = (Button) findViewById(R.id.btnSavePlayer);
        btnSavePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                if ( Player.isValidPlayerName(name) && isUniqueName(name)){
                    Player p = new Player(name);
                    playerStorage.AddPlayerToStorage(p);
                    playerStorage.SaveToFile(getApplicationContext());

                    gotoPriorActivity();

                }

                else{
                    Toast toast = Toast.makeText(getApplicationContext(),"Invalid input or duplicate user. Please try again.", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }

    //endregion

    //region Private Helper Methods

    private boolean isUniqueName(String nameInput){
        if (playerStorage.getStoredPlayersCount() > 0) {
            for (Player player : playerStorage.getPlayerStorageListArray()) {
                if (nameInput.equals(player.getName())) {
                    return false;
                }
            }
        }

        return true;
    }

    private void gotoPriorActivity(){
        Bundle b = this.getIntent().getExtras();
        if (b.getInt("PlayerKey")== 3){ //if coming from player picker...
            Bundle bundle = new Bundle();
            bundle.putSerializable("Course",course);
            Intent intent = new Intent(getApplicationContext(),PlayerPickerActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        else{ //or if coming from player editor...
            Intent intent = new Intent(getApplicationContext(),PlayerEditorActivity.class);
            startActivity(intent);
        }
    }

    private void tryRestorePlayerStorageObj() {
        Bundle b = this.getIntent().getExtras();
        if (b != null){
            playerStorage = (PlayerStorage) b.getSerializable("PlayerStorage");
        }
    }
    private void tryRestoreCourseObject(){
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null){
            course = (Course) bundle.getSerializable("Course");
        }
    }
    //endregion
}
