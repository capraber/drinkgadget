package com.globant.iot.drinkgadget.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.globant.iot.drinkgadget.Main;
import com.globant.iot.drinkgadget.R;

public class Notifications {

    private Notifications() {
        // Prevent instantiation
    }

    public static void showNotification(Context context, String title, String message) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_fingerprint_black_24dp)
                        .setContentTitle(title)
                        .setVibrate(new long[] {1000, 1000, 1000, 1000, 1000 })
                        .setContentText(message);

        Intent notificationIntent = new Intent(context, Main.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
}
