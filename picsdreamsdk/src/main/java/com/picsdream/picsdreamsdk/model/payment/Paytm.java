
package com.picsdream.picsdreamsdk.model.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Paytm {

    @SerializedName("mid")
    @Expose
    private String mid;
    @SerializedName("website")
    @Expose
    private String website;
    @SerializedName("industry")
    @Expose
    private String industry;
    @SerializedName("channel")
    @Expose
    private String channel;
    @SerializedName("checksum")
    @Expose
    private Checksum checksum;

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Checksum getChecksum() {
        return checksum;
    }

    public void setChecksum(Checksum checksum) {
        this.checksum = checksum;
    }
}
