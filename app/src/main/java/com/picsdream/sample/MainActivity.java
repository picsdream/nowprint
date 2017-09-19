package com.picsdream.sample;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.picsdream.picsdreamsdk.activity.BaseActivity;
import com.picsdream.picsdreamsdk.util.PicsDream;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;

import java.util.List;

public class MainActivity extends BaseActivity {

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
        PicsDream.getInstance()
                .with(this)
                .returnBackActivity(MainActivity.class)
                .initialize("app_key");
        PicsDream.getInstance()
                .ImageUri(uri).launch(this);
    }
}
