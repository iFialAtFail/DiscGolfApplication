package com.manleysoftware.michael.discgolfapp.ui.player;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.manleysoftware.michael.discgolfapp.application.AlreadyExistsException;
import com.manleysoftware.michael.discgolfapp.data.PlayerRepository;
import com.manleysoftware.michael.discgolfapp.domain.Player;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class PlayersEditorViewModel extends ViewModel {

    protected PlayerRepository playerRepository;

    private final MutableLiveData<List<Player>> players =
            new MutableLiveData<>();

    public LiveData<List<Player>> getPlayers() {
        return players;
    }

    @Inject
    public PlayersEditorViewModel(PlayerRepository playerRepository){
        this.playerRepository = playerRepository;
        this.players.setValue(playerRepository.getAllPlayers());
    }

    public int getCountOfPlayers() {
        if (players == null || players.getValue() == null) return 0;
        return players.getValue().size();
    }

    public void delete(Player playerToDelete, Context context) {
        playerRepository.delete(playerToDelete, context);
        players.setValue(playerRepository.getAllPlayers());
    }

    public void add(Player player, Context context) throws AlreadyExistsException {
        playerRepository.add(player,context);
        players.setValue(playerRepository.getAllPlayers());
    }
}