package com.picsdream.sample;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
    Button nav1, nav2, nav3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

        nav1 = (Button) findViewById(R.id.nav1);
        nav2 = (Button) findViewById(R.id.nav2);
        nav3 = (Button) findViewById(R.id.nav3);
        OrderPrint.getInstance()
                .with(getApplication())
                .returnBackActivity(MainActivity.class)
                .runInSandboxMode(false)
                .initialize("1512208588730720430");

        if(intent.getStringExtra("source") != null) {
            if(intent.getStringExtra("source").equals("")) {
                OrderPrint.getInstance()
                        .intro(this, MainActivity.class);
            }
        } else {
            OrderPrint.getInstance()
                    .intro(this, MainActivity.class);
        }

        nav1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeMatisse();
            }
        });

        nav2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderPrint.getInstance()
                        .with(getApplication())
                        .returnBackActivity(MainActivity.class)
                        .runInSandboxMode(false)
                        .initialize("1512208616410566060");
                initializeMatisse();
            }
        });

        nav3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderPrint.getInstance()
                        .with(getApplication())
                        .returnBackActivity(MainActivity.class)
                        .runInSandboxMode(false)
                        .initialize("1512208639687917315");
                initializeMatisse();
            }
        });
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
        AnalyticsTrackers analyticsTrackers = AnalyticsTrackers.getInstance();
        Tracker t = GoogleAnalytics.getInstance(getApplicationContext()).newTracker("UA-109712092-1");

        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder().setCategory("TEST_CAT").setAction("TEST FROM INIT").setLabel("HELLO").build());

        OrderPrint.getInstance().ImageUri(uri).ad(this);

//        OrderPrint.getInstance()
//                .ImageUri(uri).launch(this);
    }
}
