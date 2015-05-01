package com.qylk.code.demos;

import android.app.Activity;
import android.os.Bundle;

import com.qylk.code.preference.PreferenceView;

public class testPreference extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PreferenceView myPre = new PreferenceView();
		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, myPre).commit();
	}
}
