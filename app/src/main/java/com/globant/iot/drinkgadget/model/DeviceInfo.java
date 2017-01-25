package com.globant.iot.drinkgadget.model;


import android.bluetooth.BluetoothDevice;

public class DeviceInfo {

    public String name;
    public String address;
    public int status;
    public byte temperature;
    public byte battery;


    public static class Builder {
        DeviceInfo deviceInfo;

        public Builder() {
            this.deviceInfo = new DeviceInfo();
        }

        public Builder add(BluetoothDevice device) {
            deviceInfo.name = device.getName();
            deviceInfo.address = device.getAddress();
            return this;
        }

        public DeviceInfo build() {
            return deviceInfo;
        }
    }
}
