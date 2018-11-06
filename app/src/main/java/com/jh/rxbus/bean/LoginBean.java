package com.jh.rxbus.bean;

import java.io.Serializable;

/**
 * Created by jinhui on 2018/11/6.
 * email: 1004260403@qq.com
 */

public class LoginBean implements Serializable {

    private String username;
    private String password;

    public LoginBean(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
