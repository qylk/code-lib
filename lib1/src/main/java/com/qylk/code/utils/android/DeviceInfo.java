package com.qylk.code.utils.android;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import com.qylk.code.utils.network.NetworkUtils;

import java.util.HashMap;
import java.util.Map;

public class DeviceInfo {
    private final Context context;
    private String versionName;

    private DeviceInfo(Context context) {
        this.context = context;
        try {
            PackageInfo pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            versionName = pinfo.versionName;
        } catch (Exception e) {
            versionName = "";
        }
    }


    private String getNetworkType() {
        String type = NetworkUtils.getNetWork(context);
        if ("MOBILE".equals(type)) {
            type = NetworkUtils.getAPN(context);
        }
        return type;
    }

    public Map<String, String> getInfo() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("network", getNetworkType());
        map.put("app_version", versionName);
        map.put("agent", "Android " + Build.VERSION.RELEASE);
        map.put("device", Build.MODEL);
        map.put("cpu", Build.CPU_ABI);
        return map;
    }
}
