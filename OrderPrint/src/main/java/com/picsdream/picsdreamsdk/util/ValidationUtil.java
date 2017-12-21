package com.picsdream.picsdreamsdk.util;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Authored by vipulkumar on 06/09/17.
 */

public class ValidationUtil {

    public static void showErrorOnTextField(@NonNull final EditText edText,
                                            @NonNull final String error) {
        edText.setError(null);
        edText.setError(error);
        edText.requestFocus();
        edText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                edText.setError(null);

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    public static boolean validatePincode(@NonNull EditText editText) {
        String target = editText.getText().toString();
        if(TextUtils.isEmpty(target)){
            showErrorOnTextField(editText, "Please enter a valid pincode");
            return false;
        }
        return true;
    }

    public static boolean validateState(@NonNull EditText editText) {
        String target = editText.getText().toString();
        if(TextUtils.isEmpty(target)){
            showErrorOnTextField(editText, "Please enter a valid state");
            return false;
        }
        return true;
    }

    public static boolean validateCity(@NonNull EditText editText) {
        String target = editText.getText().toString();
        if(TextUtils.isEmpty(target)){
            showErrorOnTextField(editText, "Please enter a valid city");
            return false;
        }
        return true;
    }

    public static boolean validateAddress(@NonNull EditText editText) {
        String target = editText.getText().toString();
        if(TextUtils.isEmpty(target)){
            showErrorOnTextField(editText, "Please enter a valid address");
            return false;
        }
        return true;
    }

    public static boolean validateFullName(@NonNull EditText editText) {
        String target = editText.getText().toString();
        if(TextUtils.isEmpty(target)){
            showErrorOnTextField(editText, "Please enter a valid name");
            return false;
        }
        return true;
    }

    public static boolean validatePhoneNumber(@NonNull EditText editText) {
        String target = editText.getText().toString();
        if(TextUtils.isEmpty(target)){
            showErrorOnTextField(editText, "Please enter a valid phone number");
            return false;
        }
        return true;
    }


    public static boolean validateEmail(EditText editText) {
        String target = editText.getText().toString();
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()) {
            return true;
        } else {
            showErrorOnTextField(editText, "Please enter a valid email address");
            return false;
        }
    }
}
