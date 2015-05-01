package com.qylk.code.preference;

import com.qylk.code.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class IntegerPreference extends Preference {
	private int mClickCounter;

	public IntegerPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		setWidgetLayoutResource(R.layout.preference_widget_mypreference);
	}

	@Override
	protected void onBindView(View view) {
		super.onBindView(view);
		TextView mTextView = (TextView) view
				.findViewById(R.id.mypreference_widget);
		if (mTextView != null) {
			mTextView.setText(String.valueOf(mClickCounter));
		}
	}

	@Override
	protected Object onGetDefaultValue(TypedArray a, int index) {
		return a.getInt(index, 0);// ���android:defaultֵ
	}

	@Override
	protected void onClick() {
		mClickCounter += 1;
		persistInt(mClickCounter);
		notifyChanged();
	}

	@Override
	protected void onSetInitialValue(boolean restorePersistedValue,
			Object defaultValue) {
		if (restorePersistedValue) {
			mClickCounter = getPersistedInt(mClickCounter);
		} else {
			Integer value = (Integer) defaultValue;
			mClickCounter = value;
			persistInt(value);
		}
	}

}
