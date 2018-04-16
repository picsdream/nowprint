package com.picsdream.picsdreamsdk.util;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.picsdream.picsdreamsdk.R;
import com.picsdream.picsdreamsdk.activity.InitialDataLoadActivity;
import com.picsdream.picsdreamsdk.application.ContextProvider;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cz.msebera.android.httpclient.Header;

/**
 * Authored by vipulkumar on 28/08/17.
 */

public class OrderPrint {
    public static int popWidth = 0;
    public static OrderPrint getInstance() {
        popWidth = Resources.getSystem().getDisplayMetrics().widthPixels - 100;
        return new OrderPrint();
    }

    public void intro(final Context context, final Class<?> cls) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Constants.URL_BEFORE_APP_DATA, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {
                try {
                    showPop(context, cls, responseBody.getJSONObject("pop"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showPop(final Context context, final Class<?> cls, JSONObject obj) {
        try {
            if(obj.getInt("enabled") == 0)
                return;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog);

        ImageView dialogImage = (ImageView) dialog.findViewById(R.id.dialogImage);
        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButton);
        Button closeButton = (Button) dialog.findViewById(R.id.closeButton);

        try {
            dialogButton.setText(obj.getString("text"));
            Picasso.with(context)
                    .load(obj.getString("img"))
                    .resize(popWidth, 0)
                    .into(dialogImage);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = popWidth;
            dialog.show();
            dialog.getWindow().setAttributes(lp);
            ContextProvider.trackEvent(SharedPrefsUtil.getAppKey(), "Intro Popup", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, cls);
                intent.putExtra("source", "pop");
                context.startActivity(intent);
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                ContextProvider.trackEvent(SharedPrefsUtil.getAppKey(), "Intro Popup Dismiss", "");
            }
        });
    }

    public void launch(Context context) {
        ContextProvider.trackEvent(SharedPrefsUtil.getAppKey(), "SDK Launched", "");
        Intent intent = new Intent(context, InitialDataLoadActivity.class);
        NavigationUtil.startActivity(context, intent);
    }

    public void ad(final Context context) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Constants.URL_CONFIRMATION_APP_DATA, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {
                try {
                    JSONObject pop = responseBody.getJSONObject("pop");
                    adPop(context,
                            pop.getString("promptHeading"),
                            pop.getString("promptText"),
                            pop.getString("positiveText"),
                            pop.getString("negativeText"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void adPop(final Context context, String heading, String text, String positive, String negative) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_confirmation);

        LinearLayout popParent = (LinearLayout) dialog.findViewById(R.id.popParent);
        ImageView dialogImage = (ImageView) dialog.findViewById(R.id.dialogImage);
        TextView headingTextView = (TextView) dialog.findViewById(R.id.heading);
        TextView contentTextView = (TextView) dialog.findViewById(R.id.content);
        Button positiveButton = (Button) dialog.findViewById(R.id.positiveButton);
//        TextView negativeButton = (TextView) dialog.findViewById(R.id.negativeButton);
        Button closeButton = (Button) dialog.findViewById(R.id.closeButton);

        headingTextView.setText(heading);
        contentTextView.setText(text);
        positiveButton.setText(positive);
//        negativeButton.setText(negative);

        Picasso.with(context)
                .load(SharedPrefsUtil.getImageUriString())
                .resize(popWidth + 10, 0)
                .into(dialogImage);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.horizontalMargin = 0;
        lp.verticalMargin = 0;
        dialog.show();
        dialog.getWindow().setAttributes(lp);

        ContextProvider.trackEvent(SharedPrefsUtil.getAppKey(), "Print This Popup", "");

        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launch(context);
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                ContextProvider.trackEvent(SharedPrefsUtil.getAppKey(), "Print This Popup Dismiss", "");
            }
        });
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
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(ContextProvider.getApplication()
                    .getContentResolver(), uri);
            if (isImageSquare(bitmap)) {
                SharedPrefsUtil.setImageSquare(true);
            } else {
                SharedPrefsUtil.setImageSquare(false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        SharedPrefsUtil.setImageUri(uri.toString());
        return this;
    }

    public OrderPrint returnBackActivity(Class<?> cls) {
        SharedPrefsUtil.setReturnActivityName(cls.getName());
        return this;
    }

    public OrderPrint runInSandboxMode(boolean sandboxMode) {
        SharedPrefsUtil.setSandboxMode(sandboxMode);
        return this;
    }

    private boolean isImageSquare(Bitmap bitmap) {
        return Math.abs(bitmap.getWidth() - bitmap.getHeight()) < 50
                || Math.abs(bitmap.getHeight() - bitmap.getWidth()) < 50;
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
