package com.picsdream.picsdreamsdk.adapter;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
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
import com.picsdream.picsdreamsdk.application.ContextProvider;
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

    @SuppressLint("SetTextI18n")
    private void setValues(final PrefViewHolder holder) {
        final SelectableItem selectableItem = itemsList.get(holder.getAdapterPosition());
        InitialAppDataResponse initialAppDataResponse = SharedPrefsUtil.getInitialDataResponse();

        if (selectableItem instanceof Item) {
            Picasso.with(context)
                    .load(((Item) selectableItem).getImageThumb())
                    .fit().centerCrop()
                    .into(holder.ivImage);
            holder.ivImage.setVisibility(View.VISIBLE);
            holder.tvLabel.setText(((Item) selectableItem).getName());
            holder.tvLabel.setVisibility(View.VISIBLE);
            holder.tvSubLabel.setVisibility(View.INVISIBLE);
            holder.tvLabelOnImage.setVisibility(View.INVISIBLE);
        } else if (selectableItem instanceof Medium) {
            if (((Medium) selectableItem).getImg() != null) {
                Picasso.with(context)
                        .load(((Medium) selectableItem).getImg())
                        .fit().centerCrop()
                        .into(holder.ivImage);
                holder.ivImage.setVisibility(View.VISIBLE);
                holder.tvLabel.setVisibility(View.INVISIBLE);
                holder.tvSubLabel.setVisibility(View.INVISIBLE);
                holder.tvLabelOnImage.setVisibility(View.INVISIBLE);
            } else {
                holder.ivImage.setVisibility(View.INVISIBLE);
                holder.tvLabel.setVisibility(View.INVISIBLE);
                holder.tvSubLabel.setVisibility(View.INVISIBLE);
            }
            holder.tvLabelOnImage.setText(((Medium) selectableItem).getText());
        } else if (selectableItem instanceof Price) {
            holder.ivImage.setVisibility(View.INVISIBLE);
            holder.tvLabel.setVisibility(View.VISIBLE);
            holder.tvSubLabel.setVisibility(View.VISIBLE);
            holder.tvLabelOnImage.setVisibility(View.VISIBLE);
            holder.tvSubLabel.setPaintFlags(holder.tvSubLabel.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            float discountedPrice = Utils.getDiscountedPrice((int) ((Price) selectableItem).getPrice(),
                    initialAppDataResponse.getDis());
            holder.tvLabelOnImage.setText(Utils.capitalizeFirstCharacterOfEveryWord(((Price) selectableItem).getSize()));
            holder.tvSubLabel.setText(Utils.getFormattedPrice(
                    Utils.getConvertedPrice(((Price) selectableItem).getPrice())));
            holder.tvLabel.setText(Utils.getFormattedPrice(Utils.getConvertedPrice(discountedPrice)));
        }

        initFrame(holder, selectableItem);

        if (selectableItem instanceof Item ||
                (selectableItem instanceof Medium && ((Medium) selectableItem).getImg() != null)) {
            if (selectableItem.isSelected()) {
                holder.overlayView.setVisibility(View.INVISIBLE);
            } else {
                holder.overlayView.setVisibility(View.VISIBLE);
            }
        } else {
            holder.overlayView.setVisibility(View.INVISIBLE);
        }

        if (selectableItem.isSelected()) {
            createAnOrder(selectableItem);
            postSelectionToActivity(selectableItem);
            holder.tvLabel.setTextColor(context.getResources().getColor(R.color.picsDreamTextDark));
            holder.tvLabelOnImage.setTextColor(context.getResources().getColor(R.color.white));
            holder.tvSubLabel.setTextColor(context.getResources().getColor(R.color.picsDreamTextMedium));
            holder.itemCard.setCardElevation(20f);
            changeCardBackgroundColor(context.getResources().getColor(R.color.white),
                    Utils.fetchPrimaryColor(context),
                    holder.itemCard);
        } else {
            holder.tvLabel.setTextColor(context.getResources().getColor(R.color.picsDreamTextLight));
            holder.tvSubLabel.setTextColor(context.getResources().getColor(R.color.picsDreamTextLight));
            holder.tvLabelOnImage.setTextColor(context.getResources().getColor(R.color.picsDreamTextLight));
            holder.itemCard.setCardElevation(5f);
            holder.itemCard.setCardBackgroundColor(context.getResources().getColor(R.color.white));
        }

        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelection(selectableItem);
                notifyDataSetChanged();
                trackSelectionEvent(selectableItem);
            }
        });
    }

    private Item getSelectedItemType() {
        InitialAppDataResponse initialAppDataResponse = SharedPrefsUtil.getInitialDataResponse();
        Order order = SharedPrefsUtil.getOrder();
        for (Item item : initialAppDataResponse.getItems()) {
            if (item.getType().equalsIgnoreCase(order.getType())) {
                return item;
            }
        }
        return null;
    }

    private void changeCardBackgroundColor(int colorFrom, int colorTo, final CardView cardView) {
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(100);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                cardView.setCardBackgroundColor((int) animator.getAnimatedValue());
            }

        });
        colorAnimation.start();
    }

    private void trackSelectionEvent(SelectableItem selectableItem) {
        if (selectableItem instanceof Item) {
            ContextProvider.trackEvent(SharedPrefsUtil.getAppKey(), "Type", ((Item) selectableItem).getName());
        } else if (selectableItem instanceof Medium) {
            ContextProvider.trackEvent(SharedPrefsUtil.getAppKey(), "Medium", ((Medium) selectableItem).getName());
        } else if (selectableItem instanceof Price) {
            ContextProvider.trackEvent(SharedPrefsUtil.getAppKey(), "Size", ((Price) selectableItem).getSize());
        }
    }

    private void createAnOrder(SelectableItem selectableItem) {
        Order order = SharedPrefsUtil.getOrder();
        if (selectableItem instanceof Item) {
            order.setType(((Item) selectableItem).getType());
        } else if (selectableItem instanceof Medium) {
            order.setMedium(((Medium) selectableItem).getName());
            order.setMediumText(((Medium) selectableItem).getText());
        } else if (selectableItem instanceof Price) {
            order.setSize(((Price) selectableItem).getSize());
            order.setTotalCost(Utils.getConvertedPrice(((Price) selectableItem).getPrice()));
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
        TextView tvLabel;
        TextView tvLabelOnImage;
        TextView tvSubLabel;
        CardView itemCard;
        View overlayView;

        private PrefViewHolder(@NonNull View view) {
            super(view);
            findViewById(view);
        }

        private void findViewById(@NonNull View view) {
            rootLayout = view.findViewById(R.id.root_layout);
            ivImage = view.findViewById(R.id.iv_image);
            tvLabel = view.findViewById(R.id.tv_label);
            tvLabelOnImage = view.findViewById(R.id.tv_label_on_image);
            tvSubLabel = view.findViewById(R.id.tv_sub_label);
            itemCard = view.findViewById(R.id.item_card);
            overlayView = view.findViewById(R.id.overlay_view);
        }
    }
}