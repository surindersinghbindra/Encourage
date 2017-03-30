package com.nearnia.encouragement.adapters;

import java.util.ArrayList;

import com.nearnia.encouragement.util.DCircularImageView;
import com.nearnia.encouragement.R;
import com.nearnia.encouragement.VolleySingleton;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.nearnia.encouragement.beanclasses.OwnWrittenQuotes;

public class OtherWrittenAdapter extends BaseAdapter {
	Context mContext;
	ArrayList<OwnWrittenQuotes> arraylist;

	public OtherWrittenAdapter(Context ctx, ArrayList<OwnWrittenQuotes> arraylist) {
		this.mContext = ctx;
		this.arraylist = arraylist;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arraylist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final ViewHolder viewHolder;
		if (convertView == null) {
			// inflate the layout
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			convertView = inflater.inflate(R.layout.your_own_written_list_items, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.tv = (TextView) convertView.findViewById(R.id.qoute_written);
			viewHolder.tv2 = (TextView) convertView.findViewById(R.id.sub_category);
			viewHolder.circularImageView = (DCircularImageView) convertView.findViewById(R.id.user_profile_image);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();

		}

		viewHolder.tv.setText( arraylist.get(position).getQuote().replace( "\\", "" ) );
		viewHolder.tv2.setText("SubCategory: " + arraylist.get(position).getSubcategory());

		Picasso.with(mContext).load(VolleySingleton.SERVER_URL_WITH_SLASH + arraylist.get(position).getPicture())
				.placeholder(R.drawable.user1).into(viewHolder.circularImageView);

		// TODO Auto-generated method stub
		return convertView;
	}

	public static class ViewHolder {

		TextView tv, tv2;
		DCircularImageView circularImageView;
	}

}
