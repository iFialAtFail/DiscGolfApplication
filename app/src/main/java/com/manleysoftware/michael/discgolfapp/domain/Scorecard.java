package com.manleysoftware.michael.discgolfapp.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.StringJoiner;

/**
 * Created by Michael on 5/20/2016.
 */
public class Scorecard implements Serializable {

    private final Players players;
    private int currentHole;
    private final Course course;
    private String date;
    private boolean archived;

    public Scorecard(Players players, Course course) {
        this.archived = false;
        if (players.isEmpty()) throw new IllegalStateException();
        this.players = players;
        this.course = course;
        currentHole = 1;
        initializeCurrentDate();
    }

    public int getCurrentHole() {
        return currentHole;
    }

    public Players getPlayers() {
        return players;
    }

    public int getPlayersCount() {
        return players.size();
    }

    public int getCurrentPlayerSelected() {
        return players.currentPlayerSelected();
    }

    public String getCourseName() {
        return course.getName();
    }

    public Course getCourse() {
        return course;
    }

    public String getDate() {
        return date;
    }

    public boolean getArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    public String playersNames() {
        StringJoiner joiner = new StringJoiner(", ");
        players.getPlayers().stream().map(Player::getName).forEach(joiner::add);
        return joiner.toString();
    }

    public void nextHole() {
        if (currentHole < course.getHoleCount()) {
            currentHole++;
        }
    }

    public void previousHole() {
        if (currentHole > 1) {
            currentHole--;
        }
    }

    public void nextPlayer() {
        players.nextPlayer();
    }

    public void previousPlayer() {
        players.previousPlayer();
    }

    public Player currentPlayer() {
        return players.currentPlayer();
    }

    private void initializeCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm");
        date = dateFormat.format(calendar.getTime());
    }

}
