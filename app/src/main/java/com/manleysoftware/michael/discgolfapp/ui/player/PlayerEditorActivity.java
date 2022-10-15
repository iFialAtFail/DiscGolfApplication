package com.manleysoftware.michael.discgolfapp.ui.player;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.manleysoftware.michael.discgolfapp.Application.AlreadyExistsException;
import com.manleysoftware.michael.discgolfapp.data.Model.Course;
import com.manleysoftware.michael.discgolfapp.data.Model.Player;
import com.manleysoftware.michael.discgolfapp.data.filerepository.PlayerFileRepository;
import com.manleysoftware.michael.discgolfapp.R;
import com.manleysoftware.michael.discgolfapp.data.PlayerRepository;

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
                        playerRepository.add(player, context);
                    } catch (AlreadyExistsException whoops){
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

    private void initializePlayerRepository() {
        this.playerRepository = new PlayerFileRepository(this);
    }
}
