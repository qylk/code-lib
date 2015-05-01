package com.qylk.code.utils.android;

import android.content.Context;
import android.text.format.DateUtils;
import android.text.format.Time;

public class DateTimeUtils {
    /**
     * 智能转换时间
     */
    public static String formatTimeStamp2DateAndTime(Context context, long timestamp) {
        if (timestamp <= 0) {
            return "";
        }
        Time time = new Time();
        time.set(timestamp);
        Time now = new Time();
        now.setToNow();

        int format_flags = DateUtils.FORMAT_NO_MIDNIGHT | DateUtils.FORMAT_ABBREV_ALL | DateUtils.FORMAT_CAP_AMPM | DateUtils.FORMAT_24HOUR;

        if (time.year != now.year) { //if the message is from a different year,show the date and year
            format_flags |= DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_DATE;
        } else if (time.yearDay != now.yearDay) { // if it is from a different day,show the date and time
            format_flags |= DateUtils.FORMAT_SHOW_TIME;
            if (now.yearDay - time.yearDay == 1) {
                return "昨天 " + DateUtils.formatDateTime(context, timestamp, format_flags);
            } else if (now.yearDay - time.yearDay == 2) {
                return "前天 " + DateUtils.formatDateTime(context, timestamp, format_flags);
            } else {
                format_flags |= DateUtils.FORMAT_SHOW_DATE;
            }
        } else { //otherwise.if the message is from today,show the time
            format_flags |= DateUtils.FORMAT_SHOW_TIME;
        }
        return DateUtils.formatDateTime(context, timestamp, format_flags);
    }

    public static String frommatTimeStamp2Date(Context context, long timestamp) {
        if (timestamp < 0) {
            return "";
        }
        Time time = new Time();
        time.set(timestamp);
        Time now = new Time();
        now.setToNow();

        int format_flags = DateUtils.FORMAT_NO_MIDNIGHT | DateUtils.FORMAT_ABBREV_ALL | DateUtils.FORMAT_CAP_AMPM | DateUtils.FORMAT_24HOUR;

        if (time.year != now.year) {
            format_flags |= DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_DATE;
        } else if (time.yearDay != now.yearDay) {
            format_flags |= DateUtils.FORMAT_SHOW_DATE;
        } else {
            format_flags |= DateUtils.FORMAT_SHOW_TIME;
        }
        return DateUtils.formatDateTime(context, timestamp, format_flags);
    }

    /**
     * 计算时间差
     * @param timestamp old time
     */
    public static String calculateTimeGap(long timestamp) {
        if (timestamp < 0) {
            return "";
        }
        long timeGap = System.currentTimeMillis() - timestamp;
        if (timeGap < 0) {
            return "";
        } else if (timeGap < 60 * 1000) {
            return "刚刚";
        } else if (timeGap < 60 * 60 * 1000) {
            return timeGap / (60 * 1000) + "分钟前";
        } else if (timeGap < 24 * 60 * 60 * 1000) {
            return timeGap / (60 * 60 * 1000) + "小时前";
        } else {
            return timeGap / (24 * 60 * 60 * 1000) + "天前";
        }
    }
}
