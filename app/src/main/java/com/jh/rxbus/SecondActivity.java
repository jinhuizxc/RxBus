package com.jh.rxbus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.jh.rxbus.base.BaseActivity;
import com.jh.rxbus.utils.LogUtils;

import rx.functions.Action1;

/**
 * Created by jinhui on 2018/11/2.
 * email: 1004260403@qq.com
 */

public class SecondActivity extends BaseActivity {

    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        textView = findViewById(R.id.tv);

        // 设置数据
        mRxManager.on("test", new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                LogUtils.loge("SecondActivity call");
                textView.setText("接收到了数据");
            }
        });

    }
}
