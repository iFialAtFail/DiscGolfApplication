package com.manleysoftware.michael.discgolfapp.ui.player;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.manleysoftware.michael.discgolfapp.R;
import com.manleysoftware.michael.discgolfapp.application.CourseNotFoundException;
import com.manleysoftware.michael.discgolfapp.data.PlayerRepository;
import com.manleysoftware.michael.discgolfapp.domain.Course;
import com.manleysoftware.michael.discgolfapp.domain.Player;
import com.manleysoftware.michael.discgolfapp.domain.Players;
import com.manleysoftware.michael.discgolfapp.ui.adapters.MultiplePlayerDataAdapter;
import com.manleysoftware.michael.discgolfapp.ui.course.legacy.CoursePickerActivity;
import com.manleysoftware.michael.discgolfapp.ui.main.RuntimeGameActivity;
import com.manleysoftware.michael.discgolfapp.ui.player.legacy.PlayerEditorActivity;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Created by Michael on 7/10/2016.
 */
@AndroidEntryPoint
public class RoundPlayerPickerActivity extends AppCompatActivity {

    private static final String PLAYERS_SELECTED_KEY = "PLAYERS_SELECTED_KEY";
    private static final String NEW_PLAYER_BOOL = "NEW_PLAYER_BOOL";

    private MultiplePlayerDataAdapter adapter;

    @Inject
    protected PlayerRepository playerRepository;

    private final Context context = this;
    private ListView lvPlayerList;
    private boolean addedNewPlayer = false;

	private Course selectedCourse;
	private List<Player> allPlayers;
	private PlayersSelected selectedPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_picker_layout);


        initializePlayers();
        getSelectedCourse();

        handleSavedInstanceRestoration(savedInstanceState);

        lvPlayerList = findViewById(R.id.lvPlayerList);
		RelativeLayout playerPickerRelativeLayout = findViewById(R.id.playerPickerRelativeLayout);

        if (!setupPlayersListView()){ //if we don't have any players or deserialization didn't work...
			View messageLayout = getLayoutInflater().inflate(R.layout.listview_alternative_layout,null);

			ImageView backgroundImage = messageLayout.findViewById(R.id.ivImage);
			Bitmap bm5 = BitmapFactory
					.decodeResource(context.getResources(), R.drawable.jade_500x500);
			backgroundImage.setImageBitmap(bm5);

			TextView tvNoListViewMessage = messageLayout.findViewById(R.id.tvNoListViewMessage);
			tvNoListViewMessage.setText("Oops! No players here yet!\nPlease add some players.");
			playerPickerRelativeLayout.addView(messageLayout);
		}
		if (addedNewPlayer){
		    addedNewPlayer = false;
		    selectedPlayers.unselectAllPlayers();
        }
        lvPlayerList.setOnItemClickListener((parent, view, position, id) -> {
            CheckedTextView check = view.findViewById(R.id.tvPlayerName);
            selectedPlayers.togglePlayer(position);
            check.setChecked(selectedPlayers.isPlayerSelected(position));
        });



        lvPlayerList.setOnItemLongClickListener((parent, view, position, id) -> {
            AlertDialog.Builder aat = new AlertDialog.Builder(context);
            aat.setTitle("Delete?")
                    .setMessage("Are you sure you want to delete "+parent.getItemAtPosition(position).toString()+"?")
                    .setCancelable(true)
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                    .setPositiveButton("Delete", (dialog, which) -> {
                        //Make the change
                        Player playerToDelete = (Player)adapter.getItem(position);
                        playerRepository.delete(playerToDelete, context);

                        //Commit the change to persistant memory
                        adapter.notifyDataSetChanged();
                        if (adapter.getCount() == 0){
                            recreate();
                        }
                    });
            AlertDialog art = aat.create();

            art.show();
            return true;
        });
    }

    private void handleSavedInstanceRestoration(Bundle savedInstanceState) {
        if (savedInstanceState != null){
            restoreSelectedPlayersFromSavedInstanceState(savedInstanceState);
            addedNewPlayer = savedInstanceState.getBoolean(NEW_PLAYER_BOOL);
        } else{
            selectedPlayers = new PlayersSelected(allPlayers.size());
        }
    }

    private void restoreSelectedPlayersFromSavedInstanceState(Bundle savedInstanceState) {
        selectedPlayers = (PlayersSelected) savedInstanceState.getSerializable(PLAYERS_SELECTED_KEY);
        handlePlayerCountChange();
    }

    private void handlePlayerCountChange() {
        if (selectedPlayers.getPlayersSelected().length != allPlayers.size()){
            selectedPlayers = new PlayersSelected(allPlayers.size());
        }
    }


    //region Button Handling

    public void onNewGameClicked(View v){
        Players players = new Players();

        for (int i = 0; i < allPlayers.size(); i++){
            if (selectedPlayers.isPlayerSelected(i)){
                players.add((Player)lvPlayerList.getItemAtPosition(i));
            }
        }

        if (players.isEmpty()) {
            Toast.makeText(getApplicationContext(),"No players chosen, please select a player and try again.",Toast.LENGTH_LONG).show();
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable("Course",selectedCourse);
        bundle.putSerializable("Players", players);

        Intent intent = new Intent(getApplicationContext(), RuntimeGameActivity.class);
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
        if (playerRepository != null && allPlayers.size() > 0){
            lvPlayerList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            adapter = new MultiplePlayerDataAdapter(context, allPlayers, selectedPlayers);
            lvPlayerList.setAdapter(adapter);
			return true;
        }
		return false;
    }



    private void initializePlayers() {
        allPlayers = playerRepository.getAllPlayers();
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
