package com.jh.rxbus.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.jh.rxbus.R;
import com.jh.rxbus.rx.RxManager;
import com.jh.rxbus.widget.StatusBarCompat;

/**
 * Created by jinhui on 2018/11/2.
 * email: 1004260403@qq.com
 */

public class BaseActivity extends AppCompatActivity {

    public RxManager mRxManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRxManager = new RxManager();

        // 默认着色状态栏
        setStatusBarColor();

    }

    /**
     * 着色状态栏（4.4以上系统有效）
     */
    protected void setStatusBarColor() {
        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.main_color));
    }

    /**
     * 着色状态栏（4.4以上系统有效）
     */
    protected void SetStatusBarColor(int color) {
        StatusBarCompat.setStatusBarColor(this, color);
    }

    /**
     * 沉浸状态栏（4.4以上系统有效）
     */
    protected void SetTranslanteBar() {
        StatusBarCompat.translucentStatusBar(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRxManager != null) {
            mRxManager.clear();
        }
    }
}
