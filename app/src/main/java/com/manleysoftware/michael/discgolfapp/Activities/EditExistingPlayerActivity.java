package com.manleysoftware.michael.discgolfapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.manleysoftware.michael.discgolfapp.Model.Player;
import com.manleysoftware.michael.discgolfapp.Model.PlayerStorage;
import com.manleysoftware.michael.discgolfapp.R;

/**
 * Created by Michael on 8/30/2016.
 */
public class EditExistingPlayerActivity extends AppCompatActivity {

	//region Constants

	private final static String PLAYER_NAME = "Player Name";

	//endregion

	//region Private Fields

	private PlayerStorage playerStorage;
	private Player playerToEdit;
	private EditText etPlayerName;
	private Context context;


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
				playerToEdit.setName(etPlayerName.getText().toString());
				playerStorage.SaveToFile(context);
				Intent intent = new Intent(context,PlayerEditorActivity.class);
				startActivity(intent);
			}
		});
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
		Bundle b = this.getIntent().getExtras();
		if (b != null && b.getSerializable("PlayerStorage") instanceof PlayerStorage){
			playerStorage = (PlayerStorage) b.getSerializable("PlayerStorage");
		}
	}

	private void setupPlayerToEdit(){
		Bundle b = this.getIntent().getExtras();
		if (b != null){
			int position = b.getInt("Position");
			playerToEdit = playerStorage.getPlayerStorageListArray().get(position);
		}
	}


	//endregion
}
