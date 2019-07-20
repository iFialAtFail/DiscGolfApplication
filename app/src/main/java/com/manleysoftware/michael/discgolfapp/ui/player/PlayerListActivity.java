package com.manleysoftware.michael.discgolfapp.ui.player;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.manleysoftware.michael.discgolfapp.BuildConfig;
import com.manleysoftware.michael.discgolfapp.data.Model.Player;
import com.manleysoftware.michael.discgolfapp.data.filerepository.PlayerFileRepository;
import com.manleysoftware.michael.discgolfapp.R;
import com.manleysoftware.michael.discgolfapp.data.PlayerRepository;
import com.manleysoftware.michael.discgolfapp.ui.Adapters.PlayerDataAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.manleysoftware.michael.discgolfapp.ui.main.MainMenuActivity;

/**
 * Created by Michael on 6/23/2016.
 */
public class PlayerListActivity extends AppCompatActivity {

    //region Private Fields

	private ListView lvPlayerList;
	private PlayerRepository playerRepository;
    private final Context context =  this;
    private PlayerDataAdapter adapter;

    //endregion

    //region Android Lifecycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_editor);

        if (BuildConfig.FLAVOR.equals("free")) {
            AdView adView = (AdView) findViewById(R.id.adViewPE);
            AdRequest request = new AdRequest.Builder().build();
//                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//                    .build();
			if (adView != null)
	            adView.loadAd(request);
        }


        setupPlayerStorage();

        lvPlayerList = (ListView) findViewById(R.id.lvPlayerList);
		RelativeLayout playerEditorRelativelayout = (RelativeLayout) findViewById(R.id.playerEditorRelativeLayout);

        if (!setupPlayerListView()){
			View messageLayout = getLayoutInflater().inflate(R.layout.listview_alternative_layout,null);

			ImageView backgroundImage = (ImageView) messageLayout.findViewById(R.id.ivImage);
			Bitmap bm5 = BitmapFactory
					.decodeResource(context.getResources(), R.drawable.jade_500x500);
			backgroundImage.setImageBitmap(bm5);

			TextView tvNoListViewMessage = (TextView) messageLayout.findViewById(R.id.tvNoListViewMessage);
			tvNoListViewMessage.setText("Oops! No players here yet!\nPlease add some players.");
			playerEditorRelativelayout.addView(messageLayout);
		}

        lvPlayerList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           final int position, long id) {
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
                                Player playerToRemove = (Player)adapter.getItem(position);
                                playerRepository.removePlayer(playerToRemove);

                                //Commit the change to persistant memory
                                playerRepository.Save(context);
								adapter.notifyDataSetChanged();
								if (adapter.getCount()== 0){
									recreate();
								}
                            }
                        });
                AlertDialog art = aat.create();

                art.show();
                return true;

            }

        });

		lvPlayerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(context, EditExistingPlayerActivity.class);
				Bundle b = new Bundle();
				b.putInt("Position", position);
				intent.putExtras(b);
				startActivity(intent);
			}
		});

		Button btnNewPlayer = (Button) findViewById(R.id.btnNewPlayer);
        btnNewPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PlayerEditorActivity.class );
                startActivity(intent);
            }
        });
    }

    //endregion

    //region Overriding normal behaviours

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainMenuActivity.class));
        finish();

    }

    //endregion

    //region Private helper methods

    private boolean setupPlayerListView() {
        if (playerRepository != null && playerRepository.getPlayers().size() > 0) {
            adapter = new PlayerDataAdapter(context, playerRepository.getPlayers());
            lvPlayerList.setAdapter(adapter);
            return true;
        }
		return false;
    }

    private void setupPlayerStorage() {
        playerRepository = new PlayerFileRepository(context);
    }

    //endregion
}
