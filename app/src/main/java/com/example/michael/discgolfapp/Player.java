package com.example.michael.discgolfapp;

/**
 * Created by Michael on 5/22/2016.
 */
public class Player {


    private String playerName;
    private int[] score;

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

        score = (int[]) course.getCurrentHolePar().clone();
    }


    public String getPlayerName(){
        return playerName;
    }

    public void setPlayerName(String value){
        playerName = value;
    }

    public int[] getScore(){
        return score;
    }

    public void setScore(int[] value){
        score = value;
    }

    public int getCurrentTotal(){
        int total = 0;
        for (int i : score){
            total += i;
        }
        return total;
    }


    public void IncrementCurrentScore(int currentHole)
    {
        if (currentHole >= 1 && currentHole <= 18) {
            score[currentHole - 1]++;
        }
    }

    public void DecrementCurrentScore(int currentHole)
    {
        if (currentHole >= 1 && currentHole <= 18) {
            score[currentHole - 1]--;
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

    //TODO add player previous game data, stack push/pop style
}
