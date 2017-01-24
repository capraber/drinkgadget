package com.globant.iot.drinkgadget.mvp.events;


import com.punchthrough.bean.sdk.Bean;

public class BeanConnectedEvent {

    public Bean bean;

    public BeanConnectedEvent(Bean bean) {
        this.bean = bean;
    }
}
