package com.picsdream.picsdreamsdk.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.picsdream.picsdreamsdk.util.SharedPrefsUtil;

/**
 * Authored by vipulkumar on 28/08/17.
 */

public class BaseFragment extends Fragment {
    public String APP_KEY;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        APP_KEY = SharedPrefsUtil.getAppKey();

    }
}
