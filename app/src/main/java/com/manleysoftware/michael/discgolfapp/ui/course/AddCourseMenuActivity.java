package com.manleysoftware.michael.discgolfapp.ui.course;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.manleysoftware.michael.discgolfapp.Application.CourseExistsAlreadyException;
import com.manleysoftware.michael.discgolfapp.data.filerepository.CourseFileRepository;
import com.manleysoftware.michael.discgolfapp.ui.Adapters.CourseParDataAdapter;
import com.manleysoftware.michael.discgolfapp.data.Model.Course;
import com.manleysoftware.michael.discgolfapp.data.CourseRepository;
import com.manleysoftware.michael.discgolfapp.R;
import com.manleysoftware.michael.discgolfapp.ui.main.CoursePickerActivity;

import java.util.ArrayList;
import java.util.List;

import static com.manleysoftware.michael.discgolfapp.data.Model.Course.MAX_HOLE_COUNT;

/**
 * Created by Michael on 6/23/2016.
 */
public class AddCourseMenuActivity extends Activity {
    //region Class Constants
    private static final String HOLE_ARRAY = "Hole Array";
    private static final String HOLE_COUNT = "Hole Count";
    public static final int DEFAULT_PAR_VALUE = 3;
    public static final int MIN_HOLE_COUNT = 1;

    //endregion

    //region Private Fields

    //Model References
	private CourseRepository courseRepository;

    //UI References
    private List<Integer> holeParsList;
	private CourseParDataAdapter adapter;
	private TextView tvHoleCount;
    private TextView tvCourseName;
    private final Context context = this;
    private Button btnSaveCourse;
    private Button btnDecrementHoleCount;
    private Button btnIncrementHoleCount;
    private ListView lvHoleOptions;

    //endregion

    //region Android Product Lifecycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_course_menu_layout);

        initializeViewReferences();
        initializeCourseRepository();
        disableAutoKeyboard();

        //if First time go
        if (savedInstanceState == null){
            initilizeEachHoleTo3();
        } else {
            holeParsList = new ArrayList<>();
            holeParsList.addAll(toIntegerList(savedInstanceState.getIntArray(HOLE_ARRAY)));
            tvHoleCount.setText(savedInstanceState.getString(HOLE_COUNT));
        }

        adapter = new CourseParDataAdapter(context, holeParsList);
        lvHoleOptions.setAdapter(adapter);

        //Button Handlers
        btnIncrementHoleCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementViewHoleCount();
            }
        });
        btnDecrementHoleCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrementHoleCount();
            }
        });

        btnSaveCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameInput = tvCourseName.getText().toString();

                if (!validCourseName(nameInput))
                    return;

                int[] populatedPars = toIntArray(adapter.getCourseList());
                Course course = new Course(nameInput, populatedPars);
                try {
                    courseRepository.addCourse(course);
                    courseRepository.Save(getApplicationContext());
                } catch (CourseExistsAlreadyException whoops){
                    showCourseAlreadyExistsToast(course);
                    return;
                }

                //finish the activity and go back to calling activity
                finish();
            }
        });
    }

    private void showCourseAlreadyExistsToast(Course course) {
        Toast toast = Toast.makeText(context, "Course: " + course.getName() + " exists already",Toast.LENGTH_LONG);
        toast.show();
    }

    private boolean validCourseName(String nameInput) {
        if (!Course.isValidCoursename(nameInput)){
            Toast toast = Toast.makeText(context, "Not a valid course name! Try again!",Toast.LENGTH_LONG);
            toast.show();
            return false;
        }
        return true;
    }

    private void disableAutoKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void initializeViewReferences() {
        //Initialize Refs
        lvHoleOptions = (ListView) findViewById(R.id.lvHoleOptions);
        btnIncrementHoleCount = (Button) findViewById(R.id.btnIncrementHoleCount);
        btnDecrementHoleCount = (Button) findViewById(R.id.btnDecrementHoleCnt);
        btnSaveCourse = (Button) findViewById(R.id.btnSaveCourse);
        tvHoleCount = (TextView) findViewById(R.id.tvHoleCount);
        tvCourseName = (EditText) findViewById(R.id.tvCourseName);
    }

    //endregion

    //region Overridden Methods

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putIntArray(HOLE_ARRAY, toIntArray(holeParsList));
        outState.putString(HOLE_COUNT, tvHoleCount.getText().toString());

    }

    //endregion

    //region Private Helper Methods

    //TODO: Move this to repository? maybe a provider class?
    private boolean isUniqueObject(String name){
        for (Course c : courseRepository.getCourses()){
            if (c.getName().equals(name)){
                return false;
            }
        }
        return true;
    }

    private int[] toIntArray(List<Integer> list){
        int[] ret = new int[list.size()];
        for(int i = 0;i < ret.length;i++)
            ret[i] = list.get(i);
        return ret;
    }

    private List<Integer> toIntegerList(int[] array){
        List<Integer> ret = new ArrayList<>();
		for (int anArray : array) {
			ret.add(anArray);
		}
        return ret;
    }

    private void initilizeEachHoleTo3(){
        holeParsList = new ArrayList<>();
        int numberOfHoles = Integer.parseInt(tvHoleCount.getText().toString());
        for(int i = 0; i < numberOfHoles; i++){
            holeParsList.add(DEFAULT_PAR_VALUE);
        }
    }

    private void incrementViewHoleCount() {
        int currentHole = Integer.parseInt(tvHoleCount.getText().toString());
        if (currentHole >= MAX_HOLE_COUNT){
			return;
		}
		currentHole++;
        tvHoleCount.setText(String.valueOf(currentHole));
        holeParsList.add(DEFAULT_PAR_VALUE);
        adapter.notifyDataSetChanged();
    }

    private void decrementHoleCount() {
        int currentHoleCount = Integer.parseInt(tvHoleCount.getText().toString());
        if(currentHoleCount > MIN_HOLE_COUNT) {
            currentHoleCount--;
            tvHoleCount.setText(String.valueOf(currentHoleCount));
            holeParsList.remove(currentHoleCount);
            adapter.notifyDataSetChanged();
        }
    }

    private void initializeCourseRepository() {
        if (courseRepository == null){
            courseRepository = new CourseFileRepository(context);
        }
    }

    //endregion
}
