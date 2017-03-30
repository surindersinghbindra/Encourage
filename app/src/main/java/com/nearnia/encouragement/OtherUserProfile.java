package com.nearnia.encouragement;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nearnia.encouragement.util.DCircularImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.nearnia.encouragement.beanclasses.SubChannels;

@SuppressLint("InflateParams")
public class OtherUserProfile extends Fragment {

	final Context ctx;
	// ProgressDialog mProgressDialog;
	public static DCircularImageView profile_image;
	static TextView userNAme;

	static int i = 0;
	private MyPageAdapter pageAdapter;

	TextView profileTagLine, website, textViewName, textViewName1, textViewName2, textViewName3, textViewName4;
	ImageView profile_cover_image, lionRoar, tutorial7, tutorial8;
//	QuotesYouLiked quotesYouLiked;
	SubChannels sC;
	List<SubChannels> all_Selected_SubCategories = null;
	LinearLayout linearlayoutinScrollview;

	public OtherUserProfile() {
		this.ctx = getActivity();
	}

//	public OtherUserProfile(Context ctx) {
//		this.ctx = ctx;
//	}

	ProgressBar progressbarSmall;

	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//		quotesYouLiked = (QuotesYouLiked) UniversalSingleton.getInstance().getObject();

//		Log.e("OtherUser", quotesYouLiked.getUserid());

		View V = inflater.inflate(R.layout.settings_activity2, container, false);

		textViewName = (TextView) V.findViewById(R.id.textViewName);
		textViewName1 = (TextView) V.findViewById(R.id.textViewName1);
		textViewName2 = (TextView) V.findViewById(R.id.textViewName2);
		textViewName3 = (TextView) V.findViewById(R.id.textViewName3);
		textViewName4 = (TextView) V.findViewById(R.id.textViewName4);
		textViewName1.setTypeface(VolleySingleton.face);
		textViewName2.setTypeface(VolleySingleton.face);
		textViewName3.setTypeface(VolleySingleton.face);
		textViewName4.setTypeface(VolleySingleton.face);

		tutorial8 = (ImageView) V.findViewById(R.id.tutorial8);
		final ViewPager vPager = (ViewPager) V.findViewById(R.id.pager);
		vPager.setOffscreenPageLimit(2);

		lionRoar = (ImageView) V.findViewById(R.id.LionRoar);
		linearlayoutinScrollview = (LinearLayout) V.findViewById(R.id.LinearlayoutinScrollview);

		profile_image = (DCircularImageView) V.findViewById(R.id.profile_image);
		profile_cover_image = (ImageView) V.findViewById(R.id.profile_cover_image);
		profileTagLine = (TextView) V.findViewById(R.id.profileTagLine);
		website = (TextView) V.findViewById(R.id.website);
		userNAme = (TextView) V.findViewById(R.id.userNAme);

		userNAme.setTypeface(VolleySingleton.face);
		website.setTypeface(VolleySingleton.face);
		profileTagLine.setTypeface(VolleySingleton.face);
//		Picasso.with(ctx).load(quotesYouLiked.getPicture()).placeholder(R.drawable.user1).into(profile_image);

//		Picasso.with(getActivity()).load(quotesYouLiked.getPicture()).placeholder(R.drawable.regenarobinsonbackground)
//				.into(profile_cover_image);

//		userNAme.setText(quotesYouLiked.getFullname());
		// website.setText(VolleySingleton.userLogin.getString(VolleySingleton.COMPANY_NAME,
		// "your company profile"));
		// profileTagLine
		// .setText(VolleySingleton.userLogin.getString(VolleySingleton.PROFILE_TAGLINE,
		// "your profile tagline"));

		website.setOnLongClickListener(null);
		website.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				UserWebSite userWebSite = new UserWebSite();
				Bundle bundle = new Bundle();

				if (!website.getText().toString().startsWith("http://")) {

					String url = "http://" + website.getText().toString();
					bundle.putString("URL", url);
				}

				userWebSite.setArguments(bundle);

				FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
				FragmentTransaction ft = fragmentManager.beginTransaction().addToBackStack(null);
				ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.left_to_right, R.anim.from_right_to_left);
				ft.replace(R.id.fragment_container, userWebSite, "userWebSite");
				// Start the animated transition.
				ft.commit();

			}
		});

		lionRoar.setOnClickListener(null);
		lionRoar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				VolleySingleton.TABHOST = 4;
				Intent i = new Intent(getActivity(), MainActivity2.class);

				i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TOP);

				startActivity(i);

			}
		});

		profileTagLine.setOnLongClickListener(null);
		profileTagLine.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				return false;
			}
		});

		StringRequest strReq = new StringRequest(Method.GET, VolleySingleton.SERVER_URL_WITH_SLASH
				+ "index.php?method=getProfile&userid=" + VolleySingleton.userLogin.getString("USERID_RECIEVED", "0"),
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {

						Log.e(ProfileActivity.class.getSimpleName(), response.toString());

						try {
							JSONObject jsonObject = new JSONObject(response);
							Log.e("jsonObject.hasdata)", jsonObject.has("data") + "");
							if (jsonObject.getInt("success") == 1) {

								JSONArray data = jsonObject.optJSONArray("data");

								userNAme.setText(data.getJSONObject(0).optString("fullname"));
								profileTagLine.setText(data.getJSONObject(0).optString("profile"));

								SpannableString content = new SpannableString(
										data.getJSONObject(0).optString("companyUrl"));
								content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
								website.setText(content);

								// website.setText(data.getJSONObject(0).optString("companyUrl"));

								String profile_image_Url = data.getJSONObject(0).optString("picture");
								String cover_image_Url = data.getJSONObject(0).optString("coverpicture");

								Picasso.with(getActivity())
										.load(VolleySingleton.SERVER_URL_WITH_SLASH + profile_image_Url)
										.placeholder(R.drawable.user1).error(R.drawable.user1).into(profile_image);

								Picasso.with(getActivity())
										.load(VolleySingleton.SERVER_URL_WITH_SLASH + cover_image_Url)
										.placeholder(R.drawable.regenarobinsonbackground)
										.error(R.drawable.regenarobinsonbackground).into(profile_cover_image);

								if (jsonObject.has("category")) {
									JSONArray categoryArray = jsonObject.optJSONArray("category");
									for (int i = 0; i < categoryArray.length(); i++) {
										JSONObject jsoncatObj = categoryArray.getJSONObject(i);

										if (jsoncatObj.getInt("ishidden") == 1) {

										} else {

											// selectedCategories.add(jsoncatObj.getString("category"));
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

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {

					}
				});

		// Adding request to request queue
		strReq.setRetryPolicy(new DefaultRetryPolicy(1000, 10, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		strReq.setShouldCache(false);
		VolleySingleton.getInstance().addToRequestQueue(strReq, "SAVED_USER_CATEGORIES_REQUEST");

		List<Fragment> fragments = getFragments();
		 pageAdapter = new MyPageAdapter(getChildFragmentManager(), fragments);
		vPager.setAdapter(pageAdapter);

		final Button slelctedcategories_btn = (Button) V.findViewById(R.id.slelctedcategories_btn);
		slelctedcategories_btn.setSelected(true);
		final Button liked_btn = (Button) V.findViewById(R.id.liked_btn);
		final Button own_wriiten_btn = (Button) V.findViewById(R.id.own_wriiten_btn);

		slelctedcategories_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				vPager.setCurrentItem(0);
				slelctedcategories_btn.setSelected(true);
				liked_btn.setSelected(false);
				own_wriiten_btn.setSelected(false);

			}
		});
		liked_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				vPager.setCurrentItem(1);
				slelctedcategories_btn.setSelected(false);
				liked_btn.setSelected(true);
				own_wriiten_btn.setSelected(false);

			}
		});
		own_wriiten_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				vPager.setCurrentItem(2);
				slelctedcategories_btn.setSelected(false);
				liked_btn.setSelected(false);
				own_wriiten_btn.setSelected(true);

			}
		});

		vPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {

				switch (arg0) {
				case 0:
					slelctedcategories_btn.setSelected(true);
					liked_btn.setSelected(false);
					own_wriiten_btn.setSelected(false);
					break;

				case 1:
					slelctedcategories_btn.setSelected(false);
					liked_btn.setSelected(true);
					own_wriiten_btn.setSelected(false);

					break;

				default:
					slelctedcategories_btn.setSelected(false);
					liked_btn.setSelected(false);
					own_wriiten_btn.setSelected(true);
					break;
				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

		return V;
	}

	private List<Fragment> getFragments() {
		List<Fragment> fList = new ArrayList<Fragment>();

		fList.add(new FragmentB_Other());
		fList.add(new QoutesLoved_Other());
		fList.add(OwnWrittenFragment_Other.newInstance("inspire_active"));

		return fList;
	}

	private class MyPageAdapter extends FragmentPagerAdapter {
		private List<Fragment> fragments;

		public MyPageAdapter(FragmentManager fm, List<Fragment> fragments) {
			super(fm);
			this.fragments = fragments;
		}

		@Override
		public Fragment getItem(int position) {

			return this.fragments.get(position);
		}

		@Override
		public int getCount() {
			return this.fragments.size();
		}
	}

}