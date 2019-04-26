package com.wty.app.uexpress.data.model;

/**
 * Created by SDC on 2019/4/26.
 */

public class MessageEvent {
    private boolean send;

    public boolean isSend() {
        return send;
    }

    public void setSend(boolean send) {
        this.send = send;
    }

    public MessageEvent(boolean send) {
        this.send = send;
    }
}
