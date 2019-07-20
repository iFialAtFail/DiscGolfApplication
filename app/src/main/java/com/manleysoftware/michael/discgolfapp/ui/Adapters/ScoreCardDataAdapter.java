package com.manleysoftware.michael.discgolfapp.ui.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.manleysoftware.michael.discgolfapp.data.Model.ScoreCard;
import com.manleysoftware.michael.discgolfapp.data.ScorecardRepository;
import com.manleysoftware.michael.discgolfapp.R;

import java.util.List;

/**
 * Created by Michael on 8/21/2016.
 */
public class ScoreCardDataAdapter extends BaseAdapter{

	private final List<ScoreCard> scoreCardList;
	private final Context context;

	public ScoreCardDataAdapter(Context context, ScorecardRepository scorecardRepository){
		this.context = context;
		this.scoreCardList = scorecardRepository.getScoreCardStorage();
	}

	@Override
	public int getCount() {
		return scoreCardList.size();
	}

	@Override
	public Object getItem(int position) {
		return scoreCardList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.scorecard_listview_row, parent, false);
		ScoreCard scoreCard = (ScoreCard) getItem(position);

		TextView courseName = (TextView) rowView.findViewById(R.id.tvCourseName);
		TextView date = (TextView) rowView.findViewById(R.id.tvDateLP);
		TextView playersList = (TextView) rowView.findViewById(R.id.tvPlayersList);

		courseName.setText(scoreCard.getCourseName());
		courseName.setTextColor(Color.BLACK);
		date.setText(scoreCard.getDate());
		playersList.setText("Players:  " + scoreCard.getPlayersNames());

		return rowView;
	}
}
