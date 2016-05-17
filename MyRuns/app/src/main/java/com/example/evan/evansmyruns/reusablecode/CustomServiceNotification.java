package com.example.evan.evansmyruns.reusablecode;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.DrawableRes;

/**
 * Created by Evan on 5/8/2016.
 *
 * A class for notifications that are associated with a service.
 * These notifications do not swipe away, do not remove when clicked,
 *  and bring back the specified activity when clicked.
 *
 */
public class CustomServiceNotification extends CustomNotification{

    public CustomServiceNotification(Context context, Class<?> activityToShow, String titleText,
                                     String detailsText, @DrawableRes int iconId){

        // Calls the CustomNotification constructor, specifying false for swipe/remove:
        super(context, null, titleText, detailsText, iconId, false, false);

        // Causes the notification to bring back the specified activity when clicked:
        Intent intent = new Intent(context, activityToShow);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        notification.contentIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                0);
    }
}
