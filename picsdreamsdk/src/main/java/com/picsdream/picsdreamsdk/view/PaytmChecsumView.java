package com.picsdream.picsdreamsdk.view;

/**
 * Authored by vipulkumar on 02/09/17.
 */

public interface PaytmChecsumView extends BaseView {
    void onChecksumSuccess(String checksum);
    void onChecksumFailure();
}
