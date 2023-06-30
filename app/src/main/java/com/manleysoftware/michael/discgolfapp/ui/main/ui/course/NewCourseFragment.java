package com.manleysoftware.michael.discgolfapp.ui.main.ui.course;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.manleysoftware.michael.discgolfapp.R;
import com.manleysoftware.michael.discgolfapp.application.AlreadyExistsException;
import com.manleysoftware.michael.discgolfapp.databinding.FragmentNewCourseBinding;
import com.manleysoftware.michael.discgolfapp.ui.Adapters.CourseParDataAdapter;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class NewCourseFragment extends Fragment {

    private FragmentNewCourseBinding binding;

    private CourseParDataAdapter adapter;
    private TextView tvHoleCount;
    private TextView tvCourseName;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNewCourseBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        NewCourseViewModel newCourseViewModel = new ViewModelProvider(this).get(NewCourseViewModel.class);

        ListView lvHoleOptions = binding.lvHoleOptions;
        adapter = new CourseParDataAdapter(requireContext());
        lvHoleOptions.setAdapter(adapter);
        Button btnIncrementHoleCount = binding.btnIncrementHoleCount;
        Button btnDecrementHoleCount = binding.btnDecrementHoleCnt;
        Button btnSaveCourse = binding.btnSaveCourse;
        tvHoleCount = binding.tvHoleCount;
        tvCourseName = binding.tvCourseName;

        newCourseViewModel.holeCount.observe(
                getViewLifecycleOwner(), i -> tvHoleCount.setText(getString(R.string.hole_count, i))
        );

        newCourseViewModel.holeList.observe(getViewLifecycleOwner(), holeList -> {
            adapter.setHoles(holeList);
            adapter.notifyDataSetChanged();
        });

        //Button Handlers
        btnIncrementHoleCount.setOnClickListener(v -> newCourseViewModel.incrementViewHoleCount());
        btnDecrementHoleCount.setOnClickListener(v -> newCourseViewModel.decrementHoleCount());
        btnSaveCourse.setOnClickListener(v -> attemptSaveNewCourse(newCourseViewModel));

        return root;
    }

    private void attemptSaveNewCourse(NewCourseViewModel newCourseViewModel) {
        String rawNameInput = tvCourseName.getText().toString().trim();
        try {
            newCourseViewModel.createCourse(rawNameInput, requireContext());
            //finish the activity and go back to calling activity
            finish();
        } catch (AlreadyExistsException whoops) {
            showCourseAlreadyExistsToast(rawNameInput);
        } catch (InvalidCourseNameException e) {
            Toast toast = Toast.makeText(requireContext(), getString(R.string.invalid_course_name_exception), Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private void finish() {
        NavDirections action = NewCourseFragmentDirections.actionNavigationNewCourseToNavigationCourseEditor();
        Navigation.findNavController(requireView()).navigate(action);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void showCourseAlreadyExistsToast(String course) {
        String message = getString(R.string.course_name_already_exists_exception, course);
        Toast toast = Toast.makeText(requireContext(), message, Toast.LENGTH_LONG);
        toast.show();
    }
}