package com.jh.rxbus.base;

/**
 * Created by jinhui on 2018/11/6.
 * email: 1004260403@qq.com
 */

public interface BaseView {
    /*******内嵌加载*******/
    void showLoading(String title);
    void stopLoading();
    void showErrorTip(String msg);
}
