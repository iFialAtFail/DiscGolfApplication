package com.example.michael.discgolfapp.Model;

/**
 * Created by Michael on 5/22/2016.
 */
public class Player {


    private String playerName;
    private int[] score;
    private boolean gameStarted = false;
    private Course course;

    public Player(String playerName, Course course)
    {
        if (isValidPlayerName(playerName))
        {
            this.playerName = playerName;
        }
        else
        {
            //TODO Throw Java exception
            //throw new ArgumentException("Invalid input for player name. AlphaNumeric characters and spaces only.");
        }

        if (course == null)
        {
            //TODO Throw Java exception
            //throw new ArgumentNullException();
        }

        initializeScore();

    }
    public Player(String playerName){
        this.playerName = playerName;
    }


    public String getPlayerName(){
        return playerName;
    }

    @Override
    public String toString() {
        return playerName;
    }

    public void setPlayerName(String value){
        playerName = value;
    }

    public int[] getScore(){
        if (gameStarted) {
            return score;
        }
        return null;
    }

    public void setScore(int[] value){
        if (gameStarted){
            score = value;
        }
        //TODO throw exception saying game's not started yet
    }

    public int getCurrentTotal(){
        if (gameStarted) {
            int total = 0;
            for (int i : score) {
                total += i;
            }
            return total;
        }
        else{
            return -1;
        }
    }


    public void IncrementCurrentScore(int currentHole)
    {
        if (gameStarted) {
            if (currentHole >= 1 && currentHole <= 18) {
                score[currentHole - 1]++;
            }
        }
    }

    public void DecrementCurrentScore(int currentHole)
    {
        if (gameStarted) {
            if (currentHole >= 1 && currentHole <= 18) {
                if (score[currentHole - 1] < 1) {
                    score[currentHole - 1]--;
                }
            }
        }
    }

    public void StartGame(Course course){
        if (this.course == null) {
            this.course = course;
            initializeScore();
        }
        else{
            //TODO throw exception for using startgame wrong.
        }
    }


    private boolean isValidPlayerName(String nameInput)
    {
        char[] charArray = nameInput.toCharArray();


        for(char letter : charArray)
        {
            if (Character.isLetterOrDigit(letter) || letter == ' ')
            {
                //Let it keep iterating through the characters.
            }
            else { return false; }
        }
        return true;
    }

    private void initializeScore(){
        if (course != null) {
            score = (int[]) course.getCurrentHolePar().clone();
        }
    }

    //TODO add player previous game data, stack push/pop style
}
