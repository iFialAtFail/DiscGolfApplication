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
import com.manleysoftware.michael.discgolfapp.application.AlreadyExistsException;
import com.manleysoftware.michael.discgolfapp.databinding.FragmentNewPlayerBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class NewPlayerFragment extends Fragment {

    private NewPlayerViewModel viewModel;

    private Toast toast;

    private EditText etName;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentNewPlayerBinding binding = FragmentNewPlayerBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(NewPlayerViewModel.class);

        Button btnSavePlayer = binding.btnSavePlayer;
        etName = binding.etName;

        viewModel.playerName().observe(getViewLifecycleOwner(), name -> etName.setText(name));

        //Button Handlers
        btnSavePlayer.setOnClickListener(v -> {
            String rawName = etName.getText().toString();
            String name = rawName.trim();
            try {
                viewModel.add(name, requireContext());
                NavDirections action = NewPlayerFragmentDirections.actionNewPlayerFragmentToPlayersEditorFragment();
                Navigation.findNavController(requireView()).navigate(action);
            } catch (AlreadyExistsException e) {
                invalidUserToast();
            }
        });

        return binding.getRoot();
    }

    private void invalidUserToast() {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(requireContext(), getString(R.string.invalid_user_exception), Toast.LENGTH_LONG);
        toast.show();
        etName.setSelected(true);
    }

}