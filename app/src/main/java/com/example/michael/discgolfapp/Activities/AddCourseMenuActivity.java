package com.example.michael.discgolfapp.Activities;

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

import com.example.michael.discgolfapp.Adapters.CourseParDataAdapter;
import com.example.michael.discgolfapp.Model.Course;
import com.example.michael.discgolfapp.Model.CourseStorage;
import com.example.michael.discgolfapp.Model.PlayerStorage;
import com.example.michael.discgolfapp.R;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 6/23/2016.
 */
public class AddCourseMenuActivity extends Activity {
    //region Class Constants
    private static final String HOLE_ARRAY = "Hole Array";
    private static final String HOLE_COUNT = "Hole Count";
    //endregion

    //region Private Fields

    CourseStorage courseStorage;
    List<Integer> holeList;
    ListView lvHoleOptions;
    CourseParDataAdapter adapter;
    Button btnIncrementHoleCount;
    Button btnDecrementHoleCount;
    Button btnSaveCourse;
    TextView tvHoleCount;
    TextView tvCourseName;
    Context context = this;

    //endregion

    //region Android Product Lifecycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_course_menu_layout);

        lvHoleOptions = (ListView) findViewById(R.id.lvHoleOptions);
        btnIncrementHoleCount = (Button) findViewById(R.id.btnIncrementHoleCount);
        btnDecrementHoleCount = (Button) findViewById(R.id.btnDecrementHoleCnt);
        btnSaveCourse = (Button) findViewById(R.id.btnSaveCourse);
        tvHoleCount = (TextView) findViewById(R.id.tvHoleCount);
        tvCourseName = (EditText) findViewById(R.id.tvCourseName);

        tryRestoreCourseStorageObj();

        //disable the auto Keyboard
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //if First time go
        if (savedInstanceState == null){
            holeList = new ArrayList<Integer>();
            setupArrayListToThrees();
        }

        //On Orientation change or needing to implement
        //OnCreate again for any reason.
        else{
            holeList = new ArrayList<>();
            holeList.addAll(toIntegerList(savedInstanceState.getIntArray(HOLE_ARRAY)));
            tvHoleCount.setText(savedInstanceState.getString(HOLE_COUNT));
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
                //All I need is the course name and an int array of the populated par's.
                if (!Course.isValidCoursename(tvCourseName.getText().toString())){
                    Toast toast = Toast.makeText(context, "Not a valid course name! Try again!",Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }

                String courseName = tvCourseName.getText().toString();
                int[] populatedPars = toIntArray(adapter.getCourseList());

                Course course = new Course(courseName, populatedPars);
                courseStorage.AddCourseToStorage(course);

                saveCourseStorage(courseStorage);

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

        outState.putIntArray(HOLE_ARRAY, toIntArray(holeList));
        outState.putString(HOLE_COUNT, tvHoleCount.getText().toString());

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        holeList.addAll(toIntegerList(savedInstanceState.getIntArray(HOLE_ARRAY)));
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

    private void setupArrayListToThrees(){
        for(int i = 0; i < Integer.parseInt(tvHoleCount.getText().toString()); i++){
            holeList.add(3);
        }
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

    private void saveCourseStorage(Object myObject){
        try {
            // Write to disk with FileOutputStream
            FileOutputStream fos = getApplicationContext().openFileOutput("courseList.data", Context.MODE_PRIVATE);

            // Write object with ObjectOutputStream
            ObjectOutputStream oos = new
                    ObjectOutputStream (fos);

            // Write object out to disk
            oos.writeObject ( myObject );

            oos.close();
            fos.close();

        } catch(Exception ex){
            ex.printStackTrace();
            Toast toast = Toast.makeText(getApplicationContext(),"File didn't save",Toast.LENGTH_LONG);
            toast.show();
        }
    }


    //endregion
}
