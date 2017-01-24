package com.globant.iot.drinkgadget.mvp.model;

import com.globant.iot.drinkgadget.model.DeviceInfo;

public class DrinkDialogModel {
    private DeviceInfo device;
    private byte temperature;
    private byte batteryLevel;

    public DrinkDialogModel(DeviceInfo device, byte temperature, byte batteryLevel) {
        this.device = device;
        this.temperature = temperature;
        this.batteryLevel = batteryLevel;
    }

    public DeviceInfo getDevice() {
        return device;
    }

    public byte getTemperature() {
        return temperature;
    }

    public byte getBatteryLevel() {
        return batteryLevel;
    }
}
