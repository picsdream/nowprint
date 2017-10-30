package com.picsdream.picsdreamsdk.util;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.picsdream.picsdreamsdk.activity.InitialDataLoadActivity;
import com.picsdream.picsdreamsdk.application.ContextProvider;

/**
 * Authored by vipulkumar on 28/08/17.
 */

public class OrderPrint {

    public static OrderPrint getInstance() {
        return new OrderPrint();
    }

    public void launch(Context context) {
        ContextProvider.trackEvent(SharedPrefsUtil.getAppKey(), "SDK Launched", "");
        Intent intent = new Intent(context, InitialDataLoadActivity.class);
        NavigationUtil.startActivity(context, intent);
    }

    public OrderPrint initialize(String appKey) {
        SharedPrefsUtil.setAppKey(appKey);
        ContextProvider.trackEvent(appKey, "SDK Initialised", "");
        return this;
    }

    public OrderPrint with(Application application) {
        ContextProvider.initializeApplication(application);
        return this;
    }

    public OrderPrint ImageUri(Uri uri) {
        SharedPrefsUtil.setImageUri(uri.toString());
        return this;
    }

    public OrderPrint returnBackActivity(Class<?> cls) {
        SharedPrefsUtil.setColorPrimary(cls.getName());
        return this;
    }

    public OrderPrint runInSandboxMode(boolean sandboxMode) {
        SharedPrefsUtil.setSandboxMode(sandboxMode);
        return this;
    }

//    public OrderPrint colorPrimary(String colorPrimary) {
//        SharedPrefsUtil.setColorPrimary(colorPrimary);
//        return this;
//    }
//
//    public OrderPrint colorAccent(String colorAccent) {
//        SharedPrefsUtil.setColorAccent(colorAccent);
//        return this;
//    }

//    public static String getColorPrimary() {
//        return SharedPrefsUtil.getColorPrimary();
//    }
//
//    public static String getColorAccent() {
//        return SharedPrefsUtil.getColorAccent();
//    }
}
