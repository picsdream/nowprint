package com.picsdream.picsdreamsdk.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.picsdream.picsdreamsdk.R;
import com.picsdream.picsdreamsdk.model.network.InitialAppDataResponse;
import com.picsdream.picsdreamsdk.payment.PaypalHelper;
import com.picsdream.picsdreamsdk.payment.PaytmHelper;
import com.picsdream.picsdreamsdk.util.NavigationUtil;
import com.picsdream.picsdreamsdk.util.SaneToast;
import com.picsdream.picsdreamsdk.util.SharedPrefsUtil;
import com.picsdream.picsdreamsdk.view.PaytmChecsumView;

import org.json.JSONException;

/**
 * Authored by vipulkumar on 29/08/17.
 */

public class PaymentActivity extends BaseActivity implements PaytmChecsumView {
    private Toolbar toolbar;
    private PaytmChecsumView paytmChecsumView;
    final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_PRODUCTION;
    // note that these credentials will differ between live & sandbox environments.
    final int REQUEST_CODE_PAYMENT = 1;
    String TAG = "Tag";
    PayPalConfiguration config;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        paytmChecsumView = this;
        setupUi();
//        onPaymentCompleted();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, PayPalService.class));
    }

    private void setupUi() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setupToolbar(toolbar);

        if (SharedPrefsUtil.getCountry().getCountry().equalsIgnoreCase("India")) {
            initPayTm();
        } else {
            initPayPal();
        }
    }

    private void initPayTm() {
        PaytmHelper.generateChecksum(this);
    }

    private void initPayPal() {
        config = PaypalHelper.getPaypalConfig(CONFIG_ENVIRONMENT);
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
        PaypalHelper.payWithPaypal(this, config, REQUEST_CODE_PAYMENT);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onChecksumSuccess(String checksum) {
        InitialAppDataResponse initialAppDataResponse = SharedPrefsUtil.getInitialDataResponse();
        initialAppDataResponse.getGateways().getPaytm().getChecksum().setCheckSum(checksum);
        SharedPrefsUtil.setInitialDataResponse(initialAppDataResponse);
        payWithPaytm();
    }

    @Override
    public void onChecksumFailure() {
        onBackPressed();
    }

    private void payWithPaytm() {
        PaytmHelper.createPayTmOrder();
        PaytmHelper.startPayTmTransaction(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm =
                        data.getParcelableExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        Log.i(TAG, confirm.toJSONObject().toString(4));
                        Log.i(TAG, confirm.getPayment().toJSONObject().toString(4));
                        SaneToast.getToast("PaymentConfirmation info received from PayPal").show();
                        onPaymentCompleted();
                    } catch (JSONException e) {
                        onPaymentError();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                SaneToast.getToast("Payment was cancelled").show();
            } else if (resultCode == com.paypal.android.sdk.payments.PaymentActivity.RESULT_EXTRAS_INVALID) {
                onPaymentError();
            }
        }
    }

    private void onPaymentError() {
        SaneToast.getToast("Some error occurred. Please try again.").show();
    }

    public void onPaymentCompleted() {
        finish();
        Intent intent = new Intent(this, PurchaseActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        NavigationUtil.startActivity(this, intent);
    }
}
