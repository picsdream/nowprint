package com.picsdream.picsdreamsdk.network;

/**
 * Authored by vipulkumar on 15/05/17.
 */

public class Error {
    private String errorTitle;
    private String errorBody;

    public boolean isShowTryAgain() {
        return showTryAgain;
    }

    public void setShowTryAgain(boolean showTryAgain) {
        this.showTryAgain = showTryAgain;
    }

    private boolean showTryAgain;

    public String getErrorTitle() {
        return errorTitle;
    }

    public void setErrorTitle(String errorTitle) {
        this.errorTitle = errorTitle;
    }

    public String getErrorBody() {
        return errorBody;
    }

    public void setErrorBody(String errorBody) {
        this.errorBody = errorBody;
    }

    public String getErrorImageUrl() {
        return errorImageUrl;
    }

    public void setErrorImageUrl(String errorImageUrl) {
        this.errorImageUrl = errorImageUrl;
    }

    public int getErrorImageResource() {
        return errorImageResource;
    }

    public void setErrorImageResource(int errorImageResource) {
        this.errorImageResource = errorImageResource;
    }

    private String errorImageUrl;
    private int errorImageResource;
}
