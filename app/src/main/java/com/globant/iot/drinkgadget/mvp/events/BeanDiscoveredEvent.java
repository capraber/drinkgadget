package com.globant.iot.drinkgadget.mvp.events;


import com.punchthrough.bean.sdk.Bean;

public class BeanDiscoveredEvent {

    public Bean bean;

    public BeanDiscoveredEvent(Bean bean) {
        this.bean = bean;
    }
}
