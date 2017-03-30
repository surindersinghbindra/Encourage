/*package com.nearnia.encouragement;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import StringRequestActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class AfterSignUp extends Activity {

	private ImageButton closeButton;
	private Button shareWithOthers, inspireothers;
	private TextView profileString, quoteRecieved, dateOnPopUp, profileLine, company_name;
	private String mesg, recievedUserId, fullname, fullString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_after_signup);

		// fullString = "@sireigns CEO of Nearnia";
		closeButton = (ImageButton) findViewById(R.id.closeButton);
		shareWithOthers = (Button) findViewById(R.id.shareWithOthers);
		inspireothers = (Button) findViewById(R.id.inspireothers);
		profileLine = (TextView) findViewById(R.id.profileLine);
		company_name = (TextView) findViewById(R.id.company_name);
//		quoteRecieved = (TextView) findViewById(R.id.quoteRecieved);
		dateOnPopUp = (TextView) findViewById(R.id.dateOnPopUp);

		Calendar cl = Calendar.getInstance(TimeZone.getTimeZone("GMT+05:30"), Locale.US);
		final SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
		dateOnPopUp.setText(sdf.format(cl.getTime()));

		shareWithOthers.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(AfterSignUp.this, MainActivity2.class);
				VolleySingleton.TABHOST = 1;
				startActivity(i);
				finish();
			}
		});

		closeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(AfterSignUp.this, MainActivity2.class);
				VolleySingleton.TABHOST = 0;
				startActivity(i);
				finish();
			}
		});

		SpannableString profiletagline = new SpannableString("CEO");

		ClickableSpan clickableSpan1 = new ClickableSpan() {

			@Override
			public void onClick(View widget) {

				LayoutInflater li = LayoutInflater.from(AfterSignUp.this);

				View promptsView = li.inflate(R.layout.prompts, null);

				ContextThemeWrapper themedContext;
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					themedContext = new ContextThemeWrapper(AfterSignUp.this,
							android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
				} else {
					themedContext = new ContextThemeWrapper(AfterSignUp.this, android.R.style.Theme_Light_NoTitleBar);
				}
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(themedContext);
				alertDialogBuilder.setView(promptsView);
				final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);

				// set dialog message
				alertDialogBuilder.setCancelable(false).setPositiveButton("Add", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						// get user input and set it to result
						// edit text
						String companytagline = userInput.getText().toString();

						profileLine.setText(companytagline);
						VolleySingleton.userLogin.edit().putString("PROFILE_TAGLINE", companytagline).commit();

						String saveProfile = null;
						try {
							saveProfile = VolleySingleton.SERVER_URL_WITH_SLASH+"index.php?method=editProfile&userid="
									+ String.valueOf(VolleySingleton.userLogin.getInt("LOGIN_ID", 0)) + "&fullname="
									+ VolleySingleton.userLogin.getString(VolleySingleton.USERNAME, "Username")
									+ "&phone="
									+ VolleySingleton.userLogin.getString(VolleySingleton.PHONENUMBER, "phonenumber")
									+ "&dateofbirth="
									+ VolleySingleton.userLogin.getString(VolleySingleton.DATEOFBIRTH, "NOT SET")
									+ "&notification="
									+ VolleySingleton.userLogin.getString(VolleySingleton.NOTIFICATION_TIME, "0-3")
									+ "&gender=" + VolleySingleton.userLogin.getString(VolleySingleton.GENDER, "male")
									+ "&companyUrl="
									+ URLEncoder.encode(VolleySingleton.userLogin
											.getString(VolleySingleton.COMPANY_NAME, "Your company name"), "UTF-8")
									+ "&profile="
									+ URLEncoder.encode(VolleySingleton.userLogin.getString(
											VolleySingleton.PROFILE_TAGLINE, "your profile tagline"), "UTF-8")
									+ "&picture="
									+ VolleySingleton.userLogin.getString(VolleySingleton.PROFILE_IMAGE_URL,
											"DONT HAVE")
									+ VolleySingleton.userLogin.getString(VolleySingleton.COVER_IMAGE_URL, "DONT HAVE");
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						StringRequestActivity sR = new StringRequestActivity();

						String S = sR.makeStringReq(saveProfile);

					}
				}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();

					}
				});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();

				Toast.makeText(AfterSignUp.this, "Clicked", Toast.LENGTH_SHORT).show();

			}
		};

		profiletagline.setSpan(new ForegroundColorSpan(Color.CYAN), 0, profiletagline.length(), 0);
		profiletagline.setSpan(clickableSpan1, 0, profiletagline.length(), 0);
		profileLine.setMovementMethod(LinkMovementMethod.getInstance());
		profileLine.setClickable(true);
		profileLine.setText(TextUtils.concat(profiletagline));

		// spannable for company name
		SpannableString company_url = new SpannableString("Nearnia");

		ClickableSpan clickableSpan12 = new ClickableSpan() {

			@Override
			public void onClick(View widget) {

				LayoutInflater li = LayoutInflater.from(AfterSignUp.this);

				View promptsView = li.inflate(R.layout.prompts, null);

				ContextThemeWrapper themedContext;
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					themedContext = new ContextThemeWrapper(AfterSignUp.this,
							android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
				} else {
					themedContext = new ContextThemeWrapper(AfterSignUp.this, android.R.style.Theme_Light_NoTitleBar);
				}
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(themedContext);
				alertDialogBuilder.setView(promptsView);
				final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);

				// set dialog message
				alertDialogBuilder.setCancelable(false).setPositiveButton("Add", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						// get user input and set it to result
						// edit text
						String company_url = userInput.getText().toString();

						company_name.setText(company_url);
						VolleySingleton.userLogin.edit().putString(VolleySingleton.COMPANY_NAME, company_url).commit();

						String saveProfile = null;
						try {
							saveProfile = VolleySingleton.SERVER_URL_WITH_SLASH+"index.php?method=editProfile&userid="
									+ String.valueOf(VolleySingleton.userLogin.getInt("LOGIN_ID", 0)) + "&fullname="
									+ VolleySingleton.userLogin.getString(VolleySingleton.USERNAME, "Username")
									+ "&phone="
									+ VolleySingleton.userLogin.getString(VolleySingleton.PHONENUMBER, "phonenumber")
									+ "&dateofbirth="
									+ VolleySingleton.userLogin.getString(VolleySingleton.DATEOFBIRTH, "AFTERSIGNUP")
									+ "&notification="
									+ VolleySingleton.userLogin.getString(VolleySingleton.NOTIFICATION_TIME, "0-3")
									+ "&gender=" + VolleySingleton.userLogin.getString(VolleySingleton.GENDER, "male")
									+ "&companyUrl="
									+ URLEncoder.encode(VolleySingleton.userLogin
											.getString(VolleySingleton.COMPANY_NAME, "Your company name"), "UTF-8")
									+ "&profile="
									+ URLEncoder.encode(VolleySingleton.userLogin.getString(
											VolleySingleton.PROFILE_TAGLINE, "your profile tagline"), "UTF-8")
									+ "&picture="
									+ VolleySingleton.userLogin.getString(VolleySingleton.PROFILE_IMAGE_URL,
											"DONT HAVE")
									+ VolleySingleton.userLogin.getString(VolleySingleton.COVER_IMAGE_URL, "DONT HAVE");

						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						StringRequestActivity sR = new StringRequestActivity();

						String S = sR.makeStringReq(saveProfile);

					}
				}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();

					}
				});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();

				Toast.makeText(AfterSignUp.this, "Clicked", Toast.LENGTH_SHORT).show();

			}
		};

		company_url.setSpan(new ForegroundColorSpan(Color.CYAN), 0, company_url.length(), 0);
		company_url.setSpan(clickableSpan12, 0, company_url.length(), 0);
		company_name.setMovementMethod(LinkMovementMethod.getInstance());
		company_name.setClickable(true);
		company_name.setText(TextUtils.concat(company_url));

	}

}
*/