package com.jh.rxbus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.jh.rxbus.base.BaseActivity;
import com.jh.rxbus.rx.RxBus;
import com.jh.rxbus.rx.RxManager;

/**
 * RxBus组件间通讯
 * https://blog.csdn.net/barlow2015/article/details/53509153
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RxBus.getInstance().post("test", true);

        findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SecondActivity.class));
            }
        });

    }
}
