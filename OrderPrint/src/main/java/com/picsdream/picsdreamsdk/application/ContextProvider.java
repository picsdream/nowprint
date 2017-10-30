package com.picsdream.picsdreamsdk.application;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;
import com.picsdream.picsdreamsdk.util.AnalyticsTrackers;
import com.picsdream.picsdreamsdk.util.SharedPrefsUtil;

import io.intercom.android.sdk.Intercom;

/**
 * Authored by vipulkumar on 02/09/17.
 */

public class ContextProvider {
    private static Application APPLICATION;

    public static void initializeApplication(Application application) {
        APPLICATION = application;

        if (AnalyticsTrackers.getInstance() == null) {
            AnalyticsTrackers.initialize(application);
        }
        AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP);
        initIntercom(application);
    }

    private static void initIntercom(Application application) {
        Intercom.initialize(application, "android_sdk-af3ba9f907a65bd5166d23ad1c3aafe825984c9b", "l7wg2suf");
        Intercom.client().registerUnidentifiedUser();
    }

    public ContextProvider() {

    }

    public static Application getApplication() {
        return APPLICATION;
    }

    public static synchronized Tracker getGoogleAnalyticsTracker() {
        AnalyticsTrackers analyticsTrackers = AnalyticsTrackers.getInstance();
        return analyticsTrackers.get(AnalyticsTrackers.Target.APP);
    }

    /***
     * Tracking screen view
     *
     * @param screenName screen name to be displayed on GA dashboard
     */
    public static void trackScreenView(String screenName) {
        if (!SharedPrefsUtil.getSandboxMode()) {
            Tracker t = getGoogleAnalyticsTracker();

            // Set screen name.
            t.setScreenName(screenName);

            // Send a screen view.
            t.send(new HitBuilders.ScreenViewBuilder().build());

            GoogleAnalytics.getInstance(getApplication()).dispatchLocalHits();
        }
    }

    /***
     * Tracking exception
     *
     * @param e exception to be tracked
     */
    public static void trackException(Exception e) {
        if (e != null) {
            Tracker t = getGoogleAnalyticsTracker();

            t.send(new HitBuilders.ExceptionBuilder()
                    .setDescription(
                            new StandardExceptionParser(getApplication(), null)
                                    .getDescription(Thread.currentThread().getName(), e))
                    .setFatal(false)
                    .build()
            );
        }
    }

    /***
     * Tracking event
     *
     * @param category event category
     * @param action   action of the event
     * @param label    label
     */
    public static void trackEvent(String category, String action, String label) {
        if (!SharedPrefsUtil.getSandboxMode()) {
            Tracker t = getGoogleAnalyticsTracker();

            // Build and send an Event.
            t.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).setLabel(label).build());
        }
    }
}
