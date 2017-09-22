package com.picsdream.picsdreamsdk.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.picsdream.picsdreamsdk.R;
import com.picsdream.picsdreamsdk.activity.AddressActivity;
import com.picsdream.picsdreamsdk.application.ContextProvider;
import com.picsdream.picsdreamsdk.model.request.Address;
import com.picsdream.picsdreamsdk.util.SharedPrefsUtil;
import com.picsdream.picsdreamsdk.util.Utils;

/**
 * Authored by vipulkumar on 28/08/17.
 */

public class AddressReviewFragment extends BaseFragment implements View.OnClickListener {
    private TextView tvChangeAddress;
    private TextView tvFullName, tvAddress;

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
}
