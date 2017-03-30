package com.nearnia.encouragement;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nearnia.encouragement.util.ServiceHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.nearnia.encouragement.adapters.SubChannelsAdatpterDiscoverMore;
import com.nearnia.encouragement.beanclasses.Channels;
import com.nearnia.encouragement.beanclasses.QuotesYouLiked;
import com.nearnia.encouragement.beanclasses.SelectedSubChannels;
import com.nearnia.encouragement.beanclasses.SubChannels;

public class FragmentDiscoverMore extends Fragment {
	public List<SubChannels> SubChannelsList;
	public List<SubChannels> all_Sub_Categories;
	SubChannelsAdatpterDiscoverMore subChannelsAdatpter;
	ListView subChannelslistView;
	ProgressDialog mProgressDialog;
	private ImageView bussinessImage;
	private ImageView AddButtonImage;

	private TextView subcategoryName;

	static public List<SelectedSubChannels> selectedSubChannelsList = new ArrayList<SelectedSubChannels>();;
	static private String result;

	public  FragmentDiscoverMore() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		View V1 = inflater.inflate(R.layout.fragment_discover_more, container, false);
		subChannelslistView = (ListView) V1.findViewById(R.id.lvFirstIntent_discovermore);

		bussinessImage = (ImageView) V1.findViewById(R.id.bussinessImage);

		AddButtonImage = (ImageView) V1.findViewById(R.id.imageViewAddButton12);
		subcategoryName = (TextView) V1.findViewById(R.id.subcategoryName);
	
		subChannelslistView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//
				// Channels clickedObj = (Channels)
				// parent.getItemAtPosition(position);
				//
				// Singleton.getInstance().setObject(clickedObj);

				AllQoutesInSubCategoryFragment SecondFragment = new AllQoutesInSubCategoryFragment();

				FragmentTransaction ft = getFragmentManager().beginTransaction().addToBackStack(null);
				ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.left_to_right, R.anim.from_right_to_left);
				ft.replace(R.id.fragment_container, SecondFragment, "detailFragment");
				// Start the animated transition.
				ft.commit();

			}

		});

		AddButtonImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				result = "";

				for (SubChannels p : subChannelsAdatpter.getBox()) {
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
		new RemoteDataTask().execute();
		return V1;

	}

	private class RemoteDataTask extends AsyncTask<Void, String, String> {
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

			String channnels;
			ServiceHandler sH = new ServiceHandler();
			if (VolleySingleton.userLogin.getBoolean("LOGIN_STATUS", false)) {
				channnels = sH.makeServiceCall(VolleySingleton.CHANNELS_URL + "&userid="
						+ String.valueOf(VolleySingleton.userLogin.getInt("LOGIN_ID", 0)), ServiceHandler.GET);
			} else {
				channnels = sH.makeServiceCall(VolleySingleton.CHANNELS_URL, ServiceHandler.GET);
			}

			return channnels;
		}

		@Override
		protected void onPostExecute(String result) {
			mProgressDialog.dismiss();

			all_Sub_Categories = new ArrayList<SubChannels>();
			if (result != null) {

				try {

					JSONObject jsonObject = new JSONObject(result);
					JSONArray data = jsonObject.getJSONArray("data");

					QuotesYouLiked obj = (QuotesYouLiked) UniversalSingleton.getInstance().getObject();

					for (int i = 0; i < data.length(); i++) {

						Log.e(obj.getCategoryid(), data.optJSONObject(i).optString("categoryid"));

						if (obj.getCategoryid().equals(data.optJSONObject(i).optString("categoryid"))) {

							Channels channel = new Channels();

							JSONObject c = data.getJSONObject(i);

							Picasso.with(getActivity()).load(c.optString("coverpicture").replace(" ", "%20"))
									.placeholder(R.drawable.logo).into(bussinessImage);

							JSONArray subcategory = c.optJSONArray("subcategory");
							subcategoryName.setText(obj.getCategory());

							channel.setJsonArray(subcategory);

							SubChannelsList = new ArrayList<SubChannels>();

							for (int j = 0; j < subcategory.length(); j++) {
								SubChannels sC = new SubChannels();
								JSONObject subChannlesObject = subcategory.optJSONObject(j);

								sC.setSubChannelId(subChannlesObject.optString("categoryid"));
								sC.setSubChannelName(subChannlesObject.optString("category"));
								sC.setSelected(subChannlesObject.optInt("isselect") == 1 ? true : false);

								sC.setBox(false);
								SubChannelsList.add(sC);

							}
							break;

						} else {

						}

					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
//				VolleySingleton.volley_All_categories_method(all_Sub_Categories);

			}
			subChannelsAdatpter = new SubChannelsAdatpterDiscoverMore(getActivity(), SubChannelsList);
			subChannelslistView.setAdapter(subChannelsAdatpter);

		}

	}
}
