package com.jh.rxbus.base;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by jinhui on 2018/11/6.
 * email: 1004260403@qq.com
 */

public class BaseApplication extends Application {

    private static BaseApplication baseApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = this;
    }

    public static Context getAppContext() {
        return baseApplication;
    }

    public static Resources getAppResources() {
        return baseApplication.getResources();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(this);
    }

}
