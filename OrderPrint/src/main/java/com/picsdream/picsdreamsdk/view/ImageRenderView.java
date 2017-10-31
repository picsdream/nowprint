package com.picsdream.picsdreamsdk.view;

import com.picsdream.picsdreamsdk.model.network.ImageRenderResponse;
import com.picsdream.picsdreamsdk.network.Error;

/**
 * Authored by vipulkumar on 01/11/17.
 */

public interface ImageRenderView extends BaseView {
    void onImageRenderFailure(Error error);
    void onImageRenderSuccess(ImageRenderResponse imageRenderResponse);
    void onStartLoading();
    void onStopLoading();
}
