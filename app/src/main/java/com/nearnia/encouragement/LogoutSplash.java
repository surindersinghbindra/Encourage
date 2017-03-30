package com.nearnia.encouragement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class LogoutSplash extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logout_splash);
		
	

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {

				

				int pid = android.os.Process.myPid();
				android.os.Process.killProcess(pid);
				
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				startActivity(intent);
				finish();

			}
		}, 2000);

	}
}
