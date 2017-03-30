package com.nearnia.encouragement;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nearnia.encouragement.adapters.SubChannelsAdatpter;
import com.nearnia.encouragement.beanclasses.Channels;
import com.nearnia.encouragement.beanclasses.SelectedSubChannels;
import com.nearnia.encouragement.beanclasses.SubChannels;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FirstIntent extends Fragment {

	RelativeLayout topLevelLayout2;
	static public RelativeLayout topLevelLayout3;
	static public ImageView tutorial34;
	
	ListView Sub_Category_listView = null;
	ImageView coverImage = null, tutorial1, tutorial2, tutorial3, lionRoar2;
	ProgressDialog mProgressDialog;
	// List<ParseObject> ob1 = null;

	public List<SubChannels> SubChannelsList = null;
	static public List<SelectedSubChannels> selectedSubChannelsList = new ArrayList<SelectedSubChannels>();;
	Channels obj;
	static public String result;
	SharedPreferences prefs2;
	ImageView AddButtonImage;

	SubChannelsAdatpter sa = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		View V1 = inflater.inflate(R.layout.activity_first_intent, container, false);

		topLevelLayout2 = (RelativeLayout) V1.findViewById(R.id.top_layout22);
		topLevelLayout3 = (RelativeLayout) V1.findViewById(R.id.top_layout33);
		// tutorial1 = (ImageView) V1.findViewById(R.id.tutorial1);

		tutorial3 = (ImageView) V1.findViewById(R.id.tutorial3);
		
		tutorial34 = (ImageView) V1.findViewById(R.id.tutorial34);
		AddButtonImage = (ImageView) V1.findViewById(R.id.imageViewAddButton12);
		AddButtonImage.setVisibility(View.GONE);
		AddButtonImage.setEnabled(false);
		prefs2 = getActivity().getPreferences(Context.MODE_PRIVATE);
		Boolean ranBefore = VolleySingleton.screen2.getBoolean("RanBefore2", false);

		if (!ranBefore) {

			topLevelLayout2.setVisibility(View.VISIBLE);
			tutorial3.setImageResource(R.drawable.t1);
			VolleySingleton.screen2.edit().putBoolean("RanBefore2", true).commit();
		} else {
			AddButtonImage.setVisibility(View.VISIBLE);
		}

		topLevelLayout2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				topLevelLayout2.setVisibility(View.GONE);
				tutorial3.setImageResource(android.R.color.transparent);
				// tutorial1.setVisibility(View.VISIBLE);
				AddButtonImage.setVisibility(View.VISIBLE);
				AddButtonImage.setClickable(false);
			}
		});
		topLevelLayout3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				topLevelLayout3.setVisibility(View.GONE);
				tutorial34.setImageResource(android.R.color.transparent);

				// topLevelLayout2.setVisibility(View.GONE);
				AddButtonImage.setClickable(true);

			}
		});

		obj = Singleton.getInstance().getObject();
		coverImage = (ImageView) V1.findViewById(R.id.bussinessImage);

		Picasso.with(getActivity()).load(obj.getCoverImageUrl().replace(" ", "%20")).placeholder(R.drawable.logo)
				.into(coverImage);

		Sub_Category_listView = (ListView) V1.findViewById(R.id.lvFirstIntent);



		AddButtonImage.setClickable(false);
		AddButtonImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				result = "";

				for (SubChannels p : sa.getBox()) {
					if (p.box) {
						SelectedSubChannels obj = new SelectedSubChannels();
						obj.setSelelctedObjectIds(p.subChannelId);

						result += "," + p.getSubChannelId();

						selectedSubChannelsList.add(obj);
					}

				}
				StringBuilder sb = new StringBuilder(result);
				if (sb.length() > 0) {
					sb.deleteCharAt(0);
				}

				else {

				}
				result = sb.toString();

				if (VolleySingleton.userLogin.getBoolean("LOGIN_STATUS", false)) {

					VolleySingleton.TABHOST = 0;

					mProgressDialog = new ProgressDialog(getActivity(), AlertDialog.THEME_HOLO_DARK);
					mProgressDialog.setMessage("Please wait....");
					mProgressDialog.setIndeterminate(true);
					mProgressDialog.setCancelable(false);
					mProgressDialog.show();

					StringRequest strReq = new StringRequest(Method.GET,
							VolleySingleton.SERVER_URL_WITH_SLASH + "index.php?method=savecategory&userid="
									+ String.valueOf(VolleySingleton.userLogin.getInt("LOGIN_ID", 0)) + "&categoryid="
									+ result,
							new Response.Listener<String>() {

						@Override
						public void onResponse(String response) {
							// Log.e(ProfileActivity.class.getSimpleName(),
							// response.toString());
							mProgressDialog.dismiss();
							// try {
							// JSONObject jsonObject = new JSONObject(response);

							Intent intent = new Intent(getActivity(), MainActivity2.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

							startActivity(intent);

							// } catch (JSONException e) {
							//
							// e.printStackTrace();
							// }

						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {

						}
					});
					mProgressDialog.dismiss();

					strReq.setShouldCache(false);
					VolleySingleton.getInstance().addToRequestQueue(strReq, "SAVE_CATEGORIES_REQUEST");

				} else {

					Intent intent = new Intent(getActivity(), SignUpActivity.class);
					startActivity(intent);

				}

			}
		});
		// lionRoar2.setOnClickListener(null);
		// lionRoar2.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		//
		// Intent i = new Intent(getActivity(), MainActivity2.class);
		// VolleySingleton.TABHOST = 0;
		// i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
		// Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// startActivity(i);
		//
		// }
		// });
		new RemoteDataTask().execute();
		return V1;
	}

	private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected Void doInBackground(Void... params) {

			SubChannelsList = new ArrayList<SubChannels>();
			JSONArray jsonArray = obj.getJsonArray();
			for (int i = 0; i < jsonArray.length(); i++) {
				SubChannels sC = new SubChannels();
				JSONObject subChannlesObject = jsonArray.optJSONObject(i);

				sC.setSubChannelId(subChannlesObject.optString("categoryid"));
				sC.setSubChannelName(subChannlesObject.optString("category"));
				sC.setSelected(subChannlesObject.optInt("isselect") == 1 ? true : false);

				sC.setBox(false);
				SubChannelsList.add(sC);

			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			sa = new SubChannelsAdatpter(getActivity(), SubChannelsList);
			Sub_Category_listView.setAdapter(sa);
			AddButtonImage.setEnabled(true);

		}

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

}
