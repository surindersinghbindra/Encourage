package com.nearnia.encouragement;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nearnia.encouragement.util.ServiceHandler;

import com.nearnia.encouragement.adapters.ListViewAdapter;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.nearnia.encouragement.beanclasses.Channels;
import com.nearnia.encouragement.beanclasses.SubChannels;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class CategoriesActivity extends Fragment {

	RelativeLayout topLevelLayout;
	ImageView iView1, iView2, iView3, lionRoar1;
	SharedPreferences prefs;
	ListView listview;

	ProgressDialog mProgressDialog;
	ListViewAdapter adapter;

	public List<SubChannels> SubChannelsList;
	public List<SubChannels> all_Sub_Categories;
	private static List<Channels> channelList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View V = inflater.inflate(R.layout.fragment_home_activity, container, false);

		listview = (ListView) V.findViewById(R.id.forlistfragment);
		topLevelLayout = (RelativeLayout) V.findViewById(R.id.top_layout);

//		lionRoar1 = (ImageView) V.findViewById(R.id.LionRoar1);
//		lionRoar1.setVisibility(View.GONE);

//		if (VolleySingleton.userLogin.getBoolean(VolleySingleton.LOGIN_STATUS, false)) {
//			lionRoar1.setVisibility(View.VISIBLE);
//		} else {
//			lionRoar1.setVisibility(View.INVISIBLE);
//		}

//		TextView tV = (TextView) V.findViewById(R.id.textViewInspirarion);
		iView1 = (ImageView) V.findViewById(R.id.ivInstruction1);
		iView2 = (ImageView) V.findViewById(R.id.ivInstruction2);
		iView3 = (ImageView) V.findViewById(R.id.ivInstruction3);
		iView3.setScaleType(ScaleType.FIT_XY);
//		tV.setTypeface(null, Typeface.ITALIC);
//		tV.setTextSize(30);
		// prefs = getActivity().getPreferences(Context.MODE_PRIVATE)
		Boolean ranBefore = VolleySingleton.screen1.getBoolean("RanBefore", false);
		// Boolean ranBefore = prefs.getBoolean("RanBefore", false);

		if (!ranBefore) {

			topLevelLayout.setVisibility(View.VISIBLE);
			VolleySingleton.screen1.edit().putBoolean("RanBefore", true).commit();
		}

		iView1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				iView1.setVisibility(View.GONE);
				iView2.setVisibility(View.VISIBLE);

			}
		});
		iView2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				iView2.setVisibility(View.GONE);

				iView3.setVisibility(View.VISIBLE);

			}
		});
		iView3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				iView3.setVisibility(View.GONE);

				topLevelLayout.setVisibility(View.GONE);

			}
		});

//		lionRoar1.setOnClickListener(null);
//		lionRoar1.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//
//				Intent i = new Intent(getActivity(), MainActivity2.class);
//				VolleySingleton.TABHOST = 0;
//				i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivity(i);
//
//			}
//		});

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Channels clickedObj = (Channels) parent.getItemAtPosition(position);

				Singleton.getInstance().setObject(clickedObj);

				FirstIntent SecondFragment = new FirstIntent();

				FragmentTransaction ft = getFragmentManager().beginTransaction().addToBackStack(null);
				ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.left_to_right, R.anim.from_right_to_left);
				ft.replace(R.id.fragment_container, SecondFragment, "detailFragment");
				// Start the animated transition.
				ft.commit();

			}

		});

		if (channelList == null) {
			new RemoteDataTask().execute();
		} else {
			new RemoteDataTask().execute();
			adapter = new ListViewAdapter(getActivity(), channelList);

			listview.setAdapter(adapter);
		}

		return V;

	}

	// RemoteDataTask AsyncTask
	private class RemoteDataTask extends AsyncTask<Void, Void, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			mProgressDialog = new ProgressDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_DARK);
			mProgressDialog.setTitle("Updating Categories");
			mProgressDialog.setMessage("Please wait...");
			mProgressDialog.setCancelable(false);
			mProgressDialog.setIndeterminate(false);
			mProgressDialog.show();
		}

		@Override
		protected String doInBackground(Void... params) {

			String rs = "";
			String channnels;
			ServiceHandler sH = new ServiceHandler();
			if (VolleySingleton.userLogin.getBoolean("LOGIN_STATUS", false)) {
				channnels = sH.makeServiceCall(VolleySingleton.CHANNELS_URL + "&userid="
						+ String.valueOf(VolleySingleton.userLogin.getInt("LOGIN_ID", 0)), ServiceHandler.GET);
			} else {
				channnels = sH.makeServiceCall(VolleySingleton.CHANNELS_URL, ServiceHandler.GET);
			}

			all_Sub_Categories = new ArrayList<SubChannels>();
			if (channnels != null) {
				channelList = new ArrayList<Channels>();

				try {

					JSONObject jsonObject = new JSONObject(channnels);
					JSONArray data = jsonObject.getJSONArray("data");

					for (int i = 0; i < data.length(); i++) {
						Channels channel = new Channels();

						JSONObject c = data.getJSONObject(i);
						channel.setObjectId(c.getString("categoryid"));
						channel.setChannelName(c.getString("category"));
						channel.setIconImageUrl(c.getString("picture"));
						channel.setCoverImageUrl(c.getString("coverpicture"));
						JSONArray subcategory = c.optJSONArray("subcategory");
						channel.setJsonArray(subcategory);

						SubChannelsList = new ArrayList<SubChannels>();

						for (int j = 0; j < subcategory.length(); j++) {

							SubChannels sC = new SubChannels();
							JSONObject subChannlesObject = subcategory.getJSONObject(j);
							sC.setSubChannelId(subChannlesObject.getString("categoryid"));
							sC.setSubChannelName(subChannlesObject.getString("category"));

							SubChannelsList.add(sC);
							all_Sub_Categories.add(sC);
							rs = "result";

						}
						channelList.add(channel);

					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
//				VolleySingleton.volley_All_categories_method(all_Sub_Categories);
			} else {

				rs = "";

			}

			return rs;
		}

		@Override
		protected void onPostExecute(String result) {

			if (result == null || result == "") {
				mProgressDialog.dismiss();
				Toast.makeText(getActivity(), "Check your connection and try again", Toast.LENGTH_LONG).show();
			} else {
				adapter = new ListViewAdapter(getActivity(), channelList);

				listview.setAdapter(adapter);
				mProgressDialog.dismiss();
			}

		}
	}

}
