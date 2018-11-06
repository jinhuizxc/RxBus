package com.jh.rxbus.ui.login;

import com.jh.rxbus.api.Api;
import com.jh.rxbus.api.ApiConstant;
import com.jh.rxbus.api.HostType;
import com.jh.rxbus.app.AppApplication;
import com.jh.rxbus.base.BaseResponse;
import com.jh.rxbus.baserx.RxSchedulers;
import com.jh.rxbus.bean.LoginBean;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by jinhui on 2018/11/6.
 * email: 1004260403@qq.com
 */

public class LoginModel implements LoginContract.Model {

    @Override
    public Observable<BaseResponse> userLogin(LoginBean loginBean) {
        return Api.getDefault(HostType.TYPE_CATEGORY, AppApplication.getAppContext(), ApiConstant.SIGN)
                .userLogin(Api.getCacheControl(),AppApplication.getCode()+"",loginBean)
                .map(new Func1<BaseResponse, BaseResponse>() {
                    @Override
                    public BaseResponse call(BaseResponse baseResponse) {
                        return baseResponse;
                    }
                })
                .compose(RxSchedulers.<BaseResponse>io_main());
    }

}

