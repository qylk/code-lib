package com.qylk.code.widgets;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import com.qylk.code.R;

public class testButton extends Activity implements OnCheckedChangeListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buttons);
		RadioGroup radioButton = (RadioGroup) findViewById(R.id.radiobuttons);
		radioButton.setOnCheckedChangeListener(this);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

	}

}
