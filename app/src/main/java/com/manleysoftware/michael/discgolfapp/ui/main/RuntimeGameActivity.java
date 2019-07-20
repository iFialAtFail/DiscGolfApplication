package com.manleysoftware.michael.discgolfapp.ui.main;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.manleysoftware.michael.discgolfapp.ui.customViews.ObservableHorizontalScrollView;
import com.manleysoftware.michael.discgolfapp.ui.customViews.ObservableScrollView;
import com.manleysoftware.michael.discgolfapp.ui.customViews.Interfaces.IHorizontalScrollViewListener;
import com.manleysoftware.michael.discgolfapp.ui.customViews.Interfaces.IScrollViewListener;
import com.manleysoftware.michael.discgolfapp.data.Model.Course;
import com.manleysoftware.michael.discgolfapp.data.Model.Player;
import com.manleysoftware.michael.discgolfapp.data.Model.ScoreCard;
import com.manleysoftware.michael.discgolfapp.data.ScorecardRepository;
import com.manleysoftware.michael.discgolfapp.R;

import java.util.ArrayList;

public class RuntimeGameActivity extends AppCompatActivity implements IScrollViewListener, IHorizontalScrollViewListener {
    public static final String RESTORE_PLAYERS_KEY = "RestorePlayers";
    public static final String RESTORE_SCORE_CARD_KEY = "RestoreScoreCard";

    //region Private Model Variables

    private Course course;
    private Player[] players;
    private ScoreCard scoreCard;
	private ScorecardRepository finishedCards;
	private ScorecardRepository unFinishedCards;
    private Context context;
	private Boolean gameStarted = false;

    //Variable to store the last red selected cell
    private TextView lastTVBackgroundChanged;

    //Cache the scoreTotal TextViews
    private TextView[] scoreTotalTextViews;

    //endregion

    //region Constants



    private final int LAYOUT_WIDTH = 50;
    private final int TOTAL_COLUMN_WIDTH = 60;

	//endregion

    //region Control/View references

	//Custom Scrollviews
    private ObservableHorizontalScrollView parHorizontalScrollView;
    private ObservableHorizontalScrollView scoreHorizontalScrollView;
    private ObservableScrollView nameScrollView;
    private ObservableScrollView scoreScrollView;
    private ObservableScrollView currentScoreSV;

    //Static in name implies a stationary view.
    //Dynimic in name implies a view wrapped in a custom scrollview.
    private TableLayout scoreTable;
    private TableLayout nameTable;
    private TableLayout dynamicHeaderTable;
    private TableLayout staticHeaderRowsTable;
    private TableLayout currentScoreTable;
    private TableLayout staticParCountTable;

    private TextView titleCourseTextView;

	//endregion

    //region Android Product Lifecycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.setTheme(R.style.NoActionBarTheme);
        setContentView(R.layout.runtime_game_layout);
        context = this;


        initializeViewElements();


		//retrieve Player/Course/scorecard data
        retrieveGameData();

        initializeScorecardRepositorys();


        //If first time go, create new stuff
        if (savedInstanceState == null) {
            initPlayers(players);
            if (scoreCard == null) //If coming from new game path...
				scoreCard = new ScoreCard(players, course);
        }else {
            players = (Player[])savedInstanceState.getSerializable(RESTORE_PLAYERS_KEY);
            scoreCard = (ScoreCard) savedInstanceState.getSerializable(RESTORE_SCORE_CARD_KEY);
        }

        scoreTotalTextViews = new TextView[players.length];
        titleCourseTextView.setText(course.getName());
        generateTables(players, course.getHoleCount());
        updateSelectedCell();


    }

    private void initializeViewElements() {
        titleCourseTextView = (TextView) findViewById(R.id.titleCourseTextView);

        scoreTable = (TableLayout) findViewById(R.id.scoreTable);
        nameTable = (TableLayout) findViewById(R.id.nameTable);
        dynamicHeaderTable = (TableLayout) findViewById(R.id.parTable);
        staticHeaderRowsTable = (TableLayout) findViewById(R.id.parCellTable);
        staticParCountTable = (TableLayout) findViewById(R.id.staticParCountTable);
        currentScoreTable = (TableLayout) findViewById(R.id.currentScoreTable);

        parHorizontalScrollView = (ObservableHorizontalScrollView) findViewById(R.id.parHorizontalScrollView);
        parHorizontalScrollView.setHorizontalScrollViewListener(this);
        scoreHorizontalScrollView = (ObservableHorizontalScrollView) findViewById(R.id.scoreHorizontalScrollView);
        scoreHorizontalScrollView.setHorizontalScrollViewListener(this);

        nameScrollView = (ObservableScrollView) findViewById(R.id.nameScrollView);
        nameScrollView.setScrollViewListener(this);
        scoreScrollView = (ObservableScrollView) findViewById(R.id.scoreScrollView);
        scoreScrollView.setScrollViewListener(this);
        currentScoreSV = (ObservableScrollView) findViewById(R.id.currentScoreSV);
        currentScoreSV.setScrollViewListener(this);
    }

    //endregion

    private void generateTables(Player[] players, int courseHoleCount){

        //Setup Static cells (Par, Hole#)
        setupStaticHeaderRows();

        //Setup Static Header Par and Holes cells
        String[] input = {"T", course.getParTotal()+ "" };
        setupStaticParCountTable(input);

        //Setup Par/Hole# Rows
        setupDynamicHeaderTable();

        setupCurrentScoreColumn(players);

        setupNameColumn(players);

        //Generate the score table. Used to refresh the view as well.
        generateScoreTable(players, courseHoleCount);


    }

    //region Button Handler Methods

    public void OnIncrementScoreClick(View v){
		gameStarted = true;
        incrementCurrentPlayerScore();
        updateScoreTable();
        updateSelectedCell();
    }

    private void incrementCurrentPlayerScore() {
        Player currentPlayer = getCurrentPlayer();
        currentPlayer.IncrementCurrentScore(scoreCard.getCurrentHole());
    }

    private Player getCurrentPlayer() {
        return scoreCard.getCurrentPlayer();
    }

    public void OnDecrementScoreClick(View v){
		gameStarted = true;
        decrementCurrentPlayerScore();
        updateScoreTable();
        updateSelectedCell();

    }

    private void decrementCurrentPlayerScore() {
        Player currentPlayer = getCurrentPlayer();
        currentPlayer.DecrementCurrentScore(scoreCard.getCurrentHole());
    }

    public void OnNextHoleClick(View v){
        scoreCard.NextHole();
        updateSelectedCell();
    }

    public void OnPreviousHoleClick(View v){
        scoreCard.PreviousHole();
        updateSelectedCell();
    }

    public void OnLastPlayerClick(View view) {
        scoreCard.LastPlayer();
        updateSelectedCell();
    }

    public void OnNxtPlayerClick(View view) {
        scoreCard.NextPlayer();
        updateSelectedCell();
    }

	public void OnSaveFinnishGameClick(View view) {
		AlertDialog.Builder aat = new AlertDialog.Builder(context);
		aat.setTitle("End Game?")
				.setMessage("If you are finished, select \"Finished\". If you want to save your game to resume later, select \"Save\"")
				.setCancelable(true)
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				})
				.setPositiveButton("Finish", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
                        saveGame();
                    }
				})
				.setNeutralButton("Save", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which){
                        saveGameAsResumable();
                    }
				});
		AlertDialog art = aat.create();

		art.show();

	}

    private void saveGameAsResumable() {
        unFinishedCards.AddScoreCardsToStorage(scoreCard);
        unFinishedCards.SaveUnFinishedCardListToFile(context);
        Intent intent = new Intent(context, MainMenuActivity.class);
        startActivity(intent);
    }

    private void saveGame() {
        finishedCards.AddScoreCardsToStorage(scoreCard);
        finishedCards.SaveFinishedCardsToFile(context);
        Intent intent = new Intent(context, MainMenuActivity.class);
        startActivity(intent);
    }

    private void retrieveGameData(){
        Bundle b = this.getIntent().getExtras();
        if (b != null){

            scoreCard = loadFromBundle(b);

			//If coming from resume game menu....
			if (scoreCard != null){
                loadDataFromScorecard();
            } else {
                loadDataFromNewGameBundle(b);
            }
        }
    }

    private void initializeScorecardRepositorys() {
        initializeUnfinishedScorecardRepository();
        initializeFinishedScorecardRepository();
    }

    private void initializeFinishedScorecardRepository() {
        finishedCards = ScorecardRepository.LoadFinishedCardStorage(context);
        if (finishedCards == null){
            finishedCards = new ScorecardRepository();
        }
    }

    private void initializeUnfinishedScorecardRepository() {
        unFinishedCards = ScorecardRepository.LoadUnFinishedCardStorage(context);
        if (unFinishedCards == null){
            unFinishedCards = new ScorecardRepository();
        }
    }

    private void loadDataFromNewGameBundle(Bundle b) {
        players = toPlayerArray((ArrayList<Player>) b.getSerializable("Players"));
        course = (Course) b.getSerializable("Course");
    }

    private void loadDataFromScorecard() {
        players = scoreCard.getPlayerArray();
        course = scoreCard.getCourse();
    }

    private ScoreCard loadFromBundle(Bundle bundle) {
        return (ScoreCard) bundle.getSerializable("Unfinished Game");
    }

    private void updateSelectedCell(){
        TextView selectedCell = getSelectedCell();
        if (selectedCell != lastTVBackgroundChanged){
            resetLastSelectedCellToNormal();
            updateSelectedCellToRed(selectedCell);
            setFocusToCell(selectedCell);
        }
    }

    private TextView getSelectedCell(){
        TableRow playerTableRow = (TableRow) scoreTable.getChildAt(scoreCard.getPlayerSelectedIndex());
        return (TextView) playerTableRow.getChildAt(scoreCard.getCurrentHole()-1);

    }

    private void setFocusToCell(TextView tv) {
        tv.getParent().requestChildFocus(tv,tv);
    }

    private void updateSelectedCellToRed(TextView tv) {
        lastTVBackgroundChanged = tv;
        lastTVBackgroundChanged.setBackgroundResource(R.drawable.cell_shape_red);
    }

    private void resetLastSelectedCellToNormal() {
        if (lastTVBackgroundChanged != null) {
            lastTVBackgroundChanged.setBackgroundResource(R.drawable.cell_shape);
        }
    }

    private void updateScoreTable(){
        //data model has been updated to reflect the current player and hole that just got incremented/decremented
        //use that data to then get the approprite tv in the score and scoreTotal tv and update them
        TextView selectedCell = getSelectedCell();
        Player currentPlayer = getCurrentPlayer();
        int score = currentPlayer.getScoreForHole(scoreCard.getCurrentHole() - 1);
        selectedCell.setText(String.valueOf(score));
        updateScoreTotalTextView();
    }

    private void updateScoreTotalTextView() {
        TextView scoreTotalTextView = scoreTotalTextViews[scoreCard.getPlayerSelectedIndex()];
        Player selectedPlayer = getCurrentPlayer();
        int totalScoreRelativeToPar = selectedPlayer.getCurrentParDifference(course.getParTotal());
        scoreTotalTextView.setText(String.valueOf(totalScoreRelativeToPar));
    }

    private Player[] toPlayerArray(ArrayList<Player> list){
        if (list == null){
            throw new IllegalArgumentException("Cannot convert null list to array!");
        }
        Player[] ret = new Player[list.size()];
        for(int i = 0;i < ret.length;i++)
            ret[i] = list.get(i);
        return ret;
    }

    private void initPlayers(Player[] players){
        for (Player player : players){
            player.StartGame(course);
        }
    }

    private TextView setupTextViewInTable(String setText, int layoutWidthParam, int resourceID ){
        TextView tv = new TextView(context);
        tv.setLayoutParams(new TableRow.LayoutParams(layoutWidthParam,
                TableRow.LayoutParams.MATCH_PARENT, 1.0f));
        tv.setBackgroundResource(resourceID);
        tv.setTextColor(Color.BLACK);
		//tv.setTextAppearance(context,android.R.style.TextAppearance_Large);
		final int TEXT_SIZE = 20;
		tv.setTextSize(TEXT_SIZE);
		tv.setGravity(Gravity.CENTER);
        tv.setPadding(15, 5, 15, 5);
        tv.setText(setText);
        return tv;
    }

    private void generateScoreTable(Player[] players, int courseHoleCount) {
        // setup score info table
        scoreTable.removeAllViews();

        //Generate dynamic table.
        for (int i = 0; i < players.length; i++){
            TableRow row = new TableRow(context);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            for (int j = 0; j < courseHoleCount; j++) {
                String scoreForHole = String.valueOf(players[i].getScore()[j]);
                TextView tv = setupTextViewInTable(
                        scoreForHole,
                        applyLayoutWidth(LAYOUT_WIDTH),
                        R.drawable.cell_shape
                );
                row.addView(tv);
            }
            scoreTable.addView(row);
        }

    }

    private void setupStaticHeaderRows(){
        if (staticHeaderRowsTable.getChildCount() < 1) {
            String[] textInput = {"Hole", "Par"};
			for (String aTextInput : textInput) {

				LinearLayout tableRow = new LinearLayout(context); //Must me linear layout, since TableRow won't actually fill parent.
				TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1f);
				tableRow.setLayoutParams(params);

				TextView tv = setupTextViewInTable(aTextInput,
						TableRow.LayoutParams.MATCH_PARENT,//Setting only width param
						R.drawable.cell_shape_light_green); //This forced width but the next line should be a quick fix for it.

				tableRow.addView(tv);
				staticHeaderRowsTable.addView(tableRow);
			}
        }
    }

    private void setupStaticParCountTable(String[] textArray){
        if (staticParCountTable.getChildCount() < 1){
            for(int i = 0; i<2; i++) {
                TableRow tr = new TableRow(context); //<-- This time Linear Layout is acting like "Wrap content". TableRow works though.
                TableRow.LayoutParams params =
                        new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 1f);
                tr.setLayoutParams(params);

                TextView tv = setupTextViewInTable(textArray[i], applyLayoutWidth(TOTAL_COLUMN_WIDTH), R.drawable.cell_shape_light_green);
                if (i == 0){
                    tv.setTypeface(null, Typeface.BOLD);
                }

                tr.addView(tv);
                staticParCountTable.addView(tr);
            }
        }
    }

    private void setupDynamicHeaderTable() {
        if (dynamicHeaderTable.getChildCount() < 1){

			dynamicHeaderTable.setStretchAllColumns(true);
			//Setup Hole# row
            TableRow holeNum = new TableRow(this);
            TableLayout.LayoutParams params = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
            holeNum.setLayoutParams(params);


            for (int a = 0; a<course.getParArray().length; a++){

				TextView tv = setupTextViewInTable(String.valueOf(a+1), applyLayoutWidth(LAYOUT_WIDTH),R.drawable.cell_shape_light_green);
                tv.setGravity(Gravity.CENTER);
                tv.setTypeface(null, Typeface.BOLD);

                holeNum.addView(tv);
            }
            dynamicHeaderTable.addView(holeNum);

            //setup par row
            TableRow parNum = new TableRow(this);
            parNum.setLayoutParams(params);

            for (int a = 0; a<course.getParArray().length; a++){
                TextView tv = setupTextViewInTable(String.valueOf(course.getParArray()[a]),TableRow.LayoutParams.MATCH_PARENT,R.drawable.cell_shape_light_green);
                tv.setGravity(Gravity.CENTER);
                parNum.addView(tv);
            }
            dynamicHeaderTable.addView(parNum);


        }
    }

    private void setupCurrentScoreColumn(Player[] players) {

        int i = 0;
        currentScoreTable.removeAllViews();
		for (Player player : players) {
			LinearLayout _linearLayout = new LinearLayout(context);
			TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 1f);
			_linearLayout.setLayoutParams(params);

			TextView tv = setupTextViewInTable(player.getCurrentParDifference(course.getParTotal()) + "", applyLayoutWidth(TOTAL_COLUMN_WIDTH), R.drawable.cell_shape);
			tv.setSingleLine();
            scoreTotalTextViews[i] = tv;
			_linearLayout.addView(tv);
			currentScoreTable.addView(_linearLayout);
			i++;
		}

    }

    private void setupNameColumn(Player[] players) {
        if (nameTable.getChildCount() < 1){
			for (Player player : players) {
				LinearLayout linearLayout = new LinearLayout(this);
				TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1f);
				linearLayout.setLayoutParams(params);

				TextView tv = setupTextViewInTable(player.getName(), TableRow.LayoutParams.MATCH_PARENT, R.drawable.cell_shape); //This forced width but the next line should be a quick fix for it.
				tv.setSingleLine();
				linearLayout.addView(tv);

				nameTable.addView(linearLayout);
			}
        }
    }

	private int applyLayoutWidth(int width){
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, getResources().getDisplayMetrics());
	}

    //Handle Vertical Scrollview Sync
    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        if (scrollView == nameScrollView){
            scoreScrollView.scrollTo(x,y);
            currentScoreSV.scrollTo(x,y);
        } else if (scrollView == scoreScrollView){
            nameScrollView.scrollTo(x,y);
            currentScoreSV.scrollTo(x,y);
        } else if (scrollView == currentScoreSV){
            scoreScrollView.scrollTo(x,y);
            nameScrollView.scrollTo(x,y);
        }

    }

    @Override
    public void onScrollChanged(ObservableHorizontalScrollView horizontalScrollView, int x, int y, int oldx, int oldy) {
        if (horizontalScrollView == scoreHorizontalScrollView){
            parHorizontalScrollView.scrollTo(x,y);
        } else if (horizontalScrollView == parHorizontalScrollView){
            scoreHorizontalScrollView.scrollTo(x,y);
        }
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        players = (Player[])savedInstanceState.getSerializable(RESTORE_PLAYERS_KEY);
        scoreCard = (ScoreCard) savedInstanceState.getSerializable(RESTORE_SCORE_CARD_KEY);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(RESTORE_PLAYERS_KEY, players);
        outState.putSerializable(RESTORE_SCORE_CARD_KEY, scoreCard);

    }

	@Override
	public void onBackPressed() {

		if (gameStarted){ //If game started the normal way
            saveGameAsResumable();
        }

		else {
			Bundle b = this.getIntent().getExtras();

			//If Coming from resume game picker, save game and go to main menu on back press
			//Fixes the bug of going back to an adapter that deleted that game on going to it.
			if (b != null && b.getInt("From Resume Game Picker") == 2) {
                saveGameAsResumable();
            }
		}

		super.onBackPressed();

	}

	//endregion

}
