package com.manleysoftware.michael.discgolfapp.ui.player;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.manleysoftware.michael.discgolfapp.data.Model.Player;
import com.manleysoftware.michael.discgolfapp.data.filerepository.PlayerFileRepository;
import com.manleysoftware.michael.discgolfapp.R;
import com.manleysoftware.michael.discgolfapp.data.PlayerRepository;

import java.util.List;

/**
 * Created by Michael on 8/30/2016.
 */
public class EditExistingPlayerActivity extends AppCompatActivity {

	//region Constants

	private final static String PLAYER_NAME = "Player Name";

	//endregion

	//region Private Fields

	private PlayerRepository playerRepository;
	private Player playerToEdit;
	private EditText etPlayerName;
	private Context context;
	private List<Player> allPlayers;


	//endregion


	//region Android Product Lifecycle

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_player_menu_layout);
		context = this;

		etPlayerName = (EditText) findViewById(R.id.etName);
		Button btnSavePlayer = (Button) findViewById(R.id.btnSavePlayer);

		setupPlayerStorage();
		setupPlayerToEdit();

		if (savedInstanceState == null){
			etPlayerName.setText(playerToEdit.getName());
		} else {
			etPlayerName.setText(savedInstanceState.getString(PLAYER_NAME));
		}

		btnSavePlayer.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				savePlayerButtonClickHandler();
			}
		});
	}

	private void savePlayerButtonClickHandler() {
		String name = getTrimmedName();
		playerToEdit.setName(name);
		playerRepository.update(playerToEdit,context);
		Intent intent = new Intent(context, PlayerListActivity.class);
		startActivity(intent);
	}

	@NonNull
	private String getTrimmedName() {
		String rawName = etPlayerName.getText().toString();
		return rawName.trim();
	}

	//endregion

	//region Overridden Methods

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(PLAYER_NAME, etPlayerName.getText().toString());
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		etPlayerName.setText(savedInstanceState.getString(PLAYER_NAME));
	}

	//endregion

	//region Private Helper Methods
	private void setupPlayerStorage(){
		playerRepository = new PlayerFileRepository(context);
		allPlayers = playerRepository.getAllPlayers();
	}

	private void setupPlayerToEdit(){
		Bundle b = this.getIntent().getExtras();
		if (b != null){
			int position = b.getInt("Position");
			playerToEdit = allPlayers.get(position);
		}
	}


	//endregion
}
