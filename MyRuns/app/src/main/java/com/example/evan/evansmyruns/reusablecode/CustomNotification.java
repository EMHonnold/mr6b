package com.example.evan.evansmyruns.reusablecode;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.support.annotation.DrawableRes;

/**
 * Created by Evan on 5/8/2016.
 */
public class CustomNotification {

    private Context mContext;
    protected Notification notification;
    private int uniqueID;
    private boolean canBeSwipedAway;

    /* generic constructor */
    public CustomNotification(Context context, PendingIntent actionOnClick, String titleText,
                              String smallText, @DrawableRes int iconImageID,
                              boolean removeNotificationWhenClicked,
                              boolean _canBeSwipedAway){

        uniqueID = (int) System.nanoTime();
        canBeSwipedAway = _canBeSwipedAway;

        notification = new Notification.Builder(context)
                .setContentTitle(titleText)
                .setContentText(smallText)
                .setSmallIcon(iconImageID)
                .setAutoCancel(removeNotificationWhenClicked)
                .setContentIntent(actionOnClick)
                .build();
    }

    /* Causes the notification to appear and its icon to show in the top bar: */
    public void show(Context context){
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (!canBeSwipedAway)
            notification.flags = notification.flags | Notification.FLAG_ONGOING_EVENT;
        manager.notify(uniqueID, notification);
    }

    /* Deletes the notification - the icon in the top bar will go away: */
    public void remove(Context context){
        NotificationManager manager = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);
        manager.cancel(uniqueID);
    }
}
