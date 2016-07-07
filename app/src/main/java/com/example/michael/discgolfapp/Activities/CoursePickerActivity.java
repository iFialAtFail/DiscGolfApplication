package com.example.michael.discgolfapp.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.michael.discgolfapp.R;

/**
 * Created by Michael on 6/23/2016.
 */
public class CoursePickerActivity extends Activity {
    Button btnNewCourse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_picker_layout);

        btnNewCourse = (Button) findViewById(R.id.btnNewCourse);
        btnNewCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddCourseMenuActivity.class);
                startActivity(intent);
            }
        });

    }
}
