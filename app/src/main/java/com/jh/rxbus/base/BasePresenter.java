package com.jh.rxbus.base;

import android.content.Context;

import com.jh.rxbus.rx.RxManager;


/**
 * des:基类presenter
 */
public abstract class BasePresenter<T,E>{

    public Context mContext;
    public E mModel;
    public T mView;
    public RxManager mRxManage = new RxManager();

    public void setVM(T v, E m) {
        this.mView = v;
        this.mModel = m;
        this.onStart();
    }

    public void onStart(){};

    public void onDestroy() {
        mRxManage.clear();
    }



}
