package com.nearnia.encouragement.adapters;

import java.util.ArrayList;
import java.util.List;

import com.nearnia.encouragement.AllQoutesInSubCategoryFragment;
import com.nearnia.encouragement.FirstIntent;
import com.nearnia.encouragement.LoginActivity;
import com.nearnia.encouragement.R;
import com.nearnia.encouragement.Singleton;
import com.nearnia.encouragement.VolleySingleton;

import com.nearnia.encouragement.InterfacesForcallBack.ToShowTutorial;
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
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.nearnia.encouragement.beanclasses.Channels;
import com.nearnia.encouragement.beanclasses.SubChannels;

public class SubChannelsAdatpter extends BaseAdapter {

	Context context;
	LayoutInflater inflater;
	// ImageLoader imageLoader;
	private List<SubChannels> subChannelsList = null;
	// private ArrayList<SubChannels> arraylist;
	ToShowTutorial toShowTutorial;
	
//	SubChannelsAdatpter (ToShowTutorial toShowTutorial){
//		this.toShowTutorial=toShowTutorial;
//	}

	public SubChannelsAdatpter(Context context, List<SubChannels> list) {
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
			convertView = inflater.inflate(R.layout.sub_category_listview, parent, false);
		}

		convertView.setTag(position);

		final ImageView whilteBookSub = (ImageView) convertView.findViewById(R.id.whilteBookSub);

		final CheckBox cb = (CheckBox) convertView.findViewById(R.id.checbox_sub_categoty);

		whilteBookSub.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (VolleySingleton.userLogin.getBoolean(VolleySingleton.LOGIN_STATUS, false)) {

					AllQoutesInSubCategoryFragment allQoutesInSubCategoryFragment = new AllQoutesInSubCategoryFragment();
					Channels obj = (Channels) Singleton.getInstance().getObject();
					Bundle bundle = new Bundle();
					bundle.putString("SUB_CATEGORY_ID", subChannelsList.get(position).subChannelId);
					bundle.putString("CATEGORY_NAME", obj.getChannelName());
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
				
				Boolean ranBefore4 = VolleySingleton.screen4.getBoolean("RanBefore4", false);
				
				if (!ranBefore4) {

					FirstIntent.tutorial34.setImageResource(R.drawable.tutorialframesubcategory21);
					FirstIntent.topLevelLayout3.setVisibility(View.VISIBLE);
					VolleySingleton.screen4.edit().putBoolean("RanBefore4", true).commit();
				} else {
//					AddButtonImage.setVisibility(View.VISIBLE);
				}

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
				
				Boolean ranBefore4 = VolleySingleton.screen4.getBoolean("RanBefore4", false);
			
				if (!ranBefore4) {
					FirstIntent.tutorial34.setImageResource(R.drawable.tutorialframesubcategory21);
					FirstIntent.topLevelLayout3.setVisibility(View.VISIBLE);
					VolleySingleton.screen4.edit().putBoolean("RanBefore4", true).commit();
				} else {
//					AddButtonImage.setVisibility(View.VISIBLE);
				}

				return;
			}
		};
		convertView.setOnClickListener(myCheckChangList_subchannel_adapter1);

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
