package com.manleysoftware.michael.discgolfapp.ui.scorecard;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.manleysoftware.michael.discgolfapp.data.Model.ScoreCard;
import com.manleysoftware.michael.discgolfapp.R;
import com.manleysoftware.michael.discgolfapp.feature.exportdata.ExcelExporter;
import com.manleysoftware.michael.discgolfapp.feature.exportdata.ExportPdf;
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

//	private void generateTable(){
//		tblFinalScorecard.removeAllViews();
//
//		//Generate dynamic table.
//		for (int i = 0; i < scoreCard.getPlayersCount() + 2; i++){ //Plus 2 for Par And Hole # rows.
//			// outer for loop
//			TableRow row = new TableRow(context);
//			row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
//					TableRow.LayoutParams.WRAP_CONTENT));
//			// inner for loop
//			for (int j = 0; j < scoreCard.getCourse().getHoleCount() + 2; j++) { // Plus 2 for Name and Total Columns
//
//				int LAYOUT_WIDTH = 50;
//				int FIRST_COLUMN_WIDTH = 80;
//				if (i == 0 && j == 0){ //First Cell is Hole
//					TextView tv = setupTextViewInTable("Hole",applyLayoutWidth(FIRST_COLUMN_WIDTH), R.drawable.cell_shape_light_green);
//					row.addView(tv);
//				}
//
//				else if (i == 0 && j == scoreCard.getCourse().getHoleCount() + 1){ //Last Cell is T
//					TextView tv = setupTextViewInTable("T",applyLayoutWidth(LAYOUT_WIDTH), R.drawable.cell_shape_light_green);
//					row.addView(tv);
//				}
//
//				else if (i == 0){
//					TextView tv = setupTextViewInTable(String.valueOf(j),applyLayoutWidth(LAYOUT_WIDTH), R.drawable.cell_shape_light_green);
//					row.addView(tv);
//				}
//
//				if (i == 1 && j == 0){ // First Cell in second row is "Par"
//					TextView tv = setupTextViewInTable("Par",applyLayoutWidth(FIRST_COLUMN_WIDTH), R.drawable.cell_shape_light_green);
//					row.addView(tv);
//				}
//
//				else if (i == 1 && j == scoreCard.getCourse().getHoleCount() + 1){ // Last Cell is Par Total
//					TextView tv = setupTextViewInTable(String.valueOf(scoreCard.getCourse().getParTotal()),applyLayoutWidth(LAYOUT_WIDTH), R.drawable.cell_shape_light_green);
//					row.addView(tv);
//				}
//
//				else if (i == 1 && j != scoreCard.getCourse().getHoleCount()+1){
//					TextView tv = setupTextViewInTable(String.valueOf(scoreCard.getCourse().getParArray()[j-1]),applyLayoutWidth(LAYOUT_WIDTH), R.drawable.cell_shape_light_green);
//					row.addView(tv);
//				}
//
//				if (i >= 2 && j == 0){ //Set Player Names
//					TextView tv = setupTextViewInTable(scoreCard.getPlayerArray()[i-2].getName(),applyLayoutWidth(FIRST_COLUMN_WIDTH), R.drawable.cell_shape);
//					row.addView(tv);
//				}
//
//				if (i >= 2 && j >0 && j != scoreCard.getCourse().getHoleCount()+1){ //handle player scores
//					TextView tv = setupTextViewInTable(String.valueOf(scoreCard.getPlayerArray()[i-2].getScore()[j-1]),applyLayoutWidth(LAYOUT_WIDTH), R.drawable.cell_shape);
//					row.addView(tv);
//				}
//
//				if (i >= 2 && j == scoreCard.getCourse().getHoleCount() + 1){
//					TextView tv = setupTextViewInTable(String.valueOf(scoreCard.getPlayerArray()[i-2].getCurrentTotal()),applyLayoutWidth(LAYOUT_WIDTH), R.drawable.cell_shape);
//					row.addView(tv);
//				}
//
//
//			}
//			tblFinalScorecard.addView(row);
//		}
//	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
			case R.id.action_settings:
				exportToPdf();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}

	}

	private void exportToPdf() {
		if (!checkForPermissions())return;
		ExcelExporter.exportScorecard(scoreCard,context);
		//ExportPdf exportPdf = new ExportPdf();
		//exportPdf.exportUsingBitmap(parentLayout,context);
		//exportPdf.exportScorecard(scoreCard,tblFinalScorecard, context);
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


//	private TextView setupTextViewInTable(String setText, int layoutWidthParam, int resourceID ){
//		TextView tv = new TextView(context);
//		tv.setLayoutParams(new TableRow.LayoutParams(layoutWidthParam,
//				TableRow.LayoutParams.MATCH_PARENT, 1.0f));
//		tv.setBackgroundResource(resourceID);
//		tv.setTextColor(Color.BLACK);
//		//tv.setTextAppearance(context,android.R.style.TextAppearance_Large);
//		int TEXT_SIZE = 20;
//		tv.setTextSize(TEXT_SIZE);
//		tv.setGravity(Gravity.CENTER);
//		tv.setPadding(15, 5, 15, 5);
//		tv.setText(setText);
//		tv.setSingleLine();
//		return tv;
//	}
//
//	private int applyLayoutWidth(int width){
//		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, getResources().getDisplayMetrics());
//	}
}
