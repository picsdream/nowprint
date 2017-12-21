package com.picsdream.picsdreamsdk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Order {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("medium")
    @Expose
    private String medium;
    @SerializedName("medium_text")
    @Expose
    private String mediumText;
    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("total_cost")
    @Expose
    private float totalCost;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("tax")
    @Expose
    private String tax;
    @SerializedName("final_cost")
    @Expose
    private String finalCost;
    @SerializedName("total_paid")
    @Expose
    private String totalPaid;

    public String getShipping() {
        return shipping;
    }

    public void setShipping(String shipping) {
        this.shipping = shipping;
    }

    @SerializedName("shipping")
    @Expose
    private String shipping;
    private int costBeforeTax;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getMediumText() {
        return mediumText;
    }

    public void setMediumText(String mediumText) {
        this.mediumText = mediumText;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public float getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(float totalCost) {
        this.totalCost = totalCost;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getFinalCost() {
        return finalCost;
    }

    public void setFinalCost(String finalCost) {
        this.finalCost = finalCost;
    }

    public String getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(String totalPaid) {
        this.totalPaid = totalPaid;
    }

    public int getCostBeforeTax() {
        return costBeforeTax;
    }

    public void setCostBeforeTax(int costBeforeTax) {
        this.costBeforeTax = costBeforeTax;
    }
}