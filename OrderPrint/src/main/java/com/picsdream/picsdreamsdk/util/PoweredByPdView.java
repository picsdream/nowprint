package com.picsdream.picsdreamsdk.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.picsdream.picsdreamsdk.R;

/**
 * Authored by vipulkumar on 03/11/17.
 */

public class PoweredByPdView extends LinearLayout {
    private Context context;

    public PoweredByPdView(Context context) {
        super(context);
        init(context);
    }

    public PoweredByPdView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PoweredByPdView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PoweredByPdView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(final Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.view_powere_by_pd, this, true);
        TextView tvText = view.findViewById(R.id.tvText);
        tvText.setPaintFlags(tvText.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.picsdream.com");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        });
    }
}
