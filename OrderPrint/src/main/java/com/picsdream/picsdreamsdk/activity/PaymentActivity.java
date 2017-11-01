package com.picsdream.picsdreamsdk.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.picsdream.picsdreamsdk.R;
import com.picsdream.picsdreamsdk.application.ContextProvider;
import com.picsdream.picsdreamsdk.model.Order;
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
    final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_PRODUCTION;
    // note that these credentials will differ between live & sandbox environments.
    final int REQUEST_CODE_PAYMENT = 1;
    String TAG = "Tag";
    PayPalConfiguration config;
    private boolean isCod;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        if (SharedPrefsUtil.getSandboxMode()) {
            onPaymentCompleted();
        } else {
            setupUi();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, PayPalService.class));
    }

    private void setupUi() {
        isCod = getIntent().getBooleanExtra("isCod", false);
        if (isCod) {
            onPaymentCompleted();
        } else {
            if (SharedPrefsUtil.getCountry().getCountry().equalsIgnoreCase("India")) {
                ContextProvider.trackEvent(APP_KEY, "Payment Init PayTm", "");
                initPayTm();
            } else {
                ContextProvider.trackEvent(APP_KEY, "Payment Init PayPal", "");
                initPayPal();
            }
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
        ContextProvider.trackEvent(APP_KEY, "Checksum Success", "");
        InitialAppDataResponse initialAppDataResponse = SharedPrefsUtil.getInitialDataResponse();
        initialAppDataResponse.getGateways().getPaytm().getChecksum().setCheckSum(checksum);
        SharedPrefsUtil.setInitialDataResponse(initialAppDataResponse);
        payWithPaytm();
    }

    @Override
    public void onChecksumFailure() {
        ContextProvider.trackEvent(APP_KEY, "Checksum Error", "");
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
                        ContextProvider.trackEvent(APP_KEY, "Paid with payPal", "");
                        onPaymentCompleted();
                    } catch (JSONException e) {
                        onPaymentError();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                ContextProvider.trackEvent(APP_KEY, "Payment cancelled", "User cancelled PayPal");
                PaymentActivity.this.finish();
                SaneToast.getToast("Payment was cancelled").show();
            } else if (resultCode == com.paypal.android.sdk.payments.PaymentActivity.RESULT_EXTRAS_INVALID) {
                onPaymentError();
            }
        }
    }

    private void onPaymentError() {
        SaneToast.getToast("Some error occurred. Please try again.").show();
        PaymentActivity.this.finish();
    }

    public void onPaymentCompleted() {
        finish();
        Order order = SharedPrefsUtil.getOrder();
        if (isCod) {
            order.setTotalPaid(String.valueOf(0));
        } else {
            order.setTotalPaid(String.valueOf(order.getFinalCost()));
        }
        SharedPrefsUtil.saveOrder(order);
        Intent intent = new Intent(this, PurchaseActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("isCod", isCod);
        NavigationUtil.startActivity(this, intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ContextProvider.trackScreenView("Payment");
    }
}
