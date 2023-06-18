package com.manleysoftware.michael.discgolfapp.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Players extends ArrayList<Player> implements Serializable, List<Player> {

    private int currentPlayerSelected;

    public int currentPlayerIndex() {
        return currentPlayerSelected;
    }

    public Players(List<Player> players) {
        super(players);
    }

    public Players() {
        super();
    }

    public void nextPlayer() {
        currentPlayerSelected++;
        if (currentPlayerSelected > size() - 1) {
            currentPlayerSelected = size() - 1;
        }
    }

    public void previousPlayer() {
        currentPlayerSelected--;
        if (currentPlayerSelected < 0) {
            currentPlayerSelected = 0;
        }
    }

    public Player currentPlayer() {
        return get(currentPlayerSelected);
    }
}
