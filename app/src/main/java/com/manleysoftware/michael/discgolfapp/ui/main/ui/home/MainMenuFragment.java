package com.manleysoftware.michael.discgolfapp.ui.main.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.manleysoftware.michael.discgolfapp.BuildConfig;
import com.manleysoftware.michael.discgolfapp.databinding.FragmentMainMenuBinding;
import com.manleysoftware.michael.discgolfapp.ui.Adapters.MainMenuDataAdapter;
import com.manleysoftware.michael.discgolfapp.ui.main.CoursePickerActivity;
import com.manleysoftware.michael.discgolfapp.ui.player.PlayerListActivity;
import com.manleysoftware.michael.discgolfapp.ui.scorecard.FinishedScorecardsActivity;
import com.manleysoftware.michael.discgolfapp.ui.scorecard.UnfinishedScorecardsActivity;

public class MainMenuFragment extends Fragment {

    private FragmentMainMenuBinding binding;

    private final String[] menuItems = {"New Game", "Resume Game", "Course Editor", "Player Editor", "Score Cards"};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMainMenuBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        if (BuildConfig.FLAVOR.equals("free")) {
            //String ADMOB_APP_ID = "ca-app-pub-8285085024937633~8121121504"; //Test admob app id
            MobileAds.initialize(requireContext());

            AdView adView = binding.adView;
            AdRequest request = new AdRequest.Builder().build();
//					.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//					.build();
            adView.loadAd(request);
        }


        ListView lvMainMenu = binding.lvMainMenu;
        MainMenuDataAdapter adapter = new MainMenuDataAdapter(requireContext(), menuItems);
        lvMainMenu.setAdapter(adapter);
        lvMainMenu.setDivider(null); //Removes separating lines between the menu items.
        lvMainMenu.setDividerHeight(0);//^^^Same

        lvMainMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0 -> OnNewGameClick(view);
                    case 1 -> OnResumeGameClicked(view);
                    case 2 -> onEditCoursesClick();
                    case 3 -> OnEditPlayersClick(view);
                    case 4 -> OnScorecardsClick(view);
                }
            }
        });


        return root;
    }
/*
 */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void OnNewGameClick(View v) {
        Intent intent = new Intent(requireContext(), CoursePickerActivity.class);
        startActivity(intent);
    }

    private void onEditCoursesClick() {
        NavDirections action = MainMenuFragmentDirections.actionNavigationMainMenuToNavigationCourseEditor();
        Navigation.findNavController(requireView()).navigate(action);
    }

    private void OnEditPlayersClick(View v) {
        Intent intent = new Intent(requireContext(), PlayerListActivity.class);
        startActivity(intent);
    }

    private void OnScorecardsClick(View v) {
        Intent intent = new Intent(requireContext(), FinishedScorecardsActivity.class);
        startActivity(intent);
    }

    private void OnResumeGameClicked(View v) {
        Intent intent = new Intent(requireContext(), UnfinishedScorecardsActivity.class);
        startActivity(intent);
    }
}