package com.manleysoftware.michael.discgolfapp.domain;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.StringJoiner;

/**
 * Created by Michael on 5/20/2016.
 */
public class Scorecard implements Serializable {

    private Long id;

    public static final String DATE_TIME_FORMAT = "dd MMM yyyy HH:mm";
    private final Players players;
    private int currentHole;
    private final Course course;
    private final ZonedDateTime dateTime;
    private boolean archived;

    public Scorecard(Players players, Course course, ZonedDateTime dateTime) {
        this.archived = false;
        if (players.isEmpty()) throw new IllegalStateException();
        this.players = players;
        this.course = course;
        currentHole = 1;
        this.dateTime = dateTime;
    }

    public int currentHole() {
        return currentHole;
    }

    public Players players() {
        return players;
    }

    public int playerCount() {
        return players.size();
    }

    public int currentPlayerIndex() {
        return players.currentPlayerIndex();
    }

    @NotNull
    public String courseName() {
        return course.getName();
    }

    public Course course() {
        return course;
    }

    public String displayDate() {
        return formattedDate(dateTime);
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived() {
        this.archived = true;
    }

    public String playerNames() {
        StringJoiner joiner = new StringJoiner(", ");
        players.stream().map(Player::getName).forEach(joiner::add);
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

    private static String formattedDate(ZonedDateTime dateTime) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT, Locale.getDefault());
        return dateFormat.format(dateTime);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
