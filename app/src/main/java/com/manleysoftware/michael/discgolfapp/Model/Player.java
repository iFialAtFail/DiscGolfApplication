package com.manleysoftware.michael.discgolfapp.Model;

import java.io.Serializable;

/**
 * Created by Michael on 5/22/2016.
 */
public class Player implements Serializable {

    //region Private Fields

    private String playerName;

    private boolean gameStarted = false;
    private Course course = null;
    private int[] score;

	private static final int maxScoreAllowable = 20;

    //endregion

    //region Constructors

    public Player(String playerName, Course course)
    {
        validatePlayerName(playerName);

        if (course == null)
        {
            throw new NullPointerException();
        }

        initializeScore();
        gameStarted = true;

    }


    public Player(String playerName){
        validatePlayerName(playerName);
    }

    //endregion

    //region Getters and Setters

    public String getName(){
        return playerName;
    }

    public void setName(String value){
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
        else{
            throw new IllegalArgumentException("Game not started yet. Can't access without starting game");
        }
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

    public int getCurrentParDifference(int par){
        return getCurrentTotal() - par;
    }

    //endregion

    //region Public Methods

    public void IncrementCurrentScore(int currentHole)
    {
        if (!gameStarted) {
            return;
        }

        if (currentHole >= 1 && currentHole <= course.getHoleCount()) {
            if (score[currentHole - 1] >= maxScoreAllowable){
				return;
			}
			score[currentHole - 1]++;
        }

    }

    public void DecrementCurrentScore(int currentHole)
    {
        if (!gameStarted) {
            return;
        }

        if (currentHole >= 1 && currentHole <= course.getHoleCount()) {
            if (score[currentHole - 1] > 1) {
                score[currentHole - 1]--;
            }
        }

    }

    public void StartGame(Course course){
        if (this.course == null) {
            this.course = course;
            initializeScore();
        }

        gameStarted = true;
    }

    //endregion

    //region Private Helper Methods

    public static boolean isValidPlayerName(String nameInput)
    {
        nameInput = nameInput.trim();

        if (nameInput.isEmpty()){
            return false;
        }

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
            score = course.getParArray().clone();
        }
    }


    private void validatePlayerName(String playerName) {
        if (isValidPlayerName(playerName))
        {
            this.playerName = playerName;
        }
        else
        {
            throw new IllegalArgumentException("Invalid input for player name. AlphaNumeric characters and spaces only.");
        }
    }

    //endregion

    //region Overrides

    @Override
    public String toString() {
        return playerName;
    }

    @Override
    public boolean equals(Object o) {
		return (o instanceof Player) && (((Player) o).getName().equals(this.getName()));
    }



    //endregion

}
