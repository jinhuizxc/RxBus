package com.jh.rxbus.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.jh.rxbus.R;
import com.jh.rxbus.rx.RxManager;
import com.jh.rxbus.utils.TUtil;
import com.jh.rxbus.widget.StatusBarCompat;

/**
 * Created by jinhui on 2018/11/2.
 * email: 1004260403@qq.com
 */

public abstract class BaseActivity <T extends BasePresenter, E extends BaseModel> extends AppCompatActivity {

    public RxManager mRxManager;
    public T mPresenter;
    public E mModel;
    public Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRxManager = new RxManager();
        doBeforeSetContentView();
        setContentView(getLayoutId());


        mContext = this;
        mPresenter = TUtil.getT(this, 0);
        mModel = TUtil.getT(this, 1);
        if (mPresenter != null) {
            mPresenter.mContext = this;
        }

        this.initPresenter();
        this.initView();


    }

    /**
     * 初始化view
     */
    protected abstract void initView();

    /**
     *  简单页面无需mvp就不用管此方法即可,完美兼容各种实际场景的变通
     */
    protected abstract void initPresenter();

    /**
     *
     * 设置layout前配置
     */
    private void doBeforeSetContentView() {
        // 把actvity放到application栈中管理
        AppManager.getAppManager().addActivity(this);

        // 默认着色状态栏
        setStatusBarColor();

    }

    /**
     * 获取布局文件
     * @return
     */
    protected abstract int getLayoutId();

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
