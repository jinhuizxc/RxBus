package com.jh.rxbus.app;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.jh.rxbus.base.BaseApplication;

/**
 * Created by jinhui on 2018/11/6.
 * email: 1004260403@qq.com
 */

public class AppApplication extends BaseApplication {

    private static final String TAG = "AppApplication";

    private static String mVersionName;
    private static int mVersionCode;

    @Override
    public void onCreate() {
        super.onCreate();

        mVersionCode = getVersionCode(getAppContext());
//        checkVersionCode();
        mVersionName = getVersionName(getAppContext());
        Log.e(TAG, "app版本号版本名: " + mVersionCode + ", " + mVersionName);
    }

    /**
     * 保存版本号，用于检出检查是否弹出广告框
     */
//    private void checkVersionCode() {
//        if (mVersionCode > SPUtils.getSharedIntData(this, AppConstant.VERSION_CODE)){
//            SPUtils.setSharedBooleanData(this, AppConstant.SHOW_AD, true);
//        }else {
//            SPUtils.setSharedBooleanData(this, AppConstant.SHOW_AD, false);
//        }
//        SPUtils.setSharedIntData(this, AppConstant.VERSION_CODE, mVersionCode);
//    }

    public String getVersionName(Context context) {
        PackageInfo info;
        try {//增加同步块
            synchronized (context) {
                info = context.getPackageManager().getPackageInfo(
                        context.getPackageName(), 0);
            }
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public int getVersionCode(Context context){
        PackageInfo info;
        try {
            synchronized (context){
                info = context.getPackageManager().getPackageInfo(getPackageName(), 0);
            }
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getCode() {
        return mVersionCode;
    }


}
