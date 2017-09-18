package com.picsdream.picsdreamsdk.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.picsdream.picsdreamsdk.R;
import com.picsdream.picsdreamsdk.activity.PrefsActivity;
import com.picsdream.picsdreamsdk.model.Item;
import com.picsdream.picsdreamsdk.model.Medium;
import com.picsdream.picsdreamsdk.model.Order;
import com.picsdream.picsdreamsdk.model.Price;
import com.picsdream.picsdreamsdk.model.SelectableItem;
import com.picsdream.picsdreamsdk.model.network.InitialAppDataResponse;
import com.picsdream.picsdreamsdk.util.SharedPrefsUtil;
import com.picsdream.picsdreamsdk.util.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PrefsAdapter extends RecyclerView.Adapter<PrefsAdapter.PrefViewHolder>
        implements View.OnClickListener {
    private List<SelectableItem> itemsList;
    private Context context;

    public PrefsAdapter(Context context, List<SelectableItem> itemsList) {
        this.itemsList = itemsList;
        this.context = context;
    }

    @NonNull
    @Override
    public PrefViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pref, parent, false);

        return new PrefViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final PrefViewHolder holder, int position) {
        setValues(holder);
    }

    private void setValues(final PrefViewHolder holder) {
        final SelectableItem selectableItem = itemsList.get(holder.getAdapterPosition());
        InitialAppDataResponse initialAppDataResponse = SharedPrefsUtil.getInitialDataResponse();

        if (selectableItem instanceof Item) {
            holder.tvSubLabel.setVisibility(View.INVISIBLE);
            holder.tvLabel.setText(Utils.capitalizeFirstCharacterOfEveryWord(((Item) selectableItem).getName()));
        } else if (selectableItem instanceof Medium) {
            holder.tvSubLabel.setVisibility(View.INVISIBLE);
            holder.tvLabel.setText(Utils.capitalizeFirstCharacterOfEveryWord(((Medium) selectableItem).getName()));
        } else if (selectableItem instanceof Price) {
            holder.tvSubLabel.setVisibility(View.VISIBLE);
            float discountedPrice = Utils.getDiscountedPrice(((Price)selectableItem).getPrice(),
                    initialAppDataResponse.getDis());
            holder.tvSubLabel.setText(Utils.getFormattedPrice(discountedPrice));
            holder.tvLabel.setText(Utils.capitalizeFirstCharacterOfEveryWord(((Price) selectableItem).getSize()));
        }

        Picasso.with(context)
                .load(Uri.parse(SharedPrefsUtil.getImageUriString()))
                .fit().centerCrop()
                .into(holder.ivImage);
        Picasso.with(context)
                .load(Uri.parse(SharedPrefsUtil.getImageUriString()))
                .fit().centerCrop()
                .into(holder.ivImageAnim);

        initFrame(holder, selectableItem);

        if (!selectableItem.isSelected()) {
            holder.tvLabel.setTextColor(context.getResources().getColor(R.color.picsDreamTextLight));
            holder.tvSubLabel.setTextColor(context.getResources().getColor(R.color.picsDreamTextLight));
            holder.overlayView.setVisibility(View.VISIBLE);
            holder.itemCard.setCardElevation(2f);
            holder.ivImageAnim.setVisibility(View.INVISIBLE);
        } else {
            createAnOrder(selectableItem);
            postSelectionToActivity(selectableItem);
            revealAnimation(holder.ivImageAnim);
            holder.tvLabel.setTextColor(context.getResources().getColor(R.color.picsDreamTextDark));
            holder.tvSubLabel.setTextColor(Utils.fetchPrimaryColor(context));
            holder.overlayView.setVisibility(View.VISIBLE);
            holder.itemCard.setCardElevation(20f);
        }

        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelection(selectableItem);
                notifyDataSetChanged();
            }
        });
    }

    private void createAnOrder(SelectableItem selectableItem) {
        Order order = SharedPrefsUtil.getOrder();
        if (selectableItem instanceof Item) {
            order.setType(((Item) selectableItem).getType());
        } else if (selectableItem instanceof Medium){
            order.setMedium(((Medium) selectableItem).getName());
        } else if (selectableItem instanceof Price){
            order.setSize(((Price) selectableItem).getSize());
            order.setTotalCost(((Price) selectableItem).getPrice());
        }
        SharedPrefsUtil.saveOrder(order);
    }

    private void setSelection(SelectableItem itemToSelect) {
        for (SelectableItem selectableItem : itemsList) {
            if (selectableItem.equals(itemToSelect)) {
                selectableItem.setSelected(true);
            } else {
                selectableItem.setSelected(false);
            }
        }
    }

    private void postSelectionToActivity(SelectableItem selectableItem) {
        if (context instanceof PrefsActivity) {
            ((PrefsActivity) context).onItemSelected(selectableItem);
        }
    }

    private void initFrame(PrefViewHolder holder, SelectableItem item) {

    }

    private void revealAnimation(final View viewToReveal) {
        viewToReveal.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    viewToReveal.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    viewToReveal.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                Utils.revealFromCenter(context, viewToReveal);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
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
        ImageView ivImage;
        ImageView ivImageAnim;
        TextView tvLabel;
        TextView tvSubLabel;
        CardView itemCard;
        View overlayView;
        ViewGroup frameContainer;

        private PrefViewHolder(@NonNull View view) {
            super(view);
            findViewById(view);
        }

        private void findViewById(@NonNull View view) {
            rootLayout = view.findViewById(R.id.root_layout);
            ivImage = view.findViewById(R.id.iv_image);
            ivImageAnim = view.findViewById(R.id.iv_image_anim);
            tvLabel = view.findViewById(R.id.tv_label);
            tvSubLabel = view.findViewById(R.id.tv_sub_label);
            itemCard = view.findViewById(R.id.item_card);
            overlayView = view.findViewById(R.id.overlay_view);
            frameContainer = view.findViewById(R.id.frame_container);
        }
    }
}