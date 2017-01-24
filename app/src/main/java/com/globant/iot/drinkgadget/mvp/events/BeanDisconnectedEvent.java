package com.globant.iot.drinkgadget.mvp.events;


import com.punchthrough.bean.sdk.Bean;

public class BeanDisconnectedEvent {

    public Bean bean;

    public BeanDisconnectedEvent(Bean bean) {
        this.bean = bean;
    }
}
