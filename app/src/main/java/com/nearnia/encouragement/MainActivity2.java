package com.nearnia.encouragement;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.nearnia.encouragement.util.SaveProfile;

import net.hockeyapp.android.CrashManager;

import java.util.ArrayList;

public class MainActivity2 extends TabActivity {

	boolean doubleBackToExitPressedOnce = false;
	private String CURRENT_TAB = "SettingsActivity";
	private String TAG = MainActivity2.class.getSimpleName();
	private ArrayList<String> arraylist = new ArrayList<>();
	private Context context;
	public static TabHost tabHost;

	private GoogleCloudMessaging gcm;
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1;
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	public static String SENDER_ID = "778828817363";
	private String regid;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main_activity_2);

		context = getApplicationContext();
		gcm = GoogleCloudMessaging.getInstance(MainActivity2.this);

		regid = getRegistrationId(context);
		// Log.e("REGIS_ID", regid);
		VolleySingleton.userLogin.edit().putString("REGISTRAION_ID", regid).commit();
		if (regid.isEmpty()) {
			new getRegistrationId().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
		}
		Resources ressources = getResources();
		tabHost = getTabHost();
		tabHost.getTabWidget().setStripEnabled(false);
		tabHost.getTabWidget().setDividerDrawable(null);

		// Profile tab
		Intent help_activity_Inetnt = new Intent().setClass(this, HelpActivity.class);
		TabSpec help_activity_tabHost = tabHost.newTabSpec("HelpActivity")
				.setIndicator("", ressources.getDrawable(R.drawable.icon_profiletab_config))
				.setContent(help_activity_Inetnt);

		// Inspire others tab
		Intent inspire_others_Intent = new Intent().setClass(this, InspiringOthers.class);
		TabSpec inspire_others_tabHost = tabHost.newTabSpec("InspiringOthers")
				.setIndicator("", ressources.getDrawable(R.drawable.inspireothers_config))
				.setContent(inspire_others_Intent);

		// write your own tab
		Intent encourage_Others_Intent = new Intent(this, WriteYourOwn.class);
		TabSpec encourage_Others_Tabhost = tabHost.newTabSpec("WriteYourOwn")
				.setIndicator("", ressources.getDrawable(R.drawable.icon_write_your_own_config))
				.setContent(encourage_Others_Intent);

		
		// settings tab
		Intent settings_Itent = new Intent().setClass(this, SettingsActivity.class);
		TabSpec settings_activity = tabHost.newTabSpec("SettingsActivity")
				.setIndicator("", ressources.getDrawable(R.drawable.settings_config)).setContent(settings_Itent);

		// Inspire others tab
		Intent inspire_others_Intent1 = new Intent().setClass(this, CategoriesHelpActivity.class);
		TabSpec inspire_others_tabHost1 = tabHost.newTabSpec("CategoriesHelpActivity")
				.setIndicator("", ressources.getDrawable(R.drawable.line2)).setContent(inspire_others_Intent1);
		// discover more
		Intent discover_more = new Intent().setClass(this, DiscoverMoreActivity.class);
		TabSpec discover_more_tabHost1 = tabHost.newTabSpec("DiscoverMoreActivity")
				.setIndicator("", ressources.getDrawable(R.drawable.line2)).setContent(discover_more);

		Intent otherSUer = new Intent().setClass(this, OtherUserMainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		TabSpec other_User_tabHost = tabHost.newTabSpec("OtherUserMainActivity")
				.setIndicator("", ressources.getDrawable(R.drawable.line2)).setContent(otherSUer);

		// add all tabs
		tabHost.addTab(help_activity_tabHost);
		tabHost.addTab(inspire_others_tabHost);
		tabHost.addTab(encourage_Others_Tabhost);
		tabHost.addTab(settings_activity);
		tabHost.addTab(inspire_others_tabHost1);
		tabHost.addTab(discover_more_tabHost1);
		tabHost.addTab(other_User_tabHost);

		tabHost.getTabWidget().getChildAt(4).setVisibility(View.GONE);
		tabHost.getTabWidget().getChildAt(5).setVisibility(View.GONE);
		tabHost.getTabWidget().getChildAt(6).setVisibility(View.GONE);
		// add color to childViews

		tabHost.getTabWidget().setDividerDrawable(null);

		tabHost.getTabWidget().getChildAt(0).setBackgroundColor(Color.parseColor("#26A69A"));
		tabHost.getTabWidget().getChildAt(1).setBackgroundColor(Color.parseColor("#26A69A"));
		tabHost.getTabWidget().getChildAt(2).setBackgroundColor(Color.parseColor("#26A69A"));
		tabHost.getTabWidget().getChildAt(3).setBackgroundColor(Color.parseColor("#26A69A"));


		tabHost.setCurrentTab(VolleySingleton.TABHOST);

		tabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {

				// Log.e("TAB CHANGED " + tabHost.getCurrentTabTag(),
				// CURRENT_TAB);

				arraylist.add(tabId);
				if (arraylist.size() > 1)
					if (CURRENT_TAB == arraylist.get(arraylist.size() - 2)) {

						SaveProfile saveProfile = new SaveProfile();
						saveProfile.methodSaveProfile();

					}
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		call();
		checkPlayServices();
		checkForCrashes();
	}

	public void call() {
		@SuppressWarnings("static-access")
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (inputMethodManager != null) {
			if (getCurrentFocus() != null) {
				inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
			}

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

	class getRegistrationId extends AsyncTask<String, String, String> {

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
				Toast.makeText(MainActivity2.this, getString(R.string.gcm_reg_failed), Toast.LENGTH_SHORT).show();
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
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(MainActivity2.this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, MainActivity2.this, PLAY_SERVICES_RESOLUTION_REQUEST)
						.show();
			} else {
				Log.i(MainActivity2.class.getSimpleName(), "This device is not supported.");
				finish();
			}
			return false;
		}
		return true;
	}


	// @Override
	// public void onBackPressed() {
	// if (this.lastBackPressTime < System.currentTimeMillis() - 2000) {
	// toast = Toast.makeText(this, "Press back again to close this app",
	// Toast.LENGTH_LONG);
	// toast.show();
	// this.lastBackPressTime = System.currentTimeMillis();
	// } else {
	// if (toast != null) {
	// toast.cancel();
	// }
	// super.onBackPressed();
	// }
	// }
	//
	// @Override
	// public void onBackPressed() {
	// Log.e("TAGOOOOOO", "TAGOOOOOO");
	// new AlertDialog.Builder(this).setMessage("Are you sure you want to
	// exit?").setCancelable(false)
	// .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog, int id) {
	// MainActivity2.this.finish();
	// }
	// }).setNegativeButton("No", null).show();
	// }
	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// super.onKeyDown(keyCode, event);
	// Log.e("TAGOOOOOO", "TAGOOOOOO");
	// if (keyCode == KeyEvent.KEYCODE_BACK) {
	// // preventing default implementation previous to
	// // android.os.Build.VERSION_CODES.ECLAIR
	// Log.e("TAGOOOOOO", "TAGOOOOOO");
	// return false;
	// }
	// Log.e("TAGOOOOOO2", "TAGOOOOOO2");
	// return super.onKeyDown(keyCode, event);
	// }

	public void showPro(int tabNum) {
		
		tabHost.setCurrentTab(tabNum);
	}
	
	
	
	
	  private void checkForCrashes() {
	        CrashManager.register(this, "201cd61f553d4ca49086021d983ffc99");
	    }

}