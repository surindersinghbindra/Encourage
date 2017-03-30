package com.nearnia.encouragement;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

public class DiscoverMoreActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frame_layout);

		if (findViewById(R.id.fragment_container) != null) {

			// However, if we're being restored from a previous state,
			// then we don't need to do anything and should return or else
			// we could end up with overlapping fragments.
			if (savedInstanceState != null) {
				return;
			}

			FragmentDiscoverMore disMoreFragment = new FragmentDiscoverMore();

			getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, disMoreFragment).commit();
		}

	}

	@Override
	public void onBackPressed() {

		FragmentManager fm = DiscoverMoreActivity.this.getSupportFragmentManager();

		for (int entry = 0; entry < fm.getBackStackEntryCount(); entry++) {
			Log.i(CategoriesHelpActivity.class.getSimpleName(),
					"Found fragment: " + fm.getBackStackEntryAt(entry).getId());
		}

		if (fm.getBackStackEntryCount() > 0) {

			fm.popBackStack();
		} else {

			new AlertDialog.Builder(this).setMessage("Are you sure you want to exit?").setCancelable(false)
					.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {

							DiscoverMoreActivity.this.finish();
						}
					}).setNegativeButton("No", null).show();

		}

	}
}
