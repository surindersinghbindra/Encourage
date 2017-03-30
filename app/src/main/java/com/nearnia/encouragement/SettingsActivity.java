package com.nearnia.encouragement;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.nearnia.encouragement.util.CompressImage;
import com.nearnia.encouragement.util.DCircularImageView;
import com.nearnia.encouragement.util.ImageLoadingUtils;
import com.nearnia.encouragement.util.ServiceHandler;
import com.nearnia.encouragement.util.StringRequestActivity;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class SettingsActivity extends Activity implements OnLongClickListener {

	private SharedPreferences settingsPref;
	private ProgressDialog mProgressDialog;
	private ImageView lionRoar;
	private RadioButton zerotoThree, threeToSix, sixToNine, nineToTweleve;
	private 	String dateofBirth1;
	private 	RadioButton male, female;
	private	EditText fullName, phoneNumber, EmailId, editTextPassword, dateOfBirth;
	private RadioGroup notificationFrequecny;
	private Button logoutButton;
	private DCircularImageView fixedprofileImage;

	private ImageLoadingUtils utils;

	private	Calendar myCalendar = Calendar.getInstance();

	private 	String rP, pictureEncoded1;
	private 	Bitmap bp;
	byte[] bitMapData1;

	

	public SettingsActivity() {

	}

	

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		utils = new ImageLoadingUtils(this);

		final RelativeLayout settings_screen_writeYourOwn = (RelativeLayout) findViewById(
				R.id.settings_screen_writeYourOwn);
final ImageView encourageothersscreen3 = (ImageView)findViewById(R.id.encourageothersscreen3);

		settingsPref = this.getPreferences(Context.MODE_PRIVATE);
		Boolean writeyourOwnHelpScreenBool = settingsPref.getBoolean("settingsHelpScreenBool", false);

		if (!writeyourOwnHelpScreenBool) {
			encourageothersscreen3.setImageResource(R.drawable.settingstutorial);
			
			settings_screen_writeYourOwn.setVisibility(View.VISIBLE);

			settings_screen_writeYourOwn.setEnabled(true);
			settingsPref.edit().putBoolean("settingsHelpScreenBool", true).commit();
		} else {

			settingsPref.edit().putBoolean("settingsHelpScreenBool", true).commit();
		}

		settings_screen_writeYourOwn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				settings_screen_writeYourOwn.setVisibility(View.GONE);
				encourageothersscreen3.setImageResource(android.R.color.transparent);

			}
		});

		if (!VolleySingleton.userLogin.getBoolean("LOGIN_STATUS", false)) {

			Toast.makeText(getApplicationContext(), "Please SignUp First...", Toast.LENGTH_SHORT).show();
			Intent i = new Intent(this, SignUpActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
			overridePendingTransition(R.anim.enter, R.anim.exit);

		} else {

			final SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
			final EditText datePicker = (EditText) findViewById(R.id.DATEOFBIRTHPICKER);
			datePicker.setFocusableInTouchMode(false);
			final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

					myCalendar.set(Calendar.YEAR, year);
					myCalendar.set(Calendar.MONTH, monthOfYear);
					myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
					dateofBirth1 = sdf.format(myCalendar.getTime());
					datePicker.setText(dateofBirth1);
					VolleySingleton.userLogin.edit().putString("DATEOFBIRTH", dateofBirth1).commit();

				}

			};

			datePicker.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					new DatePickerDialog(SettingsActivity.this, date, 1995, 01, 01).show();

				}
			});

			fullName = (EditText) findViewById(R.id.fullName);
			phoneNumber = (EditText) findViewById(R.id.phoneNumber);

			fixedprofileImage = (DCircularImageView) findViewById(R.id.fixedprofileImage);

			notificationFrequecny = (RadioGroup) this.findViewById(R.id.notification_frequecny);

			zerotoThree = (RadioButton) findViewById(R.id.zerotoThree);
			threeToSix = (RadioButton) findViewById(R.id.threeToSix);
			sixToNine = (RadioButton) findViewById(R.id.sixToNine);
			nineToTweleve = (RadioButton) findViewById(R.id.nineToTweleve);

			lionRoar = (ImageView) findViewById(R.id.addProfileImageButton);
			EmailId = (EditText) findViewById(R.id.EmailId);

			editTextPassword = (EditText) findViewById(R.id.editTextPassword);
			dateOfBirth = (EditText) findViewById(R.id.DATEOFBIRTHPICKER);
			

			male = (RadioButton) findViewById(R.id.MALE);
			female = (RadioButton) findViewById(R.id.FEMALE);
			RadioGroup gender = (RadioGroup) this.findViewById(R.id.gender_setting);

			logoutButton = (Button) findViewById(R.id.LogoutButton);

			fullName.setText(VolleySingleton.userLogin.getString(VolleySingleton.USERNAME, "Username"));
			phoneNumber.setText(VolleySingleton.userLogin.getString(VolleySingleton.PHONENUMBER, "phonenumber"));
			EmailId.setText(VolleySingleton.userLogin.getString(VolleySingleton.EMAIL_ID, "EmailID"));
			EmailId.setEnabled(false);
			editTextPassword.setText(VolleySingleton.userLogin.getString(VolleySingleton.PASSWORD, "password"));
			// editTextPassword.setEnabled(false);
			dateOfBirth.setText(VolleySingleton.userLogin.getString(VolleySingleton.DATEOFBIRTH, "01-JAN-1995"));
			VolleySingleton.userLogin.getString(VolleySingleton.NOTIFICATION_TIME, "6-9");
			fixedprofileImage.setOnLongClickListener(null);

			editTextPassword.setOnLongClickListener(this);

			fixedprofileImage.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {

					Intent intent = new Intent();

					intent.setType("image/*");
					intent.setAction(Intent.ACTION_GET_CONTENT);

					startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
					return false;
				}
			});

			fullName.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {

					VolleySingleton.userLogin.edit().putString(VolleySingleton.USERNAME, s.toString()).commit();
					ProfileActivity.userNAme.setText(s.toString());

				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {

				}

				@Override
				public void afterTextChanged(Editable s) {

					VolleySingleton.userLogin.edit().putString(VolleySingleton.USERNAME, s.toString()).commit();

				}
			});

			phoneNumber.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {

					VolleySingleton.userLogin.edit().putString(VolleySingleton.PHONENUMBER, s.toString()).commit();

				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {

				}

				@Override
				public void afterTextChanged(Editable s) {

					VolleySingleton.userLogin.edit().putString(VolleySingleton.PHONENUMBER, s.toString()).commit();

				}
			});
			if ((VolleySingleton.userLogin.getString(VolleySingleton.GENDER, "male")).equals("male")) {

				male.setChecked(true);

			} else {

				female.setChecked(true);

			}
			if (VolleySingleton.userLogin.getBoolean("LOGIN_THRU_SIGNIN", false)) {
				Picasso.with(SettingsActivity.this)
						.load(VolleySingleton.SERVER_URL_WITH_SLASH
								+ VolleySingleton.userLogin.getString(VolleySingleton.PROFILE_IMAGE_URL, ""))
						.placeholder(R.drawable.user1).error(R.drawable.user1).into(fixedprofileImage);
			} else {
				fixedprofileImage.setImageBitmap(decodeBase64(
						VolleySingleton.userLogin.getString(VolleySingleton.PROFILE_IMAGE_BYTE_DATA, " NOT SET")));
			}

			gender.setOnCheckedChangeListener(null);

			final OnCheckedChangeListener radiochecker = new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {

					if (checkedId == R.id.FEMALE) {

						VolleySingleton.userLogin.edit().putString(VolleySingleton.GENDER, "female").commit();

					} else if (checkedId == R.id.MALE) {

						VolleySingleton.userLogin.edit().putString(VolleySingleton.GENDER, "male").commit();

					}

				}
			};
			gender.setOnCheckedChangeListener(radiochecker);

			notificationFrequecnyChecker();

			notificationFrequecny.setOnCheckedChangeListener(null);

			notificationFrequecny.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {

					switch (checkedId) {
					case R.id.zerotoThree:
						VolleySingleton.userLogin.edit().putString(VolleySingleton.NOTIFICATION_TIME, "6-9").commit();

						break;
					case R.id.threeToSix:
						VolleySingleton.userLogin.edit().putString(VolleySingleton.NOTIFICATION_TIME, "12-15").commit();

						break;
					case R.id.sixToNine:
						VolleySingleton.userLogin.edit().putString(VolleySingleton.NOTIFICATION_TIME, "15-18").commit();

						break;
					case R.id.nineToTweleve:
						VolleySingleton.userLogin.edit().putString(VolleySingleton.NOTIFICATION_TIME, "18-21").commit();

						break;
					default:
					}

				}
			});

			logoutButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					 unRegister();
					
					 Toast.makeText(getApplicationContext(), "logged Out",
					 Toast.LENGTH_SHORT).show();
					
					 VolleySingleton.userLogin.edit().clear().commit();
					
					 Intent intent = new Intent(SettingsActivity.this,
					 LoginActivity.class);
					
					 startActivity(intent);
					 overridePendingTransition(R.anim.enter, R.anim.exit);
					 finish();

					

				}
			});

			lionRoar.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					VolleySingleton.TABHOST = 4;
					Intent i = new Intent(SettingsActivity.this, MainActivity2.class);
					// VolleySingleton.userLogin.edit().putBoolean("LION_PRESSES",
					// true).commit();
					i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
					startActivity(i);
					overridePendingTransition(R.anim.exit, R.anim.enter);

				}
			});

		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		return true;
	}

	private void notificationFrequecnyChecker() {
		if ((VolleySingleton.userLogin.getString(VolleySingleton.NOTIFICATION_TIME, "6-9")).equals("6-9")) {
			zerotoThree.setChecked(true);

		} else if (VolleySingleton.userLogin.getString(VolleySingleton.NOTIFICATION_TIME, "6-9").equals("12-15")) {

			threeToSix.setChecked(true);

		} else if (VolleySingleton.userLogin.getString(VolleySingleton.NOTIFICATION_TIME, "6-9").equals("15-18")) {

			sixToNine.setChecked(true);

		} else if (VolleySingleton.userLogin.getString(VolleySingleton.NOTIFICATION_TIME, "6-9").equals("18-21")) {

			nineToTweleve.setChecked(true);

		} else {
			zerotoThree.setChecked(true);
		}

	}

	public Bitmap decodeBase64(String input) {
		byte[] decodedByte = Base64.decode(input, 0);
		return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {

			Uri uri1 = data.getData();

			CompressImage cI = new CompressImage();
			rP = cI.compressImage(this, uri1);

			bp = utils.decodeBitmapFromPath(rP);

			fixedprofileImage.setImageBitmap(bp);

			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
			bitMapData1 = stream.toByteArray();

			pictureEncoded1 = Base64.encodeToString(bitMapData1, Base64.DEFAULT);
			System.gc();

			new RemoteDataTask1().execute();
		}
	}

	private class RemoteDataTask1 extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog = new ProgressDialog(SettingsActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);

			mProgressDialog.setMessage("uploading image...");
			mProgressDialog.setCancelable(false);
			mProgressDialog.setIndeterminate(true);
			mProgressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			ServiceHandler sH = new ServiceHandler();

			String g = sH.toUploadImage(VolleySingleton.IMAGEUPLOAD_URL, rP);

			return g;
		}

		@Override
		protected void onPostExecute(String result) {

			try {
				JSONObject reader = new JSONObject(result);

				int success = reader.getInt("success");
	

				if (success == 1) {
					mProgressDialog.dismiss();
					String imageName = reader.getString("imageName");

					VolleySingleton.userLogin.edit().putString(VolleySingleton.PROFILE_IMAGE_URL, imageName).commit();
					VolleySingleton.userLogin.edit().putString(VolleySingleton.PROFILE_IMAGE_BYTE_DATA, pictureEncoded1)
							.commit();

					ProfileActivity.profile_image.setImageBitmap(decodeBase64(
							VolleySingleton.userLogin.getString(VolleySingleton.PROFILE_IMAGE_BYTE_DATA, " NOTSET")));

					pictureEncoded1 = null;
					Log.e("String while signup", String.valueOf(imageName));

				} else {
					mProgressDialog.dismiss();
					Toast.makeText(SettingsActivity.this, "image upload failed", Toast.LENGTH_LONG).show();

					fixedprofileImage.setImageBitmap(decodeBase64(
							VolleySingleton.userLogin.getString(VolleySingleton.PROFILE_IMAGE_BYTE_DATA, " NOTSET")));
					ProfileActivity.profile_image.setImageBitmap(decodeBase64(
							VolleySingleton.userLogin.getString(VolleySingleton.PROFILE_IMAGE_BYTE_DATA, " NOTSET")));

				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			mProgressDialog.dismiss();
			return;
		}
	}

	private void unRegister() {

		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(getBaseContext());
		try {
			gcm.unregister();
		} catch (IOException e) {
			System.out.println("Error Message: " + e.getMessage());
		}
		return;
	}

	@Override
	public boolean onLongClick(View v) {

		final ContextThemeWrapper themedContext;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			themedContext = new ContextThemeWrapper(SettingsActivity.this,
					android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
		} else {
			themedContext = new ContextThemeWrapper(SettingsActivity.this, android.R.style.Theme_Light_NoTitleBar);
		}

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(themedContext);
		alertDialog.setTitle("Change Password");
		final EditText oldPass = new EditText(SettingsActivity.this);
		final EditText newPass = new EditText(SettingsActivity.this);
		final EditText confirmPass = new EditText(SettingsActivity.this);

		oldPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
		newPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
		confirmPass.setTransformationMethod(PasswordTransformationMethod.getInstance());

		oldPass.setHint("Old Password");
		newPass.setHint("New Password");
		confirmPass.setHint("Confirm Password");
		LinearLayout ll = new LinearLayout(SettingsActivity.this);
		ll.setOrientation(LinearLayout.VERTICAL);

		ll.addView(oldPass);

		ll.addView(newPass);
		ll.addView(confirmPass);
		alertDialog.setView(ll);
		alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				if (newPass.getText().toString().equals(confirmPass.getText().toString())
						&& !TextUtils.isEmpty(newPass.getText().toString())
						&& !TextUtils.isEmpty(oldPass.getText().toString())
						&& !TextUtils.isEmpty(confirmPass.getText().toString())) {

					StringRequestActivity stringRequestActivity = new StringRequestActivity();
					stringRequestActivity.makeStringReqWithProgressbarnew(SettingsActivity.this,
							VolleySingleton.PASSWORD_CHANGE_URL
									+ String.valueOf(VolleySingleton.userLogin.getInt(VolleySingleton.LOGIN_ID, 0))
									+ "&opassword=" + oldPass.getText().toString() + "&npassword=" + newPass);
					// Toast.makeText(themedContext, "password changed",
					// Toast.LENGTH_SHORT).show();
					// dialog.cancel();
				} else {

					Toast.makeText(themedContext, "password not matched", Toast.LENGTH_SHORT).show();
				}

			}
		});
		alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});

		final AlertDialog alert11 = alertDialog.create();
		alert11.show();

		return false;
	}

	@Override
	public void onBackPressed() {

		new AlertDialog.Builder(this).setMessage("Are you sure you want to exit?").setCancelable(false)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						SettingsActivity.this.finish();
					}
				}).setNegativeButton("No", null).show();

	}

}
