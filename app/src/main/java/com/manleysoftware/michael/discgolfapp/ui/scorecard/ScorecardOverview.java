package com.manleysoftware.michael.discgolfapp.ui.scorecard;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.manleysoftware.michael.discgolfapp.data.Model.ScoreCard;
import com.manleysoftware.michael.discgolfapp.R;
import com.manleysoftware.michael.discgolfapp.feature.exportdata.ExcelExporter;
import com.manleysoftware.michael.discgolfapp.ui.utils.TableGenerator;

/**
 * Created by Michael on 8/22/2016.
 */
public class ScorecardOverview extends AppCompatActivity {
	private Context context;
	private TableLayout tblFinalScorecard;
	private ScoreCard scoreCard;
	private RelativeLayout parentLayout;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scorecard_finished_overview_layout);
		context = this;

		parentLayout = (RelativeLayout) findViewById(R.id.finishedScorecardParentLayout);
		tblFinalScorecard = (TableLayout) findViewById(R.id.tblFinalScorecard);
		TextView tvFSCourseTitle = (TextView) findViewById(R.id.tvFSCourseTitle);

		initScoreCardData();

		TableGenerator tableGenerator = new TableGenerator();
		tblFinalScorecard = tableGenerator.generateTable(context,tblFinalScorecard,scoreCard);
		tvFSCourseTitle.setText(scoreCard.getCourseName());


	}

	private void initScoreCardData(){
		Intent intent = getIntent();
		Bundle b = intent.getExtras();
		if (b != null){
			scoreCard = (ScoreCard) b.getSerializable("Finished Game");
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
			case R.id.action_export_excel:
				exportToExcel();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}

	}

	private void exportToExcel() {
		if (!checkForPermissions()){
			if (!checkForPermissions())
				return;
		}

		ExcelExporter.exportScorecard(scoreCard,context);
	}

	private boolean checkForPermissions() {
		// Here, thisActivity is the current activity
		if (ContextCompat.checkSelfPermission(this,
				Manifest.permission.WRITE_EXTERNAL_STORAGE)
				!= PackageManager.PERMISSION_GRANTED) {

			// Permission is not granted
			// Should we show an explanation?
			if (ActivityCompat.shouldShowRequestPermissionRationale(this,
					Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
				// Show an explanation to the user *asynchronously* -- don't block
				// this thread waiting for the user's response! After the user
				// sees the explanation, try again to request the permission.
			} else {
				// No explanation needed; request the permission
				ActivityCompat.requestPermissions(this,
						new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
						3);

				// MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
				// app-defined int constant. The callback method gets the
				// result of the request.
			}
			return false;
		} else {
			return true;
			// Permission has already been granted
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.scorecard_overview_menu,menu);

		return true;
	}

}
