package com.nearnia.encouragement;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

public class TutorialSignup extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		findViewById(R.id.button_dismiss).setBackgroundResource(R.drawable.firstintent2);
	}

	public void dismiss(View v) {
		finish();
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}

}
