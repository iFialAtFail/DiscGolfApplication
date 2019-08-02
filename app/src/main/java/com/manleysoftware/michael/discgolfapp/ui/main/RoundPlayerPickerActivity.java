package com.manleysoftware.michael.discgolfapp.ui.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.manleysoftware.michael.discgolfapp.Application.PlayersSelected;
import com.manleysoftware.michael.discgolfapp.data.Model.*;
import com.manleysoftware.michael.discgolfapp.Application.CourseNotFoundException;
import com.manleysoftware.michael.discgolfapp.ui.Adapters.MultiplePlayerDataAdapter;
import com.manleysoftware.michael.discgolfapp.data.filerepository.PlayerFileRepository;
import com.manleysoftware.michael.discgolfapp.R;
import com.manleysoftware.michael.discgolfapp.ui.player.PlayerEditorActivity;

import java.util.ArrayList;

/**
 * Created by Michael on 7/10/2016.
 */
public class RoundPlayerPickerActivity extends AppCompatActivity {

    public static final int PLAYER_PICKER_INTENT = 3;
    private static final String PLAYERS_SELECTED_KEY = "PLAYERS_SELECTED_KEY";
    private static final String NEW_PLAYER_BOOL = "NEW_PLAYER_BOOL";

    private MultiplePlayerDataAdapter adapter;
    private PlayerFileRepository playerRepository;
    private final Context context = this;
    private ListView lvPlayerList;
    private boolean addedNewPlayer = false;

	private Course selectedCourse;
	private PlayersSelected selectedPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_picker_layout);


        initializePlayerStorage();
        getSelectedCourse();

        handleSavedInstanceRestoration(savedInstanceState);

        lvPlayerList = (ListView) findViewById(R.id.lvPlayerList);
		RelativeLayout playerPickerRelativeLayout = (RelativeLayout) findViewById(R.id.playerPickerRelativeLayout);

        if (!setupPlayersListView()){ //if we don't have any players or deserialization didn't work...
			View messageLayout = getLayoutInflater().inflate(R.layout.listview_alternative_layout,null);

			ImageView backgroundImage = (ImageView) messageLayout.findViewById(R.id.ivImage);
			Bitmap bm5 = BitmapFactory
					.decodeResource(context.getResources(), R.drawable.jade_500x500);
			backgroundImage.setImageBitmap(bm5);

			TextView tvNoListViewMessage = (TextView) messageLayout.findViewById(R.id.tvNoListViewMessage);
			tvNoListViewMessage.setText("Oops! No players here yet!\nPlease add some players.");
			playerPickerRelativeLayout.addView(messageLayout);
		}
		if (addedNewPlayer){
		    addedNewPlayer = false;
		    selectedPlayers.unselectAllPlayers();
        }
        lvPlayerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckedTextView check = (CheckedTextView)view.findViewById(R.id.tvPlayerName);
                selectedPlayers.togglePlayer(position);
                check.setChecked(selectedPlayers.isPlayerSelected(position));
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
                                dialog.cancel();
                            }

                        })
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Make the change
                                Player playerToDelete = (Player)adapter.getItem(position);
                                playerRepository.removePlayer(playerToDelete);

                                //Commit the change to persistant memory
                                playerRepository.Save(context);
								adapter.notifyDataSetChanged();
								if (adapter.getCount() == 0){
									recreate();
								}
                            }
                        });
                AlertDialog art = aat.create();

                art.show();
                return true;
            }
        });
    }

    private void handleSavedInstanceRestoration(Bundle savedInstanceState) {
        if (savedInstanceState != null){
            restoreSelectedPlayersFromSavedInstanceState(savedInstanceState);
            addedNewPlayer = savedInstanceState.getBoolean(NEW_PLAYER_BOOL);
        } else{
            selectedPlayers = new PlayersSelected(playerRepository.getPlayers().size());
        }
    }

    private void restoreSelectedPlayersFromSavedInstanceState(Bundle savedInstanceState) {
        selectedPlayers = (PlayersSelected) savedInstanceState.getSerializable(PLAYERS_SELECTED_KEY);
        handlePlayerCountChange();
    }

    private void handlePlayerCountChange() {
        if (selectedPlayers.getPlayersSelected().length != playerRepository.getPlayers().size()){
            selectedPlayers = new PlayersSelected(playerRepository.getPlayers().size());
        }
    }


    //region Button Handling

    public void onNewGameClicked(View v){
		ArrayList<Player> playersPlaying = new ArrayList<>();

        for (int i = 0; i < playerRepository.getPlayers().size(); i++){
            if (selectedPlayers.isPlayerSelected(i)){
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
        Intent intent = new Intent(getApplicationContext(), PlayerEditorActivity.class);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        addedNewPlayer = true;
        recreate();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(PLAYERS_SELECTED_KEY,selectedPlayers);
        outState.putBoolean(NEW_PLAYER_BOOL, addedNewPlayer);
        super.onSaveInstanceState(outState);
    }

    //endregion

    //region Private Helper Methods

    private boolean setupPlayersListView(){
        if (playerRepository != null && playerRepository.getPlayers().size() > 0){
            lvPlayerList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            adapter = new MultiplePlayerDataAdapter(context, playerRepository.getPlayers(), selectedPlayers);
            lvPlayerList.setAdapter(adapter);
			return true;
        }
		return false;
    }



    private void initializePlayerStorage() {
        playerRepository = new PlayerFileRepository(context);
    }




    private void getSelectedCourse(){
        Bundle b = this.getIntent().getExtras();
        if (b != null){
            selectedCourse = (Course)b.getSerializable("Course");
        } else{
            throw new CourseNotFoundException();
        }
    }

    //endregion

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(context, CoursePickerActivity.class);
        startActivity(intent);
    }
}
