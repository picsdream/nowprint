package com.picsdream.picsdreamsdk.application;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.analytics.ecommerce.Product;
import com.google.android.gms.analytics.ecommerce.ProductAction;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.picsdream.picsdreamsdk.util.AnalyticsTrackers;
import com.picsdream.picsdreamsdk.util.SharedPrefsUtil;

import static com.google.firebase.analytics.FirebaseAnalytics.*;

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

    public static void trackEcommerce(Product product, String productActionString,  String category, String action, String label) {
        if (!SharedPrefsUtil.getSandboxMode()) {
            Tracker t = getGoogleAnalyticsTracker();

            ProductAction productAction = new ProductAction(ProductAction.ACTION_CLICK)
                    .setProductActionList(productActionString);

            t.send(new HitBuilders.EventBuilder().addProduct(product)
                    .setProductAction(productAction).setCategory(category).setAction(action).setLabel(label).build());
        }
    }

    public static void trackEcommerce(Activity activity, String name, String cat, String medium, String size, double price) {
        Product product = new Product()

                .setName(name)
                .setCategory(cat)
                .setBrand(medium)
                .setVariant(size)
                .setPosition(1);

        ProductAction productAction = new ProductAction(ProductAction.ACTION_CLICK)
                .setProductActionList("View Product");

        HitBuilders.ScreenViewBuilder builder = new HitBuilders.ScreenViewBuilder()
                .addProduct(product)
                .setProductAction(productAction);

        getGoogleAnalyticsTracker().send(builder.build());
    }
}
