package com.qylk.code.widgets;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.CheckBox;
import com.qylk.code.R;

public class testWindowFlags extends Activity implements OnClickListener {

    private CheckBox mCheckBox01, mCheckBox02, mCheckBox03, mCheckBox04,
            mCheckBox05, mCheckBox06;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.windowflags);
        initViews();
        findViewById(R.id.apply).setOnClickListener(this);
    }

    private void initViews() {
        mCheckBox01 = (CheckBox) findViewById(R.id.cb1);
        mCheckBox02 = (CheckBox) findViewById(R.id.cb2);
        mCheckBox03 = (CheckBox) findViewById(R.id.cb3);
        mCheckBox04 = (CheckBox) findViewById(R.id.cb4);
        mCheckBox05 = (CheckBox) findViewById(R.id.cb5);
        mCheckBox06 = (CheckBox) findViewById(R.id.cb6);

        mCheckBox01.setText("FLAG_SHOW_WHEN_LOCKED");
        mCheckBox02.setText("FLAG_DISMISS_KEYGUARD");
        mCheckBox03.setText("FLAG_TURN_SCREEN_ON");
        mCheckBox04.setText("FLAG_KEEP_SCREEN_ON");
        mCheckBox05.setText("FLAG_SECURE");
        mCheckBox06.setText("FLAG_LAYOUT_NO_LIMITS");
    }

    private int getWindowFlags() {
        int flags = 0;
        if (mCheckBox01.isChecked())
            flags |= WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
        if (mCheckBox02.isChecked())
            flags |= WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD;
        if (mCheckBox03.isChecked())
            flags |= WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON;
        if (mCheckBox04.isChecked())
            flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        if (mCheckBox05.isChecked())
            flags |= WindowManager.LayoutParams.FLAG_SECURE;
        if (mCheckBox06.isChecked())
            flags |= WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        return flags;
    }

    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            startService(new Intent(testWindowFlags.this, localService.class)
                    .putExtra("flags", getWindowFlags()));
            return true;
        }
    });

    @Override
    public void onClick(View v) {
        handler.sendEmptyMessageDelayed(0, 10000);
    }
}
