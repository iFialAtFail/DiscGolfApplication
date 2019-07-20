package com.manleysoftware.michael.discgolfapp.view;

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

import com.manleysoftware.michael.discgolfapp.data.CourseFileRepository;
import com.manleysoftware.michael.discgolfapp.view.Adapters.CourseDataAdapter;
import com.manleysoftware.michael.discgolfapp.Model.Course;
import com.manleysoftware.michael.discgolfapp.data.CourseRepository;
import com.manleysoftware.michael.discgolfapp.R;

/**
 * Created by Michael on 6/23/2016.
 */
public class CoursePickerActivity extends AppCompatActivity {
    private static final String COURSE_PICKER_KEY = "Course Picker Key";
    private static final int COURSE_PICKER_INTENT = 1;

    private CourseDataAdapter adapter;
    private CourseRepository courseRepository;
    private final Context context = this;
	private ListView lvCourseList;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_picker_layout);
        initializeCourseRepository();

        lvCourseList = (ListView) findViewById(R.id.lvCourseList);
		RelativeLayout coursePickerRelativeLayout = (RelativeLayout) findViewById(R.id.coursePickerRelativeLayout);
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
                Intent intent = new Intent(getApplicationContext(), RoundPlayerPickerActivity.class);
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
                                dialog.cancel();
                            }

                        })
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Make the change
                                Course courseToDelete = (Course)adapter.getItem(position);
                                courseRepository.removeCourse(courseToDelete);

                                //Commit the change to persistant memory
                                courseRepository.Save(context);
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
		Button btnNewCourse = (Button) findViewById(R.id.btnNewCourse);
        btnNewCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddCourseMenuActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(COURSE_PICKER_KEY,COURSE_PICKER_INTENT);
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
        if (courseRepository != null && courseRepository.getCourses().size() > 0){
            adapter = new CourseDataAdapter(context, courseRepository.getCourses());
            lvCourseList.setAdapter(adapter);
			return true;
        }
		return false;
    }

    private void initializeCourseRepository() {
        courseRepository = new CourseFileRepository(context);
    }
}
