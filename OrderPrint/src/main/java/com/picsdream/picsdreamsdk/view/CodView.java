package com.picsdream.picsdreamsdk.view;

import com.picsdream.picsdreamsdk.model.CodResponse;
import com.picsdream.picsdreamsdk.network.Error;

/**
 * Authored by vipulkumar on 31/10/17.
 */

public interface CodView extends BaseView {
    void onStartLoading();
    void onStopLoading();
    void onCodSuccess(CodResponse codResponse);
    void onCodFailure(Error error);
}
