package com.nearnia.encouragement.adapters;

import java.util.ArrayList;
import java.util.List;

import com.nearnia.encouragement.R;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.nearnia.encouragement.beanclasses.Channels;

public class ListViewAdapter extends BaseAdapter {

	Context context;
	LayoutInflater inflater;
//	ImageLoader imageLoader;
	private List<Channels> channelList = null;
	private ArrayList<Channels> arraylist;

	public ListViewAdapter(Context context, List<Channels> list) {
		this.context = context;
		this.channelList = list;
		inflater = LayoutInflater.from(context);
		this.arraylist = new ArrayList<Channels>();
		this.arraylist.addAll(list);
//		imageLoader = new ImageLoader(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return channelList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return channelList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.tabhostlistview_items, parent, false);
		}

		TextView category = (TextView) convertView.findViewById(R.id.tv1);

		ImageView imageV = (ImageView) convertView.findViewById(R.id.iv1);

		category.setText(channelList.get(position).getChannelName());

		Picasso.with(context).load(channelList.get(position).getIconImageUrl()).placeholder(R.drawable.logo)
				.error(R.drawable.logo).into(imageV);

		// TODO Auto-generated method stub
		return convertView;
	}

}
