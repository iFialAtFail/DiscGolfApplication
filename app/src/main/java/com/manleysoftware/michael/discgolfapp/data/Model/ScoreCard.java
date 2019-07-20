package com.manleysoftware.michael.discgolfapp.data.Model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Michael on 5/20/2016.
 */
public class ScoreCard implements Serializable {

    //region Private Fields

    private Player[] players;
    private int currentHole;
    private int playerSelectedIndex;
    private final Course course;
	private String date;

    //endregion

    //region Constructors

    public ScoreCard(Player[] players, Course course)
    {
        this.players = players;
        this.course = course;
        currentHole = 1;
		getCurrentDate();
    }

    //endregion

    //region Getters and Setters

    public int getCurrentHole(){
        return currentHole;
    }

    public void setCurrentHole(int value){
        if (isWithinCourseConstraints(value)){
            currentHole = value;
        }
    }

    public Player[] getPlayerArray(){
        return players;
    }

    public void setPlayerArray(Player[] value){
        players = value;
    }

    public int getPlayersCount(){
        return players.length;
    }

    public int getPlayerSelectedIndex(){
        return playerSelectedIndex;
    }

	public String getCourseName(){
		return course.getName();
	}

	public Course getCourse(){return course;}

	public String getDate(){
		return date;
	}

	public String getPlayersNames(){
		String concatNames = "";
		for(Player player : players){
			concatNames += (player.getName() + ", ");
		}
		return concatNames.substring(0, concatNames.length()-2);
	}

    //endregion

    //region Public Methods

    public void NextHole()
    {
        if (currentHole < course.getHoleCount())
        {
            currentHole++;
        }
    }

    public void PreviousHole()
    {
        if (currentHole > 1)
        {
            currentHole--;
        }
    }

    public void NextPlayer(){
        playerSelectedIndex++;
        if (playerSelectedIndex > players.length -1){
            playerSelectedIndex = players.length - 1;
        }
    }

    public void LastPlayer(){
        playerSelectedIndex--;
        if (playerSelectedIndex < 0){
            playerSelectedIndex = 0;
        }
    }

    public Player getCurrentPlayer(){
        return players[playerSelectedIndex];
    }

    //endregion

    //region Private Helper Methods

    private boolean isWithinCourseConstraints(int value) {
        return (value >= 1 && value <= course.getHoleCount());
    }

	private void getCurrentDate(){
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
		date = dateFormat.format(calendar.getTime());
	}

    //endregion

}
