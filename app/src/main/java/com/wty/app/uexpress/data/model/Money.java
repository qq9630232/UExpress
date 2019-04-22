package com.wty.app.uexpress.data.model;

/**
 * Created by SDC on 2019/4/22.
 */

public class Money  extends BObj {
    private String receive_id;
    private int money;


    public String getReceive_id() {
        return receive_id;
    }

    public void setReceive_id(String receive_id) {
        this.receive_id = receive_id;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

}
