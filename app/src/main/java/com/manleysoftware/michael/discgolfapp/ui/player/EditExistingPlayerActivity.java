package com.manleysoftware.michael.discgolfapp.ui.player;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.manleysoftware.michael.discgolfapp.R;
import com.manleysoftware.michael.discgolfapp.data.PlayerRepository;
import com.manleysoftware.michael.discgolfapp.domain.Player;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Created by Michael on 8/30/2016.
 */
@AndroidEntryPoint
public class EditExistingPlayerActivity extends AppCompatActivity {

    private final static String PLAYER_NAME = "Player Name";

    @Inject
    protected PlayerRepository playerRepository;

    private Player playerToEdit;
    private EditText etPlayerName;
    private Context context;
    private List<Player> allPlayers;
    private static final String TAG = "EDIT_EXISTING_ACTIVITY";
    private Toast toast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_player_menu_layout);
        context = this;

        etPlayerName = findViewById(R.id.etName);
        Button btnSavePlayer = findViewById(R.id.btnSavePlayer);

        allPlayers = playerRepository.getAllPlayers();
        setupPlayerToEdit();

        if (savedInstanceState == null) {
            etPlayerName.setText(playerToEdit.getName());
        } else {
            etPlayerName.setText(savedInstanceState.getString(PLAYER_NAME));
        }

        btnSavePlayer.setOnClickListener(v -> savePlayerButtonClickHandler());
    }

    private void savePlayerButtonClickHandler() {
        String name = getTrimmedName();
        Log.d(TAG, "Player to update: " + playerToEdit.toString() + " with new name: " + name);
        String oldName = playerToEdit.getName();
        playerToEdit.setName(name);
        try {
            playerRepository.update(playerToEdit, context);
        } catch (IllegalStateException ex) {
            shoNotUniqueNameMessage(playerToEdit);
            //Restore old name so there's less confusion.
            etPlayerName.setText(oldName);
            etPlayerName.selectAll();
            return;
        }
        Intent intent = new Intent(context, PlayerListActivity.class);
        startActivity(intent);
    }

    private void shoNotUniqueNameMessage(Player player) {
        if (toast != null) toast.cancel();
        toast = Toast.makeText(context, "Player: " + player.getName() + " exists already", Toast.LENGTH_LONG);
        toast.show();
    }

    @NonNull
    private String getTrimmedName() {
        String rawName = etPlayerName.getText().toString();
        return rawName.trim();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(PLAYER_NAME, etPlayerName.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        etPlayerName.setText(savedInstanceState.getString(PLAYER_NAME));
    }

    private void setupPlayerToEdit() {
        Bundle b = this.getIntent().getExtras();
        if (b != null) {
            int position = b.getInt("Position");
            Log.d(TAG, "Position: " + position);
            playerToEdit = allPlayers.get(position);
        }
    }
}
