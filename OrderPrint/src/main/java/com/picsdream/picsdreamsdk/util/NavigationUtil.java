package com.picsdream.picsdreamsdk.util;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.View;

/**
 * Authored by vipulkumar on 15/09/17.
 */

public class NavigationUtil {
    public static void startActivityWithClipReveal(Context context, Class<?> landingActivity, View view) {
        Intent intent = new Intent(context, landingActivity);
        ActivityOptions options = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            options = ActivityOptions.makeClipRevealAnimation(view, 0, 0,
                    view.getWidth(), view.getHeight());
            context.startActivity(intent, options.toBundle());
        } else {
            context.startActivity(intent);
        }
    }

    public static void startActivityWithClipReveal(Context context, Intent intent, View view) {
        ActivityOptions options = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            options = ActivityOptions.makeClipRevealAnimation(view, 0, 0,
                    view.getWidth(), view.getHeight());
            context.startActivity(intent, options.toBundle());
        } else {
            context.startActivity(intent);
        }
    }

    public static void startActivity(Context context, Class<?> landingActivity) {
        Intent intent = new Intent(context, landingActivity);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, Intent intent) {
        context.startActivity(intent);
    }
}
