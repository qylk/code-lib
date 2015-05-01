package com.qylk.code.widgets.defined;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PhoneValidator implements AdvancedEditText.Validator {
	private static final String PATTERN_PHONE = "^(13[0-9]|15[0|3|6|7|8|9]|18[8|9])\\d{8}$";
	private Pattern mPattern = Pattern.compile(PATTERN_PHONE);

	@Override
	public boolean isValid(CharSequence s) {
		Matcher matcher = mPattern.matcher(s);
		return matcher.matches();
	}

}
