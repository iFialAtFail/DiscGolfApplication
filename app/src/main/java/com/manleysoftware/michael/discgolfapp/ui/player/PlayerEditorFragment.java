package com.manleysoftware.michael.discgolfapp.ui.player;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.manleysoftware.michael.discgolfapp.R;
import com.manleysoftware.michael.discgolfapp.databinding.FragmentPlayerEditorBinding;
import com.manleysoftware.michael.discgolfapp.viewmodel.player.PlayerEditorViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PlayerEditorFragment extends Fragment {

    private PlayerEditorViewModel playerEditorViewModel;

    private Toast toast;

    private EditText etName;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentPlayerEditorBinding binding = FragmentPlayerEditorBinding.inflate(inflater, container, false);
        playerEditorViewModel = new ViewModelProvider(this).get(PlayerEditorViewModel.class);
        etName = binding.etName;
        Button btnSavePlayer = binding.btnSavePlayer;
        if (getArguments() == null) throw new IllegalStateException();
        playerEditorViewModel.setPlayerNameArg(PlayerEditorFragmentArgs.fromBundle(getArguments()).getPlayerNameArg());

        playerEditorViewModel.playerName().observe(getViewLifecycleOwner(), etName::setText);

        //Button Handlers
        btnSavePlayer.setOnClickListener(v -> {
            String rawName = etName.getText().toString();
            String name = rawName.trim();
            try {
                playerEditorViewModel.updatePlayer(name, requireContext());
                finish();
            } catch (IllegalStateException ex) {
                invalidUserToast();
            }
        });
        return binding.getRoot();
    }

    private void invalidUserToast() {
        toast = Toast.makeText(requireContext(), getString(R.string.invalid_user_exception), Toast.LENGTH_LONG);
        toast.cancel();
        toast.show();
        etName.setSelected(true);
    }

    private void finish() {
        NavDirections action = PlayerEditorFragmentDirections.actionPlayerEditorFragmentToPlayersEditorFragment();
        Navigation.findNavController(requireView()).navigate(action);
    }

}