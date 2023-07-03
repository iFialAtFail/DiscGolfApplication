package com.manleysoftware.michael.discgolfapp.ui.player;

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
public class NewPlayerViewModel extends ViewModel {
    protected PlayerRepository playerRepository;

    private final MutableLiveData<String> _playerName =
            new MutableLiveData<>();

    public LiveData<String> playerName() {
        return _playerName;
    }

    @Inject
    public NewPlayerViewModel(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public void add(String playerName, Context context) throws AlreadyExistsException {
        Player player = new Player(playerName);
        playerRepository.add(player, context);
    }
}