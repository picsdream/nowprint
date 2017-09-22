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

public class NowPrint {

    public static NowPrint getInstance() {
        return new NowPrint();
    }

    public void launch(Context context) {
        Intent intent = new Intent(context, InitialDataLoadActivity.class);
        NavigationUtil.startActivity(context, intent);
    }

    public NowPrint initialize(String appKey) {
        SharedPrefsUtil.setAppKey(appKey);
        return this;
    }

    public NowPrint with(Application application) {
        ContextProvider.initializeApplication(application);
        return this;
    }

    public NowPrint ImageUri(Uri uri) {
        SharedPrefsUtil.setImageUri(uri.toString());
        return this;
    }

    public NowPrint returnBackActivity(Class<?> cls) {
        SharedPrefsUtil.setColorPrimary(cls.getName());
        return this;
    }

    public NowPrint runInSandboxMode(boolean sandboxMode) {
        SharedPrefsUtil.setSandboxMode(sandboxMode);
        return this;
    }

//    public NowPrint colorPrimary(String colorPrimary) {
//        SharedPrefsUtil.setColorPrimary(colorPrimary);
//        return this;
//    }
//
//    public NowPrint colorAccent(String colorAccent) {
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
