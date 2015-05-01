package com.qylk.code.widgets;


import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import com.qylk.code.R;
import com.qylk.code.widgets.defined.AdvancedEditText;

public class testEditText extends Activity implements OnClickListener {
	private EditText mEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edittext);
		mEditText = (EditText) findViewById(R.id.edittext);
		findViewById(R.id.submit).setOnClickListener(this);

		AdvancedEditText userBox = (AdvancedEditText) findViewById(R.id.userBox);
		userBox.setInputType(InputType.TYPE_CLASS_TEXT);
		AdvancedEditText pwdBox = (AdvancedEditText) findViewById(R.id.pwdBox);
		pwdBox.setTitle("手机号:");
		pwdBox.setInputType(InputType.TYPE_CLASS_NUMBER);
		pwdBox.setValidatorType(AdvancedEditText.VALIDATOR_TYPE_PHONE);
	}

	@Override
	public void onClick(View v) {
		String text = mEditText.getText().toString();
		if (TextUtils.isEmpty(text)) {
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			mEditText.startAnimation(shake);
		}
	}
}
