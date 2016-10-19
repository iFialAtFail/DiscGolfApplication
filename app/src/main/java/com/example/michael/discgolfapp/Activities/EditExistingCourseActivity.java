package com.example.michael.discgolfapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michael.discgolfapp.Adapters.CourseParDataAdapter;
import com.example.michael.discgolfapp.Model.Course;
import com.example.michael.discgolfapp.Model.CourseStorage;
import com.example.michael.discgolfapp.R;

import java.util.ArrayList;
import java.util.List;

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

	CourseStorage courseStorage;
	Course courseToEdit;
	List<Integer> holeList;
	ListView lvHoleOptions;
	CourseParDataAdapter adapter;
	Button btnIncrementHoleCount;
	Button btnDecrementHoleCount;
	Button btnSaveCourse;
	TextView tvHoleCount;
	TextView tvCourseName;
	Context context;

	//endregion

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_course_menu_layout);
		context = this;

		lvHoleOptions = (ListView) findViewById(R.id.lvHoleOptions);
		btnIncrementHoleCount = (Button) findViewById(R.id.btnIncrementHoleCount);
		btnDecrementHoleCount = (Button) findViewById(R.id.btnDecrementHoleCnt);
		btnSaveCourse = (Button) findViewById(R.id.btnSaveCourse);
		tvHoleCount = (TextView) findViewById(R.id.tvHoleCount);
		tvCourseName = (EditText) findViewById(R.id.tvCourseName);



		//disable the auto Keyboard
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		//if First time go set everything to course to edit
		if (savedInstanceState == null){

			//Setup course storage then get coure from it.
			tryRestoreCourseStorageObj();
			setupCourseFromPreviousActivity();
			holeList = toIntegerList(courseToEdit.getParArray());
			tvHoleCount.setText(String.valueOf(courseToEdit.getHoleCount()));
			tvCourseName.setText(courseToEdit.getName());
		}

		//On Orientation change or needing to implement
		//OnCreate again for any reason.
		//Set to the last known edited value from the view's.
		else{
			courseStorage = (CourseStorage) savedInstanceState.getSerializable(COURSE_STORAGE);
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
				String nameInput = tvCourseName.getText().toString();

				if (!Course.isValidCoursename(nameInput)){
					Toast toast = Toast.makeText(context, "Not a valid course name! Try again!",Toast.LENGTH_LONG);
					toast.show();
					return;
				}

				int[] populatedPars = toIntArray(adapter.getCourseList());

				courseToEdit.setName(nameInput);
				courseToEdit.setParArray(populatedPars);

				courseStorage.SaveToFile(getApplicationContext());

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

		outState.putSerializable(COURSE_STORAGE, courseStorage);
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
		for(int i = 0; i < array.length; i++){
			ret.add(array[i]);
		}
		return ret;
	}

	private void incrementViewHoleCount() {
		int currentHole = Integer.parseInt(tvHoleCount.getText().toString());
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

	private void tryRestoreCourseStorageObj() {
		Bundle b = this.getIntent().getExtras();
		if (b != null){
			courseStorage = (CourseStorage) b.getSerializable("CourseStorage");
		}
	}

	private void setupCourseFromPreviousActivity() {
		Bundle b = this.getIntent().getExtras();
		if (b == null) {
			return;
		}

		if (courseStorage != null) {
			courseToEdit = courseStorage.getCourseStorage().get(b.getInt("Position"));
		}
	}


}
