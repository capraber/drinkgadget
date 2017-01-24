package com.globant.iot.drinkgadget.mvp.model;

import com.globant.iot.drinkgadget.model.DeviceInfo;

public class DrinkDialogModel {
    private DeviceInfo device;
    private byte temperature;
    private byte batteryLevel;

    public DrinkDialogModel(DeviceInfo device) {
        this.device = device;
        this.temperature = 0;
        this.batteryLevel = 0;
    }


    public boolean isValid(String address) {
        return device.address.equals(address);
    }

    public DeviceInfo getDevice() {
        return device;
    }

    public byte getTemperature() {
        return temperature;
    }

    public void setTemperature(byte temperature) {
        this.temperature = temperature;
    }

    public byte getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(byte batteryLevel) {
        this.batteryLevel = batteryLevel;
    }
}
