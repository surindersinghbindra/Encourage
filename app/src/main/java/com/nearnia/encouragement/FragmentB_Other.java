package com.nearnia.encouragement;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class FragmentB_Other extends Fragment {

	
	List<String> selectedCategories;
	LinearLayout linearlayoutinScrollview;
	ProgressDialog mProgressDialog;

	List<SubChannels> all_Selected_SubCategories = null;
//	List<SubChannels> all_Selected_SubCategories = null;

	JSONObject jsonObj;

	public FragmentB_Other() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v2 = new View(getActivity());

	

		v2 = inflater.inflate(R.layout.fragmentb_layout, container, false);

		linearlayoutinScrollview = (LinearLayout) v2.findViewById(R.id.LinearlayoutinScrollview12);

//		all_Selected_SubCategories = new ArrayList<SubChannels>();
		
		
		all_Selected_SubCategories = new ArrayList<SubChannels>();
//		selectedCategories = new ArrayList<String>();

		mProgressDialog = new ProgressDialog(getActivity(), AlertDialog.THEME_HOLO_DARK);

		mProgressDialog.setMessage("Please wait....");
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setCancelable(false);
		mProgressDialog.show();
		
		
		
		

		StringRequest strReq = new StringRequest(Method.GET, VolleySingleton.SERVER_URL_WITH_SLASH
				+ "index.php?method=getProfile&userid=" + VolleySingleton.userLogin.getString("USERID_RECIEVED", "0"), new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {

						Log.e(ProfileActivity.class.getSimpleName(), response.toString());

						try {
							JSONObject jsonObject = new JSONObject(response);
							Log.e("jsonObject.hasdata)", jsonObject.has("data") + "");
							if (jsonObject.getInt("success") == 1) {

								JSONArray data = jsonObject.optJSONArray("data");

//								userNAme.setText(data.getJSONObject(0).optString("fullname"));
//								website.setText(data.getJSONObject(0).optString("profile"));
//								profileTagLine.setText(data.getJSONObject(0).optString("companyUrl"));

								String profile_image_Url = data.getJSONObject(0).optString("picture");
								String cover_image_Url = data.getJSONObject(0).optString("coverpicture");

//								Picasso.with(getActivity())
//										.load(VolleySingleton.SERVER_URL_WITH_SLASH + profile_image_Url)
//										.placeholder(R.drawable.user1).error(R.drawable.user1).into(profile_image);

//								Picasso.with(getActivity())
//										.load(VolleySingleton.SERVER_URL_WITH_SLASH + cover_image_Url)
//										.placeholder(R.drawable.regenarobinsonbackground)
//										.error(R.drawable.regenarobinsonbackground).into(profile_cover_image);

								if (jsonObject.has("category")) {
									JSONArray categoryArray = jsonObject.optJSONArray("category");
									for (int i = 0; i < categoryArray.length(); i++) {
										JSONObject jsoncatObj = categoryArray.getJSONObject(i);

										if (jsoncatObj.getInt("ishidden") == 1) {

										} else {
											SubChannels sC = new SubChannels();
											
											sC.setSubChannelId(jsoncatObj.getString("categoryid"));
											sC.setSubChannelName(jsoncatObj.getString("category"));
											all_Selected_SubCategories.add(sC);
//											selectedCategories.add(jsoncatObj.getString("category"));
										}

									}
								} else {
									Toast.makeText(getActivity(), "No category selected by the user",
											Toast.LENGTH_SHORT).show();
									;
								}

							} else {
								Toast.makeText(getActivity(), jsonObject.getString("unable to load profile"),
										Toast.LENGTH_SHORT).show();
								Intent i = new Intent(getActivity(), MainActivity2.class);

								i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
								VolleySingleton.userLogin.edit().putBoolean("LION_PRESSES", false).commit();
								startActivity(i);
							}

						} catch (JSONException e) {

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
		VolleySingleton.getInstance().addToRequestQueue(strReq, "SAVED_USER_CATEGORIES_REQUEST");
		return v2;
	}

	private void set_Data_In_List() {
		// TODO Auto-generated method stub

		linearlayoutinScrollview.removeAllViews();
		final List<SubChannels> all_Categories_List = all_Selected_SubCategories;

		for (int i = 0; i < all_Categories_List.size(); i++) {
			LayoutInflater inflater = getActivity().getLayoutInflater();
			View vi = inflater.inflate(R.layout.other_user_selected_items_list_layout, null);

			TextView sub_Category_Name = (TextView) vi.findViewById(R.id.category_name);
			ImageView whilteBookSub = (ImageView) vi.findViewById(R.id.whilteBookSub);
			whilteBookSub.setTag(i);
			whilteBookSub.setOnClickListener( new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					AllQoutesInSubCategoryFragment allQoutesInSubCategoryFragment = new AllQoutesInSubCategoryFragment();
//					Channels obj = (Channels) Singleton.getInstance().getObject();
					Bundle bundle = new Bundle();
					bundle.putString("SUB_CATEGORY_ID", all_Categories_List.get((int) v.getTag()).subChannelId);
					bundle.putString("CATEGORY_NAME", "Loading....");
					bundle.putString("SUB_CATEGORY_NAME", all_Categories_List.get((int) v.getTag()).subChannelName);
					allQoutesInSubCategoryFragment.setArguments(bundle);
					FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
					FragmentTransaction ft = fragmentManager.beginTransaction().addToBackStack(null);
					ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.left_to_right, R.anim.from_right_to_left);
					ft.replace(R.id.fragment_container, allQoutesInSubCategoryFragment, "detailFragment");
					// Start the animated transition.
					ft.commit();
					
					
				}
			});
			// sub_Category_Name.setTextSize(25);
			// sub_Category_Name.setTypeface(VolleySingleton.face);
			sub_Category_Name.setText(all_Categories_List.get(i).subChannelName);

			linearlayoutinScrollview.addView(vi);

		}
	}

}
