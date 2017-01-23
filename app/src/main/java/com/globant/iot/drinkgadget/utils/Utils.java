package com.globant.iot.drinkgadget.utils;

public class Utils {

    private Utils() {
        // Prevent instantiation
    }

    public static byte  convertToFahrenheit(byte temperature) {
        return (byte) (1.8 * (float) temperature + 32);
    }
}
