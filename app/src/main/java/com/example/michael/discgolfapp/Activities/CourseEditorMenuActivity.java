package com.example.michael.discgolfapp.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.michael.discgolfapp.Adapters.CourseDataAdapter;
import com.example.michael.discgolfapp.Model.Course;
import com.example.michael.discgolfapp.Model.CourseStorage;
import com.example.michael.discgolfapp.R;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Michael on 6/22/2016.
 */
public class CourseEditorMenuActivity extends Activity {
    Button btnNewCourse;
    ListView lvCourseList;
    CourseStorage courseStorage;
    Context context = this;
    CourseDataAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_editor_layout);

        setupCourseStorage();

        lvCourseList = (ListView) findViewById(R.id.lvCourseList);
        setupCourseListView();
        lvCourseList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder aat = new AlertDialog.Builder(context);
                aat.setTitle("Delete?")
                        .setMessage("Are you sure you want to delete "+parent.getItemAtPosition(position).toString()+"?")
                        .setCancelable(true)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                dialog.cancel();
                            }

                        })
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                //Make the change
                                courseStorage.DeleteCourseFromStorage(position);

                                //Commit the change to persistant memory
                                saveCourseStorage(courseStorage);
                                onCreate(null);
                            }
                        });
                AlertDialog art = aat.create();

                art.show();
                return true;
            }
        });

        btnNewCourse = (Button) findViewById(R.id.btnNewCourse);
        btnNewCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Bundle up object to pass over
                Bundle extras = new Bundle();
                extras.putSerializable("CourseStorage",courseStorage);

                //Create intent and add bundle to it.
                Intent intent = new Intent(getApplicationContext(), AddCourseMenuActivity.class);
                intent.putExtras(extras);

                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy(){
        saveCourseStorage(courseStorage);
        super.onDestroy();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainMenuActivity.class));
        finish();
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

    private CourseStorage retrieveCourseStorage(){
        CourseStorage _courseStorage;
        try {
            // Read from disk using FileInputStream
            FileInputStream fis = context.openFileInput("courseList.data");

            // Read object using ObjectInputStream
            ObjectInputStream ois =
                    new ObjectInputStream (fis);

            // Read an object
            Object obj = ois.readObject();

            if (obj instanceof CourseStorage)
            {
                _courseStorage = (CourseStorage) obj;
                return _courseStorage;
            }

        }catch (Exception ex){
            Toast toast = Toast.makeText(context,"Something done messed up",Toast.LENGTH_LONG);
            toast.show();
            ex.printStackTrace();

            return null;
        }
        return null;
    }

    private void setupCourseListView(){
        if (courseStorage != null && courseStorage.getStoredCoursesCount() > 0){
            adapter = new CourseDataAdapter(context, courseStorage.getCourseStorage());
            lvCourseList.setAdapter(adapter);
        }
    }

    private void setupCourseStorage() {
        courseStorage = retrieveCourseStorage();
        if (courseStorage == null){
            courseStorage = new CourseStorage();
            Toast toast = Toast.makeText(getApplicationContext(),"File Not Found",Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
