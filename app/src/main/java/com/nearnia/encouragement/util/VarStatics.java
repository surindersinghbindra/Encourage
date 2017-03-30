package com.nearnia.encouragement.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VarStatics {

	// Method for Validating the Email id
	public static boolean isEmailValid(String email) {

		boolean isValid = false;

		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		CharSequence inputStr = email;

		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}
}
