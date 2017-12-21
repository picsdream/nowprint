package com.picsdream.picsdreamsdk.payment;

import android.app.Activity;
import android.content.Intent;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.picsdream.picsdreamsdk.model.Order;
import com.picsdream.picsdreamsdk.model.Region;
import com.picsdream.picsdreamsdk.model.payment.Paypal;
import com.picsdream.picsdreamsdk.util.SharedPrefsUtil;

import java.math.BigDecimal;

/**
 * Authored by vipulkumar on 12/09/17.
 */

public class PaypalHelper {
    public static void payWithPaypal(Activity context, PayPalConfiguration config, int REQUEST_CODE_PAYMENT) {
        PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(context, com.paypal.android.sdk.payments.PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_PAYMENT, thingToBuy);
        context.startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

    public static PayPalPayment getThingToBuy(String paymentIntent) {
        Order order = SharedPrefsUtil.getOrder();
        Region region = SharedPrefsUtil.getRegion();
        return new PayPalPayment(new BigDecimal(order.getFinalCost()), region.getCurrency(), order.getId(),
                paymentIntent);
    }

    public static PayPalConfiguration getPaypalConfig(String CONFIG_ENVIRONMENT) {
        Paypal paypal = SharedPrefsUtil.getInitialDataResponse().getGateways().getPaypal();
        return new PayPalConfiguration()
                .environment(CONFIG_ENVIRONMENT)
                .acceptCreditCards(false)
                .clientId(paypal.getClientId());
    }
}
