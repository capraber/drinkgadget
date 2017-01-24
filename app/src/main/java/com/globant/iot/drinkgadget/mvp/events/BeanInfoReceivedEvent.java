package com.globant.iot.drinkgadget.mvp.events;


public class BeanInfoReceivedEvent {

    public String address;
    public byte temperature;
    public byte battery;

    public BeanInfoReceivedEvent(String address, byte temperature, byte battery) {
        this.address = address;
        this.temperature = temperature;
        this.battery = battery;
    }
}
