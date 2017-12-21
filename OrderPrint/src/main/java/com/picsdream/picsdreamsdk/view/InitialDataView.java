package com.picsdream.picsdreamsdk.view;

import com.picsdream.picsdreamsdk.model.network.InitialAppDataResponse;
import com.picsdream.picsdreamsdk.network.Error;

/**
 * Authored by vipulkumar on 02/09/17.
 */

public interface InitialDataView extends BaseView {
    void onStartLoading();
    void onStopLoading();
    void onDataFetchSuccess(InitialAppDataResponse initialAppDataResponse);
    void onDataFetchFailure(Error error);
}
