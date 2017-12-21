package com.picsdream.picsdreamsdk.view;

import com.picsdream.picsdreamsdk.model.request.Address;

/**
 * Created by Ankur on 29-Nov-17.
 */

public interface AddressView extends BaseView {
    public void onAddressFetchSuccess(Address address);
    public void onAddressFetchError();
}
