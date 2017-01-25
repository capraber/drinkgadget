package com.globant.iot.drinkgadget.utils;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.app.NotificationManagerCompat;

import com.globant.iot.drinkgadget.Main;
import com.globant.iot.drinkgadget.R;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static android.support.v4.app.NotificationCompat.PRIORITY_DEFAULT;
import static android.support.v4.app.NotificationCompat.PRIORITY_HIGH;


public final class Notifications {

    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger(0);
    private static final int VIBRATE = 1000;
    private static HashMap<String, String> drinksReady = new HashMap<>();
    private static HashMap<String, String> drinksAlmostFrozen = new HashMap<>();

    private Notifications() {
        // Prevent instantiation
    }

    private static int getID() {
        return ATOMIC_INTEGER.incrementAndGet();
    }

    public static void resetNotifications() {
        drinksReady.clear();
        drinksAlmostFrozen.clear();
    }

    public static void showNotificationAlmostFrozen(Context context, String address) {
        //only send one notification
        if (drinksAlmostFrozen.containsKey(address)) {
            return;
        }
        drinksAlmostFrozen.put(address, address);
        showNotification(context, R.string.frozen_title, R.string.frozen_message, R.drawable.ic_danger, PRIORITY_HIGH);
    }

    public static void showNotificationDrinkReady(Context context, String address) {
        //only send one notification
        if (drinksReady.containsKey(address)) {
            return;
        }
        drinksReady.put(address, address);
        showNotification(context, R.string.ready_title, R.string.ready_message, R.drawable.ic_ready, PRIORITY_DEFAULT);
    }

    private static void showNotification(Context context, @StringRes int title, @StringRes int message, @DrawableRes int icon,
                                         int priority) {

        Intent notificationIntent = new Intent(context, Main.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Notification builder =
                new Builder(context)
                .setSmallIcon(icon)
                .setContentTitle(context.getString(title))
                .setContentText(context.getString(message))
                .setVibrate(new long[] {VIBRATE, VIBRATE, VIBRATE, VIBRATE, VIBRATE })
                .setOngoing(false)
                .setShowWhen(true)
                .setAutoCancel(true)
                .setPriority(priority)
                .setContentIntent(contentIntent)
                .build();

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(context);
        notificationManager.notify(getID(), builder);
    }
}
