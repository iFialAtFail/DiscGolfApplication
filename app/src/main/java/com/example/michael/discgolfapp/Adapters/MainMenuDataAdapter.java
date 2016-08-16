package com.example.michael.discgolfapp.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.michael.discgolfapp.R;

/**
 * Created by Michael on 8/16/2016.
 */
public class MainMenuDataAdapter extends BaseAdapter {

	Context context;
	String[] menuLabels;

	public MainMenuDataAdapter(Context context, String[] menuLabels){
		this.context = context;
		this.menuLabels = menuLabels;
	}

	@Override
	public int getCount() {
		return menuLabels.length;
	}

	@Override
	public Object getItem(int position) {
		return menuLabels[position];
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.main_menu_listview_row, parent, false);
		String title = (String) getItem(position);
		ImageView ivBackground = (ImageView) rowView.findViewById(R.id.ivBackground);
		TextView tvTitle = (TextView) rowView.findViewById(R.id.tvTitle);


		switch (title){
			case "New Game":
				Bitmap bm = BitmapFactory
						.decodeResource(context.getResources(), R.mipmap.nuke_banner);
				ivBackground.setImageBitmap(bm);
				tvTitle.setText(title);
				break;
			case "Resume Game":
				Bitmap bm2 = BitmapFactory
						.decodeResource(context.getResources(), R.mipmap.buzz_banner);
				ivBackground.setImageBitmap(bm2);
				tvTitle.setText(title);
				break;
			case "Course Editor":
				Bitmap bm3 = BitmapFactory
						.decodeResource(context.getResources(), R.mipmap.roadrunner_banner);
				ivBackground.setImageBitmap(bm3);
				tvTitle.setText(title);
				break;
			case "Player Editor":
				Bitmap bm4 = BitmapFactory
						.decodeResource(context.getResources(), R.mipmap.jade_banner);
				ivBackground.setImageBitmap(bm4);
				tvTitle.setText(title);
				break;
			case "Score Cards":
				Bitmap bm5 = BitmapFactory
						.decodeResource(context.getResources(), R.mipmap.katana_banner);
				ivBackground.setImageBitmap(bm5);
				tvTitle.setText(title);
				break;
		}

		return rowView;
	}
}
