package com.picsdream.picsdreamsdk.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.picsdream.picsdreamsdk.application.ContextProvider;
import com.picsdream.picsdreamsdk.model.Country;
import com.picsdream.picsdreamsdk.model.Order;
import com.picsdream.picsdreamsdk.model.RenderedImage;
import com.picsdream.picsdreamsdk.model.network.PurchaseResponse;
import com.picsdream.picsdreamsdk.model.Region;
import com.picsdream.picsdreamsdk.model.network.CouponsResponse;
import com.picsdream.picsdreamsdk.model.request.Address;
import com.picsdream.picsdreamsdk.model.network.InitialAppDataResponse;

/**
 * Authored by vipulkumar on 02/08/17.
 */

public final class SharedPrefsUtil {
    @NonNull
    private static SharedPreferences.Editor edit() {
        return getPreferences().edit();
    }

    @NonNull
    private static SharedPreferences getPreferences() {
        return getContext().getSharedPreferences(getContext().getApplicationContext().getPackageName(), Context.MODE_PRIVATE);
    }

    @NonNull
    public static Resources getResources() {
        return getContext().getResources();
    }

    @NonNull
    private static Context getContext() {
        return ContextProvider.getApplication();
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return getPreferences().getBoolean(key, defaultValue);
    }

    public static void setBoolean(String key, boolean value) {
        edit().putBoolean(key, value).apply();
    }

    public static String getString(String key, String defaultValue) {
        return getPreferences().getString(key, defaultValue);
    }

    public static void setString(String key, String value) {
        edit().putString(key, value).apply();
    }

    public static int getInt(String key, int defaultValue) {
        return getPreferences().getInt(key, defaultValue);
    }

    public static void setInt(String key, int value) {
        edit().putInt(key, value).apply();
    }

    public static InitialAppDataResponse getInitialDataResponse() {
        return new Gson().fromJson(getString("sources", ""), InitialAppDataResponse.class);
    }

    public static void setInitialDataResponse(@NonNull InitialAppDataResponse initialDataResponse) {
        String initialAppDataString = new Gson().toJson(initialDataResponse);
        setString("sources", initialAppDataString);
    }

    public static void setAddress(Address address) {
        String string = new Gson().toJson(address);
        setString("address", string);
    }

    public static Address getAddress() {
        return new Gson().fromJson(getString("address", ""), Address.class);
    }

    public static void setCountry(Country country) {
        String countryString = new Gson().toJson(country);
        setString("country", countryString);
    }

    public static Country getCountry() {
        return new Gson().fromJson(getString("country", ""), Country.class);
    }

    public static void setRegion(Region region) {
        String string = new Gson().toJson(region);
        setString("region", string);
    }

    public static Region getRegion() {
        return new Gson().fromJson(getString("region", ""), Region.class);
    }

    public static void setCouponResponse(CouponsResponse couponResponse) {
        String string = new Gson().toJson(couponResponse);
        setString("couponResponse", string);
    }

    public static CouponsResponse getCouponResponse() {
        return new Gson().fromJson(getString("couponResponse", ""), CouponsResponse.class);
    }

    public static void saveOrder(Order order) {
        String orderString = new Gson().toJson(order);
        setString("current_order", orderString);
    }

    public static Order getOrder() {
        return new Gson().fromJson(getString("current_order", ""), Order.class);
    }


    public static void setTempJsonPurchase(PurchaseResponse purchase) {
        String orderString = new Gson().toJson(purchase);
        setString("purchase", orderString);
    }

    public static PurchaseResponse getTempJsonPurchase() {
        return new Gson().fromJson(getString("purchase", ""), PurchaseResponse.class);
    }


    public static void setRenderedImage(RenderedImage renderedImage) {
        String string = new Gson().toJson(renderedImage);
        setString("renderedImage", string);
    }

    public static RenderedImage getRenderedImage() {
        return new Gson().fromJson(getString("renderedImage", ""), RenderedImage.class);
    }

    public static void setColorPrimary(String colorPrimary) {
        setString("colorPrimary", colorPrimary);
    }

    public static String getColorPrimary() {
        return getString("colorPrimary", "#5533FF");
    }

    public static void setSandboxMode(boolean sandboxMode) {
        setBoolean("sandbox", sandboxMode);
    }

    public static boolean getSandboxMode() {
        return getBoolean("sandbox", true);
    }

    public static void setAppKey(String appKey) {
        setString("appKey", appKey);
    }

    public static String getAppKey() {
        return getString("appKey", "#");
    }


    public static void setReturnActivityName(String returnActivityName) {
        setString("returnActivity", returnActivityName);
    }

    public static String getReturnActivityName() {
        return getString("returnActivity", "");
    }

    public static void setColorAccent(String colorAccent) {
        setString("colorAccent", colorAccent);
    }

    public static String getColorAccent() {
        return getString("colorAccent", "#ff3658");
    }

    public static void setImageUri(String uri) {
        setString("imageUri", uri);
    }

    public static String getImageUriString() {
        return getString("imageUri", "");
    }

    public static void setImageSquare(boolean isSquare) {
        setBoolean("isImageSquare", isSquare);
    }

    public static boolean isImageSquare() {
        return getBoolean("isImageSquare", false);
    }

    private SharedPrefsUtil() {
    }
}
