package com.manleysoftware.michael.discgolfapp.viewmodel.player;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.manleysoftware.michael.discgolfapp.application.AlreadyExistsException;
import com.manleysoftware.michael.discgolfapp.data.PlayerRepository;
import com.manleysoftware.michael.discgolfapp.domain.Player;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class PlayerEditorViewModel extends ViewModel {

    private final MutableLiveData<String> _playerName = new MutableLiveData<>();

    private String playerNameArg;

    private Player player;

    protected PlayerRepository playerRepository;

    @Inject
    public PlayerEditorViewModel(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public LiveData<String> playerName() {
        if (_playerName.isInitialized()) {
            return _playerName;
        }
        _playerName.setValue(playerNameArg);
        return _playerName;
    }

    public void update(Player player, Context context) throws AlreadyExistsException {
        playerRepository.update(player, context);
    }

    public void setPlayerNameArg(String playerNameArg) {
        this.playerNameArg = playerNameArg;
        this.player = playerRepository.findByPrimaryKey(new Player(playerNameArg));
    }
    public void setPlayerNameUserText(String playerNameInput) {
        _playerName.setValue(playerNameInput);
    }

    public void updatePlayer(String updatedName, Context context) {
        player.setName(updatedName);
        playerRepository.update(player, context);
    }
}