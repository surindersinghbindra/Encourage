package com.nearnia.encouragement;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nearnia.encouragement.beanclasses.SubChannels;
import com.nearnia.encouragement.util.CompressImage;
import com.nearnia.encouragement.util.DCircularImageView;
import com.nearnia.encouragement.util.ImageLoadingUtils;
import com.nearnia.encouragement.util.SaveProfile;
import com.nearnia.encouragement.util.ServiceHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("InflateParams")
public class ProfileActivity extends Fragment {

	final Context ctx;
	private ImageLoadingUtils utils;
//	private ListView profile_Settings_listView;
	// ProgressDialog mProgressDialog;
	public static DCircularImageView profile_image;
	public static TextView userNAme;

	public Bitmap bp;

	static int i = 0;
	private MyPageAdapter pageAdapter;

	private TextView profileTagLine, website, textViewName, textViewName1, textViewName2, textViewName3, textViewName4;
	private ImageView profile_cover_image, lionRoar, tutorial8;

	SubChannels sC;
	List<SubChannels> all_Selected_SubCategories = null;
	LinearLayout linearlayoutinScrollview;

	byte[] coverImageBytedata;

	SharedPreferences prefs_Profile;

	RelativeLayout top_layout_profile;
	RelativeLayout WithNameGreatJob;

	private String pictureEncoded1, rP;

	public ProfileActivity() {
		this.ctx = getActivity();
	}



	ProgressBar progressbarSmall;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



		View V = inflater.inflate(R.layout.settings_activity2, container, false);

		utils = new ImageLoadingUtils(getActivity());

		progressbarSmall = (ProgressBar) V.findViewById(R.id.progressbarSmall);

		// mProgressDialog = new ProgressDialog(getActivity(),
		// AlertDialog.THEME_HOLO_DARK);
		//
		// mProgressDialog.setMessage("Please wait....");
		// mProgressDialog.setIndeterminate(true);
		// mProgressDialog.setCancelable(false);

		top_layout_profile = (RelativeLayout) V.findViewById(R.id.top_layout_profile);
		WithNameGreatJob = (RelativeLayout) V.findViewById(R.id.WithNameGreatJob);

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

		Boolean ranBefore = VolleySingleton.screen3.getBoolean("RanBefore3", false);
		

		
		if (!ranBefore) {
			WithNameGreatJob.setBackgroundResource(R.drawable.tutorial7);
			
			top_layout_profile.setVisibility(View.VISIBLE);

			WithNameGreatJob.setVisibility(View.VISIBLE);
			String s = "Great job " + VolleySingleton.userLogin.getString(VolleySingleton.USERNAME, " ") + "!";

			textViewName.setTypeface(VolleySingleton.face);
			textViewName.setText(s);

			VolleySingleton.screen3.edit().putBoolean("RanBefore3", true).commit();
		}

		WithNameGreatJob.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				WithNameGreatJob.setVisibility(View.GONE);
				WithNameGreatJob.setBackgroundResource(android.R.color.transparent);
				tutorial8.setBackgroundResource(R.drawable.tutorial8);
				tutorial8.setVisibility(View.VISIBLE);

			}
		});
		tutorial8.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				tutorial8.setVisibility(View.GONE);
				tutorial8.setBackgroundResource(android.R.color.transparent);

				top_layout_profile.setVisibility(View.GONE);

			}
		});

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
		if (!VolleySingleton.userLogin.getBoolean(VolleySingleton.LOGIN_STATUS, false)) {

			Toast.makeText(ctx, "You are logged out!! \n Please Login Again", Toast.LENGTH_LONG).show();
			Intent i = new Intent(getActivity(), SignUpActivity.class);
			startActivity(i);
			FragmentTransaction ft = getFragmentManager().beginTransaction().addToBackStack(null);
			ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.enter, R.anim.exit);
			ft.commit();
		} else {

		

			Picasso.with(getActivity())
					.load(VolleySingleton.SERVER_URL_WITH_SLASH
							+ VolleySingleton.userLogin.getString(VolleySingleton.PROFILE_IMAGE_URL, ""))
					.placeholder(R.drawable.user1).into(profile_image);

			Picasso.with(getActivity())
					.load(VolleySingleton.SERVER_URL_WITH_SLASH
							+ VolleySingleton.userLogin.getString(VolleySingleton.COVER_IMAGE_URL, ""))
					.placeholder(R.drawable.regenarobinsonbackground).into(profile_cover_image);

			userNAme.setText(VolleySingleton.userLogin.getString(VolleySingleton.USERNAME, "USERNAME"));
			website.setText(VolleySingleton.userLogin.getString(VolleySingleton.COMPANY_NAME, "your company profile"));
			profileTagLine.setText(
					VolleySingleton.userLogin.getString(VolleySingleton.PROFILE_TAGLINE, "your profile tagline"));
			
		
		}

		website.setOnLongClickListener(null);
		website.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				LayoutInflater li = LayoutInflater.from(getActivity());

				View promptsView = li.inflate(R.layout.prompts, null);

				ContextThemeWrapper themedContext;
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					themedContext = new ContextThemeWrapper(getActivity(),
							android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
				} else {
					themedContext = new ContextThemeWrapper(getActivity(), android.R.style.Theme_Light_NoTitleBar);
				}
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(themedContext);
				alertDialogBuilder.setView(promptsView);
				final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);

				// set dialog message
				alertDialogBuilder.setCancelable(false).setPositiveButton("ADD", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {

						String companytagline = userInput.getText().toString();
						if (TextUtils.isEmpty(companytagline))
							companytagline = "bussiness not set";

						website.setText(companytagline);
						VolleySingleton.userLogin.edit().putString(VolleySingleton.COMPANY_NAME, companytagline)
								.commit();

						SaveProfile saveProfile = new SaveProfile();
						saveProfile.methodSaveProfile();

					}
				}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();

					}
				});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();
				return false;
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
				LayoutInflater li = LayoutInflater.from(getActivity());

				View promptsView = li.inflate(R.layout.prompts, null);
				ContextThemeWrapper themedContext;
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					themedContext = new ContextThemeWrapper(getActivity(),
							android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
				} else {
					themedContext = new ContextThemeWrapper(getActivity(), android.R.style.Theme_Light_NoTitleBar);
				}
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(themedContext);

				alertDialogBuilder.setView(promptsView);
				final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);

				// set dialog message
				alertDialogBuilder.setCancelable(false).setPositiveButton("ADD", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {

						String pTagLine = userInput.getText().toString();
						if (TextUtils.isEmpty(pTagLine))
							pTagLine = "profile not set";

						profileTagLine.setText(pTagLine);
						VolleySingleton.userLogin.edit().putString(VolleySingleton.PROFILE_TAGLINE, pTagLine).commit();
						SaveProfile saveProfile = new SaveProfile();
						saveProfile.methodSaveProfile();

					}
				}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();
				return false;
			}
		});

		profile_cover_image.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {

				Intent intent = new Intent();

				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);

				startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);

				return false;
			}
		});

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
		List<Fragment> fList = new ArrayList<>();

		fList.add(new FragmentB());
		fList.add(new QoutesYouLoved());
		fList.add(YourOwnWrittenFragment.newInstance("inspire_active"));

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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {

			Uri uri1 = data.getData();

			CompressImage compressImage = new CompressImage();
			rP = compressImage.compressImage(getActivity(), uri1);
			bp = utils.decodeBitmapFromPath(rP);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
			coverImageBytedata = stream.toByteArray();

			pictureEncoded1 = Base64.encodeToString(coverImageBytedata, Base64.DEFAULT);
			new RemoteDataTask2().execute();
			System.gc();
		}
	}

	private class RemoteDataTask2 extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			progressbarSmall.setVisibility(View.VISIBLE);
			progressbarSmall.bringToFront();
			// mProgressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {

			ServiceHandler sH = new ServiceHandler();

			String g = sH.toUploadCoverImage(VolleySingleton.COVER_IMAGE_UPLOAD_URL, rP,
					String.valueOf(VolleySingleton.userLogin.getInt(VolleySingleton.LOGIN_ID, 0)));

			return g;
		}

		@Override
		protected void onPostExecute(String result) {

			try {
				JSONObject reader = new JSONObject(result);

				int success = reader.getInt("success");

				if (success == 1) {
					// mProgressDialog.dismiss();

					progressbarSmall.setVisibility(View.GONE);

					String cover_image_url = reader.getString("imageName");

					VolleySingleton.userLogin.edit().putString(VolleySingleton.COVER_IMAGE_URL, cover_image_url)
							.commit();
					VolleySingleton.userLogin.edit().putString(VolleySingleton.COVER_IMAGE_BYTE_DATA, pictureEncoded1)
							.commit();
					profile_cover_image.setImageBitmap(decodeBase64(
							VolleySingleton.userLogin.getString(VolleySingleton.COVER_IMAGE_BYTE_DATA, "NOTSET")));

					SaveProfile saveProfile = new SaveProfile();
					saveProfile.methodSaveProfile();

					coverImageBytedata = null;
					pictureEncoded1 = null;
					Log.e("String while signup", String.valueOf(cover_image_url));
					System.gc();

				} else {
					progressbarSmall.setVisibility(View.GONE);
					// mProgressDialog.dismiss();
					Toast.makeText(getActivity(), "image upload failed", Toast.LENGTH_LONG).show();

				}

			} catch (JSONException e) {

				e.printStackTrace();
			}
			progressbarSmall.setVisibility(View.GONE);
			// mProgressDialog.dismiss();
			return;
		}
	}

	public Bitmap decodeBase64(String input) {
		byte[] decodedByte = Base64.decode(input, 0);
		return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
	}

}