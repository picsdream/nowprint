
package com.picsdream.picsdreamsdk.model.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.picsdream.picsdreamsdk.model.Country;
import com.picsdream.picsdreamsdk.model.Item;
import com.picsdream.picsdreamsdk.model.Region;
import com.picsdream.picsdreamsdk.model.payment.Gateways;

import java.util.List;

public class InitialAppDataResponse {

    @SerializedName("dis")
    @Expose
    private Integer dis;
    @SerializedName("regions")
    @Expose
    private List<Region> regions = null;
    @SerializedName("delivery")
    @Expose
    private String delivery;
    @SerializedName("contact")
    @Expose
    private String contact;
    @SerializedName("cart_support")
    @Expose
    private String cartSupport;
    @SerializedName("address_support")
    @Expose
    private String addressSupport;
    @SerializedName("payment_support")
    @Expose
    private String paymentSupport;
    @SerializedName("items")
    @Expose
    private List<Item> items = null;
    @SerializedName("gateways")
    @Expose
    private Gateways gateways;
    @SerializedName("countries")
    @Expose
    private List<Country> countries = null;

    public Integer getDis() {
        return dis;
    }

    public void setDis(Integer dis) {
        this.dis = dis;
    }

    public List<Region> getRegions() {
        return regions;
    }

    public void setRegions(List<Region> regions) {
        this.regions = regions;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getCartSupport() {
        return cartSupport;
    }

    public void setCartSupport(String cartSupport) {
        this.cartSupport = cartSupport;
    }

    public String getAddressSupport() {
        return addressSupport;
    }

    public void setAddressSupport(String addressSupport) {
        this.addressSupport = addressSupport;
    }

    public String getPaymentSupport() {
        return paymentSupport;
    }

    public void setPaymentSupport(String paymentSupport) {
        this.paymentSupport = paymentSupport;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Gateways getGateways() {
        return gateways;
    }

    public void setGateways(Gateways gateways) {
        this.gateways = gateways;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

}
