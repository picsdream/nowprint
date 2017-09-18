package com.picsdream.picsdreamsdk.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.picsdream.picsdreamsdk.R;
import com.picsdream.picsdreamsdk.model.network.PurchaseResponse;
import com.picsdream.picsdreamsdk.model.network.UploadPhotoResponse;
import com.picsdream.picsdreamsdk.presenter.PurchasePresenter;
import com.picsdream.picsdreamsdk.util.NavigationUtil;
import com.picsdream.picsdreamsdk.util.SaneToast;
import com.picsdream.picsdreamsdk.util.SharedPrefsUtil;
import com.picsdream.picsdreamsdk.util.Utils;
import com.picsdream.picsdreamsdk.view.PurchaseView;

/**
 * Authored by vipulkumar on 12/09/17.
 */

public class PurchaseActivity extends BaseActivity implements PurchaseView, View.OnClickListener {
    private ViewGroup confirmationLayout, loadingLayout, retryLayout;
    private PurchasePresenter purchasePresenter;
    private ViewGroup proceedLayout;
    private TextView helpLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);
        purchasePresenter = new PurchasePresenter(this);
        initUi();
        makePurchaseCall();
//        makeUploadPhotoCall(SharedPrefsUtil.getTempJsonPurchase());
//        onUploadPhotoSuccess(null);
    }

    private void makePurchaseCall() {
        confirmationLayout.setVisibility(View.GONE);
        retryLayout.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);
        purchasePresenter.purchaseItem();
    }

    private void makeUploadPhotoCall(PurchaseResponse purchaseResponse) {
        confirmationLayout.setVisibility(View.GONE);
        retryLayout.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);
        purchasePresenter.uploadPhoto(purchaseResponse);
    }

    private void initUi() {
        loadingLayout = (ViewGroup) findViewById(R.id.loadingLayout);
        confirmationLayout = (ViewGroup) findViewById(R.id.confirmationLayout);
        retryLayout = (ViewGroup) findViewById(R.id.retryLayout);
        proceedLayout = (ViewGroup) findViewById(R.id.proceedLayout);
        helpLayout = (TextView) findViewById(R.id.tvHelp);
        confirmationLayout.setVisibility(View.GONE);
        retryLayout.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);
        helpLayout.setOnClickListener(this);
        proceedLayout.setOnClickListener(this);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onPurchaseFailure() {
        loadingLayout.setVisibility(View.GONE);
        retryLayout.setVisibility(View.VISIBLE);
        SaneToast.getToast("Some error occurred. Please try again").show();
        retryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makePurchaseCall();
            }
        });
    }

    @Override
    public void onPurchaseSuccess() {
    }

    @Override
    public void onUploadPhotoSuccess(UploadPhotoResponse uploadPhotoResponse) {
        loadingLayout.setVisibility(View.GONE);
        confirmationLayout.setVisibility(View.VISIBLE);
        retryLayout.setVisibility(View.GONE);
    }

    @Override
    public void onUploadPhotoFailure(final PurchaseResponse purchaseResponse) {
        loadingLayout.setVisibility(View.GONE);
        retryLayout.setVisibility(View.VISIBLE);
        SaneToast.getToast("Some error occurred. Please try again").show();
        retryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeUploadPhotoCall(purchaseResponse);
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.proceedLayout) {
            try {
                Class<?> c = Class.forName(SharedPrefsUtil.getReturnActivityName());
                Intent intent = new Intent(PurchaseActivity.this, c);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                NavigationUtil.startActivity(PurchaseActivity.this, intent);

            } catch (ClassNotFoundException ignored) {
            }
        } else if (view.getId() == R.id.tvHelp) {
            Utils.initiateHelp(PurchaseActivity.this);
        }
    }
}
