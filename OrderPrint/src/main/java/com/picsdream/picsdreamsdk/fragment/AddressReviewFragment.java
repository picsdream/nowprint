package com.picsdream.picsdreamsdk.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.picsdream.picsdreamsdk.R;
import com.picsdream.picsdreamsdk.activity.AddressActivity;
import com.picsdream.picsdreamsdk.activity.PaymentActivity;
import com.picsdream.picsdreamsdk.application.ContextProvider;
import com.picsdream.picsdreamsdk.model.CodResponse;
import com.picsdream.picsdreamsdk.model.request.Address;
import com.picsdream.picsdreamsdk.network.Error;
import com.picsdream.picsdreamsdk.presenter.CodPresenter;
import com.picsdream.picsdreamsdk.util.NavigationUtil;
import com.picsdream.picsdreamsdk.util.SaneToast;
import com.picsdream.picsdreamsdk.util.SharedPrefsUtil;
import com.picsdream.picsdreamsdk.util.Utils;
import com.picsdream.picsdreamsdk.view.CodView;

/**
 * Authored by vipulkumar on 28/08/17.
 */

public class AddressReviewFragment extends BaseFragment implements View.OnClickListener, CodView {
    private TextView tvChangeAddress;
    private TextView tvFullName, tvAddress;
    private CodPresenter codPresenter;
    private ProgressBar progressBar;
    private ViewGroup paymentLayout;
    private TextView tvPayOnline;
    private ViewGroup payCodLayout, payOnlineLayout;

    public static AddressReviewFragment newInstance() {
        Bundle args = new Bundle();
        AddressReviewFragment fragment = new AddressReviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_address_review, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvChangeAddress = view.findViewById(R.id.tv_change);
        tvFullName = view.findViewById(R.id.tvFullName);
        tvAddress = view.findViewById(R.id.tvAddress);
        tvChangeAddress.setOnClickListener(this);
        progressBar = view.findViewById(R.id.progressBar);
        paymentLayout = view.findViewById(R.id.paymentLayout);
        payCodLayout = view.findViewById(R.id.payCodLayout);
        payOnlineLayout = view.findViewById(R.id.payOnlineLayout);
        tvPayOnline = view.findViewById(R.id.tvPayOnlineHeading);
        paymentLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        codPresenter = new CodPresenter(this);

        codPresenter.isCodAvailable();
        setValues();
    }

    private void setValues() {
        Address address = SharedPrefsUtil.getAddress();
        tvFullName.setText(Utils.capitalizeFirstCharacterOfEveryWord(address.getName()));

        String addressString = address.getAddress() + "\n" +
                address.getCity() + ", " +
                address.getState() + "\n" +
                address.getPincode() + "\n" +
                address.getMobile();

        tvAddress.setText(addressString);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_change) {
            ContextProvider.trackEvent(APP_KEY, "Change address", "");
            ((AddressActivity)getActivity()).setupAddressFragment(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ContextProvider.trackScreenView("Review Address");
    }

    @Override
    public void onStartLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStopLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onCodSuccess(CodResponse codResponse) {
        paymentLayout.setVisibility(View.VISIBLE);
        if (SharedPrefsUtil.getCountry().getCountry().equalsIgnoreCase("India")) {
            tvPayOnline.setText("Pay with PayTm");
        } else {
            tvPayOnline.setText("Pay with PayPal");
        }

        if (codResponse.getEnabled() == 1) {
            payCodLayout.setVisibility(View.VISIBLE);
        } else {
            payCodLayout.setVisibility(View.GONE);
        }

        payOnlineLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PaymentActivity.class);
                intent.putExtra("isCod", false);
                NavigationUtil.startActivity(getContext(), intent);
            }
        });

        payCodLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setMessage("Proceed with COD?")
                        .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ContextProvider.trackEvent(SharedPrefsUtil.getAppKey(),
                                        "Proceed with COD", "COD dialog confirm");
                                Intent intent = new Intent(getActivity(), PaymentActivity.class);
                                intent.putExtra("isCod", true);
                                NavigationUtil.startActivity(getContext(), intent);
                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ContextProvider.trackEvent(SharedPrefsUtil.getAppKey(),
                                        "COD cancelled", "COD dialog cancel");
                                dialogInterface.dismiss();
                            }
                        }).show();
            }
        });
    }

    @Override
    public void onCodFailure(Error error) {
        SaneToast.getToast(error.getErrorBody()).show();
    }
}
