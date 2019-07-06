package com.manleysoftware.michael.discgolfapp.view.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.manleysoftware.michael.discgolfapp.Model.Course;
import com.manleysoftware.michael.discgolfapp.R;

import java.util.List;

/**
 * Created by Michael on 7/6/2016.
 */
public class CourseDataAdapter extends BaseAdapter {

    private final List<Course> courseList;
    private final Context context;


    public CourseDataAdapter(Context context, List<Course> courseStorage) {
        courseList = courseStorage;
        this.context = context;
    }

    @Override
    public int getCount() {
        return courseList.size();
    }

    @Override
    public Object getItem(int position) {
        return courseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.course_listview_row, parent, false);
        Course course = (Course) getItem(position);

        ImageView thumbnail = (ImageView) rowView.findViewById(R.id.imgThumbnail);
        TextView tvNameText = (TextView) rowView.findViewById(R.id.tvPlayerName);

        if (position % 2 == 0) {
            thumbnail.setImageResource(R.mipmap.buzz_blank_100x100);
        }
        else {
            thumbnail.setImageResource(R.mipmap.roadrunnder_blank_100x100);
        }

        tvNameText.setText(course.getName());
        return rowView;
    }
}