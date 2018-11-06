package com.jh.rxbus.api;

import com.jh.rxbus.base.BaseResponse;
import com.jh.rxbus.bean.LoginBean;

import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by jinhui on 2018/11/6.
 * email: 1004260403@qq.com
 */

public interface ApiService {

    //登录接口
    @POST("user/{version}/login")
    Observable<BaseResponse> userLogin(@Header("Cache-Control") String cacheControl,
                                       @Path("version") String version,
                                       @Body LoginBean loginBean);

}
