package com.manleysoftware.michael.discgolfapp.ui.course;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.manleysoftware.michael.discgolfapp.data.filerepository.CourseFileRepository;
import com.manleysoftware.michael.discgolfapp.ui.Adapters.CourseParDataAdapter;
import com.manleysoftware.michael.discgolfapp.domain.Course;
import com.manleysoftware.michael.discgolfapp.data.CourseRepository;
import com.manleysoftware.michael.discgolfapp.R;

import java.util.ArrayList;
import java.util.List;

import static com.manleysoftware.michael.discgolfapp.domain.Course.MAX_HOLE_COUNT;

/**
 * Created by Michael on 8/28/2016.
 */
public class EditExistingCourseActivity extends AppCompatActivity {

	//region Constants

	private static final String HOLE_ARRAY = "Hole Array";
	private static final String HOLE_COUNT = "Hole Count";
	private static final String COURSE_NAME = "Course Name";
	private static final String COURSE_OBJECT = "Course Object";
	private static final String COURSE_STORAGE = "Course Storage";

	//endregion


	//region Private Fields

	private CourseRepository courseRepository;
	private Course courseToEdit;
	private List<Integer> holeList;
	private CourseParDataAdapter adapter;
	private TextView tvHoleCount;
	private TextView tvCourseName;
	private Context context;

	//endregion

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_course_menu_layout);
		context = this;

		ListView lvHoleOptions = (ListView) findViewById(R.id.lvHoleOptions);
		Button btnIncrementHoleCount = (Button) findViewById(R.id.btnIncrementHoleCount);
		Button btnDecrementHoleCount = (Button) findViewById(R.id.btnDecrementHoleCnt);
		Button btnSaveCourse = (Button) findViewById(R.id.btnSaveCourse);
		tvHoleCount = (TextView) findViewById(R.id.tvHoleCount);
		tvCourseName = (EditText) findViewById(R.id.tvCourseName);



		//disable the auto Keyboard
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		//if First time go set everything to course to edit
		if (savedInstanceState == null){

			//Setup course storage then get coure from it.
			initializeCourseRepository();
			setupCourseFromPreviousActivity();
			holeList = toIntegerList(courseToEdit.getParArray());
			tvHoleCount.setText(String.valueOf(courseToEdit.getHoleCount()));
			tvCourseName.setText(courseToEdit.getName());
		}

		//On Orientation change or needing to implement
		//OnCreate again for any reason.
		//Set to the last known edited value from the view's.
		else{
			courseRepository = (CourseRepository) savedInstanceState.getSerializable(COURSE_STORAGE);
			courseToEdit = (Course) savedInstanceState.getSerializable(COURSE_OBJECT);
			holeList = toIntegerList(savedInstanceState.getIntArray(HOLE_ARRAY));
			tvHoleCount.setText(String.valueOf(savedInstanceState.getString(HOLE_COUNT)));
			tvCourseName.setText(savedInstanceState.getString(COURSE_NAME));
		}

		adapter = new CourseParDataAdapter(context,holeList);
		lvHoleOptions.setAdapter(adapter);



		btnIncrementHoleCount.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				incrementViewHoleCount();
			}
		});
		btnDecrementHoleCount.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				decrementViewHoleCount();
			}
		});

		btnSaveCourse.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String rawNameInput = tvCourseName.getText().toString();
				String nameInput = rawNameInput.trim();

				if (!Course.isValidCoursename(nameInput)){
					Toast toast = Toast.makeText(context, "Not a valid course name! Try again!",Toast.LENGTH_LONG);
					toast.show();
					return;
				}

				int[] populatedPars = toIntArray(adapter.getCourseList());

				courseToEdit.setName(nameInput);
				courseToEdit.setParArray(populatedPars);

				courseRepository.update(courseToEdit,getApplicationContext());

				Intent intent = new Intent(getApplicationContext(),CourseEditorMenuActivity.class);
				startActivity(intent);
			}
		});
	}



	//endregion

	//region Overridden Methods

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putSerializable(COURSE_OBJECT, courseToEdit);
		outState.putIntArray(HOLE_ARRAY, toIntArray(holeList));
		outState.putString(HOLE_COUNT, tvHoleCount.getText().toString());
		outState.putString(COURSE_NAME, tvCourseName.getText().toString());
	}

	//endregion

	//region Private Helper Methods

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

	private void incrementViewHoleCount() {
		int currentHole = Integer.parseInt(tvHoleCount.getText().toString());
		if (currentHole >= MAX_HOLE_COUNT){
			return;
		}
		currentHole++;
		tvHoleCount.setText(String.valueOf(currentHole));
		holeList.add(3);
		adapter.notifyDataSetChanged();
	}

	private void decrementViewHoleCount() {
		int currentHole = Integer.parseInt(tvHoleCount.getText().toString());
		if(currentHole > 1) {
			currentHole--;
			tvHoleCount.setText(String.valueOf(currentHole));
			holeList.remove(currentHole);
			adapter.notifyDataSetChanged();
		}
	}

	private void initializeCourseRepository() {
		courseRepository = new CourseFileRepository(context);
	}

	private void setupCourseFromPreviousActivity() {
		Bundle b = this.getIntent().getExtras();
		if (b == null) {
			return;
		}

		if (courseRepository != null) {
			courseToEdit = courseRepository.getAllCourses().get(b.getInt("Position"));
		}
	}


}
