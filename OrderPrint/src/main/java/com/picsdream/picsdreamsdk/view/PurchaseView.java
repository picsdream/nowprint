package com.picsdream.picsdreamsdk.view;

import com.picsdream.picsdreamsdk.model.network.PurchaseResponse;
import com.picsdream.picsdreamsdk.model.network.UploadPhotoResponse;

/**
 * Authored by vipulkumar on 02/09/17.
 */

public interface PurchaseView extends BaseView {
    void onPurchaseFailure();
    void onPurchaseSuccess(PurchaseResponse purchaseResponse);
    void onUploadPhotoSuccess(UploadPhotoResponse uploadPhotoResponse, String size);
    void onUploadPhotoFailure(PurchaseResponse purchaseResponse, String size);
}
