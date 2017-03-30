package com.nearnia.encouragement;

import com.nearnia.encouragement.util.DCircularImageView;
import com.nearnia.encouragement.util.StringRequestActivity;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.nearnia.encouragement.beanclasses.QuotesYouLiked;

public class YourEncouragementFragment extends Activity implements OnClickListener {

	String qouteFromIntent;
	int heartGrowth;
	TextView yourencouragement, username_who_written;
	ImageButton heart;
	LinearLayout write_your_own_layour_button, discover_more_layout;
	private DCircularImageView user_profile_image;
	ImageView logowithEmailIdLogo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		final QuotesYouLiked quotesYouLiked = (QuotesYouLiked) UniversalSingleton.getInstance().getObject();

		qouteFromIntent = quotesYouLiked.getQuote().replace("\\", "");
		heartGrowth = quotesYouLiked.getHeartGrowth();

		setContentView(R.layout.activity_yourencouragementfragment);

		yourencouragement = (TextView) findViewById(R.id.quoteRecievedFromIntent);

		user_profile_image = (DCircularImageView) findViewById(R.id.user_profile_image);

		username_who_written = (TextView) findViewById(R.id.username_who_written);

		logowithEmailIdLogo = (ImageView) findViewById(R.id.logowithEmailIdLogo);

		logowithEmailIdLogo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				VolleySingleton.TABHOST = 4;
				Intent i = new Intent(YourEncouragementFragment.this, MainActivity2.class);

				i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TOP);

				startActivity(i);

			}
		});

		SpannableString content = new SpannableString("@-" + quotesYouLiked.getFullname());
		content.setSpan(new UnderlineSpan(), 2, content.length(), 0);
		username_who_written.setText(content);

		username_who_written.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				VolleySingleton.userLogin.edit().putString("USERID_RECIEVED", quotesYouLiked.getUserid()).commit();

				VolleySingleton.TABHOST = 6;
				Intent i = new Intent(YourEncouragementFragment.this, MainActivity2.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TOP);

				startActivity(i);
				overridePendingTransition(R.anim.enter, R.anim.exit);

				// finish();

			}
		});
		Picasso.with(this).load(VolleySingleton.SERVER_URL_WITH_SLASH + quotesYouLiked.getPicture())
				.placeholder(R.drawable.user1).into(user_profile_image);

		heart = (ImageButton) findViewById(R.id.heart);

		write_your_own_layour_button = (LinearLayout) findViewById(R.id.write_your_own_layour_button);
		discover_more_layout = (LinearLayout) findViewById(R.id.discover_more_layout);
		write_your_own_layour_button.setOnClickListener(this);
		discover_more_layout.setOnClickListener(this);
		yourencouragement.setText(qouteFromIntent);

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

					stringRequestActivity.likeQuote(quotesYouLiked.getQuoteid());

					// list2.get(position).setHeartGrowth(growth);
					heart.setBackgroundResource(QoutesYouLoved.imagId[heartGrowth]);
				} else if (heartGrowth == 1) {
					heartGrowth = 2;
					stringRequestActivity.likeQuote(quotesYouLiked.getQuoteid());
					// list2.get(position).setHeartGrowth(growth);

					heart.setBackgroundResource(QoutesYouLoved.imagId[heartGrowth]);
				} else if (heartGrowth == 2) {
					heartGrowth = 3;

					// list2.get(position).setHeartGrowth(growth);
					stringRequestActivity.likeQuote(quotesYouLiked.getQuoteid());
					heart.setBackgroundResource(QoutesYouLoved.imagId[heartGrowth]);
				} else {
					heartGrowth = 0;
					heart.setBackgroundResource(QoutesYouLoved.imagId[heartGrowth]);
					stringRequestActivity.likeQuote(quotesYouLiked.getQuoteid());

					Toast.makeText(YourEncouragementFragment.this, "Quote fully unliked", Toast.LENGTH_SHORT).show();
				}

			}
		});

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if (v.getId() == R.id.write_your_own_layour_button) {

			Intent i = new Intent(YourEncouragementFragment.this, MainActivity2.class);
			VolleySingleton.TABHOST = 2;
			startActivity(i);
			finish();

		}
		if (v.getId() == R.id.discover_more_layout) {

			Intent i = new Intent(YourEncouragementFragment.this, MainActivity2.class);
			VolleySingleton.TABHOST = 5;
			i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivity(i);
			// overridePendingTransition(R.anim.exit, R.anim.enter);
			finish();

		}

	}

	@Override
	public void onBackPressed() {

		new AlertDialog.Builder(this).setMessage("Are you sure you want to exit?").setCancelable(false)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						YourEncouragementFragment.this.finish();
					}
				}).setNegativeButton("No", null).show();

	}

}
