package com.jh.rxbus.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jh.rxbus.R;
import com.jh.rxbus.api.ApiConstant;
import com.jh.rxbus.base.BaseActivity;
import com.jh.rxbus.base.BaseResponse;
import com.jh.rxbus.bean.LoginBean;
import com.jh.rxbus.rx.RxBus;
import com.jh.rxbus.ui.login.LoginContract;
import com.jh.rxbus.ui.login.LoginModel;
import com.jh.rxbus.ui.login.LoginPresenter;
import com.jh.rxbus.utils.LogUtils;
import com.jh.rxbus.utils.SecurityUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * RxBus组件间通讯
 * https://blog.csdn.net/barlow2015/article/details/53509153
 */
public class MainActivity extends BaseActivity<LoginPresenter, LoginModel> implements LoginContract.View {


    @Bind(R.id.et_username)
    EditText etUsername;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.rl_password)
    RelativeLayout rlPassword;
    @Bind(R.id.bt_login)
    Button btLogin;

    private String mUsername;
    private String mPassword;

    @Override
    protected void initView() {

    }

    @Override
    protected void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
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
        LogUtils.loge("登录返回的数据 = " + baseResponse);
        if (baseResponse.success()) {
            startActivity(new Intent(MainActivity.this, SecondActivity.class));
        } else {
            Toast.makeText(getApplication(), baseResponse.rspMsg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.bt_login)
    public void onViewClicked() {
        mUsername = etUsername.getText().toString().trim();
        mPassword = etPassword.getText().toString().trim();

        // 用户登录请求
        try {
            mPresenter.userLoginRequest(new LoginBean(mUsername, SecurityUtil.encrypt(mPassword, ApiConstant.PUBLICKEY)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        RxBus.getInstance().post("test", true);
    }
}
