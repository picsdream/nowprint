package com.picsdream.picsdreamsdk.payment;

import android.app.Activity;
import android.os.Bundle;

import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.picsdream.picsdreamsdk.activity.PaymentActivity;
import com.picsdream.picsdreamsdk.application.ContextProvider;
import com.picsdream.picsdreamsdk.model.Order;
import com.picsdream.picsdreamsdk.model.network.ChecksumResponse;
import com.picsdream.picsdreamsdk.model.network.InitialAppDataResponse;
import com.picsdream.picsdreamsdk.model.payment.Paytm;
import com.picsdream.picsdreamsdk.model.request.Address;
import com.picsdream.picsdreamsdk.model.request.ChecksumRequest;
import com.picsdream.picsdreamsdk.network.Error;
import com.picsdream.picsdreamsdk.network.RequestHandler;
import com.picsdream.picsdreamsdk.network.RetrofitHandler;
import com.picsdream.picsdreamsdk.util.SaneToast;
import com.picsdream.picsdreamsdk.util.SharedPrefsUtil;
import com.picsdream.picsdreamsdk.view.PaytmChecsumView;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Authored by vipulkumar on 29/08/17.
 */

public class PaytmHelper {

    private static Map<String, String> getHashMap() {
        String paytmMid = "Addazz59638411532660";
        String website = "APP_STAGING";
        String merchantKey = "p!dtvHkQYWbravXf";
        String channelId = "WAP";
        String industryId = "Retail";
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("MID", paytmMid);
        paramMap.put("ORDER_ID", "ORDER000000001");
        paramMap.put("CUST_ID", "10000988111");
        paramMap.put("INDUSTRY_TYPE_ID", industryId);
        paramMap.put("CHANNEL_ID", channelId);
        paramMap.put("TXN_AMOUNT", "1");
        paramMap.put("WEBSITE", website);
        paramMap.put("CALLBACK_URL", "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=ORDER0000000001");
        paramMap.put("EMAIL", "abc@gmail.com");
        paramMap.put("MOBILE_NO", "9999999999");
        paramMap.put("CHECKSUMHASH", "w2QDRMgp1/BNdEnJEAPCIOmNgQvsi+" +
                "BhpqijfM9KvFfRiPmGSt3Ddzw+oTaGCLneJwxFFq5mqTMwJXdQE2EzK4px2xruDqKZjHupz9yXev4=");
        return paramMap;
    }

    private static Map<String, String> getRealhashMap() {
        InitialAppDataResponse initialAppDataResponse = SharedPrefsUtil.getInitialDataResponse();
        Order order = SharedPrefsUtil.getOrder();
        Address address = SharedPrefsUtil.getAddress();
        Paytm paytm = initialAppDataResponse.getGateways().getPaytm();
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("MID", paytm.getMid());
        paramMap.put("ORDER_ID", order.getId());
        paramMap.put("CUST_ID", getUsernameFromEmail(address.getEmail()));
        paramMap.put("INDUSTRY_TYPE_ID", paytm.getIndustry());
        paramMap.put("CHANNEL_ID", paytm.getChannel());
        paramMap.put("TXN_AMOUNT", order.getFinalCost());
        paramMap.put("WEBSITE", paytm.getWebsite());
        paramMap.put("CALLBACK_URL", "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=" + order.getId());
//        paramMap.put("EMAIL", address.getEmail());
//        paramMap.put("MOBILE_NO", formatMobileNumber(address.getMobile()));
        paramMap.put("CHECKSUMHASH", paytm.getChecksum().getCheckSum());
        return paramMap;
    }

    public static ChecksumRequest getChecksumRequest() {
        Paytm paytm = SharedPrefsUtil.getInitialDataResponse().getGateways().getPaytm();
        Address address = SharedPrefsUtil.getAddress();
        Order order = SharedPrefsUtil.getOrder();

        final ChecksumRequest checksumRequest = new ChecksumRequest();
        checksumRequest.setMid(paytm.getMid());
        checksumRequest.setCallbackUrl("https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=" + order.getId());
        checksumRequest.setChannelID(paytm.getChannel());
        checksumRequest.setCustId(getUsernameFromEmail(address.getEmail()));
        checksumRequest.setIndustryTypeID(paytm.getIndustry());
        checksumRequest.setOrderId(order.getId());
        checksumRequest.setTxnAmount(order.getFinalCost());
        checksumRequest.setWebsite(paytm.getWebsite());

        return checksumRequest;
    }

    public static void createPayTmOrder() {
        PaytmOrder Order = new PaytmOrder(getRealhashMap());
        PaytmPGService.getProductionService().initialize(Order, null);
    }

    public static void startPayTmTransaction(final Activity context) {
        PaytmPGService.getProductionService().startPaymentTransaction(context, true, true,
                new PaytmPaymentTransactionCallback() {
            @Override
            public void onTransactionResponse(Bundle bundle) {
                if(bundle.getString("STATUS").equals("TXN_SUCCESS")) {
                    ContextProvider.trackEvent(SharedPrefsUtil.getAppKey(),
                            "Paid with PayTM", "");
                    if (context instanceof PaymentActivity) {
                        ((PaymentActivity) context).onPaymentCompleted();
                        SaneToast.getToast("Success").show();
                    }
                } else {
                    context.finish();
                }
            }

            @Override
            public void networkNotAvailable() {
                context.finish();
                SaneToast.getToast("Not Available").show();
            }

            @Override
            public void clientAuthenticationFailed(String s) {
                context.finish();
                SaneToast.getToast("Auth Failed").show();
            }

            @Override
            public void someUIErrorOccurred(String s) {
                context.finish();
                SaneToast.getToast("UI error").show();
            }

            @Override
            public void onErrorLoadingWebPage(int i, String s, String s1) {
                context.finish();
                SaneToast.getToast("Error").show();
            }

            @Override
            public void onBackPressedCancelTransaction() {
                context.finish();
                ContextProvider.trackEvent(SharedPrefsUtil.getAppKey(),
                        "Payment cancelled", "User cancelled PayTm");
                SaneToast.getToast("Payment cancelled").show();
            }

            @Override
            public void onTransactionCancel(String s, Bundle bundle) {
                context.finish();
                SaneToast.getToast("Cancelled").show();
            }
        });
    }

    private static String getUsernameFromEmail(String email) {
        email = email.substring(0, email.indexOf("@"));
        return removeSpecialChars(email);
    }

    private static String removeSpecialChars(String input) {
        Pattern pt = Pattern.compile("[^a-zA-Z0-9]");
        Matcher match = pt.matcher(input);
        while (match.find()) {
            String s = match.group();
            input = input.replaceAll("\\" + s, "");
        }
        return input;
    }

    public static void generateChecksum(final PaytmChecsumView paytmChecsumView) {
        final Call<ResponseBody> call = RetrofitHandler.getApiService().generatePaytmChecksum(PaytmHelper.getChecksumRequest());
        RequestHandler.getInstance().createCall(paytmChecsumView, call, new RequestHandler.RequestCallback() {
            @Override
            public void onSuccess(Object o) {
                ChecksumResponse checksumResponse = (ChecksumResponse) o;
                if (checksumResponse != null) {
                    paytmChecsumView.onChecksumSuccess(checksumResponse.getChecksumHash());
                }
            }

            @Override
            public void onFailure(Error error) {
                paytmChecsumView.onChecksumFailure();
            }
        }, ChecksumResponse.class);
    }
}
