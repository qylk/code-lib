package com.qylk.code.widgets;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class localService extends Service {
	private WindowManager mWm;
	private final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
	private View mContentView;
	private boolean isShowing = false;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mContentView = createGenericView();
		mWm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
	}

	private View createGenericView() {
		TextView tv = new TextView(this);
		tv.setTextSize(40);
		tv.setText("Window 123");
		return tv;
	}

	private void showWindow(int windowflags) {
		final WindowManager.LayoutParams params = mParams;
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.flags = windowflags;
		params.format = PixelFormat.TRANSLUCENT;
		params.type = WindowManager.LayoutParams.TYPE_PHONE;
		params.setTitle("window");
		if (isShowing)
			mWm.removeView(mContentView);
		mWm.addView(mContentView, params);
		isShowing = true;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null && intent.getExtras() != null) {
			int windowflags = intent.getExtras().getInt("flags");
			windowflags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
			windowflags |= WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
			showWindow(windowflags);
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		mWm.removeView(mContentView);
		super.onDestroy();
	}

}
