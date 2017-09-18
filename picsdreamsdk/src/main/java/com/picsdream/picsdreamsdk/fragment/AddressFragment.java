package com.picsdream.picsdreamsdk.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.picsdream.picsdreamsdk.R;
import com.picsdream.picsdreamsdk.model.request.Address;
import com.picsdream.picsdreamsdk.util.SharedPrefsUtil;
import com.picsdream.picsdreamsdk.util.ValidationUtil;

/**
 * Authored by vipulkumar on 28/08/17.
 */

public class AddressFragment extends BaseFragment {
    private android.widget.EditText etEmail;
    private android.widget.EditText etMobile;
    private android.widget.EditText etFullName;
    private android.widget.EditText etAddress;
    private android.widget.EditText etCity;
    private android.widget.EditText etState;
    private android.widget.EditText etPincode;
    private android.widget.EditText etCountry;

    public static AddressFragment newInstance() {
        Bundle args = new Bundle();
        AddressFragment fragment = new AddressFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_address, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViewsById(view);
        etCountry.setText(SharedPrefsUtil.getCountry().getCountry());
    }

    public boolean validateUserInfo() {
        if (ValidationUtil.validateEmail(etEmail)) {
            if (ValidationUtil.validatePhoneNumber(etMobile)) {
                if (ValidationUtil.validateFullName(etFullName)) {
                    if (ValidationUtil.validateAddress(etAddress)) {
                        if (ValidationUtil.validateCity(etCity)) {
                            if (ValidationUtil.validateState(etState)) {
                                if (ValidationUtil.validatePincode(etPincode)) {
                                    saveUserInfo();
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private void saveUserInfo() {
        Address address = new Address();
        address.setEmail(etEmail.getText().toString());
        address.setMobile(etMobile.getText().toString());
        address.setName(etFullName.getText().toString());
        address.setAddress(etAddress.getText().toString());
        address.setCity(etCity.getText().toString());
        address.setState(etState.getText().toString());
        address.setPincode(etPincode.getText().toString());
        SharedPrefsUtil.setAddress(address);
    }

    private void findViewsById(View view) {
        this.etCity = view.findViewById(R.id.etCity);
        this.etAddress = view.findViewById(R.id.etAddress);
        this.etState = view.findViewById(R.id.etState);
        this.etPincode = view.findViewById(R.id.etPincode);
        this.etFullName = view.findViewById(R.id.etFullName);
        this.etMobile = view.findViewById(R.id.etMobile);
        this.etEmail = view.findViewById(R.id.etEmail);
        this.etCountry = view.findViewById(R.id.etCountry);
    }
}
