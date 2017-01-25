package com.globant.iot.drinkgadget.utils;

public final class Utils {

    private static final int RANGE = 32;
    private static final double FACTOR = 1.8;
    public static final int TEN = 10;
    public static final int TWENTY = 20;

    private Utils() {
        // Prevent instantiation
    }

    public static byte  convertToFahrenheit(byte temperature) {
        return (byte) (FACTOR * (float) temperature + RANGE);
    }
}
