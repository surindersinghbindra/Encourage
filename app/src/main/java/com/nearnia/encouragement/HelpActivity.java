package com.nearnia.encouragement;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class HelpActivity extends FragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frame_layout);

		// Check that the activity is using the layout version with
		// the fragment_container FrameLayout
		if (findViewById(R.id.fragment_container) != null) {

			// However, if we're being restored from a previous state,
			// then we don't need to do anything and should return or else
			// we could end up with overlapping fragments.
			if (savedInstanceState != null) {
				return;
			}

			// Create a new Fragment to be placed in the activity layout

			if (VolleySingleton.userLogin.getBoolean("LOGIN_STATUS", false)) {

				if (VolleySingleton.userLogin.getBoolean("LION_PRESSES", false)) {

					VolleySingleton.userLogin.edit().putBoolean("LION_PRESSES", false).commit();
					MainActivity2.tabHost.getTabWidget().getChildAt(0).setSelected(false);
					MainActivity2.tabHost.setClickable(true);

					HomeActivity firstFragment = new HomeActivity();

					getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, firstFragment).commit();
				} else if (VolleySingleton.userLogin.getBoolean("NOTIFICATION_RECIEVED", false)) {

					VolleySingleton.userLogin.edit().putBoolean("NOTIFICATION_RECIEVED", false);
					OtherUserProfileActivity otherUserProfileActivity = new OtherUserProfileActivity();
					getSupportFragmentManager().beginTransaction()
							.add(R.id.fragment_container, otherUserProfileActivity).commit();
				} else {

					ProfileActivity profileActivity = new ProfileActivity();

					getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, profileActivity)
							.commit();

				}

			} else {

				HomeActivity firstFragment = new HomeActivity();
				getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, firstFragment).commit();

			}
		}
	}

	@Override
	public void onBackPressed() {

		new AlertDialog.Builder(this).setMessage("Are you sure you want to exit?").setCancelable(false)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						HelpActivity.this.finish();
					}
				}).setNegativeButton("No", null).show();

	}
}
