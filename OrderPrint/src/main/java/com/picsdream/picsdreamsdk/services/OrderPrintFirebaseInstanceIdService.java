package com.picsdream.picsdreamsdk.services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.picsdream.picsdreamsdk.util.SharedPrefsUtil;

/**
 * Created by Ankur on 01-Dec-17.
 */

public class OrderPrintFirebaseInstanceIdService extends FirebaseInstanceIdService {
    private static final String TAG = "OrderPrintFirebase";
    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
    }

}
