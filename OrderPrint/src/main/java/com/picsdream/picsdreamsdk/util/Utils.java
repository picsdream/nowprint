package com.picsdream.picsdreamsdk.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;

import com.picsdream.picsdreamsdk.R;
import com.picsdream.picsdreamsdk.activity.BaseActivity;
import com.picsdream.picsdreamsdk.activity.EmailPhotoActivity;
import com.picsdream.picsdreamsdk.application.ContextProvider;
import com.picsdream.picsdreamsdk.model.Country;
import com.picsdream.picsdreamsdk.model.Region;
import com.picsdream.picsdreamsdk.model.network.InitialAppDataResponse;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import io.intercom.android.sdk.Intercom;

/**
 * Authored by vipulkumar on 02/09/17.
 */

public class Utils {

    public static void setRecyclerViewProperties(Context context, @NonNull RecyclerView recyclerView, int orientation) {
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        mLayoutManager.setOrientation(orientation);
        mLayoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public static Bitmap makeBlackAndWhite(Bitmap src) {
        // image size
        int width = src.getWidth();
        int height = src.getHeight();
        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        // color information
        int A, R, G, B;
        int pixel;
        // get contrast value
        int value = 50;
        double contrast = Math.pow((100 + value) / 100, 2);

        // scan through all pixels
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                // apply filter contrast for every channel R, G, B
                R = Color.red(pixel);
                R = (int) (((((R / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if (R < 0) {
                    R = 0;
                } else if (R > 255) {
                    R = 255;
                }

                G = Color.red(pixel);
                G = (int) (((((G / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if (G < 0) {
                    G = 0;
                } else if (G > 255) {
                    G = 255;
                }

                B = Color.red(pixel);
                B = (int) (((((B / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if (B < 0) {
                    B = 0;
                } else if (B > 255) {
                    B = 255;
                }

                // set new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        return bmOut;
    }

    public static void revealFromCenter(Context context, View viewTorReveal) {
        viewTorReveal.setVisibility(View.INVISIBLE);
        int cx = (int) ((viewTorReveal.getWidth() + viewTorReveal.getX()) / 2);
        int cy = (int) ((viewTorReveal.getHeight() + viewTorReveal.getY()) / 2);
        int radius = Math.max(viewTorReveal.getWidth(), viewTorReveal.getHeight());
        Animator anim = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            anim = android.view.ViewAnimationUtils.createCircularReveal(viewTorReveal, cx, cy, 0, radius);
            anim.setDuration(500);
            viewTorReveal.setVisibility(View.VISIBLE);
            anim.start();
        } else {
            viewTorReveal.setVisibility(View.VISIBLE);
        }
    }

    public static void hideFromCenter(Context context, final View viewTorReveal) {
        viewTorReveal.setVisibility(View.INVISIBLE);
        int cx = (int) ((viewTorReveal.getWidth() + viewTorReveal.getX()) / 2);
        int cy = (int) ((viewTorReveal.getHeight() + viewTorReveal.getY()) / 2);
        int radius = Math.max(viewTorReveal.getWidth(), viewTorReveal.getHeight());
        Animator anim = null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            anim = android.view.ViewAnimationUtils.createCircularReveal(viewTorReveal, cx, cy, radius, 0);
            anim.setDuration(300);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    viewTorReveal.setVisibility(View.INVISIBLE);
                }
            });
            anim.start();
        } else {
            viewTorReveal.setVisibility(View.INVISIBLE);
        }
    }


    public static String capitalizeFirstCharacterOfEveryWord(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        } else {
            string = string.toLowerCase();
            StringBuffer res = new StringBuffer();
            if (TextUtils.isEmpty(string))
                return "";
            String[] strArr = string.split(" ");
            for (String str : strArr) {
                char[] stringArray = str.trim().toCharArray();
                stringArray[0] = Character.toUpperCase(stringArray[0]);
                str = new String(stringArray);

                res.append(str).append(" ");
            }
            return res.toString().trim();
        }
    }

    public static Region setRegionAfterCountry() {
        Region selectedRegion = null;
        InitialAppDataResponse initialAppDataResponse = SharedPrefsUtil.getInitialDataResponse();
        Country selectedCountry = SharedPrefsUtil.getCountry();
        List<Region> regions = initialAppDataResponse.getRegions();

        for (Region region : regions) {
            if (region.getType().equalsIgnoreCase(Constants.REGION_TYPE_COUNTRY)) {
                if (selectedCountry.getCountry().equalsIgnoreCase(region.getName())) {
                    selectedRegion = region;
                }
            } else if (region.getType().equalsIgnoreCase(Constants.REGION_TYPE_COUNTRY)) {
                selectedRegion = region;
            }
        }

        if (selectedRegion == null) {
            for (Region region : regions) {
                if (region.getName().equalsIgnoreCase("default")) {
                    selectedRegion = region;
                }
            }
        }
        SharedPrefsUtil.setRegion(selectedRegion);
        return selectedRegion;
    }

    public static String getFormattedPrice(float price) {
        Region region = SharedPrefsUtil.getRegion();
        return region.getCurrency() + " " + price;
    }

    public static float getConvertedPrice(float price) {
        Region region = SharedPrefsUtil.getRegion();
        return  Utils.roundOffFloat(Math.round(price * region.getConversion()));
    }

    public static float roundOffFloat(float number) {
        BigDecimal bd = new BigDecimal(Float.toString(number));
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    public static String generateOrderId() {
        String ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm";
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(5);
        for (int i = 0; i < 5; ++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    public static float getDiscountedPrice(float price, int discountPer) {
        return Utils.roundOffFloat(price - ((price * discountPer) / 100));
    }

    public static int fetchAccentColor(Context context) {
        TypedValue typedValue = new TypedValue();

        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[]{R.attr.colorAccent});
        int color = a.getColor(0, 0);

        a.recycle();

        return color;
    }

    public static int fetchPrimaryColor(Context context) {
        TypedValue typedValue = new TypedValue();
        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[]{R.attr.colorPrimary});
        int color = a.getColor(0, 0);
        a.recycle();

        return color;
    }

    public static void intiateCall(final Context context) {
        InitialAppDataResponse initialAppDataResponse = SharedPrefsUtil.getInitialDataResponse();
        final String contact = initialAppDataResponse.getContact();
        new AlertDialog.Builder(context)
                .setTitle("Would you like to call NowPrint Support?")
                .setPositiveButton("Call", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ContextProvider.trackEvent(SharedPrefsUtil.getAppKey(),
                                "Call initiated", "Call dialog confirmed");
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contact));
                        context.startActivity(intent);
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ContextProvider.trackEvent(SharedPrefsUtil.getAppKey(),
                                "Call cancelled", "Call dialog cancel");
                        dialogInterface.dismiss();
                    }
                }).show();
    }

    public static String formatDeliveryDateString(String rawDeliveryDateString) {
        String[] splitValues = rawDeliveryDateString.split("-");
        int lowerDays = Integer.parseInt(splitValues[0]);
        int higherDays = Integer.parseInt(splitValues[1]);
        SimpleDateFormat df = new SimpleDateFormat("EEEE, dd MMM");

        Calendar c = Calendar.getInstance();

        c.add(Calendar.DAY_OF_MONTH, lowerDays);
        String lowerDate = df.format(c.getTime());

        c.add(Calendar.DAY_OF_MONTH, higherDays - lowerDays);
        String higherDate = df.format(c.getTime());

        String formattedDate = lowerDate + " - " + higherDate;
        return formattedDate;
    }

    public static void onHelpItemsClicked(MenuItem item, BaseActivity activity, String screen) {
        if (item.getItemId() == R.id.menu_call) {
            ContextProvider.trackEvent(activity.APP_KEY, "Call button", screen);
            Utils.intiateCall(activity);
        } else if (item.getItemId() == R.id.menu_email) {
            ContextProvider.trackEvent(activity.APP_KEY, "Email button", screen);
            onEmailClicked(activity);
        } else if (item.getItemId() == R.id.menu_chat) {
            ContextProvider.trackEvent(activity.APP_KEY, "Chat button", screen);
            launchIntercom();
        }
    }

    public static void launchIntercom() {
        Intercom.client().setLauncherVisibility(Intercom.Visibility.GONE);
        Intercom.client().displayConversationsList();
    }

    public static void onEmailClicked(Context context) {
        NavigationUtil.startActivity(context, EmailPhotoActivity.class);
    }
}
