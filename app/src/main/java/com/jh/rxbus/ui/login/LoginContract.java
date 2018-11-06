package com.jh.rxbus.ui.login;

import com.jh.rxbus.base.BaseModel;
import com.jh.rxbus.base.BasePresenter;
import com.jh.rxbus.base.BaseResponse;
import com.jh.rxbus.base.BaseView;
import com.jh.rxbus.bean.LoginBean;

import rx.Observable;

/**
 * Created by jinhui on 2018/11/6.
 * email: 1004260403@qq.com
 */

public interface LoginContract {

    interface Model extends BaseModel {
        //请求登录
        Observable<BaseResponse> userLogin(LoginBean loginBean);
    }
    interface View extends BaseView {

        void returnUserInfo(BaseResponse baseResponse);

    }
    abstract class Presenter extends BasePresenter<View,Model> {

        public  abstract void userLoginRequest(LoginBean loginBean);

    }
}

