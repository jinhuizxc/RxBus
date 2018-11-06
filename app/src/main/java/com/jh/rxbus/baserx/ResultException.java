package com.jh.rxbus.baserx;


import java.io.IOException;

public class ResultException extends IOException {

    private String errMsg;
    private String errCode;

    public ResultException(String errMsg, String errCode){
        this.errMsg = errMsg;
        this.errCode = errCode;
    }


    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    @Override
    public String toString() {
        return "ResultException{" +
                "errMsg='" + errMsg + '\'' +
                ", errCode='" + errCode + '\'' +
                '}';
    }
}
