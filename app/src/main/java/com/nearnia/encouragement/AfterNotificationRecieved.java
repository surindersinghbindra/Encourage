package com.nearnia.encouragement;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import com.nearnia.encouragement.util.DCircularImageView;
import com.nearnia.encouragement.util.StringRequestActivity;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.nearnia.encouragement.beanclasses.QuotesYouLiked;

public class AfterNotificationRecieved extends Activity implements OnClickListener {

	private ImageButton closeButton;
	// private Button shareWithOthers, inspireothers;
	private TextView quoteRecieved, dateOnPopUp, username_who_written;
	private String mesg, recievedUserId, fullname, user_picture, quote_id, categoryid;
	private DCircularImageView user_profile_image;
	private ImageButton heart;
	private int heartGrowth = 0;

	private LinearLayout write_your_own_layour_button, discover_more_layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		new StringRequestActivity().makeStringReq(VolleySingleton.ANALYTIC_NOTIFICATION_OPEN_COUNT_URL
				+ String.valueOf(VolleySingleton.userLogin.getInt(VolleySingleton.LOGIN_ID, 0)));

		Bundle extras = getIntent().getExtras();

		try {
			if (extras != null) {
				mesg = extras.getString("message").replace("\\", "");
				recievedUserId = extras.getString("type_status");
				fullname = extras.getString("fullname");
				fullname = extras.getString("fullname");
				user_picture = extras.getString("user_picture");
				categoryid = extras.getString("categoryid");
				quote_id = extras.getString("quote_id");

				Log.e("message1123", extras.getString("message"));
				Log.e("userid", extras.getString("type_status"));
				Log.e("fullname234", extras.getString("fullname"));
				Log.e("user_picture", extras.getString("user_picture"));

			} else {
				Log.e("message--extras", "null");
			}
		}

		catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}

		setContentView(R.layout.activity_after_notification_recieved);

		heart = (ImageButton) findViewById(R.id.heart);

		closeButton = (ImageButton) findViewById(R.id.closeButton);
		// shareWithOthers = (Button) findViewById(R.id.shareWithOthers);
		// inspireothers = (Button) findViewById(R.id.inspireothers);
		quoteRecieved = (TextView) findViewById(R.id.encouragementQoute);
		quoteRecieved.setTypeface(VolleySingleton.face);
		dateOnPopUp = (TextView) findViewById(R.id.dateOnPopUp);
		username_who_written = (TextView) findViewById(R.id.username_who_written);
		user_profile_image = (DCircularImageView) findViewById(R.id.user_profile_image);

		write_your_own_layour_button = (LinearLayout) findViewById(R.id.write_your_own_layour_button);
		discover_more_layout = (LinearLayout) findViewById(R.id.discover_more_layout);
		write_your_own_layour_button.setOnClickListener(this);
		discover_more_layout.setOnClickListener(this);

		quoteRecieved.setText(mesg);

		Calendar cl = Calendar.getInstance(TimeZone.getTimeZone("GMT+05:30"), Locale.US);
		final SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
		dateOnPopUp.setText(sdf.format(cl.getTime()));

		SpannableString content = new SpannableString("@-" + fullname);
		content.setSpan(new UnderlineSpan(), 2, content.length(), 0);
		username_who_written.setText(content);

		Picasso.with(AfterNotificationRecieved.this).load(VolleySingleton.SERVER_URL_WITH_SLASH + user_picture)
				.placeholder(R.drawable.user1).into(user_profile_image);

		username_who_written.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// VolleySingleton.userLogin.edit().putBoolean("NOTIFICATION_RECIEVED",
				// true).commit();
				VolleySingleton.userLogin.edit().putString("USERID_RECIEVED", recievedUserId).commit();

				VolleySingleton.TABHOST = 6;
				Intent intent = new Intent(AfterNotificationRecieved.this, MainActivity2.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TOP);
				overridePendingTransition(R.anim.enter, R.anim.exit);

				startActivity(intent);

				finish();

				Log.e("SUCCESS", "SUCCESS");

			}
		});

		// inspireothers.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Intent i = new Intent(AfterNotificationRecieved.this,
		// MainActivity2.class);
		// VolleySingleton.TABHOST = 0;
		// startActivity(i);
		// finish();
		// }
		// });
		//
		// shareWithOthers.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Intent i = new Intent(AfterNotificationRecieved.this,
		// MainActivity2.class);
		// VolleySingleton.TABHOST = 0;
		// startActivity(i);
		// finish();
		// }
		// });
		closeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();
			}
		});

		switch (heartGrowth) {

		case 0:
			heart.setBackgroundResource(R.drawable.bt_fullheart);

			break;
		case 1:
			heart.setBackgroundResource(R.drawable.onetthird_heart);
			break;
		case 2:
			heart.setBackgroundResource(R.drawable.twothird_heart);
			break;
		case 3:
			heart.setBackgroundResource(R.drawable.fullheart);
			break;

		default:
			heart.setBackgroundResource(R.drawable.bt_fullheart);
			break;
		}

		heart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				StringRequestActivity stringRequestActivity = new StringRequestActivity();
				if (heartGrowth == 0) {
					heartGrowth = 1;

					stringRequestActivity.likeQuote(quote_id);

					// list2.get(position).setHeartGrowth(growth);
					heart.setBackgroundResource(QoutesYouLoved.imagId[heartGrowth]);
				} else if (heartGrowth == 1) {
					heartGrowth = 2;
					stringRequestActivity.likeQuote(quote_id);
					// list2.get(position).setHeartGrowth(growth);

					heart.setBackgroundResource(QoutesYouLoved.imagId[heartGrowth]);
				} else if (heartGrowth == 2) {
					heartGrowth = 3;

					// list2.get(position).setHeartGrowth(growth);
					stringRequestActivity.likeQuote(quote_id);
					heart.setBackgroundResource(QoutesYouLoved.imagId[heartGrowth]);
				} else {
					heartGrowth = 0;
					heart.setBackgroundResource(QoutesYouLoved.imagId[heartGrowth]);
					stringRequestActivity.likeQuote(quote_id);

					Toast.makeText(AfterNotificationRecieved.this, "Quote fully unliked", Toast.LENGTH_SHORT).show();
				}

			}
		});

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.write_your_own_layour_button) {

			Intent i = new Intent(AfterNotificationRecieved.this, MainActivity2.class);
			VolleySingleton.TABHOST = 2;
			startActivity(i);
			finish();

		}
		if (v.getId() == R.id.discover_more_layout) {

			QuotesYouLiked quotesYouLiked = new QuotesYouLiked();
			quotesYouLiked.setCategoryid(categoryid);

			UniversalSingleton.getInstance().setObject(quotesYouLiked);

			Intent i = new Intent(AfterNotificationRecieved.this, MainActivity2.class);
			VolleySingleton.TABHOST = 5;
			i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivity(i);
			// overridePendingTransition(R.anim.exit, R.anim.enter);
			finish();

		}

	}

}
