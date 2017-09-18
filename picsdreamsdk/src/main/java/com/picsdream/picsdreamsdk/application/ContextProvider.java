package com.picsdream.picsdreamsdk.application;

import android.app.Application;
import android.content.Context;

/**
 * Authored by vipulkumar on 02/09/17.
 */

public class ContextProvider extends Application {
    private static Context INSTANCE;
    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
    }

    public ContextProvider() {
    }

    public static Context getInstance() {
        return INSTANCE;
    }
}
