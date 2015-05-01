package com.qylk.code.preference;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import com.qylk.code.R;

public class PreferenceView2 extends PreferenceFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences_inner);
		Toast.makeText(getActivity(), "Arguments: " + getArguments(), 1).show();
	}

}
