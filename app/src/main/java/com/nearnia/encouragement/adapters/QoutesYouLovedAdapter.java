package com.nearnia.encouragement.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.nearnia.encouragement.MainActivity2;
import com.nearnia.encouragement.QoutesYouLoved;
import com.nearnia.encouragement.R;
import com.nearnia.encouragement.UniversalSingleton;
import com.nearnia.encouragement.VolleySingleton;
import com.nearnia.encouragement.YourEncouragementFragment;
import com.nearnia.encouragement.beanclasses.QuotesYouLiked;
import com.nearnia.encouragement.util.DCircularImageView;
import com.nearnia.encouragement.util.StringRequestActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class QoutesYouLovedAdapter extends BaseAdapter implements Filterable {

	ValueFilter valueFilter;
	Context mContext;
	ArrayList<QuotesYouLiked> arraylist;

	List<QuotesYouLiked> list2;

	public QoutesYouLovedAdapter(Context ctx, ArrayList<QuotesYouLiked> arraylist) {
		this.mContext = ctx;
		this.arraylist = arraylist;
		list2 = arraylist;
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
		viewHolder.username_who_written.setText("@-" + arraylist.get(position).getFullname());

		SpannableString content = new SpannableString("@-" + arraylist.get(position).getFullname());
		content.setSpan(new UnderlineSpan(), 2, content.length(), 0);
		viewHolder.username_who_written.setText(content);

		Picasso.with(mContext).load(VolleySingleton.SERVER_URL_WITH_SLASH + arraylist.get(position).getPicture())
				.placeholder(R.drawable.user1).into(viewHolder.user_profile_image);

		viewHolder.username_who_written.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UniversalSingleton singleton = UniversalSingleton.getInstance();
				singleton.setObject(arraylist.get(position));
				
				VolleySingleton.userLogin.edit().putString("USERID_RECIEVED", arraylist.get(position).getUserid()).commit();
				
				MainActivity2 activity2 = new MainActivity2();
				activity2.showPro(6);

//				VolleySingleton.TABHOST = 6;
//				Intent i = new Intent(mContext, MainActivity2.class);
//				i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				((FragmentActivity) mContext).overridePendingTransition(R.anim.enter, R.anim.exit);
//				((FragmentActivity) mContext).startActivity(i);

				// mContext.startActivity(i);

				// ((FragmentActivity) mContext).finish();

				Log.e("SUCCESS", "SUCCESS");

			}
		});
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

					// stringRequestActivity.likeQuote(arraylist.get(position).getQuoteid());
					stringRequestActivity.likeQuote(list2.get(position).getQuoteid());
					// list2.get(position).setHeartGrowth(growth);
					viewHolder.heart.setBackgroundResource(QoutesYouLoved.imagId[growth]);
				} else if (growth == 1) {
					growth = 2;
					arraylist.get(position).setHeartGrowth(growth);
					// list2.get(position).setHeartGrowth(growth);
					// stringRequestActivity.likeQuote(arraylist.get(position).getQuoteid());
					stringRequestActivity.likeQuote(list2.get(position).getQuoteid());
					viewHolder.heart.setBackgroundResource(QoutesYouLoved.imagId[growth]);
				} else if (growth == 2) {
					growth = 3;
					arraylist.get(position).setHeartGrowth(growth);
					// list2.get(position).setHeartGrowth(growth);
					// stringRequestActivity.likeQuote(arraylist.get(position).getQuoteid());
					stringRequestActivity.likeQuote(list2.get(position).getQuoteid());
					viewHolder.heart.setBackgroundResource(QoutesYouLoved.imagId[growth]);
				} else {
					// stringRequestActivity.likeQuote(arraylist.get(position).getQuoteid());
					stringRequestActivity.likeQuote(list2.get(position).getQuoteid());
					arraylist.remove(position);
					notifyDataSetChanged();
					Toast.makeText(mContext, "Quote fully unliked", Toast.LENGTH_SHORT).show();
				}

			}
		});

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				UniversalSingleton singleton = UniversalSingleton.getInstance();
				singleton.setObject(arraylist.get(position));
				Intent intent = new Intent(mContext, YourEncouragementFragment.class);
				mContext.startActivity(intent);

			}
		});

		return convertView;
	}

	@Override
	public Filter getFilter() {
		if (valueFilter == null) {
			valueFilter = new ValueFilter();
		}
		return valueFilter;
	}

	private class ValueFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results = new FilterResults();

			if (constraint != null && constraint.length() > 0) {
				List<QuotesYouLiked> filterList = new ArrayList<QuotesYouLiked>();
				for (int i = 0; i < list2.size(); i++) {
					if (String.valueOf((list2.get(i).getHeartGrowth())).equals(constraint.toString())) {

						QuotesYouLiked filtredObj = new QuotesYouLiked();

						// Log.e("STRING_GROWTH", list2.get(i).getQoute());
						filtredObj.setQuote(list2.get(i).getQuote());
						filtredObj.setHeartGrowth(list2.get(i).getHeartGrowth());
						filterList.add(filtredObj);
					}
				}
				results.count = filterList.size();
				results.values = filterList;
			} else {
				results.count = list2.size();
				results.values = list2;
			}
			return results;

		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			arraylist = (ArrayList<QuotesYouLiked>) results.values;
			notifyDataSetChanged();
		}

	}

}
