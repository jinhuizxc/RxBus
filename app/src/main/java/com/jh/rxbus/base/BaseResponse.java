package com.jh.rxbus.base;

import java.io.Serializable;

/**
 * Created by jinhui on 2018/11/6.
 * email: 1004260403@qq.com
 */

public class BaseResponse <T> implements Serializable {

    public String rspCode;
    public String rspMsg;
    public T data;



    public boolean success() {
        return "200".equals(rspCode);
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "rspCode='" + rspCode + '\'' +
                ", rspMsg='" + rspMsg + '\'' +
                ", data=" + data +
                '}';
    }
}
