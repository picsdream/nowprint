package com.picsdream.picsdreamsdk.view;

import com.picsdream.picsdreamsdk.model.network.PurchaseResponse;
import com.picsdream.picsdreamsdk.model.network.UploadPhotoResponse;

/**
 * Authored by vipulkumar on 02/09/17.
 */

public interface PurchaseView extends BaseView {
    void onPurchaseFailure();
    void onPurchaseSuccess();
    void onUploadPhotoSuccess(UploadPhotoResponse uploadPhotoResponse);
    void onUploadPhotoFailure(PurchaseResponse purchaseResponse);
}
