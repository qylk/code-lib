package com.qylk.code.utils.service;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class ServiceUtils {

	public static boolean isServiceAlive(Context context, Class<?> serviceClazz) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> serviceList = am.getRunningServices(100);
		for (RunningServiceInfo info : serviceList) {
			if (info.service.getClassName().equals(serviceClazz.getName()))
				return true;
		}
		return false;
	}
}
