package com.manleysoftware.michael.discgolfapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.manleysoftware.michael.discgolfapp.R;

import java.util.List;

/**
 * Created by Michael on 7/8/2016.
 */
public class CourseParDataAdapter extends BaseAdapter {

    private final Context context;
    private LayoutInflater inflater;
    private final List<Integer> course;

    public CourseParDataAdapter(Context context, List<Integer> course) {
        this.context = context;
        this.course = course;
    }

    //custom get method to return list of integers to create course object
    //This just makes it easier (If it works)
    public List<Integer> getCourseList(){
        return course;
    }

    @Override
    public int getCount() {
        return course.size();
    }

    @Override
    public Object getItem(int position) {
        return course.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.course_hole_par_editor_row, parent, false);
        //Player player = (Player) getItem(position);


        TextView tvHoleNumber = (TextView)rowView.findViewById(R.id.tvHoleNumber);
        TextView tvCurrentHoleCustomPar = (TextView) rowView.findViewById(R.id.tvCurrentHoleCustomPar);
        Button btnDecrementParValue = (Button)rowView.findViewById(R.id.btnDecrementParValue);
        Button btnIncrementParValue = (Button)rowView.findViewById(R.id.btnIncrementParValue);

        tvHoleNumber.setText("Hole " + String.valueOf(position + 1));
        tvCurrentHoleCustomPar.setText(String.valueOf(course.get(position)));

        btnDecrementParValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (course.get(position) > 2){//since we're talking about par's, they can't be below 2.
                    course.set(position, (course.get(position)- 1));
                    notifyDataSetChanged();
                }

            }
        });

        btnIncrementParValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                course.set(position,(course.get(position)+1));
                notifyDataSetChanged();
            }
        });

        return rowView;
    }
}
