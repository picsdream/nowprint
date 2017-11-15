package com.picsdream.sample;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.picsdream.picsdreamsdk.util.AnalyticsTrackers;
import com.picsdream.picsdreamsdk.util.OrderPrint;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_CHOOSE = 200;
    private List<Uri> mSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeMatisse();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initializeMatisse() {
        Matisse.from(MainActivity.this)
                .choose(MimeType.allOf())
                .countable(false)
                .maxSelectable(1)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new PicassoEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);
            initializePicsDream(mSelected.get(0));
            Log.d("Matisse", "mSelected: " + mSelected);
        }
    }

    private void initializePicsDream(Uri uri) {
        AnalyticsTrackers analyticsTrackers = AnalyticsTrackers.getInstance();;
        Tracker t = GoogleAnalytics.getInstance(getApplicationContext()).newTracker("UA-109712092-1");

        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder().setCategory("TEST_CAT").setAction("TEST FROM INIT").setLabel("HELLO").build());
        OrderPrint.getInstance()
                .with(this.getApplication())
                .returnBackActivity(MainActivity.class)
                .runInSandboxMode(false)
                .initialize("43748398643785726");

        OrderPrint.getInstance()
                .ImageUri(uri).launch(this);
    }
}
