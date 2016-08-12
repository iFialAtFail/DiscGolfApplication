package com.example.michael.discgolfapp.Activities;


import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michael.discgolfapp.CustomViews.ObservableHorizontalScrollView;
import com.example.michael.discgolfapp.CustomViews.ObservableScrollView;
import com.example.michael.discgolfapp.Interfaces.IHorizontalScrollViewListener;
import com.example.michael.discgolfapp.Interfaces.IScrollViewListener;
import com.example.michael.discgolfapp.Model.Course;
import com.example.michael.discgolfapp.Model.Player;
import com.example.michael.discgolfapp.Model.ScoreCard;
import com.example.michael.discgolfapp.R;

import java.util.ArrayList;
import java.util.List;

public class RuntimeGameActivity extends AppCompatActivity implements IScrollViewListener, IHorizontalScrollViewListener {

    //region Private Model Variables

    Course course;
    Player[] players;
    ScoreCard newGame;
    int currentPlayerSelected = 0;

    //endregion

    //region Constants

    private final String PLAYER_SCORES = "Player Score";
    private final String PLAYER_NAMES = "Player Name";
    private final String CURRENT_HOLE = "Current Hole";

    //endregion

    //region Control/View references

    Button btnIncrementScore;
    Button btnDecrementScore;
    Button btnNextHole;
    Button btnPreviousHole;

    List<TableLayout> tableDiscGolf;


    ObservableHorizontalScrollView parHorizontalScrollView;
    ObservableHorizontalScrollView scoreHorizontalScrollView;
    ObservableScrollView nameScrollView;
    ObservableScrollView scoreScrollView;

    TableLayout scoreTable;
    TableLayout nameTable;
    TableLayout parTable;
    TableLayout parCellTable;

    //endregion

    //region Android Product Lifecycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTheme(R.style.NoActionBarTheme);
        setContentView(R.layout.runtime_game_layout);

        //region Setup References

        btnIncrementScore = (Button)findViewById(R.id.btnIncrementScore);
        btnDecrementScore = (Button)findViewById(R.id.btnDecrementScore);
        btnNextHole = (Button)findViewById(R.id.btnNextHole);
        btnPreviousHole = (Button) findViewById(R.id.btnPreviousHole);

        tableDiscGolf = new ArrayList<TableLayout>();
        scoreTable = (TableLayout) findViewById(R.id.scoreTable);
        nameTable = (TableLayout) findViewById(R.id.nameTable);
        parTable = (TableLayout) findViewById(R.id.parTable);
        parCellTable = (TableLayout) findViewById(R.id.parCellTable);

        parHorizontalScrollView = (ObservableHorizontalScrollView) findViewById(R.id.parHorizontalScrollView);
        parHorizontalScrollView.setHorizontalScrollViewListener(this);
        scoreHorizontalScrollView = (ObservableHorizontalScrollView) findViewById(R.id.scoreHorizontalScrollView);
        scoreHorizontalScrollView.setHorizontalScrollViewListener(this);

        nameScrollView = (ObservableScrollView) findViewById(R.id.nameScrollView);
        nameScrollView.setScrollViewListener(this);
        scoreScrollView = (ObservableScrollView) findViewById(R.id.scoreScrollView);
        scoreScrollView.setScrollViewListener(this);
        //endregion

        //retrieve Player/Course data
        //and setup references
        retrieveGameData();



        //If first time go, create new stuff
        if (savedInstanceState == null) {
            initPlayers(players);
            newGame  = new ScoreCard(players, course);
            generateTable(players, course.getHoleCount());

            //todo IMPLEMENT current score per player
            //tvCurrentTotal.setText(String.valueOf(players[0].getCurrentTotal()));

        }

        //If on recreation, pull out the saved state information and reset the object
        //to the previous state.
        if (savedInstanceState != null) {
            players = (Player[])savedInstanceState.getSerializable("RestorePlayers");
            newGame = (ScoreCard) savedInstanceState.getSerializable("RestoreScoreCard");
            generateTable(players, course.getHoleCount());


            //todo implement current score per player
            //tvCurrentTotal.setText("Current Score: " + String.valueOf(players[0].getCurrentTotal()));
            //((GradientDrawable)tvArray[newGame.getCurrentHole()-1].getBackground()).setColor(Color.RED);
            //((GradientDrawable)tvArray[newGame.getCurrentHole()].getBackground()).setColor(Color.WHITE);

        }

        //initially set block to color red

        TableRow tr = (TableRow) scoreTable.getChildAt(currentPlayerSelected);
        TextView tv = (TextView) tr.getChildAt(newGame.getCurrentHole()-1);
        ((GradientDrawable)tv.getBackground()).setColor(Color.RED);
        TextView tv2 = (TextView) tr.getChildAt(newGame.getCurrentHole());
        ((GradientDrawable)tv2.getBackground()).setColor(Color.WHITE);

    }

    //endregion




    private void generateTable(Player[] players, int courseHoleCount){

        //Setup Par single cell
        if (parCellTable.getChildCount() < 1){
            LinearLayout _linearLayout = new LinearLayout(this);
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT,1f);
            _linearLayout.setLayoutParams(params);
            TextView tv = setupTextViewInTable("Par",R.drawable.cell_shape_light_green);
            _linearLayout.addView(tv);
            parCellTable.addView(_linearLayout);
        }

        //Setup Par Row
        if (parTable.getChildCount() < 1){
            LinearLayout _linearLayout = new LinearLayout(this);
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT,1f);
            _linearLayout.setLayoutParams(params);

            for (int a = 0; a<course.getCurrentHolePar().length; a++){
                TextView tv = new TextView(getApplicationContext());
                tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tv.setBackgroundResource(R.drawable.cell_shape_light_green);
                tv.setTextSize(30);
                tv.setPadding(5, 5, 5, 5);
                tv.setText(String.valueOf(course.getCurrentHolePar()[a]));
                tv.setTextColor(Color.BLACK);
                _linearLayout.addView(tv);
            }
            parTable.addView(_linearLayout);
        }


        //Setup Name Column
        if (nameTable.getChildCount() < 1){
            for (int z = 0; z < players.length; z++) {
                LinearLayout linearLayout = new LinearLayout(this);
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT,1f);
                linearLayout.setLayoutParams(params);

                TextView tv = setupTextViewInTable(players[z].getName(), R.drawable.cell_shape);
                tv.setSingleLine();
                linearLayout.addView(tv);

                nameTable.addView(linearLayout);
            }
        }
        generateScoreTable(players, courseHoleCount);


    }



    //region Button Handler Methods

    public void OnIncrementScoreClick(View v){
        players[0].IncrementCurrentScore(newGame.getCurrentHole()); //TODO replace 0 with current player selected
        generateScoreTable(players, course.getHoleCount());

        TableRow tr = (TableRow) scoreTable.getChildAt(currentPlayerSelected);

        TextView tv = (TextView) tr.getChildAt(newGame.getCurrentHole()-1);
        tv.setBackgroundResource(R.drawable.cell_shape_red);
       ((GradientDrawable)tv.getBackground()).setColor(Color.RED);

        if (newGame.getCurrentHole()-2 < 0) {
            TextView tv2 = (TextView) tr.getChildAt(newGame.getCurrentHole());
            ((GradientDrawable)tv2.getBackground()).setColor(Color.WHITE);
        }
        else {
            TextView tv2 = (TextView) tr.getChildAt(newGame.getCurrentHole() - 2);
            ((GradientDrawable)tv2.getBackground()).setColor(Color.WHITE);
        }


    }
    public void OnDecrementScoreClick(View v){
        newGame.getPlayerArray()[0].DecrementCurrentScore(newGame.getCurrentHole());
        generateScoreTable(players, course.getHoleCount());

        TableRow tr = (TableRow) scoreTable.getChildAt(currentPlayerSelected);

        TextView tv = (TextView) tr.getChildAt(newGame.getCurrentHole()-1);
        ((GradientDrawable)tv.getBackground()).setColor(Color.RED);

        if (newGame.getCurrentHole()-2 < 0) {
            TextView tv2 = (TextView) tr.getChildAt(newGame.getCurrentHole());
            ((GradientDrawable)tv2.getBackground()).setColor(Color.WHITE);
        }
        else {
            TextView tv2 = (TextView) tr.getChildAt(newGame.getCurrentHole() - 2);
            ((GradientDrawable)tv2.getBackground()).setColor(Color.WHITE);
        }

    }
    public void OnNextHoleClick(View v){
        newGame.NextHole();
        //generateScoreTable(players, course.getHoleCount());


        TableRow tr = (TableRow) scoreTable.getChildAt(currentPlayerSelected);
        TextView tv = (TextView) tr.getChildAt(newGame.getCurrentHole()-1);
        ((GradientDrawable)tv.getBackground()).setColor(Color.RED);
        TextView tv2 = (TextView) tr.getChildAt(newGame.getCurrentHole()-2);
        ((GradientDrawable)tv2.getBackground()).setColor(Color.WHITE);

        tv.getParent().requestChildFocus(tv,tv);


    }
    public void OnPreviousHoleClick(View v){
        newGame.PreviousHole();
        //generateScoreTable(players, course.getHoleCount());


        TableRow tr = (TableRow) scoreTable.getChildAt(currentPlayerSelected);
        TextView tv = (TextView) tr.getChildAt(newGame.getCurrentHole()-1);
        ((GradientDrawable)tv.getBackground()).setColor(Color.RED);
        TextView tv2 = (TextView) tr.getChildAt(newGame.getCurrentHole());
        ((GradientDrawable)tv2.getBackground()).setColor(Color.WHITE);

        tv.getParent().requestChildFocus(tv,tv);

    }

    public void OnLastPlayerClick(View view) {
    }

    public void OnNxtPlayerClick(View view) {
    }

    //endregion

    //region Private Helper Methods

    private void retrieveGameData(){
        Bundle b = this.getIntent().getExtras();
        if (b != null){
            players = toPlayerArray((ArrayList<Player>) b.getSerializable("Players"));
            course = (Course) b.getSerializable("Course");

            if (course != null && players != null){
                Toast.makeText(this,"SUCCESS!",Toast.LENGTH_LONG).show();
            }
        }
    }

    private Player[] toPlayerArray(ArrayList<Player> list){
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

    private TextView setupTextViewInTable(String setText, int resourceID ){
        int textSize = 30;

        TextView tv = new TextView(getApplicationContext());
        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT));
        tv.setBackgroundResource(resourceID);
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(textSize);
        tv.setPadding(5, 5, 5, 5);
        tv.setText(setText);
        return tv;
    }

    private void generateScoreTable(Player[] players, int courseHoleCount) {
        // setup score info table
        scoreTable.removeAllViews();

        //Generate dynamic table.
        for (int i = 1; i <= players.length; i++){

            TableLayout tableLayout = new TableLayout(this);
            // outer for loop
            TableRow row = new TableRow(getApplicationContext());
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            // inner for loop
            for (int j = 1; j <= courseHoleCount; j++) {
                TextView tv = setupTextViewInTable(String.valueOf(players[i-1].getScore()[j-1]), R.drawable.cell_shape);
                row.addView(tv);
            }
            scoreTable.addView(row);
        }
    }


    //endregion

    //region Overridden Methods

    //Custom Interface Override-- Handles the syncing of the scrollviews.

    //Handle Vertical Scrollview Sync
    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        if (scrollView == nameScrollView){
            scoreScrollView.scrollTo(x,y);
        } else if (scrollView == scoreScrollView){
            nameScrollView.scrollTo(x,y);
        }
    }

    //Handle Horizontal Scrollview Sync
    @Override
    public void onScrollChanged(ObservableHorizontalScrollView horizontalScrollView, int x, int y, int oldx, int oldy) {
        if (horizontalScrollView == scoreHorizontalScrollView){
            parHorizontalScrollView.scrollTo(x,y);
        } else if (horizontalScrollView == parHorizontalScrollView){
            scoreHorizontalScrollView.scrollTo(x,y);
        }
    }

    //Android Product Lifecycle overrides

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        players = (Player[])savedInstanceState.getSerializable("RestorePlayers");
        newGame = (ScoreCard) savedInstanceState.getSerializable("RestoreScoreCard");
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("RestorePlayers",players);
        outState.putSerializable("RestoreScoreCard",newGame);

    }

    //endregion
}
