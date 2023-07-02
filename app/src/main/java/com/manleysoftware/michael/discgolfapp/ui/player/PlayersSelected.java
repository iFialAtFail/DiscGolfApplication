package com.manleysoftware.michael.discgolfapp.ui.player;

import java.io.Serializable;

public class PlayersSelected implements Serializable {
    private boolean[] playersSelected;

    public PlayersSelected(int numberOfPlayers) {
        this.playersSelected = new boolean[numberOfPlayers];
    }

    public boolean[] getPlayersSelected() {
        return playersSelected;
    }

    public void setPlayersSelected(boolean[] playersSelected) {
        this.playersSelected = playersSelected;
    }

    public boolean isPlayerSelected(int index){
        return playersSelected[index];
    }

    public void setPlayerSelected(int index, boolean selected){
        playersSelected[index] = selected;
    }

    public void togglePlayer(int position) {
        playersSelected[position] = !playersSelected[position];
    }

    public void unselectAllPlayers() {
        for (int i = 0; i < playersSelected.length; i++) {
            playersSelected[i] = false;
        }
    }
}
