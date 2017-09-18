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
import com.picsdream.picsdreamsdk.model.Coupon;
import com.picsdream.picsdreamsdk.model.Item;
import com.picsdream.picsdreamsdk.model.Order;
import com.picsdream.picsdreamsdk.model.Region;
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
    private TextView tvProductType, tvProductPrice, tvInitialPrice, tvDiscount, tvTax, tvPayableAmount, tvProductSize;
    private String orderType, size, delivaryString;
    private int costBeforeTax, totalCost, discount, tax, finalCost;
    private InitialAppDataResponse initialAppDataResponse;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_order);
        initialAppDataResponse = SharedPrefsUtil.getInitialDataResponse();
        setupUi();
    }

    private void setupUi() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ivProductImage = (ImageView) findViewById(R.id.ivProductImage);
        tvProductType = (TextView) findViewById(R.id.tvProductType);
        tvProductPrice = (TextView) findViewById(R.id.tvProductPrice);
        tvInitialPrice = (TextView) findViewById(R.id.tvInitialPrice);
        tvDiscount = (TextView) findViewById(R.id.tvDiscount);
        tvTax = (TextView) findViewById(R.id.tvTax);
        tvPayableAmount = (TextView) findViewById(R.id.tvPayableAmount);
        tvProductSize = (TextView) findViewById(R.id.tvProductSize);
        tvChangeDiscount = (TextView) findViewById(R.id.tvChangeDiscount);
        setupToolbar(toolbar);

        proceedLayout = (ViewGroup) findViewById(R.id.proceedLayout);
        proceedLayout.setOnClickListener(this);

        setOrderProperties(0f);
        manageDiscount();
    }

    private void manageDiscount() {
        tvChangeDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetDialog dialog = new BottomSheetDialog(ReviewOrderActivity.this);
                dialog.setContentView(R.layout.view_bottomsheet);
                dialog.show();

                final EditText etPromoCode = (EditText) dialog.findViewById(R.id.etPromoCode);
                LinearLayout applyPromoLayout = (LinearLayout) dialog.findViewById(R.id.applyPromoLayout);
                final TextView tvPromoError = (TextView) dialog.findViewById(R.id.tvPromoError);
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
                                    dialog.dismiss();
                                }
                            }
                        } else {
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

        totalCost = order.getTotalCost();
        discount = getDiscount(totalCost, discountPer);
        costBeforeTax = Utils.getDiscountedPrice(totalCost, (int) discountPer);

        orderType = getProductType(initialAppDataResponse, order);
        size = order.getSize();

        tax = (costBeforeTax * region.getNum()) / 100;

        finalCost = getPayablePrice(costBeforeTax, tax);

        delivaryString = getFormattedDeliveryDate(initialAppDataResponse.getDelivery());

        order.setSize(String.valueOf(size));
        order.setCurrency(region.getCurrency());
        order.setFinalCost(String.valueOf(finalCost));
        order.setDiscount(String.valueOf(discount));
        order.setTax(String.valueOf(tax));
        order.setTotalCost(totalCost);
        order.setTotalPaid(String.valueOf(finalCost));
        SharedPrefsUtil.saveOrder(order);

        setValues();
    }

    private void setValues() {
        Picasso.with(this)
                .load(SharedPrefsUtil.getImageUriString())
                .into(ivProductImage);

        tvProductType.setText(orderType);
        tvInitialPrice.setText(Utils.getFormattedPrice(totalCost));
        tvDiscount.setText(" - " + Utils.getFormattedPrice(discount));
        tvTax.setText(Utils.getFormattedPrice(tax));
        tvProductPrice.setText(Utils.getFormattedPrice(costBeforeTax));
        tvPayableAmount.setText(Utils.getFormattedPrice(finalCost));
        tvProductSize.setText(size);
    }

    private String getProductType(InitialAppDataResponse initialAppDataResponse, Order order) {
        return Utils.capitalizeFirstCharacterOfEveryWord(
                getItemNameFromType(initialAppDataResponse, order.getType()) + " " + order.getMedium());
    }

    private String getItemNameFromType(InitialAppDataResponse initialAppDataResponse, String type) {
        for (Item item : initialAppDataResponse.getItems()) {
            if (item.getType().equalsIgnoreCase(type)) {
                return item.getName();
            }
        }
        return "";
    }

    private int getPayablePrice(float finalPrice, float tax) {
        return (int) (finalPrice + tax);
    }

    private String getFormattedDeliveryDate(String delivery) {
        return "";
    }

    private int getInitialPriceByPercentage(float finalAmount, float discount) {
        return (int) ((finalAmount * 100) / (100 - discount));
    }

    private int getDiscount(float initialPrice, float discount) {
        return (int) ((initialPrice * discount) / 100);
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
        if (item.getItemId() == R.id.menu_help) {
            Utils.initiateHelp(this);
        }
        return super.onOptionsItemSelected(item);
    }
}
