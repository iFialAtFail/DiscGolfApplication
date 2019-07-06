package com.manleysoftware.michael.discgolfapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.manleysoftware.michael.discgolfapp.Model.Course;
import com.manleysoftware.michael.discgolfapp.Model.Player;
import com.manleysoftware.michael.discgolfapp.data.PlayerRepository;
import com.manleysoftware.michael.discgolfapp.R;

/**
 * Created by Michael on 6/23/2016.
 */
public class AddPlayerMenuActivity extends AppCompatActivity {

    //region Private Fields

    //UI References
    private EditText etName;

	//Model References
    private PlayerRepository playerRepository;
    private Course course;

    //endregion

    //region Android Product Lifecycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_player_menu_layout);

        //Setup Model
        tryRestorePlayerStorageObj(); //using serializable and bundle
        tryRestoreCourseObject();

        etName = (EditText) findViewById(R.id.etName);
		Button btnSavePlayer = (Button) findViewById(R.id.btnSavePlayer);

        //Button Handlers
        btnSavePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                if ( Player.isValidPlayerName(name) && isUniqueName(name)){
                    Player p = new Player(name);
                    playerRepository.AddPlayerToStorage(p);
                    playerRepository.SaveToFile(getApplicationContext());

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
        if (playerRepository.getStoredPlayersCount() > 0) {
            for (Player player : playerRepository.getPlayerStorageListArray()) {
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
            playerRepository = (PlayerRepository) b.getSerializable("PlayerRepository");
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
