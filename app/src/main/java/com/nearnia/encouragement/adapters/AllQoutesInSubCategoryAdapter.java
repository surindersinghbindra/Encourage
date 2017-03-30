package com.nearnia.encouragement.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.nearnia.encouragement.MainActivity2;
import com.nearnia.encouragement.QoutesYouLoved;
import com.nearnia.encouragement.R;
import com.nearnia.encouragement.VolleySingleton;
import com.nearnia.encouragement.beanclasses.AllQoutesBean;
import com.nearnia.encouragement.util.DCircularImageView;
import com.nearnia.encouragement.util.StringRequestActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AllQoutesInSubCategoryAdapter extends BaseAdapter {

	Context mContext;
	ArrayList<AllQoutesBean> arraylist;

	public AllQoutesInSubCategoryAdapter(Context ctx, ArrayList<AllQoutesBean> arraylist) {
		this.mContext = ctx;
		this.arraylist = arraylist;

	}

	@Override
	public int getCount() {

		return arraylist.size();
	}

	@Override
	public Object getItem(int position) {

		return position;
	}

	@Override
	public long getItemId(int position) {

		return 0;
	}

	static class ViewHolder {

		TextView encouragementQoute, username_who_written;
		DCircularImageView user_profile_image;
		ImageButton heart;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub.
		final ViewHolder viewHolder;
		if (convertView == null) {

			// inflate the layout
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			convertView = inflater.inflate(R.layout.listview_encouragementyouloved, parent, false);

			// well set up the ViewHolder
			viewHolder = new ViewHolder();
			viewHolder.encouragementQoute = (TextView) convertView.findViewById(R.id.encouragementQoute);
			viewHolder.username_who_written = (TextView) convertView.findViewById(R.id.username_who_written);
			viewHolder.user_profile_image = (DCircularImageView) convertView.findViewById(R.id.user_profile_image);
			viewHolder.heart = (ImageButton) convertView.findViewById(R.id.heart);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();

		}
		viewHolder.encouragementQoute.setText( arraylist.get(position).getQuote().replace( "\\", "" ));
		// viewHolder.username_who_written.setText("@-" +
		// arraylist.get(position).getFullname());

		SpannableString content = new SpannableString("@-" + arraylist.get(position).getFullname());
		content.setSpan(new UnderlineSpan(), 2, content.length(), 0);
		viewHolder.username_who_written.setText(content);

		viewHolder.username_who_written.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				UniversalSingleton singleton = UniversalSingleton.getInstance();
//				singleton.setObject(arraylist.get(position));
				
				
				VolleySingleton.userLogin.edit().putString("USERID_RECIEVED", arraylist.get(position).getUserid()).commit();
				VolleySingleton.TABHOST = 6;
				Intent i = new Intent(mContext, MainActivity2.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TOP);

				((FragmentActivity) mContext).startActivity(i);

			}
		});

		Picasso.with(mContext).load(VolleySingleton.SERVER_URL_WITH_SLASH + arraylist.get(position).getPicture())
				.placeholder(R.drawable.user1).into(viewHolder.user_profile_image);

		viewHolder.heart.setOnClickListener(null);

		switch (arraylist.get(position).getHeartGrowth()) {

		case 0:
			viewHolder.heart.setBackgroundResource(R.drawable.bt_fullheart);
			break;
		case 1:
			viewHolder.heart.setBackgroundResource(R.drawable.onetthird_heart);
			break;
		case 2:
			viewHolder.heart.setBackgroundResource(R.drawable.twothird_heart);
			break;

		case 3:
			viewHolder.heart.setBackgroundResource(R.drawable.fullheart);
			break;

		default:
			viewHolder.heart.setBackgroundResource(R.drawable.bt_fullheart);
			break;
		}

		viewHolder.heart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int growth;
				growth = arraylist.get(position).getHeartGrowth();
				StringRequestActivity stringRequestActivity = new StringRequestActivity();
				if (growth == 0) {
					growth = 1;
					arraylist.get(position).setHeartGrowth(growth);
					stringRequestActivity.likeQuote(arraylist.get(position).getQuoteid());
					viewHolder.heart.setBackgroundResource(QoutesYouLoved.imagId[growth]);
				} else if (growth == 1) {
					growth = 2;
					arraylist.get(position).setHeartGrowth(growth);
					stringRequestActivity.likeQuote(arraylist.get(position).getQuoteid());
					viewHolder.heart.setBackgroundResource(QoutesYouLoved.imagId[growth]);
				} else if (growth == 2) {
					growth = 3;
					arraylist.get(position).setHeartGrowth(growth);
					stringRequestActivity.likeQuote(arraylist.get(position).getQuoteid());
					viewHolder.heart.setBackgroundResource(QoutesYouLoved.imagId[growth]);
				} else {
					arraylist.get(position).setHeartGrowth(0);
					viewHolder.heart.setBackgroundResource(QoutesYouLoved.imagId[0]);
					// arraylist.remove(position);
					stringRequestActivity.likeQuote(arraylist.get(position).getQuoteid());
					notifyDataSetChanged();

					Toast.makeText(mContext, "Quote Fully Unliked", Toast.LENGTH_SHORT).show();
				}

			}
		});

		return convertView;
	}

}
