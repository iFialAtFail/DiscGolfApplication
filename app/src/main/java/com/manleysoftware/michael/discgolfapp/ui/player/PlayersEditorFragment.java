package com.manleysoftware.michael.discgolfapp.ui.player;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.manleysoftware.michael.discgolfapp.BuildConfig;
import com.manleysoftware.michael.discgolfapp.R;
import com.manleysoftware.michael.discgolfapp.databinding.FragmentPlayersEditorBinding;
import com.manleysoftware.michael.discgolfapp.domain.Player;
import com.manleysoftware.michael.discgolfapp.ui.adapters.PlayerDataAdapter;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PlayersEditorFragment extends Fragment {

    private PlayersEditorViewModel playersEditorViewModel;
    private PlayerDataAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentPlayersEditorBinding binding = FragmentPlayersEditorBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        if (BuildConfig.FLAVOR.equals("free") && !BuildConfig.BUILD_TYPE.equals("debug")) {
            AdView adView = binding.adViewPE;
            AdRequest request = new AdRequest.Builder().build();
            if (adView != null)
                adView.loadAd(request);
        }
        playersEditorViewModel = new ViewModelProvider(this).get(PlayersEditorViewModel.class);

        ListView lvPlayerList = binding.lvPlayerList;
        RelativeLayout playerEditorRelativelayout = binding.playerEditorRelativeLayout;
        adapter = new PlayerDataAdapter(requireContext());
        lvPlayerList.setAdapter(adapter);
        if (playersEditorViewModel.getCountOfPlayers() > 0) {
            playersEditorViewModel.getPlayers().observe(getViewLifecycleOwner(), players -> {
                adapter.setPlayerList(players);
                adapter.notifyDataSetChanged();
            });
        } else {
            View messageLayout = getLayoutInflater().inflate(R.layout.listview_alternative_layout, binding.getRoot());

            ImageView backgroundImage = (ImageView) messageLayout.findViewById(R.id.ivImage);
            Bitmap bm5 = BitmapFactory
                    .decodeResource(requireContext().getResources(), R.drawable.jade_500x500);
            backgroundImage.setImageBitmap(bm5);

            TextView tvNoListViewMessage = (TextView) messageLayout.findViewById(R.id.tvNoListViewMessage);
            tvNoListViewMessage.setText(getString(R.string.no_players_found_message));
            playerEditorRelativelayout.addView(messageLayout);
        }

        lvPlayerList.setOnItemLongClickListener((parent, view, position, id) -> {
            AlertDialog.Builder aat = new AlertDialog.Builder(requireContext());
            aat.setTitle(getString(R.string.deletePrompt))
                    .setMessage(getString(R.string.delete_confirmation_question, parent.getItemAtPosition(position).toString()))
                    .setCancelable(true)
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                    .setPositiveButton(getString(R.string.delete_button), (dialog, which) -> {
                        //Make the change
                        Player playerToRemove = (Player) adapter.getItem(position);
                        playersEditorViewModel.delete(playerToRemove, requireContext());

                        //Commit the change to persistent memory
                        adapter.notifyDataSetChanged();
                        if (adapter.getCount() == 0) {
                            recreate();
                        }
                    });
            AlertDialog art = aat.create();

            art.show();
            return true;

        });

        lvPlayerList.setOnItemClickListener((parent, view, position, id) -> {
            Player player = (Player) adapter.getItem(position);
            NavDirections action = PlayersEditorFragmentDirections.actionPlayersEditorFragmentToPlayerEditorFragment(player.getName());
            Navigation.findNavController(requireView()).navigate(action);
        });

        Button btnNewPlayer = binding.btnNewPlayer;
        btnNewPlayer.setOnClickListener(v -> {
            NavDirections action = PlayersEditorFragmentDirections.actionPlayersEditorFragmentToNewPlayerFragment();
            Navigation.findNavController(requireView()).navigate(action);
        });
        return root;
    }

    private void recreate() {
        getParentFragmentManager()
                .beginTransaction()
                .detach(PlayersEditorFragment.this)
                .commit();
        getParentFragmentManager()
                .beginTransaction()
                .attach(PlayersEditorFragment.this)
                .commit();
    }
}