package com.manleysoftware.michael.discgolfapp.ui.main.ui.course;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.manleysoftware.michael.discgolfapp.domain.Course;
import com.manleysoftware.michael.discgolfapp.ui.Adapters.CourseParDataAdapter;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class NewCourseFragment extends Fragment {

    private static final String HOLE_ARRAY = "Hole Array";
    private static final String HOLE_COUNT = "Hole Count";

    private FragmentNewCourseBinding binding;

//    @Inject
//    protected NewCourseViewModel newCourseViewModel;

    private CourseParDataAdapter adapter;
    private TextView tvHoleCount;
    private TextView tvCourseName;
    private Button btnSaveCourse;
    private Button btnDecrementHoleCount;
    private Button btnIncrementHoleCount;
    private ListView lvHoleOptions;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNewCourseBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        CourseEditorViewModel courseEditorViewModel =
                new ViewModelProvider(this).get(CourseEditorViewModel.class);
        NewCourseViewModel newCourseViewModel = new ViewModelProvider(this).get(NewCourseViewModel.class);
        lvHoleOptions = binding.lvHoleOptions;
        adapter = new CourseParDataAdapter(requireContext());
        lvHoleOptions.setAdapter(adapter);
        btnIncrementHoleCount = binding.btnIncrementHoleCount;
        btnDecrementHoleCount = binding.btnDecrementHoleCnt;
        btnSaveCourse = binding.btnSaveCourse;
        tvHoleCount = binding.tvHoleCount;
        tvCourseName = binding.tvCourseName;

        disableAutoKeyboard();

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
        btnSaveCourse.setOnClickListener(v -> {
            String rawNameInput = tvCourseName.getText().toString();
            String nameInput = rawNameInput.trim();

            if (!validCourseName(nameInput))
                return;

            int[] populatedPars = toIntArray(adapter.getCourseList());
            Course course = new Course(nameInput, populatedPars);
            try {
                courseEditorViewModel.add(course, requireContext());
            } catch (AlreadyExistsException whoops) {
                showCourseAlreadyExistsToast(course);
                return;
            }

            //finish the activity and go back to calling activity
            finish();
        });
        return root;
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

    private void showCourseAlreadyExistsToast(Course course) {
        Toast toast = Toast.makeText(requireContext(), "Course: " + course.getName() + " exists already", Toast.LENGTH_LONG);
        toast.show();
    }

    private boolean validCourseName(String nameInput) {
        if (!Course.isValidCoursename(nameInput)) {
            Toast toast = Toast.makeText(requireContext(), "Not a valid course name! Try again!", Toast.LENGTH_LONG);
            toast.show();
            return false;
        }
        return true;
    }

    private void disableAutoKeyboard() {
        if (this.getActivity() != null)
            this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private int[] toIntArray(List<Integer> list) {
        int[] ret = new int[list.size()];
        for (int i = 0; i < ret.length; i++)
            ret[i] = list.get(i);
        return ret;
    }
}