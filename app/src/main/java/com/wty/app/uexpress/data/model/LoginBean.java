package com.wty.app.uexpress.data.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by SDC on 2019/2/28.
 */

public class LoginBean extends BmobObject {
    private String username;
    private String password;

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
