package com.example.michael.discgolfapp.Activities;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michael.discgolfapp.Model.Course;
import com.example.michael.discgolfapp.Model.Player;
import com.example.michael.discgolfapp.Model.ScoreCard;
import com.example.michael.discgolfapp.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class RuntimeGameActivity extends AppCompatActivity {

    Course course;
    Player player;
    Player player2;
    Player[] players;
    ScoreCard newGame;
    int currentPlayerSelected = 0;

    private final String PLAYER_SCORE = "Player Score";
    private final String PLAYER_NAME = "Player Name";
    private final String CURRENT_HOLE = "Current Hole";


    Button btnIncrementScore;
    Button btnDecrementScore;
    Button btnNextHole;
    Button btnPreviousHole;

    TextView tvName;
    TextView tvCurrentTotal;

    List<TableLayout> tableDiscGolf;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.runtime_game_layout);

        btnIncrementScore = (Button)findViewById(R.id.btnIncrementScore);
        btnDecrementScore = (Button)findViewById(R.id.btnDecrementScore);
        btnNextHole = (Button)findViewById(R.id.btnNextHole);
        btnPreviousHole = (Button) findViewById(R.id.btnPreviousHole);

        tvName = (TextView) findViewById(R.id.tvName);
        tvCurrentTotal = (TextView) findViewById(R.id.tvCurrentTotal);

        tableDiscGolf = new ArrayList<TableLayout>();
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);


        retrieveGameData();



        //If first time go, create new stuff
        if (savedInstanceState == null) {

            player.StartGame(course);
            player2 = new Player("Num2");
            player2.StartGame(course);
            players = new Player[]{player,player2};

            newGame  = new ScoreCard(players, course);
            generateTable(players, course.getHoleCount());

            //todo IMPLEMENT current score per player
            //tvCurrentTotal.setText(String.valueOf(players[0].getCurrentTotal()));

        }

        //If on recreation, pull out the saved state information and reset the object
        //to the previous state.
        if (savedInstanceState != null) {
            player.StartGame(course);
            player.setScore(savedInstanceState.getIntArray(PLAYER_SCORE));
            players = new Player[]{player};
            newGame = new ScoreCard(players, course);
            newGame.setCurrentHole(savedInstanceState.getInt(CURRENT_HOLE));
            generateTable(players, course.getHoleCount());

            //todo implement current score per player
            //tvCurrentTotal.setText("Current Score: " + String.valueOf(players[0].getCurrentTotal()));
            //((GradientDrawable)tvArray[newGame.getCurrentHole()-1].getBackground()).setColor(Color.RED);
            //((GradientDrawable)tvArray[newGame.getCurrentHole()].getBackground()).setColor(Color.WHITE);

        }
    }

    /*
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        player.StartGame(course);
        player.setScore(savedInstanceState.getIntArray(PLAYER_SCORE));
        players = new Player[]{player};
        newGame = new ScoreCard(players, course);
        newGame.setCurrentHole(savedInstanceState.getInt(CURRENT_HOLE));
    }
    */

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(PLAYER_NAME, player.getName());
        outState.putIntArray(PLAYER_SCORE, player.getScore());
        outState.putInt(CURRENT_HOLE,newGame.getCurrentHole());
    }


    private void generateTable(Player[] players, int courseHoleCount){
        linearLayout.removeAllViews();
        for (int i = 1; i <= players.length; i++){

            TableLayout tableLayout = new TableLayout(this);
            // outer for loop
            TableRow row = new TableRow(getApplicationContext());
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            // inner for loop
            for (int j = 1; j <= courseHoleCount; j++) {
                TextView tv = new TextView(getApplicationContext());
                tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tv.setBackgroundResource(R.drawable.cell_shape);
                tv.setTextSize(30);
                tv.setPadding(5, 5, 5, 5);
                tv.setText(String.valueOf(players[i-1].getScore()[j-1]));//TODO add proper score.
                row.addView(tv);
            }
            tableLayout.addView(row);


            linearLayout.addView(tableLayout);


        }
    }

    private void updateTable(){

    }

    public void OnIncrementScoreClick(View v){
        players[0].IncrementCurrentScore(newGame.getCurrentHole()); //TODO replace 0 with current player selected
        /*
        TableLayout tb = (TableLayout) linearLayout.getChildAt(0);
        TableRow tr = (TableRow) tb.getChildAt();
        TextView tv = (TextView) tr.getChildAt(0);
        tv.setText(String.valueOf(String.valueOf(players[0].getScore()[newGame.getCurrentHole()])));
        */
        generateTable(players, course.getHoleCount());
        tvCurrentTotal.setText("Current Score: " + String.valueOf(players[0].getCurrentTotal()));
    }
    public void OnDecrementScoreClick(View v){
        newGame.getPlayerArray()[0].DecrementCurrentScore(newGame.getCurrentHole());
        generateTable(players, course.getHoleCount());

        tvCurrentTotal.setText("Current Score: " + String.valueOf(players[0].getCurrentTotal()));

    }
    public void OnNextHoleClick(View v){
        newGame.NextHole();
        generateTable(players, course.getHoleCount());

        TableLayout tb = (TableLayout) linearLayout.getChildAt(currentPlayerSelected);
        TableRow tr = (TableRow) tb.getChildAt(0);
        TextView tv = (TextView) tr.getChildAt(newGame.getCurrentHole()-1);
        ((GradientDrawable)tv.getBackground()).setColor(Color.RED);
        TextView tv2 = (TextView) tr.getChildAt(newGame.getCurrentHole()-2);
        ((GradientDrawable)tv2.getBackground()).setColor(Color.WHITE);


    }
    public void OnPreviousHoleClick(View v){
        newGame.PreviousHole();
        generateTable(players, course.getHoleCount());

        TableLayout tb = (TableLayout) linearLayout.getChildAt(currentPlayerSelected);
        TableRow tr = (TableRow) tb.getChildAt(0);
        TextView tv = (TextView) tr.getChildAt(newGame.getCurrentHole()-1);
        ((GradientDrawable)tv.getBackground()).setColor(Color.RED);
        TextView tv2 = (TextView) tr.getChildAt(newGame.getCurrentHole());
        ((GradientDrawable)tv2.getBackground()).setColor(Color.WHITE);


    }

    private void retrieveGameData(){
        Bundle b = this.getIntent().getExtras();
        if (b != null){
            player = (Player) b.getSerializable("Player");
            course = (Course) b.getSerializable("Course");

            if (course != null && player != null){
                Toast.makeText(this,"SUCCESS!",Toast.LENGTH_LONG).show();
                player.StartGame(course);
            }
        }
    }

}
