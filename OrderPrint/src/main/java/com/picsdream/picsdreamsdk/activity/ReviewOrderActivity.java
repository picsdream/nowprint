package com.picsdream.picsdreamsdk.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.picsdream.picsdreamsdk.R;
import com.picsdream.picsdreamsdk.application.ContextProvider;
import com.picsdream.picsdreamsdk.model.Country;
import com.picsdream.picsdreamsdk.model.Coupon;
import com.picsdream.picsdreamsdk.model.Item;
import com.picsdream.picsdreamsdk.model.Order;
import com.picsdream.picsdreamsdk.model.Region;
import com.picsdream.picsdreamsdk.model.ShippingText;
import com.picsdream.picsdreamsdk.model.network.CouponsResponse;
import com.picsdream.picsdreamsdk.model.network.InitialAppDataResponse;
import com.picsdream.picsdreamsdk.util.NavigationUtil;
import com.picsdream.picsdreamsdk.util.SaneToast;
import com.picsdream.picsdreamsdk.util.SharedPrefsUtil;
import com.picsdream.picsdreamsdk.util.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Authored by vipulkumar on 29/08/17.
 */

public class ReviewOrderActivity extends BaseActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private ViewGroup proceedLayout;
    private TextView tvChangeDiscount;
    private ImageView ivProductImage;
    private TextView tvProductType, tvProductPrice, tvInitialPrice,
            tvDiscount, tvTax, tvPayableAmount, tvProductSize, tvDeliveryDate, tvShipping, tvShippingLabel;
    private String orderType, size, deliveryString;
    private float costBeforeTax, totalCost, discount, tax, finalCost, shipping;
    private InitialAppDataResponse initialAppDataResponse;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_order);
        initialAppDataResponse = SharedPrefsUtil.getInitialDataResponse();
        setupUi();
    }

    private void setupUi() {
        toolbar = findViewById(R.id.toolbar);
        ivProductImage = findViewById(R.id.ivProductImage);
        tvProductType = findViewById(R.id.tvProductType);
        tvProductPrice = findViewById(R.id.tvProductPrice);
        tvInitialPrice = findViewById(R.id.tvInitialPrice);
        tvDiscount = findViewById(R.id.tvDiscount);
        tvTax = findViewById(R.id.tvTax);
        tvPayableAmount = findViewById(R.id.tvPayableAmount);
        tvProductSize = findViewById(R.id.tvProductSize);
        tvChangeDiscount = findViewById(R.id.tvChangeDiscount);
        tvDeliveryDate = findViewById(R.id.tvDeliveryDate);
        tvShipping = findViewById(R.id.tvShipping);
        tvShippingLabel = findViewById(R.id.tvShippingLabel);
        setupToolbar(toolbar);

        proceedLayout = findViewById(R.id.proceedLayout);
        proceedLayout.setOnClickListener(this);

        setOrderProperties(0f);
        manageDiscount();
    }

    private void manageDiscount() {
        tvChangeDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContextProvider.trackEvent(APP_KEY, "Change promo", "");
                final BottomSheetDialog dialog = new BottomSheetDialog(ReviewOrderActivity.this);
                dialog.setContentView(R.layout.view_bottomsheet);
                dialog.show();

                final EditText etPromoCode = dialog.findViewById(R.id.etPromoCode);
                LinearLayout applyPromoLayout = dialog.findViewById(R.id.applyPromoLayout);
                final TextView tvPromoError = dialog.findViewById(R.id.tvPromoError);
                tvPromoError.setVisibility(View.GONE);
                applyPromoLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean validPromo = false;
                        tvPromoError.setVisibility(View.GONE);
                        String promoCode = etPromoCode.getText().toString();
                        CouponsResponse couponsResponse = SharedPrefsUtil.getCouponResponse();
                        if (couponsResponse != null && couponsResponse.getCoupons() !=
                                null && couponsResponse.getCoupons().size() > 0) {
                            List<Coupon> coupons = SharedPrefsUtil.getCouponResponse().getCoupons();
                            for (Coupon coupon : coupons) {
                                if (coupon.getCouponCode().equalsIgnoreCase(promoCode)) {
                                    validPromo = true;
                                    applyPromo(coupon);
                                    SaneToast.getToast("Promo applied").show();
                                    ContextProvider.trackEvent(APP_KEY, "Promo applied", "");
                                    dialog.dismiss();
                                }
                            }
                        } else {
                            ContextProvider.trackEvent(APP_KEY, "Wrong coupon code", "");
                            validPromo = false;
                        }
                        if (!validPromo) {
                            tvPromoError.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });
    }

    private void applyPromo(Coupon coupon) {
        setOrderProperties((float) (coupon.getNumValue() * SharedPrefsUtil.getRegion().getConversion()));
    }

    private void setOrderProperties(float discountPer) {
        Order order = SharedPrefsUtil.getOrder();
        Region region = SharedPrefsUtil.getRegion();
        if (discount == 0) {
            discountPer = initialAppDataResponse.getDis();
        }

        shipping = Utils.getConvertedPrice(getShipping());

        totalCost = order.getTotalCost();
        discount = getDiscount(totalCost, discountPer);
        costBeforeTax = Utils.getDiscountedPrice(totalCost, (int) discountPer);

        orderType = getProductType(initialAppDataResponse, order);
        size = order.getSize();

        tax = (costBeforeTax * region.getNum()) / 100;

        finalCost = getPayablePrice(costBeforeTax, tax, shipping);

        deliveryString = getFormattedDeliveryDate(initialAppDataResponse.getDelivery());


        order.setSize(String.valueOf(size));
        order.setCurrency(region.getCurrency());
        order.setFinalCost(String.valueOf(finalCost));
        order.setDiscount(String.valueOf(discount));
        order.setTax(String.valueOf(tax));
        order.setTotalCost(totalCost);
        order.setShipping(String.valueOf(shipping));
        SharedPrefsUtil.saveOrder(order);

        setValues();
    }

    private int getShipping() {
        Country currentCountry = SharedPrefsUtil.getCountry();
        for (Country country : initialAppDataResponse.getCountries()) {
            if (currentCountry.getCountry().equalsIgnoreCase(country.getCountry())) {
                return country.getShipping();
            }
        }
        return 0;
    }

    private String getShippingLabel() {
        Country currentCountry = SharedPrefsUtil.getCountry();
        for (ShippingText shippingText : initialAppDataResponse.getShippingTexts()) {
            if (currentCountry.getCountry().equalsIgnoreCase(shippingText.getName())) {
                return shippingText.getText();
            }
        }
        for (ShippingText shippingText : initialAppDataResponse.getShippingTexts()) {
            if (shippingText.getName().equalsIgnoreCase("default")) {
                return shippingText.getText();
            }
        }
        return "shipping";
    }

    private void setValues() {
        Picasso.with(this)
                .load(SharedPrefsUtil.getImageUriString())
                .into(ivProductImage);

        tvProductType.setText(orderType);
        tvInitialPrice.setText(Utils.getFormattedPrice(totalCost));
        tvDiscount.setText(" - " + Utils.getFormattedPrice(discount));
        tvTax.setText(Utils.getFormattedPrice(tax));
        tvShipping.setText(Utils.getFormattedPrice(shipping));
        tvShippingLabel.setText(getShippingLabel());
        tvProductPrice.setText(Utils.getFormattedPrice(costBeforeTax));
        tvPayableAmount.setText(Utils.getFormattedPrice(finalCost));
        tvProductSize.setText(size);
        tvDeliveryDate.setText(deliveryString);
    }

    private String getProductType(InitialAppDataResponse initialAppDataResponse, Order order) {
        return Utils.capitalizeFirstCharacterOfEveryWord(
                getItemNameFromType(initialAppDataResponse, order.getType()) + " " + order.getMediumText());
    }

    private String getItemNameFromType(InitialAppDataResponse initialAppDataResponse, String type) {
        for (Item item : initialAppDataResponse.getItems()) {
            if (item.getType().equalsIgnoreCase(type)) {
                return item.getName();
            }
        }
        return "";
    }

    private float getPayablePrice(float finalPrice, float tax, float shipping) {
        return Utils.roundOffFloat(finalPrice + tax + shipping);
    }

    private String getFormattedDeliveryDate(String delivery) {
        return Utils.formatDeliveryDateString(delivery);
    }

    private float getDiscount(float initialPrice, float discount) {
        return Utils.roundOffFloat((initialPrice * discount) / 100);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.proceedLayout) {
            NavigationUtil.startActivity(ReviewOrderActivity.this, AddressActivity.class);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_help, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Utils.onHelpItemsClicked(item, this, "Review Order");
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ContextProvider.trackScreenView("Review Order");
    }
}
