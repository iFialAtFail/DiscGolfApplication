package com.manleysoftware.michael.discgolfapp.data.Model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Michael on 5/20/2016.
 */
public class Scorecard implements Serializable {

    //region Private Fields

    private Player[] players;
    private int currentHole;
    private int currentPlayerSelected;
    private final Course course;
	private String date;
	private Boolean archived;

    //endregion

    //region Constructors

    public Scorecard(Player[] players, Course course)
    {
        this.archived = false;
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

    public int getCurrentPlayerSelected(){
        return currentPlayerSelected;
    }

	public String getCourseName(){
		return course.getName();
	}

	public Course getCourse(){return course;}

	public String getDate(){
		return date;
	}

    public Boolean getArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
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

    public void nextHole()
    {
        if (currentHole < course.getHoleCount())
        {
            currentHole++;
        }
    }

    public void previousHole()
    {
        if (currentHole > 1)
        {
            currentHole--;
        }
    }

    public void nextPlayer(){
        currentPlayerSelected++;
        if (currentPlayerSelected > players.length -1){
            currentPlayerSelected = players.length - 1;
        }
    }

    public void LastPlayer(){
        currentPlayerSelected--;
        if (currentPlayerSelected < 0){
            currentPlayerSelected = 0;
        }
    }

    public Player getCurrentPlayer(){
        return players[currentPlayerSelected];
    }

    //endregion

    //region Private Helper Methods

    private boolean isWithinCourseConstraints(int value) {
        return (value >= 1 && value <= course.getHoleCount());
    }

	private void getCurrentDate(){
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm");
		date = dateFormat.format(calendar.getTime());
	}

    //endregion

}
