package com.picsdream.picsdreamsdk.util;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.picsdream.picsdreamsdk.R;
import com.picsdream.picsdreamsdk.application.ContextProvider;

/**
 * Authored by vipulkumar on 02/08/17.
 */

public final class SaneToast {
    private static Toast SANE_TOAST = null;

    public static Toast getToast() {
        if (SANE_TOAST == null) {
            SANE_TOAST = new Toast(ContextProvider.getInstance());
        }
        return SANE_TOAST;
    }

    public static Toast getToast(final String message) {
        LayoutInflater inflater = LayoutInflater.from(ContextProvider.getInstance());
        View layout = inflater.inflate(R.layout.widget_toast, null);
        TextView text = layout.findViewById(R.id.tv_toast_msg);
        text.setText(message);

        //Prevent creating multiple toasts at a time
        Toast toast = getToast();
        toast.setView(layout);
        toast.setDuration(Toast.LENGTH_SHORT);
        return toast;
    }

    public static Toast getToast(final String message, int length) {
        Toast toast = getToast(message);
        toast.setDuration(length);
        return toast;
    }

    public static Toast getToast(String message, Integer iconResource) {
        Toast toast = getToast(message);
        View view = toast.getView();
        ImageView imageView = view.findViewById(R.id.iv_toast_icon);
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageResource(iconResource);
        toast.setView(view);
        return toast;
    }

    private SaneToast() {
    }
}
