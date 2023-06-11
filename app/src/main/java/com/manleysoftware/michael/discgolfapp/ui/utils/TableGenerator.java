package com.manleysoftware.michael.discgolfapp.ui.utils;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.manleysoftware.michael.discgolfapp.R;
import com.manleysoftware.michael.discgolfapp.domain.Scorecard;

public class TableGenerator {

    public TableLayout generateTable(Context context, TableLayout tableLayout, Scorecard scoreCard){
        tableLayout.removeAllViews();
        //Generate dynamic table.
        for (int i = 0; i < scoreCard.playerCount() + 2; i++){ //Plus 2 for Par And Hole # rows.
            // outer for loop
            TableRow row = new TableRow(context);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            // inner for loop
            for (int j = 0; j < scoreCard.course().getHoleCount() + 2; j++) { // Plus 2 for Name and Total Columns

                int LAYOUT_WIDTH = 50;
                int FIRST_COLUMN_WIDTH = 80;
                if (i == 0 && j == 0){ //First Cell is Hole
                    TextView tv = setupTextViewInTable("Hole",applyLayoutWidth(FIRST_COLUMN_WIDTH, context), R.drawable.cell_shape_light_green, context);
                    row.addView(tv);
                }

                else if (i == 0 && j == scoreCard.course().getHoleCount() + 1){ //Last Cell is T
                    TextView tv = setupTextViewInTable("T",applyLayoutWidth(LAYOUT_WIDTH, context), R.drawable.cell_shape_light_green, context);
                    row.addView(tv);
                }

                else if (i == 0){
                    TextView tv = setupTextViewInTable(String.valueOf(j),applyLayoutWidth(LAYOUT_WIDTH, context), R.drawable.cell_shape_light_green, context);
                    row.addView(tv);
                }

                if (i == 1 && j == 0){ // First Cell in second row is "Par"
                    TextView tv = setupTextViewInTable("Par",applyLayoutWidth(FIRST_COLUMN_WIDTH, context), R.drawable.cell_shape_light_green, context);
                    row.addView(tv);
                }

                else if (i == 1 && j == scoreCard.course().getHoleCount() + 1){ // Last Cell is Par Total
                    TextView tv = setupTextViewInTable(String.valueOf(scoreCard.course().getParTotal()),applyLayoutWidth(LAYOUT_WIDTH, context), R.drawable.cell_shape_light_green, context);
                    row.addView(tv);
                }

                else if (i == 1 && j != scoreCard.course().getHoleCount()+1){
                    TextView tv = setupTextViewInTable(String.valueOf(scoreCard.course().getParArray()[j-1]),applyLayoutWidth(LAYOUT_WIDTH, context), R.drawable.cell_shape_light_green, context);
                    row.addView(tv);
                }

                if (i >= 2 && j == 0){ //Set Player Names
                    TextView tv = setupTextViewInTable(scoreCard.players().get(i-2).getName(),applyLayoutWidth(FIRST_COLUMN_WIDTH, context), R.drawable.cell_shape, context);
                    row.addView(tv);
                }

                if (i >= 2 && j >0 && j != scoreCard.course().getHoleCount()+1){ //handle player scores
                    TextView tv = setupTextViewInTable(String.valueOf(scoreCard.players().get(i-2).getScores()[j-1]),applyLayoutWidth(LAYOUT_WIDTH, context), R.drawable.cell_shape, context);
                    row.addView(tv);
                }

                if (i >= 2 && j == scoreCard.course().getHoleCount() + 1){
                    TextView tv = setupTextViewInTable(String.valueOf(scoreCard.players().get(i-2).getCurrentTotal()),applyLayoutWidth(LAYOUT_WIDTH, context), R.drawable.cell_shape, context);
                    row.addView(tv);
                }


            }
            tableLayout.addView(row);
        }
        return tableLayout;
    }

    private TextView setupTextViewInTable(String setText, int layoutWidthParam, int resourceID, Context context){
        TextView tv = new TextView(context);
        tv.setLayoutParams(new TableRow.LayoutParams(layoutWidthParam,
                TableRow.LayoutParams.MATCH_PARENT, 1.0f));
        tv.setBackgroundResource(resourceID);
        tv.setTextColor(Color.BLACK);
        //tv.setTextAppearance(context,android.R.style.TextAppearance_Large);
        int TEXT_SIZE = 20;
        tv.setTextSize(TEXT_SIZE);
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(15, 5, 15, 5);
        tv.setText(setText);
        tv.setSingleLine();
        return tv;
    }

    private int applyLayoutWidth(int width, Context context){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, context.getResources().getDisplayMetrics());
    }
}
