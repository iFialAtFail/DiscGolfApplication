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

    public void IncrementCurrentScore(int currentHole)
    {
        score[currentHole]++;
    }

    public void DecrementCurrentScore(int currentHole)
    {
        score[currentHole]--;
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
