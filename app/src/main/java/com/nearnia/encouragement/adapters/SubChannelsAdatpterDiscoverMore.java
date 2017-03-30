package com.nearnia.encouragement.adapters;

import java.util.ArrayList;
import java.util.List;

import com.nearnia.encouragement.AllQoutesInSubCategoryFragment;
import com.nearnia.encouragement.LoginActivity;
import com.nearnia.encouragement.R;
import com.nearnia.encouragement.UniversalSingleton;
import com.nearnia.encouragement.VolleySingleton;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.nearnia.encouragement.beanclasses.QuotesYouLiked;
import com.nearnia.encouragement.beanclasses.SubChannels;

public class SubChannelsAdatpterDiscoverMore extends BaseAdapter {

	Context context;
	LayoutInflater inflater;
	// ImageLoader imageLoader;
	private List<SubChannels> subChannelsList;
	// private ArrayList<SubChannels> arraylist;

	public SubChannelsAdatpterDiscoverMore(Context context, List<SubChannels> list) {
		this.context = context;
		this.subChannelsList = list;
		inflater = LayoutInflater.from(context);
		// this.arraylist = new ArrayList<SubChannels>();
		// this.arraylist.addAll(list);
		// imageLoader = new ImageLoader(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return subChannelsList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return subChannelsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.discover_more_single_item, parent, false);
		}

		convertView.setTag(position);
		ImageView whilteBookSub = (ImageView) convertView.findViewById(R.id.whilteBookSub);

		whilteBookSub.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (VolleySingleton.userLogin.getBoolean(VolleySingleton.LOGIN_STATUS, false)) {

					AllQoutesInSubCategoryFragment allQoutesInSubCategoryFragment = new AllQoutesInSubCategoryFragment();

					QuotesYouLiked obj = (QuotesYouLiked) UniversalSingleton.getInstance().getObject();
					Bundle bundle = new Bundle();
					bundle.putString("SUB_CATEGORY_ID", subChannelsList.get(position).subChannelId);
					bundle.putString("CATEGORY_NAME", obj.getCategory());
					bundle.putString("SUB_CATEGORY_NAME", subChannelsList.get(position).subChannelName);
					allQoutesInSubCategoryFragment.setArguments(bundle);
					FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
					FragmentTransaction ft = fragmentManager.beginTransaction().addToBackStack(null);
					ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.left_to_right, R.anim.from_right_to_left);
					ft.replace(R.id.fragment_container, allQoutesInSubCategoryFragment, "detailFragment");
					// Start the animated transition.
					ft.commit();
				} else {
					Intent intent = new Intent(context, LoginActivity.class);
					context.startActivity(intent);
					((FragmentActivity) context).finish();

				}

			}
		});

		final CheckBox cb = (CheckBox) convertView.findViewById(R.id.checbox_sub_categoty);
		cb.setTag(position);

		// SubChannels p = getSubChannel(position);
		TextView subcategory = (TextView) convertView.findViewById(R.id.sub_category_name);
		subcategory.setText(subChannelsList.get(position).getSubChannelName());

		// convertView.setOnClickListener(null);
		cb.setOnCheckedChangeListener(null);
		cb.setChecked(subChannelsList.get(position).isSelected);
		// cb.isChecked();

		OnCheckedChangeListener myCheckChangList_subchannel_adapter = new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				getSubChannel((Integer) buttonView.getTag()).box = isChecked;

			}
		};
		cb.setOnCheckedChangeListener(myCheckChangList_subchannel_adapter);

		OnClickListener myCheckChangList_subchannel_adapter1 = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Toast.makeText(context, "CLICKED",
				// Toast.LENGTH_SHORT).show();
				if (getSubChannel((Integer) v.getTag()).box == true) {
					cb.setChecked(false);

					getSubChannel((Integer) v.getTag()).box = false;

				} else {
					cb.setChecked(true);
					getSubChannel((Integer) v.getTag()).box = true;

				}

				return;
			}
		};

		// convertView.setOnClickListener(myCheckChangList_subchannel_adapter1);

		return convertView;
	}

	SubChannels getSubChannel(int position) {
		return ((SubChannels) getItem(position));
	}

	public ArrayList<SubChannels> getBox() {
		ArrayList<SubChannels> box = new ArrayList<SubChannels>();
		for (SubChannels p : subChannelsList) {
			if (p.box)
				box.add(p);
		}
		return box;
	}

}
