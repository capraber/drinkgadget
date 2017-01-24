package com.globant.iot.drinkgadget.mvp.events;


import com.punchthrough.bean.sdk.Bean;

public class BeanInfoReceivedEvent {

    public Bean bean;
    public String temperature;
    public String battery;

    public BeanInfoReceivedEvent(Bean bean, String temperature, String battery) {
        this.bean = bean;
        this.temperature = temperature;
        this.battery = battery;
    }
}
