package com.picsdream.picsdreamsdk.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.picsdream.picsdreamsdk.R;
import com.picsdream.picsdreamsdk.activity.PrefsActivity;
import com.picsdream.picsdreamsdk.activity.ProductsActivity;
import com.picsdream.picsdreamsdk.application.ContextProvider;
import com.picsdream.picsdreamsdk.model.Item;
import com.picsdream.picsdreamsdk.model.Notification;
import com.picsdream.picsdreamsdk.model.Order;
import com.picsdream.picsdreamsdk.util.Constants;
import com.picsdream.picsdreamsdk.util.NavigationUtil;
import com.picsdream.picsdreamsdk.util.SharedPrefsUtil;
import com.picsdream.picsdreamsdk.util.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Ankur on 02-Dec-17.
 */

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.PrefViewHolder> implements View.OnClickListener {
    private List<Notification> notificationsList;
    private Context context;

    public NotificationsAdapter(Context context, List<Notification> notificationsList) {
        this.notificationsList= notificationsList;
        this.context = context;
    }

    @NonNull
    @Override
    public PrefViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View notificationView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new PrefViewHolder(notificationView);
    }

    @Override
    public void onBindViewHolder(PrefViewHolder holder, int position) {
        setValues(holder);
    }

    private void setValues(final PrefViewHolder holder) {
        final Notification notification = notificationsList.get(holder.getAdapterPosition());
        holder.tvHeading.setText(notification.getTitle());
        holder.tvDesc.setText(notification.getMsg());
        holder.tvDate.setText(notification.getCreatedAt());
        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(notification.getTarget().equals("app")) {
                    NavigationUtil.startActivity(context, ProductsActivity.class);
                } else {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(notification.getLink()));
                    context.startActivity(intent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

    @Override
    public void onClick(View v) {

    }

    static class PrefViewHolder extends RecyclerView.ViewHolder {
        ViewGroup rootLayout;
        ImageView ivImage;
        TextView tvHeading, tvDesc, tvDate;

        private PrefViewHolder(@NonNull View view) {
            super(view);
            findViewById(view);
        }

        private void findViewById(@NonNull View view) {
            rootLayout = view.findViewById(R.id.root_layout);
            tvHeading = view.findViewById(R.id.tvHeading);
            tvDesc = view.findViewById(R.id.tvDesc);
            tvDate = view.findViewById(R.id.tvDate);
        }
    }
}
