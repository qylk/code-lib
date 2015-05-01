package com.qylk.code.demos;

import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;

public class clickNtimes implements OnClickListener {
    long[] mHits = new long[3];// three clicks

    @Override
    public void onClick(View v) {
        System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
        mHits[mHits.length - 1] = SystemClock.uptimeMillis();
        if (mHits[0] >= SystemClock.uptimeMillis() - 500) {
            System.out.println("three clicks");
        }
    }
}
