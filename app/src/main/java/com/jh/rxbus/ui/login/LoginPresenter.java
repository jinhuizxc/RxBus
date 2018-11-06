package com.jh.rxbus.ui.login;

import android.util.Log;

import com.jh.rxbus.base.BaseResponse;
import com.jh.rxbus.baserx.RxSubscriber;
import com.jh.rxbus.bean.LoginBean;
import com.jh.rxbus.utils.LogUtils;

/**
 * Created by jinhui on 2018/11/6.
 * email: 1004260403@qq.com
 */

public class LoginPresenter extends LoginContract.Presenter {

    private static final String TAG = "LoginPresenter";

    @Override
    public void userLoginRequest(LoginBean loginBean) {
        mRxManage.add(mModel.userLogin(loginBean).subscribe(new RxSubscriber<BaseResponse>(mContext, true) {
            @Override
            protected void _onNext(BaseResponse baseResponse) {
                mView.stopLoading();
                mView.returnUserInfo(baseResponse);
            }

            @Override
            protected void _onError(String message) {
                mView.stopLoading();
                mView.showErrorTip(message);
                LogUtils.loge("请求失败" + message);
//                mView.returnFailureMessage(message);
            }
        }));
    }

}
