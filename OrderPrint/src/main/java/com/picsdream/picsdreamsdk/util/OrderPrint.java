package com.picsdream.picsdreamsdk.util;

import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.picsdream.picsdreamsdk.R;
import com.picsdream.picsdreamsdk.activity.InitialDataLoadActivity;
import com.picsdream.picsdreamsdk.activity.PurchaseActivity;
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

    public static OrderPrint getInstance() {
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
        dialog.setTitle("");

        ImageView dialogImage = (ImageView) dialog.findViewById(R.id.dialogImage);
        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButton);
        Button closeButton = (Button) dialog.findViewById(R.id.closeButton);

        try {
            dialogButton.setText(obj.getString("text"));
            Picasso.with(context)
                    .load(obj.getString("img"))
                    .into(dialogImage);
            dialog.show();
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
            }
        });
    }

    public void launch(Context context) {
        ContextProvider.trackEvent(SharedPrefsUtil.getAppKey(), "SDK Launched", "");
        Intent intent = new Intent(context, InitialDataLoadActivity.class);
        NavigationUtil.startActivity(context, intent);
    }

    public void ad(final Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_confirmation);
        dialog.setTitle("Title");

        ImageView dialogImage = (ImageView) dialog.findViewById(R.id.dialogImage);
        Button positiveButton = (Button) dialog.findViewById(R.id.positiveButton);
        Button negativeButton = (Button) dialog.findViewById(R.id.negativeButton);
        Button closeButton = (Button) dialog.findViewById(R.id.closeButton);

        Picasso.with(context)
                .load(SharedPrefsUtil.getImageUriString())
                .resize(Utils.dpToPx(250), 0)
                .into(dialogImage);
        dialog.show();
        ContextProvider.trackEvent(SharedPrefsUtil.getAppKey(), "Print This Popup", "");

        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launch(context);
            }
        });

        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
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
