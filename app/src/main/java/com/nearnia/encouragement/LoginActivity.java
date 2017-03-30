package com.nearnia.encouragement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nearnia.encouragement.util.ServiceHandler;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private String TAG = LoginActivity.class.getSimpleName();
	private EditText userName, password;
	private Button btnSignin;
	private TextView signUp, forgetPassword;
	private String email, passwordNew;
	private ProgressDialog mProgressDialog;

	private GoogleCloudMessaging gcm;
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1;
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	public static String SENDER_ID = "778828817363";
	private String regid;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loginactivity);

		context = getApplicationContext();
		gcm = GoogleCloudMessaging.getInstance(LoginActivity.this);

		regid = getRegistrationId(context);
		// Log.e("REGIS_ID", regid);
		VolleySingleton.userLogin.edit().putString("REGISTRAION_ID", regid).commit();
		if (regid.isEmpty()) {
			new getRegistrationId().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
		}
		userName = (EditText) findViewById(R.id.EmailIdforLogin);
		password = (EditText) findViewById(R.id.userPasswordforLogin);
		btnSignin = (Button) findViewById(R.id.signin);
		signUp = (TextView) findViewById(R.id.signup);
		forgetPassword = (TextView) findViewById(R.id.forgetPassword);
		PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);

		SpannableString content = new SpannableString("Forgot Password?");
		content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
		forgetPassword.setText(content);

		SpannableString content1 = new SpannableString("DON'T HAVE AN ACCOUNT? SIGNUP");
		content1.setSpan(new UnderlineSpan(), 23, content1.length(), 0);
		signUp.setText(content1);

		boolean Login = VolleySingleton.userLogin.getBoolean("LOGIN_STATUS", false);

		if (Login) {

			userName.setText(VolleySingleton.userLogin.getString("EMAILID", ""));
			password.setText(VolleySingleton.userLogin.getString("PASSWORD", ""));
		}

		btnSignin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				email = userName.getText().toString();
				passwordNew = password.getText().toString();

				if (email.equals("") || passwordNew.equals("") || !isEmailValid(email)) {

					if (email.equals("")) {

						Toast.makeText(LoginActivity.this, "Log in attempt with no password and improper email",
								Toast.LENGTH_SHORT).show();
					} else if (!isEmailValid(email)) {
						Toast.makeText(getApplicationContext(), "Log in attempt with no password and improper email",
								Toast.LENGTH_LONG).show();
					} else
						Toast.makeText(LoginActivity.this, "Log in attempt with no password and improper email",
								Toast.LENGTH_SHORT).show();

				} else {

					String userSignInData = "&email=" + email + "&password=" + passwordNew + "&timezone="
							+ VolleySingleton.userLogin.getString("TIME_ZONE", "America/Los_Angeles") + "&devicetoken="
							+ VolleySingleton.userLogin.getString("REGISTRAION_ID", "defValue") + "&device=" + "2";
					new RemoteDataTask1().execute(userSignInData);

				}

			}
		});

		forgetPassword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent iforget = new Intent(LoginActivity.this, ForgetPassword.class);
				startActivity(iforget);
				overridePendingTransition(R.anim.enter, R.anim.exit);

			}
		});
		signUp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent in = new Intent(LoginActivity.this, SignUpActivity.class);

				in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(in);
				overridePendingTransition(R.anim.enter, R.anim.exit);
				finish();
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();

		checkPlayServices();

	}

	void showToast(String s) {
		Toast.makeText(getBaseContext(), s, Toast.LENGTH_LONG).show();

	}

	public static boolean isEmailValid(String email) {

		boolean isValid = false;

		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		CharSequence inputStr = email;

		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}

	@Override
	public void onBackPressed() {
		overridePendingTransition(R.anim.enter, R.anim.exit);
		finish();
		super.onBackPressed();
	}

	// RemoteDataTask AsyncTask
	private class RemoteDataTask1 extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			mProgressDialog = new ProgressDialog(LoginActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);

			mProgressDialog.setTitle("Signing In...");
			mProgressDialog.setCancelable(false);
			mProgressDialog.setMessage("Please wait....");
			mProgressDialog.setIndeterminate(true);
			mProgressDialog.show();
			btnSignin.setEnabled(false);

		}

		@Override
		protected String doInBackground(String... params) {
			ServiceHandler sH = new ServiceHandler();

			String response = null;
			response = sH.makeServiceCall(VolleySingleton.SIGNIN_URL + params[0], ServiceHandler.GET);
			return response;
		}

		@Override
		protected void onPostExecute(String response) {

			try {
				JSONObject reader = new JSONObject(response);

				int success = reader.getInt("success");

				Log.e("RESPONSE AFTER LOGIN", String.valueOf(response));

				if (success == 1) {

					Log.e("IN IF", String.valueOf(success));

					mProgressDialog.dismiss();
					JSONArray jsonArray = reader.getJSONArray("data");

					JSONObject jsonObject = jsonArray.getJSONObject(0);

					VolleySingleton.userLogin.edit().putBoolean(VolleySingleton.LOGIN_STATUS, true).commit();
					VolleySingleton.userLogin.edit().putInt(VolleySingleton.LOGIN_ID, jsonObject.optInt("userid"))
							.commit();
					VolleySingleton.userLogin.edit().putString(VolleySingleton.EMAIL_ID, jsonObject.optString("email"))
							.commit();
					VolleySingleton.userLogin.edit()
							.putString(VolleySingleton.USERNAME, jsonObject.optString("fullname")).commit();
					VolleySingleton.userLogin.edit()
							.putString(VolleySingleton.PHONENUMBER, jsonObject.optString("phone")).commit();
					VolleySingleton.userLogin.edit()
							.putString(VolleySingleton.DATEOFBIRTH, jsonObject.optString("dateofbirth")).commit();
					VolleySingleton.userLogin.edit()
							.putString("NOTIFICATION_TIME", jsonObject.optString("notification")).commit();
					VolleySingleton.userLogin.edit().putString(VolleySingleton.GENDER, jsonObject.getString("gender"))
							.commit();

					VolleySingleton.userLogin.edit()
							.putString(VolleySingleton.COMPANY_NAME, jsonObject.optString("companyUrl").equals("")
									? "set here your bussiness name" : jsonObject.optString("companyUrl"))
							.commit();
					VolleySingleton.userLogin.edit()
							.putString(VolleySingleton.PROFILE_TAGLINE, jsonObject.optString("profile").equals("")
									? "set here your profile" : jsonObject.optString("profile"))
							.commit();
					VolleySingleton.userLogin.edit()
							.putString(VolleySingleton.PROFILE_IMAGE_URL, jsonObject.optString("picture")).commit();
					VolleySingleton.userLogin.edit()
							.putString(VolleySingleton.COVER_IMAGE_URL, jsonObject.optString("coverpicture")).commit();

					VolleySingleton.userLogin.edit().putBoolean("LOGIN_THRU_SIGNIN", true).commit();

					Log.e("===LOGIN_ID===", String.valueOf(VolleySingleton.userLogin.getInt("LOGIN_ID", 0)));
					Log.e("EMAIL_ID",
							String.valueOf(VolleySingleton.userLogin.getString(VolleySingleton.EMAIL_ID, "NOTFILL")));

					Log.e("COMPANY_URL", VolleySingleton.userLogin.getString(VolleySingleton.COMPANY_NAME, "NOTFILL"));
					Log.e("PROFILE_TAGLINE",
							VolleySingleton.userLogin.getString(VolleySingleton.PROFILE_TAGLINE, "NOTFILL"));

					Log.e("PROFILE_IMAGE_URL",
							VolleySingleton.userLogin.getString(VolleySingleton.PROFILE_IMAGE_URL, "NOTFILL"));
					Log.e("COVER_IMAGE_URL",
							VolleySingleton.userLogin.getString(VolleySingleton.COVER_IMAGE_URL, "NOTFILL"));
					Log.e("DATEOFBIRTH", VolleySingleton.userLogin.getString(VolleySingleton.DATEOFBIRTH, "NOTFILL"));
					Log.e("GENDER", VolleySingleton.userLogin.getString(VolleySingleton.GENDER, "NOTFILL"));

					signUp.setEnabled(true);

					Intent intent = new Intent(LoginActivity.this, MainActivity2.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					finish();

					overridePendingTransition(R.anim.enter, R.anim.exit);

				} else {

					mProgressDialog.dismiss();
					Toast.makeText(LoginActivity.this, reader.getString("message"), Toast.LENGTH_LONG).show();
					VolleySingleton.userLogin.edit().putBoolean("LOGIN_STATUS", false).commit();
					btnSignin.setEnabled(true);
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				btnSignin.setEnabled(true);
			}

			return;
		}
	}

	public String getRegistrationId(Context context) {
		final SharedPreferences prefs = getGCMPreferences(context);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		if (registrationId.isEmpty()) {
			Log.i(TAG, "Registration not found.");
			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i(TAG, "App version changed.");
			return "";
		}
		return registrationId;
	}

	private SharedPreferences getGCMPreferences(Context context) {
		// This sample app persists the registration ID in shared preferences,
		// but
		// how you store the regID in your app is up to you.
		return getSharedPreferences(TAG, Context.MODE_PRIVATE);
	}

	private class getRegistrationId extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			try {
				if (gcm == null) {
					gcm = GoogleCloudMessaging.getInstance(context);
				}
				regid = gcm.register(SENDER_ID);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return regid;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (result.equalsIgnoreCase("")) {
				Toast.makeText(LoginActivity.this, getString(R.string.gcm_reg_failed), Toast.LENGTH_SHORT).show();
			} else {
				storeRegistrationId(context, regid);

			}
		}

	}

	public void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getGCMPreferences(context);
		int appVersion = getAppVersion(context);
		VolleySingleton.userLogin.edit().putString("REGISTRAION_ID", regId).commit();
		Log.i(TAG, "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);
		editor.commit();
	}

	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(LoginActivity.this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, LoginActivity.this, PLAY_SERVICES_RESOLUTION_REQUEST)
						.show();
			} else {
				Log.i(MainActivity2.class.getSimpleName(), "This device is not supported.");
				finish();
			}
			return false;
		}
		return true;
	}

}
