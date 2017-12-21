package com.picsdream.picsdreamsdk.view;

import com.picsdream.picsdreamsdk.model.network.DefaultResponse;

/**
 * Created by Ankur on 30-Nov-17.
 */

public interface CallView extends BaseView {
    public void onRequestCallSuccess(DefaultResponse defaultResponse);
    public void onRequestCallError();
}
