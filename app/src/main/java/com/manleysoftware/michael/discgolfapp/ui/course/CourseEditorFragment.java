package com.manleysoftware.michael.discgolfapp.ui.course;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.manleysoftware.michael.discgolfapp.databinding.FragmentCourseEditorBinding;
import com.manleysoftware.michael.discgolfapp.domain.Course;
import com.manleysoftware.michael.discgolfapp.ui.adapters.CourseDataAdapter;
import com.manleysoftware.michael.discgolfapp.ui.course.legacy.EditExistingCourseActivity;
import com.manleysoftware.michael.discgolfapp.viewmodel.course.CourseEditorViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CourseEditorFragment extends Fragment {

    private FragmentCourseEditorBinding binding;

    public CourseEditorFragment(){
        super(R.layout.fragment_course_editor);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCourseEditorBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        if (BuildConfig.FLAVOR.equals("free")) {
            AdView adView = binding.adViewCE;
            AdRequest request = new AdRequest.Builder()
                    .build();
            adView.loadAd(request);
        }
        CourseEditorViewModel courseEditorViewModel =
                new ViewModelProvider(this).get(CourseEditorViewModel.class);

        ListView lvCourseList = binding.lvCourseList;

        //Used to add a view if the listview is not there.
        RelativeLayout courseEditorRelativeLayout = binding.courseEditorRelativeLayout;


        CourseDataAdapter adapter = new CourseDataAdapter(requireContext());
        if (courseEditorViewModel.getCountOfCourses() > 0) {//If nothing to populate adapter and listview fails to be created
            lvCourseList.setAdapter(adapter);
            courseEditorViewModel.getCourses().observe(getViewLifecycleOwner(), cl -> {
                adapter.updateCourseList(cl);
                adapter.notifyDataSetChanged();
            });
        } else {
            changeLayoutToNoCoursesAlternative(courseEditorRelativeLayout);
        }

        lvCourseList.setOnItemLongClickListener((parent, view1, position, id) -> {
            AlertDialog.Builder aat = new AlertDialog.Builder(requireContext());
            aat.setTitle("Delete?")
                    .setMessage("Are you sure you want to delete " + parent.getItemAtPosition(position).toString() + "?")
                    .setCancelable(true)
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                    .setPositiveButton("Delete", (dialog, which) -> {
                        //Make the change
                        Course toDelete = (Course) adapter.getItem(position);
                        courseEditorViewModel.delete(toDelete, requireContext());
                        adapter.notifyDataSetChanged();

                        //If listview is empty, recreate to show disc/empty view.
                        if (adapter.getCount() == 0) {
                            recreate();
                        }
                    });
            AlertDialog art = aat.create();

            art.show();
            return true;
        });

        lvCourseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle b = new Bundle();
                b.putInt("Position", position);
                Intent intent = new Intent(requireContext(), EditExistingCourseActivity.class);
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        Button btnNewCourse = binding.btnNewCourse;
        btnNewCourse.setOnClickListener(v -> {
            NavDirections action = CourseEditorFragmentDirections.actionNavigationCourseEditorToNavigationNewCourse();
            Navigation.findNavController(requireView()).navigate(action);
        });
        return root;
    }

    private void recreate() {
        getParentFragmentManager()
                .beginTransaction()
                .detach(CourseEditorFragment.this)
                .commit();
        getParentFragmentManager()
                .beginTransaction()
                .attach(CourseEditorFragment.this)
                .commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void changeLayoutToNoCoursesAlternative(RelativeLayout courseEditorRelativeLayout) {
        View messageLayout =
                getLayoutInflater().inflate(R.layout.listview_alternative_layout, binding.getRoot());

        ImageView backgroundImage = messageLayout.findViewById(R.id.ivImage);
        Bitmap bm5 = BitmapFactory
                .decodeResource(requireContext().getResources(), R.drawable.roadrunner_500x500);
        backgroundImage.setImageBitmap(bm5);

        TextView tvNoListViewMessage = messageLayout.findViewById(R.id.tvNoListViewMessage);
        tvNoListViewMessage.setText("Oops! No courses here!\nPlease add a course.");
        courseEditorRelativeLayout.addView(messageLayout);
    }
}