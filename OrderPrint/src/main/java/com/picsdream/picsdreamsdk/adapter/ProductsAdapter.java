package com.picsdream.picsdreamsdk.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.picsdream.picsdreamsdk.R;
import com.picsdream.picsdreamsdk.activity.PrefsActivity;
import com.picsdream.picsdreamsdk.model.Item;
import com.picsdream.picsdreamsdk.model.Order;
import com.picsdream.picsdreamsdk.util.Constants;
import com.picsdream.picsdreamsdk.util.NavigationUtil;
import com.picsdream.picsdreamsdk.util.SharedPrefsUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.PrefViewHolder>
        implements View.OnClickListener {
    private List<Item> productsList;
    private Context context;

    public ProductsAdapter(Context context, List<Item> productsList) {
        this.productsList = productsList;
        this.context = context;
    }

    @NonNull
    @Override
    public PrefViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);

        return new PrefViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final PrefViewHolder holder, int position) {
        setValues(holder);
    }

    private void setValues(final PrefViewHolder holder) {
        final Item product = productsList.get(holder.getAdapterPosition());
        holder.tvProductType.setText(product.getName());
        Picasso.with(context)
                .load(product.getImageThumb())
                .into(holder.ivImage);
        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Order order = SharedPrefsUtil.getOrder();
                order.setType(product.getType());
                SharedPrefsUtil.saveOrder(order);
                Intent intent = new Intent(context, PrefsActivity.class);
                intent.putExtra("tag", Constants.TAG_MEDIA);
                NavigationUtil.startActivity(context, intent);
            }
        });
    }

    public void updateList(List<Item> list){
        productsList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(@NonNull View v) {

    }

    static class PrefViewHolder extends RecyclerView.ViewHolder {
        ViewGroup rootLayout;
        TextView tvProductType;
        ImageView ivImage;

        private PrefViewHolder(@NonNull View view) {
            super(view);
            findViewById(view);
        }

        private void findViewById(@NonNull View view) {
            rootLayout = view.findViewById(R.id.root_layout);
            tvProductType = view.findViewById(R.id.tvItemType);
            ivImage = view.findViewById(R.id.ivImage);
        }
    }
}