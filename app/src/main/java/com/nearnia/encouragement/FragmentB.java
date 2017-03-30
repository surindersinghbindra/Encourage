package com.nearnia.encouragement;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.nearnia.encouragement.beanclasses.SubChannels;

public class FragmentB extends Fragment {

	private  LinearLayout linearlayoutinScrollview;
	private ProgressDialog mProgressDialog;

	private List<SubChannels> all_Selected_SubCategories = null;

	 public FragmentB() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v2 = new View(getActivity());

		v2 = inflater.inflate(R.layout.fragmentb_layout, container, false);

		linearlayoutinScrollview = (LinearLayout) v2.findViewById(R.id.LinearlayoutinScrollview12);

		all_Selected_SubCategories = new ArrayList<SubChannels>();

		mProgressDialog = new ProgressDialog(getActivity(), AlertDialog.THEME_HOLO_DARK);

		mProgressDialog.setMessage("Please wait....");
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setCancelable(false);
		mProgressDialog.show();

		StringRequest strReq = new StringRequest(Method.GET,
				VolleySingleton.SAVED_CATEGORIES_USER + "&userid="
						+ String.valueOf(VolleySingleton.userLogin.getInt("LOGIN_ID", 0)),
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.e(ProfileActivity.class.getSimpleName(), response.toString());

						try {
							JSONObject jsonObject = new JSONObject(response);

							if (jsonObject.has("data")) {
								JSONArray data = jsonObject.optJSONArray("data");

								for (int i = 0; i < data.length(); i++) {
									JSONObject obj = data.getJSONObject(i);

									if (obj.getInt("ishidden") == 1) {

										// masked Sub - categories
										SubChannels sC = new SubChannels();
										sC.setBox(true);
										sC.setIsmasked(true);
										sC.setSubChannelId(obj.getString("categoryid"));
										sC.setSubChannelName(obj.getString("category"));
										all_Selected_SubCategories.add(sC);

									} else {

										// Unmasked Sub - categories
										SubChannels sC = new SubChannels();
										sC.setBox(true);
										sC.setIsmasked(false);
										sC.setSubChannelId(obj.getString("categoryid"));
										sC.setSubChannelName(obj.getString("category"));
										all_Selected_SubCategories.add(sC);
									}

								}

							} else {
								Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT)
										.show();
								Intent i = new Intent(getActivity(), MainActivity2.class);

								i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
								VolleySingleton.userLogin.edit().putBoolean("LION_PRESSES", true).commit();
								startActivity(i);
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block

							e.printStackTrace();
						}

						mProgressDialog.dismiss();
						set_Data_In_List();

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						mProgressDialog.dismiss();

					}
				});

		// Adding request to request queue
		strReq.setRetryPolicy(new DefaultRetryPolicy(1000, 10, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		strReq.setShouldCache(false);
		VolleySingleton.getInstance().addToRequestQueue(strReq, "SAVED_CATEGORIES_REQUEST");
		// set_Data_In_List();

		return v2;
	}

	private void set_Data_In_List() {
		// TODO Auto-generated method stub

		linearlayoutinScrollview.removeAllViews();
		final List<SubChannels> all_Categories_List = all_Selected_SubCategories;
		Log.e("*******", String.valueOf(all_Categories_List.size()));

		for (int i = 0; i < all_Categories_List.size(); i++) {
			LayoutInflater inflater = getActivity().getLayoutInflater();
			View vi = inflater.inflate(R.layout.settings_list_view, null);
			// vi.setTag(i);
			CheckBox mask_unmask_checkBox = (CheckBox) vi.findViewById(R.id.mask_unmask_checkBox);
			CheckBox checbox_Selected_Sub_categoty = (CheckBox) vi.findViewById(R.id.checbox_Selected_Sub_categoty);
			TextView sub_Category_Name = (TextView) vi.findViewById(R.id.category_name);

			mask_unmask_checkBox.setTag(i);
			checbox_Selected_Sub_categoty.setTag(i);

			OnCheckedChangeListener mask_unmask_Listner = new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

					all_Categories_List.get((Integer) buttonView.getTag()).setIsmasked(isChecked);
					all_Categories_List.get((Integer) buttonView.getTag()).setBox(isChecked);

					StringRequest strReq = new StringRequest(Method.GET,
							VolleySingleton.HIDECATEGORY_URL + "&userid="
									+ String.valueOf(VolleySingleton.userLogin.getInt("LOGIN_ID", 0)) + "&categoryid="
									+ String.valueOf(
											all_Categories_List.get((Integer) buttonView.getTag()).getSubChannelId()),
							new Response.Listener<String>() {

						@Override
						public void onResponse(String response) {
							Log.e(ProfileActivity.class.getSimpleName(), response.toString());

							try {
								JSONObject jsonObject = new JSONObject(response);
								jsonObject.optString("message");

							}

							catch (JSONException e) {

								e.printStackTrace();
							}

						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {

						}
					});
					// Adding request to request queue
					strReq.setShouldCache(false);
					VolleySingleton.getInstance().addToRequestQueue(strReq, "MASK_UNMASKED_REQUEST");

					all_Categories_List.get((Integer) buttonView.getTag()).setIsmasked(isChecked);

				}
			};

			mask_unmask_checkBox.setOnCheckedChangeListener(null);
			if (all_Categories_List.get(i).ismasked)
				mask_unmask_checkBox.setChecked(true);
			else
				mask_unmask_checkBox.setChecked(false);
			mask_unmask_checkBox.setOnCheckedChangeListener(mask_unmask_Listner);

			OnCheckedChangeListener checbox_Selected_Sub_categoty_listner = new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

					if (isChecked) {

						all_Categories_List.get((Integer) buttonView.getTag()).setBox(isChecked);

					} else {

						all_Categories_List.get((Integer) buttonView.getTag()).setBox(isChecked);

						StringRequest strReq = new StringRequest(Method.GET,
								VolleySingleton.DELETE_CATEGORIES_URL + "&userid="
										+ String.valueOf(VolleySingleton.userLogin.getInt(VolleySingleton.LOGIN_ID, 0))
										+ "&categoryid=" + String.valueOf(all_Categories_List
												.get((Integer) buttonView.getTag()).getSubChannelId()),
								new Response.Listener<String>() {

							@Override
							public void onResponse(String response) {
								Log.e(ProfileActivity.class.getSimpleName(), response.toString());

								try {
									JSONObject jsonObject = new JSONObject(response);
									String data = jsonObject.optString("message");
									Log.e("message", data + "");

								}

								catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
						}, new Response.ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError error) {

							}
						});
						// Adding request to request queue
						strReq.setShouldCache(false);
						VolleySingleton.getInstance().addToRequestQueue(strReq, "REMOVE_SUBCATEGORY_REQUEST");
					}

				}
			};

			checbox_Selected_Sub_categoty.setOnCheckedChangeListener(null);
			if (all_Categories_List.get(i).box)
				checbox_Selected_Sub_categoty.setChecked(true);
			else
				checbox_Selected_Sub_categoty.setChecked(false);

			checbox_Selected_Sub_categoty.setOnCheckedChangeListener(checbox_Selected_Sub_categoty_listner);

			sub_Category_Name.setText(all_Categories_List.get(i).subChannelName);
			mask_unmask_checkBox.setChecked(all_Categories_List.get(i).ismasked);
			checbox_Selected_Sub_categoty.setChecked(all_Categories_List.get(i).box);

			linearlayoutinScrollview.addView(vi);

		}
	}

}
