package com.manleysoftware.michael.discgolfapp.ui.course;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.manleysoftware.michael.discgolfapp.domain.Course;
import com.manleysoftware.michael.discgolfapp.data.filerepository.CourseFileRepository;
import com.manleysoftware.michael.discgolfapp.ui.Adapters.CourseDataAdapter;
import com.manleysoftware.michael.discgolfapp.BuildConfig;
import com.manleysoftware.michael.discgolfapp.data.CourseRepository;
import com.manleysoftware.michael.discgolfapp.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.manleysoftware.michael.discgolfapp.ui.main.MainMenuActivity;

/**
 * Created by Michael on 6/22/2016.
 */
public class CourseEditorMenuActivity extends AppCompatActivity {
    private static final String COURSE_EDITOR_KEY = "Course Editor Key";
    private static final int COURSE_EDITOR_INTENT = 2;

	private ListView lvCourseList;
	private CourseRepository courseRepository;
    private final Context context = this;
    private CourseDataAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_editor_layout);

        if (BuildConfig.FLAVOR.equals("free")) {
            AdView adView = (AdView) findViewById(R.id.adViewCE);
            AdRequest request = new AdRequest.Builder().build();
//                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//                    .build();
			if (adView != null)
	            adView.loadAd(request);
        }


        initializeCourseRepository();

        lvCourseList = (ListView) findViewById(R.id.lvCourseList);

        //Used to add a view if the listview is not there.
		RelativeLayout courseEditorRelativeLayout = (RelativeLayout) findViewById(R.id.courseEditorRelativeLayout);


        if (isCoursesToDisplay()){//If nothing to populate adapter and listview fails to be created
            setupCourseDataAdapter();
        } else{
            changeLayoutToNoCoursesAlternative(courseEditorRelativeLayout);
        }

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
                                Course toDelete = (Course) adapter.getItem(position);
                                courseRepository.delete(toDelete, context);

                                adapter.notifyDataSetChanged();

                                //If listview is empty, recreate to show disc/empty view.
								if (adapter.getCount() == 0){
									recreate();
								}
                            }
                        });
                AlertDialog art = aat.create();

                art.show();
                return true;
            }
        });

		lvCourseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Bundle b = new Bundle();
				b.putInt("Position", position);
				Intent intent = new Intent(context, EditExistingCourseActivity.class);
				intent.putExtras(b);
				startActivity(intent);
			}
		});

		Button btnNewCourse = (Button) findViewById(R.id.btnNewCourse);
        btnNewCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), AddCourseMenuActivity.class);
                startActivityForResult(intent, 2);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        recreate();
    }

    private void changeLayoutToNoCoursesAlternative(RelativeLayout courseEditorRelativeLayout) {
        View messageLayout = getLayoutInflater().inflate(R.layout.listview_alternative_layout,null);

        ImageView backgroundImage = (ImageView) messageLayout.findViewById(R.id.ivImage);
        Bitmap bm5 = BitmapFactory
                .decodeResource(context.getResources(), R.drawable.roadrunner_500x500);
        backgroundImage.setImageBitmap(bm5);

        TextView tvNoListViewMessage = (TextView) messageLayout.findViewById(R.id.tvNoListViewMessage);
        tvNoListViewMessage.setText("Oops! No courses here!\nPlease add a course.");
        courseEditorRelativeLayout.addView(messageLayout);
    }


    @Override
    public void onBackPressed(){
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainMenuActivity.class));
        finish();
    }



    private boolean isCoursesToDisplay(){
        return courseRepository != null && courseRepository.getAllCourses().size() > 0;
    }

    private void setupCourseDataAdapter() {
        adapter = new CourseDataAdapter(context, courseRepository.getAllCourses());
        lvCourseList.setAdapter(adapter);
    }

    private void initializeCourseRepository() {
        courseRepository = new CourseFileRepository(context);
    }
}
