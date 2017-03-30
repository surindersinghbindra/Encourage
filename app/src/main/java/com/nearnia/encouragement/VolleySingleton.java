package com.nearnia.encouragement;

import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.TimeZone;

public class VolleySingleton extends Application {

	public static int TABHOST = 0;
	public static SharedPreferences userLogin;
	public static SharedPreferences screen1, screen2, screen3,screen4;
	public static Typeface face;

	static String notificationMessage = "";
	static String profileIdRecieved = "";

	static String fullname = "";
	static String company_name = "";


	
	static public String COVER_IMAGE_URL = "COVER_IMAGE_URL";
	static public String PROFILE_IMAGE_URL = "PROFILE_IMAGE_URL";
	static public String PROFILE_TAGLINE = "PROFILE_TAGLINE";
	static public String COMPANY_NAME = "COMPANY_NAME";
	static public String USERNAME = "USERNAME";
	static public String PASSWORD = "PASSWORD";
	static public String PHONENUMBER = "PHONENUMBER";
	static public String DATEOFBIRTH = "DATEOFBIRTH";
	static public String NOTIFICATION_TIME = "NOTIFICATION_TIME";
	static public String LOGIN_ID = "LOGIN_ID";
	static public String GENDER = "GENDER";
	static public String EMAIL_ID = "EMAIL_ID";
	static public String LOGIN_STATUS = "LOGIN_STATUS";
	static public String COVER_IMAGE_BYTE_DATA = "COVERIMAGE_BYTE_DATA";
	static public String PROFILE_IMAGE_BYTE_DATA = "PROFILE_IMAGE_BYTE_DATA";

	
	/*<----------ALL URl's------------>*/
	static public String SERVER_URL_WITH_SLASH = "http://techwinlabs.com/inspiration/";
	public static String IMAGEUPLOAD_URL = SERVER_URL_WITH_SLASH + "uploadPic.php";
	public static String EDITPROFILE_URL = SERVER_URL_WITH_SLASH + "index.php?method=editProfile";
	public static String SERVER_URL = SERVER_URL_WITH_SLASH + "index.php?";
	public static String FORGETPASSOWRD_URL = SERVER_URL_WITH_SLASH + "index.php?method=forgetpwd&email=";
	public static String SIGNUP = SERVER_URL + "method=signup";
	public static String SUB_CATEGORIES = SERVER_URL_WITH_SLASH + "index.php?method=getsubCategory";
	public static String SIGNIN_URL = SERVER_URL_WITH_SLASH + "index.php?method=login";
	public static String CHANNELS_URL = SERVER_URL_WITH_SLASH + "index.php?method=getCategory";
	public static String COVER_IMAGE_UPLOAD_URL = SERVER_URL_WITH_SLASH + "coverPic.php";
	public static String DELETE_CATEGORIES_URL = SERVER_URL_WITH_SLASH + "index.php?method=deleteCategory";
	public static String HIDECATEGORY_URL = SERVER_URL_WITH_SLASH + "index.php?method=hideCategory";
	public static String ADD_QUOTE = SERVER_URL_WITH_SLASH + "index.php?method=requestQuote";
	public static String SAVED_CATEGORIES_USER = SERVER_URL_WITH_SLASH + "index.php?method=getsavedcat";
	public static String ANALYTIC_APP_OPEN_COUNT_URL = SERVER_URL_WITH_SLASH + "index.php?method=openapp&userid=";
	public static String ANALYTIC_NOTIFICATION_OPEN_COUNT_URL = SERVER_URL_WITH_SLASH
			+ "index.php?method=opennotifications&userid=";
	public static String LIKE_QOUTE_URL = SERVER_URL_WITH_SLASH
			+ "index.php?method=likedQuotes&userid=";
	public static String QUOTES_YOU_WRITTEN_URL = SERVER_URL_WITH_SLASH
			+ "index.php?method=getWrittenQuotes&userid=";
	
	public static String PASSWORD_CHANGE_URL = SERVER_URL_WITH_SLASH
			+ "index.php?method=changepassword&userid=";
	
	
	public static String LIKE_A_QOUTE_URL = SERVER_URL_WITH_SLASH
			+ "index.php?method=likeQuote&userid=";
	
	public static final String TAG = VolleySingleton.class.getSimpleName();
	private RequestQueue mRequestQueue;
	private static VolleySingleton mInstance;


	@Override
	public void onCreate() {

		super.onCreate();
		userLogin = this.getSharedPreferences("USERDETAILS", MODE_PRIVATE);
		screen1 = this.getSharedPreferences("screenHome", MODE_PRIVATE);
		screen2 = this.getSharedPreferences("screenHome", MODE_PRIVATE);
		screen3 = this.getSharedPreferences("screenHome", MODE_PRIVATE);
		screen4 = this.getSharedPreferences("screenHome", MODE_PRIVATE);
		mInstance = this;

		face = Typeface.createFromAsset(getAssets(), "majalla.ttf");

		VolleySingleton.userLogin.edit().putString("TIME_ZONE", TimeZone.getDefault().getID()).commit();
		
		

	}

	public static synchronized VolleySingleton getInstance() {
		return mInstance;
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {

		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}
}