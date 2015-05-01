package com.qylk.code.widgets.defined;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EmailValidator implements AdvancedEditText.Validator {
	private static final String PATTERN_EMAIL = "/^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$/";
	private Pattern mPattern = Pattern.compile(PATTERN_EMAIL);

	@Override
	public boolean isValid(CharSequence s) {
		Matcher matcher = mPattern.matcher(s);
		return matcher.matches();
	}
}
