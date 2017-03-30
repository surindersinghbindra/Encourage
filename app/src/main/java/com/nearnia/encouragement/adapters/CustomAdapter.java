package com.nearnia.encouragement.adapters;

import java.util.ArrayList;

import com.nearnia.encouragement.R;
import com.nearnia.encouragement.WriteYourOwn;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.nearnia.encouragement.beanclasses.SubChannels;

public class CustomAdapter extends ArrayAdapter<String> {

	private Activity activity;
	private ArrayList<SubChannels> data;
	public Resources res;
	SubChannels tempValues = null;
	LayoutInflater inflater;

	public CustomAdapter(WriteYourOwn activitySpinner, int textViewResourceId, ArrayList objects, Resources resLocal) {
		super(activitySpinner, textViewResourceId, objects);

		activity = activitySpinner;
		data = objects;
		res = resLocal;

		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}

	public View getCustomView(int position, View convertView, ViewGroup parent) {

		View row = inflater.inflate(R.layout.custom_spinner, parent, false);

		tempValues = null;
		tempValues = data.get(position);

		TextView label = (TextView) row.findViewById(R.id.SubCategoryName);

		label.setText(tempValues.getSubChannelName());

		return row;
	}
}