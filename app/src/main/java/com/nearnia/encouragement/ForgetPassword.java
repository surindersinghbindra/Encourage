package com.nearnia.encouragement;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgetPassword extends Activity {

	private ProgressDialog pDialog;
	EditText emailId;
	TextView tv1, tv2, tv3, tv4, tv5, contactus, contactusemail;
	Button forgetPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		setContentView(R.layout.forgetpassword);

		forgetPassword = (Button) findViewById(R.id.forgetPasswordbutton);
		emailId = (EditText) findViewById(R.id.emailId);
		tv1 = (TextView) findViewById(R.id.textView1);
		tv2 = (TextView) findViewById(R.id.textView2);
		tv3 = (TextView) findViewById(R.id.textView3);
		tv4 = (TextView) findViewById(R.id.textView4);
		tv5 = (TextView) findViewById(R.id.textView5);
		contactus = (TextView) findViewById(R.id.contactus);
		contactusemail = (TextView) findViewById(R.id.contactusemail);
		tv1.setTypeface(VolleySingleton.face);
		tv2.setTypeface(VolleySingleton.face);
		tv3.setTypeface(VolleySingleton.face);
		tv4.setTypeface(VolleySingleton.face);
		tv5.setTypeface(VolleySingleton.face);
		contactus.setTypeface(VolleySingleton.face);
		emailId.setTypeface(VolleySingleton.face);
		contactusemail.setTag(VolleySingleton.face);
		forgetPassword.setTypeface(VolleySingleton.face);

		SpannableString styledString = new SpannableString("Email us:");
		styledString.setSpan("", 0, styledString.length(), 0);
		styledString.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), 0, styledString.length(), 0);
		SpannableString styledString1 = new SpannableString("thepride@encouragementapp.com");
		styledString1.setSpan(new ForegroundColorSpan(Color.parseColor("#26A69A")), 0, styledString1.length(), 0);
		contactusemail.setTextSize(15);
		contactusemail.setText(TextUtils.concat(styledString, styledString1));

		forgetPassword.setOnClickListener(null);

		forgetPassword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String userEmailId = emailId.getText().toString();

				if (TextUtils.isEmpty(userEmailId)) {

					Toast.makeText(ForgetPassword.this, "Please enter your email id", Toast.LENGTH_SHORT).show();
				} else {

					pDialog = new ProgressDialog(ForgetPassword.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
					pDialog.setMessage("Please wait...");
					pDialog.setCancelable(false);

					pDialog.show();

					String request = null;

					request = VolleySingleton.FORGETPASSOWRD_URL + userEmailId;
					StringRequest strReq = new StringRequest(Method.GET, request, new Response.Listener<String>() {

						@Override
						public void onResponse(String response) {
							pDialog.dismiss();

							Log.e(ForgetPassword.class.getSimpleName(), response.toString());
							try {
								JSONObject jsonObject = new JSONObject(response);

								String message = jsonObject.getString("message");

								Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							pDialog.dismiss();
							Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();

						}
					});

					// Adding request to request queue
					strReq.setShouldCache(false);
					VolleySingleton.getInstance().addToRequestQueue(strReq, "FORGOT_PASSWORD_REQUEST");

				}
			}

		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		return true;
	}

}
