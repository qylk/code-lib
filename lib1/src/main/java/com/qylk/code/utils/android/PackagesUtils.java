package com.qylk.code.utils.android;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class PackagesUtils {

    public static final int PACKAGES_SYSTEM = 1;
    public static final int PACKAGES_USER = 2;
    public static final int PACKAGES_ALL = PACKAGES_SYSTEM | PACKAGES_USER;

    public static int getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static String getVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<PackageInfo> getInstalledApplications(Context context,
                                                             int flags) {
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfos = packageManager
                .getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        if (flags == PACKAGES_ALL) {
            return packageInfos;
        }
        if (flags == PACKAGES_SYSTEM) {
            List<PackageInfo> local_pkgInfoSys = new ArrayList<PackageInfo>();
            for (PackageInfo packageInfo : packageInfos) {
                if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                    local_pkgInfoSys.add(packageInfo);
                }
            }
            return local_pkgInfoSys;
        }
        List<PackageInfo> local_pkgInfoNoSys = new ArrayList<PackageInfo>();
        for (PackageInfo packageInfo : packageInfos) {
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                local_pkgInfoNoSys.add(packageInfo);
            }
        }
        return local_pkgInfoNoSys;
    }

}
