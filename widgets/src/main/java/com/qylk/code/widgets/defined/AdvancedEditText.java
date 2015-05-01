package com.qylk.code.widgets.defined;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.qylk.code.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdvancedEditText extends LinearLayout {
	private EditText mContentView;
	private ImageView mIconRight;
	private TextView mTitle;
	private Validator mValidator;

	public static final int VALIDATOR_TYPE_NONE = 0;
	public static final int VALIDATOR_TYPE_EMAIL = 1;
	public static final int VALIDATOR_TYPE_PHONE = 2;

	public AdvancedEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews();
	}

	private void initViews() {
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View root = inflater.inflate(R.layout.advanced_edittext, null);
		addView(root);
		mTitle = (TextView) root.findViewById(R.id.title);
		mContentView = (EditText) root.findViewById(R.id.content);
		mIconRight = (ImageView) root.findViewById(R.id.validateIcon);
		mContentView.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					if (mValidator != null) {
						EditText textBox = (EditText) v;
						mIconRight.setVisibility(mValidator.isValid(textBox
								.getText()) ? View.GONE : View.VISIBLE);
					}
				}
			}
		});
	}

	public void setTitle(CharSequence title) {
		mTitle.setText(title);
	}

	public void setIconRight(Drawable icon) {
		mIconRight.setImageDrawable(icon);
	}

	public void setIconVisiablity(int visibility) {
		mIconRight.setVisibility(visibility);
	}

	public String getContent() {
		return mContentView.getText().toString();
	}

	public void setInputType(int inputType) {
		mContentView.setInputType(inputType);
	}

	public void setValidator(Validator validator) {
		mValidator = validator;
	}

	public void setValidatorType(int type) {
		if (type == VALIDATOR_TYPE_EMAIL) {
			mValidator = new EmailValidator();
		} else if (type == VALIDATOR_TYPE_PHONE)
			mValidator = new PhoneValidator();
		else if (type == VALIDATOR_TYPE_NONE)
			mValidator = null;
	}

	public interface Validator {
		public boolean isValid(CharSequence s);
	}

	private class PhoneValidator implements Validator {
		private static final String PATTERN_PHONE = "^(13[0-9]|15[0|3|6|7|8|9]|18[8|9])\\d{8}$";
		private Pattern mPattern = Pattern.compile(PATTERN_PHONE);

		@Override
		public boolean isValid(CharSequence s) {
			Matcher matcher = mPattern.matcher(s);
			return matcher.matches();
		}
	}

	private class EmailValidator implements Validator {
		private static final String PATTERN_EMAIL = "/^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$/";
		private Pattern mPattern = Pattern.compile(PATTERN_EMAIL);

		@Override
		public boolean isValid(CharSequence s) {
			Matcher matcher = mPattern.matcher(s);
			return matcher.matches();
		}
	}
}
