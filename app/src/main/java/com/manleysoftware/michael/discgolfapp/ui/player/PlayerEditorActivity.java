package com.manleysoftware.michael.discgolfapp.ui.player;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.manleysoftware.michael.discgolfapp.Application.PlayerExistsAlreadyException;
import com.manleysoftware.michael.discgolfapp.data.Model.Course;
import com.manleysoftware.michael.discgolfapp.data.Model.Player;
import com.manleysoftware.michael.discgolfapp.data.filerepository.PlayerFileRepository;
import com.manleysoftware.michael.discgolfapp.R;
import com.manleysoftware.michael.discgolfapp.data.PlayerRepository;
import com.manleysoftware.michael.discgolfapp.ui.main.RoundPlayerPickerActivity;

import static com.manleysoftware.michael.discgolfapp.ui.main.RoundPlayerPickerActivity.PLAYER_PICKER_INTENT;

/**
 * Created by Michael on 6/23/2016.
 */
public class PlayerEditorActivity extends AppCompatActivity {

    //region Private Fields

    //UI References
    private EditText etName;
    private Context context;

	//Model References
    private PlayerRepository playerRepository;
    private Course course;

    //endregion

    //region Android Product Lifecycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_player_menu_layout);
        context = this;

        //Setup Model
        initializePlayerRepository(); //using serializable and bundle
        //tryRestoreCourseObject();

        etName = (EditText) findViewById(R.id.etName);
		Button btnSavePlayer = (Button) findViewById(R.id.btnSavePlayer);

        //Button Handlers
        btnSavePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rawName = etName.getText().toString();
                String name = rawName.trim();

                if (Player.isValidPlayerName(name)){
                    Player player = new Player(name);
                    try {
                        playerRepository.addPlayer(player);
                        playerRepository.Save(getApplicationContext());
                    } catch (PlayerExistsAlreadyException whoops){
                        showPlayerAlreadyExistsToast(player);
                        return;
                    }
                    finish();
                }

                else{
                    Toast toast = Toast.makeText(getApplicationContext(),"Invalid input or duplicate user. Please try again.", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }

    private void showPlayerAlreadyExistsToast(Player player) {
        Toast toast = Toast.makeText(context, "Player: " + player.getName() + " exists already",Toast.LENGTH_LONG);
        toast.show();
    }

    //endregion

    //region Private Helper Methods


//    private void gotoPriorActivity(){
//        Bundle b = this.getIntent().getExtras();
//        if (b != null && b.getInt("PlayerKey")== PLAYER_PICKER_INTENT){ //if coming from player picker...
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("Course",course);
//            Intent intent = new Intent(getApplicationContext(), RoundPlayerPickerActivity.class);
//            intent.putExtras(bundle);
//            startActivity(intent);
//        }
//        else{ //or if coming from player editor...
//            Intent intent = new Intent(getApplicationContext(), PlayerListActivity.class);
//            startActivity(intent);
//        }
//    }

    private void initializePlayerRepository() {
        this.playerRepository = new PlayerFileRepository(this);
    }
//    private void tryRestoreCourseObject(){
//        Bundle bundle = this.getIntent().getExtras();
//        if (bundle != null){
//            course = (Course) bundle.getSerializable("Course");
//        }
//    }
    //endregion
}
