package com.example.evan.evansmyruns.evanhelpers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

import android.support.annotation.DrawableRes;

/**
 *
 * Created by Evan on 5/1/2016.
 *
 */
public class NotificationHelpers {

    /* Shows a notification (and the top-bar icon) based on the arguments given: */
    public static void showNotification(
                                        Context context,
                                        @DrawableRes int iconImageID,
                                        String titleText,   // Text to display on the bar that
                                        String detailsText, // appears after you swipe down
                                        int notificationID,  // Not required - used to locate the notification later
                                        boolean removeNotificationWhenClicked,
                                        Class<?> goToOnClick
    ) {

        // Sets what happens when the notification is clicked:
        Intent intent = new Intent(context, goToOnClick);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent clickNotificationAction = PendingIntent.getActivity(
                context,
                0,
                intent,
                0);

        Notification notification = new Notification.Builder(context)
                .setContentTitle(titleText)
                .setContentText(detailsText)
                .setSmallIcon(iconImageID)
                .setAutoCancel(removeNotificationWhenClicked)
                .setContentIntent(clickNotificationAction)
                .build();

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
        notification.flags = notification.flags
                | Notification.FLAG_ONGOING_EVENT;      // can't "swipe away" the notification

        mNotificationManager.notify(notificationID, notification);
    }

    public static void killNotification(Context context, int notificationID){
        NotificationManager manager = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);

        manager.cancel(notificationID);
    }

}
