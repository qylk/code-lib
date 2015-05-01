package com.qylk.code.utils.android;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by yinduo on 14-1-15.
 */
public class ReceiverUtils {
    /**
     * 让给定的BroadcastReceiver生效
     */
    public static void enableReceiver(Context context,
                                      Class<? extends BroadcastReceiver> receiverClass) {
        changeComponentEnabledState(context, receiverClass,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED);
    }

    /**
     * 让给定的BroadcastReceiver失效
     */
    public static void disableReceiver(Context context,
                                       Class<? extends BroadcastReceiver> receiverClass) {
        changeComponentEnabledState(context, receiverClass,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED);
    }

    /**
     * 改变Component的状态
     *
     * @param componentEnabledState The legal values for this state
     *                              are:
     *                              {@link PackageManager#COMPONENT_ENABLED_STATE_ENABLED},
     *                              {@link PackageManager#COMPONENT_ENABLED_STATE_DISABLED}
     *                              and
     *                              {@link PackageManager#COMPONENT_ENABLED_STATE_DEFAULT}
     */
    private static void changeComponentEnabledState(Context context,
                                                    Class<?> componentClass, int componentEnabledState) {
        ComponentName component = new ComponentName(context, componentClass);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(component, componentEnabledState,
                PackageManager.DONT_KILL_APP);

    }
}
