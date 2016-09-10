package com.example.michael.discgolfapp.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
 * Created by Michael on 6/23/2016.
 */
public class CoursePickerActivity extends AppCompatActivity {
    private static final String COURSE_PICKER_KEY = "Course Picker Key";
    private static final int COURSE_PICKER_INTENT = 1;

    CourseDataAdapter adapter;
    CourseStorage courseStorage;
    Context context = this;
	RelativeLayout coursePickerRelativeLayout;
    ListView lvCourseList;
    Button btnNewCourse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_picker_layout);
        setupCourseStorage();

        lvCourseList = (ListView) findViewById(R.id.lvCourseList);
		coursePickerRelativeLayout = (RelativeLayout) findViewById(R.id.coursePickerRelativeLayout);
        if (!setupCourseListView()){
			View messageLayout = getLayoutInflater().inflate(R.layout.listview_alternative_layout,null);

			ImageView backgroundImage = (ImageView) messageLayout.findViewById(R.id.ivImage);
			Bitmap bm5 = BitmapFactory
					.decodeResource(context.getResources(), R.drawable.roadrunner_500x500);
			backgroundImage.setImageBitmap(bm5);

			TextView tvNoListViewMessage = (TextView) messageLayout.findViewById(R.id.tvNoListViewMessage);
			tvNoListViewMessage.setText("Oops! No courses here!\nPlease add a course.");
			coursePickerRelativeLayout.addView(messageLayout);
		}
        lvCourseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Course course = (Course)adapter.getItem(position);
                if (course == null){
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable("Course",course);
                Intent intent = new Intent(getApplicationContext(),PlayerPickerActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
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
                                courseStorage.SaveToFile(context);
                                adapter.notifyDataSetChanged();
								if (adapter.getCount()== 0){
									recreate();
								}
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
                Intent intent = new Intent(getApplicationContext(), AddCourseMenuActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(COURSE_PICKER_KEY,COURSE_PICKER_INTENT);
                bundle.putSerializable("CourseStorage", courseStorage);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainMenuActivity.class));
        finish();
    }

    private boolean setupCourseListView(){
        if (courseStorage != null && courseStorage.getStoredCoursesCount() > 0){
            adapter = new CourseDataAdapter(context, courseStorage.getCourseStorage());
            lvCourseList.setAdapter(adapter);
			return true;
        }
		return false;
    }

    private void setupCourseStorage() {
        courseStorage = CourseStorage.LoadFromFile(context);
        if (courseStorage == null){
            courseStorage = new CourseStorage();
            Toast toast = Toast.makeText(getApplicationContext(),"File Not Found",Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
