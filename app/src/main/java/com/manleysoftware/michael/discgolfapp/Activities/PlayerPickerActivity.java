package com.manleysoftware.michael.discgolfapp.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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

import com.manleysoftware.michael.discgolfapp.Adapters.MultiplePlayerDataAdapter;
import com.manleysoftware.michael.discgolfapp.Model.Course;
import com.manleysoftware.michael.discgolfapp.Model.Player;
import com.manleysoftware.michael.discgolfapp.Model.PlayerStorage;
import com.manleysoftware.michael.discgolfapp.R;

import java.util.ArrayList;

/**
 * Created by Michael on 7/10/2016.
 */
public class PlayerPickerActivity extends AppCompatActivity {

    private static final int PLAYER_PICKER_INTENT = 3;

    private MultiplePlayerDataAdapter adapter;
    private PlayerStorage playerStorage;
    private Context context = this;
    private ListView lvPlayerList;
	private RelativeLayout playerPickerRelativeLayout;
    private ArrayList<Player> playersPlaying;


    private Course selectedCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_picker_layout);


        setupPlayerStorage();
        tryCourseRetrieval();

        lvPlayerList = (ListView) findViewById(R.id.lvPlayerList);
		playerPickerRelativeLayout = (RelativeLayout) findViewById(R.id.playerPickerRelativeLayout);

        if (!setupPlayersListView()){
			View messageLayout = getLayoutInflater().inflate(R.layout.listview_alternative_layout,null);

			ImageView backgroundImage = (ImageView) messageLayout.findViewById(R.id.ivImage);
			Bitmap bm5 = BitmapFactory
					.decodeResource(context.getResources(), R.drawable.jade_500x500);
			backgroundImage.setImageBitmap(bm5);

			TextView tvNoListViewMessage = (TextView) messageLayout.findViewById(R.id.tvNoListViewMessage);
			tvNoListViewMessage.setText("Oops! No players here yet!\nPlease add some players.");
			playerPickerRelativeLayout.addView(messageLayout);
		}
        lvPlayerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckedTextView check = (CheckedTextView)view.findViewById(R.id.tvPlayerName);
                check.toggle();

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
                                playerStorage.DeletePlayerFromStorage(position);

                                //Commit the change to persistant memory
                                playerStorage.SaveToFile(context);
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


    //region Button Handling

    public void onNewGameClicked(View v){
        playersPlaying = new ArrayList<>();

        SparseBooleanArray sparseBooleanArray = lvPlayerList.getCheckedItemPositions();

        for (int i = 0; i < playerStorage.getStoredPlayersCount(); i++){
            if (sparseBooleanArray.get(i)){
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
        Intent intent = new Intent(getApplicationContext(), AddPlayerMenuActivity.class);
        Bundle bundle = new Bundle();

        bundle.putInt("PlayerKey",PLAYER_PICKER_INTENT);
        bundle.putSerializable("PlayerStorage", playerStorage);
        bundle.putSerializable("Course", selectedCourse);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //endregion

    //region Private Helper Methods

    private boolean setupPlayersListView(){
        if (playerStorage != null && playerStorage.getStoredPlayersCount() > 0){
            lvPlayerList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            adapter = new MultiplePlayerDataAdapter(context, playerStorage.getPlayerStorageListArray());
            lvPlayerList.setAdapter(adapter);
			return true;
        }
		return false;
    }

    private void setupPlayerStorage() {
        playerStorage = PlayerStorage.LoadPlayerStorage(context);
        if (playerStorage == null){
            playerStorage = new PlayerStorage();
        }
    }




    private void tryCourseRetrieval(){
        Bundle b = this.getIntent().getExtras();
        if (b != null){
            selectedCourse = (Course)b.getSerializable("Course");
        }
    }

    //endregion

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(context,CoursePickerActivity.class);
        startActivity(intent);
    }
}
