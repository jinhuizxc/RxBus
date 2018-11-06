package com.jh.rxbus.baserx;

import android.util.Log;

import com.google.gson.Gson;
import com.jh.rxbus.base.BaseResponse;
import com.jh.rxbus.utils.LogUtils;


import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final Type type;

    GsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    /**
     * 针对数据返回成功、错误不同类型字段处理
     */
    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        try {
            BaseResponse baseResponse = gson.fromJson(response, BaseResponse.class);
            Log.e("服务器返回的数据", baseResponse.toString());
            LogUtils.loge("服务器返回的数据" + baseResponse.toString());
//            if (!baseResponse.rspCode.equals("200")) {
//                throw new ResultException(baseResponse.rspMsg, baseResponse.rspCode);
//            }else
            if (baseResponse.rspCode.equals("204")) {
                return gson.fromJson(response, type);
            } else {
                return gson.fromJson(response, type);
            }
        } finally {
            value.close();
        }
    }
}
