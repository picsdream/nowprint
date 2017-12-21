
package com.picsdream.picsdreamsdk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Medium extends SelectableItem {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("support_text")
    @Expose
    private String supportText = "";
    @SerializedName("support_sub_text")
    @Expose
    private String supportSubText = "";
    @SerializedName("img")
    @Expose
    private String img;
    @SerializedName("placeholder")
    @Expose
    private String placeholder;
    @SerializedName("prices")
    @Expose
    private List<Price> prices = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Price> getPrices() {
        return prices;
    }

    public void setPrices(List<Price> prices) {
        this.prices = prices;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public String getSupportText() {
        return supportText;
    }

    public void setSupportText(String supportText) {
        this.supportText = supportText;
    }

    public String getSupportSubText() {
        return supportSubText;
    }

    public void setSupportSubText(String supportSubText) {
        this.supportSubText = supportSubText;
    }
}
