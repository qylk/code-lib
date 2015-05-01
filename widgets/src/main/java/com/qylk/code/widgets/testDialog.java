package com.qylk.code.widgets;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;
import com.qylk.code.R;

import java.sql.Date;
import java.util.Calendar;

public class testDialog extends Activity implements OnClickListener,
		OnDateSetListener, OnTimeSetListener {
	Calendar c = Calendar.getInstance();
	int mYear = c.get(Calendar.YEAR);
	int mMonth = c.get(Calendar.MONTH);
	int mDay = c.get(Calendar.DAY_OF_MONTH);
	int mHour = c.get(Calendar.HOUR_OF_DAY);
	int mMinute = c.get(Calendar.MINUTE);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialogs_selector);
		findViewById(R.id.datepicker).setOnClickListener(this);
		findViewById(R.id.timePicker).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.datepicker:
			new DatePickerDialog(this, this, mYear, mMonth, mDay).show();
			break;
		case R.id.timePicker:
			new TimePickerDialog(this, this, mHour, mMinute, true).show();
			break;
		default:
			break;
		}

	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		Toast.makeText(
				this,
				new Date(year - 1900, monthOfYear, dayOfMonth).toLocaleString(),
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		Toast.makeText(this, hourOfDay + ":" + minute, Toast.LENGTH_SHORT)
				.show();
	}
}
