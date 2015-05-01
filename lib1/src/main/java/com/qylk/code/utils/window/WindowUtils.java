package com.qylk.code.utils.window;

import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

public class WindowUtils {
	public static void setFullScreen(Activity act, boolean fullScreen) {
		setWindowFlag(act, WindowManager.LayoutParams.FLAG_FULLSCREEN,
				fullScreen);
	}

	public static void setWindowFlag(Activity act, int flag, boolean on) {
		Window win = act.getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_FULLSCREEN;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}
}
