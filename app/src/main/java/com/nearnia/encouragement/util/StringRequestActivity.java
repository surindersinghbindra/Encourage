package com.nearnia.encouragement.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.nearnia.encouragement.R;
import com.nearnia.encouragement.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import com.nearnia.encouragement.InterfacesForcallBack.IAsyncResponse;

public class StringRequestActivity {

	private String TAG = StringRequestActivity.class.getSimpleName();

	private ProgressDialog pDialog;
	private String URL;
	Dialog dialog;
	String result = null;
	public IAsyncResponse ctx2;

	// This tag will be used to cancel the request
	private String tag_string_req = "string_req";

	public StringRequestActivity() {

	}

	public StringRequestActivity(IAsyncResponse c) {

		ctx2 = c;
	}

	/**
	 * Making json object request
	 */
	public String makeStringReq(String url) {
		this.URL = url;

		// showProgressDialog();

		StringRequest strReq = new StringRequest(Method.GET, URL, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				Log.e("INRESPONSE", response.toString());
				result = response;
				// msgResponse.setText(response.toString());
				// hideProgressDialog();

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				VolleyLog.e("INERROR", "Error: " + error.getMessage());
				result = error.toString();
				// hideProgressDialog();
			}
		});

		// Adding request to request queue
		strReq.setShouldCache(false);
		VolleySingleton.getInstance().addToRequestQueue(strReq, tag_string_req);
		return result;

	}

	public void makeStringReqWithInterface(String url) {
		this.URL = url;

		// showProgressDialog();

		StringRequest strReq = new StringRequest(Method.GET, URL, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				Log.e("INRESPONSE", response.toString());

				ctx2.onRestIntractionResponse(response);
				result = response;
				// msgResponse.setText(response.toString());
				// hideProgressDialog();

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				VolleyLog.e("INERROR", "Error: " + error.getMessage());
				ctx2.onRestIntractionError(error.toString());
				// result = error.toString();
				// hideProgressDialog();
			}
		});

		// Adding request to request queue
		strReq.setShouldCache(false);
		VolleySingleton.getInstance().addToRequestQueue(strReq, tag_string_req);
		// return result;

	}

	/**
	 * Making json object request
	 */
	public String makeStringReqWithProgressbar(final Context ctx, String url) {
		this.URL = url;
		pDialog = new ProgressDialog(ctx, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
		pDialog.setMessage("Loading...");
		pDialog.setCancelable(false);

		pDialog.show();

		StringRequest strReq = new StringRequest(Method.GET, URL, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				Log.e(TAG, response.toString());
				result = response;
				pDialog.dismiss();
				dialog = new Dialog(ctx);

				Calendar cl = Calendar.getInstance(TimeZone.getTimeZone("GMT+05:30"), Locale.US);
				final SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
				dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
						WindowManager.LayoutParams.FLAG_FULLSCREEN);
				dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
				dialog.setContentView(R.layout.customdialog);

				dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
				TextView DateOnPopUp = (TextView) dialog.findViewById(R.id.DateOnPopUp);

				DateOnPopUp.setText(sdf.format(cl.getTime()));
				//
				ImageView popUpCancel = (ImageView) dialog.findViewById(R.id.popUpCancel);
				// if button is clicked, close the custom dialog
				popUpCancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});

				dialog.show();

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				VolleyLog.e(TAG, "Error: " + error.getMessage());
				result = error.toString();
				pDialog.dismiss();
				// hideProgressDialog();
			}
		});

		// Adding request to request queue
		strReq.setShouldCache(false);
		VolleySingleton.getInstance().addToRequestQueue(strReq, tag_string_req);
		return result;

	}

	public String likeQuote(String Quoteid) {

		// showProgressDialog();

		StringRequest strReq = new StringRequest(Method.GET,
				VolleySingleton.LIKE_A_QOUTE_URL + "&userid="
						+ String.valueOf(VolleySingleton.userLogin.getInt(VolleySingleton.LOGIN_ID, 0)) + "&quoteid="
						+ Quoteid,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.e("INRESPONSE", response.toString());
						result = response;
						// msgResponse.setText(response.toString());
						// hideProgressDialog();

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.e("INERROR", "Error: " + error.getMessage());
						result = error.toString();
						// hideProgressDialog();
					}
				});

		// Adding request to request queue
		strReq.setShouldCache(false);
		VolleySingleton.getInstance().addToRequestQueue(strReq, tag_string_req);
		return result;

	}

	/**
	 * Making json object request
	 */
	public String makeStringReqWithProgressbarnew(final Context ctx, String url) {
		this.URL = url;
		pDialog = new ProgressDialog(ctx, AlertDialog.THEME_HOLO_DARK);
		pDialog.setMessage("Loading...");
		pDialog.setCancelable(false);

		pDialog.show();

		StringRequest strReq = new StringRequest(Method.GET, URL, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				Log.e(TAG, response.toString());

				try {
					JSONObject jsonObject = new JSONObject(response);
					if (jsonObject.optInt("success") == 1) {
						Toast.makeText(ctx, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();

					} else {
						Toast.makeText(ctx, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				result = response;

				pDialog.dismiss();

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				VolleyLog.e(TAG, "Error: " + error.getMessage());
				result = error.toString();
				Toast.makeText(ctx, result, Toast.LENGTH_SHORT).show();
				pDialog.dismiss();
				// hideProgressDialog();
			}
		});

		// Adding request to request queue
		strReq.setShouldCache(false);
		VolleySingleton.getInstance().addToRequestQueue(strReq, tag_string_req);
		return result;

	}
}