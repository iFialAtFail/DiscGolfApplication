package com.manleysoftware.michael.discgolfapp.ui.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.manleysoftware.michael.discgolfapp.domain.Scorecard;
import com.manleysoftware.michael.discgolfapp.R;

import java.util.List;

/**
 * Created by Michael on 8/21/2016.
 */
public class ScoreCardDataAdapter extends BaseAdapter{

	private List<Scorecard> scorecardList;
	private final Context context;

	public ScoreCardDataAdapter(Context context, List<Scorecard> scorecards){
		this.context = context;
		this.scorecardList = scorecards;
	}

	public void updateScorecards(List<Scorecard> scorecards){
	    this.scorecardList = scorecards;
	    notifyDataSetChanged();
    }

	@Override
	public int getCount() {
		return scorecardList.size();
	}

	@Override
	public Object getItem(int position) {
		return scorecardList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.scorecard_listview_row, parent, false);
		Scorecard scoreCard = (Scorecard) getItem(position);

		TextView courseName = (TextView) rowView.findViewById(R.id.tvCourseName);
		TextView date = (TextView) rowView.findViewById(R.id.tvDateLP);
		TextView playersList = (TextView) rowView.findViewById(R.id.tvPlayersList);

		courseName.setText(scoreCard.courseName());
		courseName.setTextColor(Color.BLACK);
		date.setText(scoreCard.displayDate());
		playersList.setText("Players:  " + scoreCard.playerNames());

		return rowView;
	}
}
