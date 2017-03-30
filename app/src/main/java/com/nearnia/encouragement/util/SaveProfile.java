package com.nearnia.encouragement.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.nearnia.encouragement.VolleySingleton;

import android.util.Log;

public class SaveProfile {

	public String methodSaveProfile() {

		String resultJson = "RESULT_INITIALIZAION";
		String saveProfile = null;

		try {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(VolleySingleton.EDITPROFILE_URL);
			stringBuilder.append("&userid=");
			stringBuilder.append(String.valueOf(VolleySingleton.userLogin.getInt(VolleySingleton.LOGIN_ID, 0)));
			stringBuilder.append("&fullname=");
			stringBuilder.append(
					URLEncoder.encode(VolleySingleton.userLogin.getString(VolleySingleton.USERNAME, "Username")));
			stringBuilder.append("&phone=");
			stringBuilder.append(VolleySingleton.userLogin.getString(VolleySingleton.PHONENUMBER, "0123456789"));
			stringBuilder.append("&dateofbirth=");
			stringBuilder.append(VolleySingleton.userLogin.getString(VolleySingleton.DATEOFBIRTH, "01-Jan-1970"));
			stringBuilder.append("&notification=");
			stringBuilder.append(VolleySingleton.userLogin.getString(VolleySingleton.NOTIFICATION_TIME, "6-9"));
			stringBuilder.append("&gender=");
			stringBuilder.append(VolleySingleton.userLogin.getString(VolleySingleton.GENDER, "male"));
			stringBuilder.append("&companyUrl=");
			stringBuilder.append(URLEncoder.encode(
					VolleySingleton.userLogin.getString(VolleySingleton.COMPANY_NAME, "your companys's tagline"),
					"UTF-8"));
			stringBuilder.append("&profile=");
			stringBuilder.append(URLEncoder.encode(
					VolleySingleton.userLogin.getString(VolleySingleton.PROFILE_TAGLINE, "your profile tagline"),
					"UTF-8"));
			stringBuilder.append("&picture=");
			stringBuilder.append(URLEncoder
					.encode(VolleySingleton.userLogin.getString(VolleySingleton.PROFILE_IMAGE_URL, "DONT HAVE")));
			stringBuilder.append("&coverpicture=");
			stringBuilder.append(URLEncoder
					.encode(VolleySingleton.userLogin.getString(VolleySingleton.COVER_IMAGE_URL, "DONT HAVE")));
			saveProfile = stringBuilder.toString();

			Log.e("SAVE_STRING", saveProfile);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StringRequestActivity sR = new StringRequestActivity();

		resultJson = sR.makeStringReq(saveProfile);
		// Log.e("IN SAVE PROFILE ", resultJson );
		return resultJson;

	}

}
