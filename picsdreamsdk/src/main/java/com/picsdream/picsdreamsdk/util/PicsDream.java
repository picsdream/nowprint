package com.picsdream.picsdreamsdk.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.picsdream.picsdreamsdk.activity.InitialDataLoadActivity;

/**
 * Authored by vipulkumar on 28/08/17.
 */

public class PicsDream {
    private Context context;
    private Uri uri;

    public static PicsDream getInstance() {
        return new PicsDream();
    }

    public void launch(Context context) {
        Intent intent = new Intent(context, InitialDataLoadActivity.class);
        NavigationUtil.startActivity(context, intent);
    }

    public PicsDream initialize(String appKey) {
        return this;
    }

    public PicsDream with(Context context) {
        this.context = context;
        return this;
    }

    public PicsDream ImageUri(Uri uri) {
        this.uri = uri;
        SharedPrefsUtil.setImageUri(uri.toString());
        return this;
    }

    public PicsDream returnBackActivity(Class<?> cls) {
        SharedPrefsUtil.setColorPrimary(cls.getName());
        return this;
    }

//    public PicsDream colorPrimary(String colorPrimary) {
//        SharedPrefsUtil.setColorPrimary(colorPrimary);
//        return this;
//    }
//
//    public PicsDream colorAccent(String colorAccent) {
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
