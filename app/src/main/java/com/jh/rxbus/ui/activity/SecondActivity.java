package com.jh.rxbus.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.jh.rxbus.R;
import com.jh.rxbus.base.BaseActivity;
import com.jh.rxbus.base.BaseResponse;
import com.jh.rxbus.ui.login.LoginContract;
import com.jh.rxbus.ui.login.LoginModel;
import com.jh.rxbus.ui.login.LoginPresenter;
import com.jh.rxbus.utils.LogUtils;

import rx.functions.Action1;

/**
 * Created by jinhui on 2018/11/2.
 * email: 1004260403@qq.com
 */

public class SecondActivity extends BaseActivity <LoginPresenter, LoginModel> implements LoginContract.View{

    TextView textView;


    @Override
    protected void initView() {

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

    @Override
    protected void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_second;
    }

    @Override
    public void showLoading(String title) {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorTip(String msg) {

    }

    @Override
    public void returnUserInfo(BaseResponse baseResponse) {

    }
}
