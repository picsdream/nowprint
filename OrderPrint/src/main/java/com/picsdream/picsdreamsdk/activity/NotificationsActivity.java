package com.picsdream.picsdreamsdk.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.picsdream.picsdreamsdk.R;
import com.picsdream.picsdreamsdk.adapter.NotificationsAdapter;
import com.picsdream.picsdreamsdk.model.Notification;
import com.picsdream.picsdreamsdk.model.network.InitialAppDataResponse;
import com.picsdream.picsdreamsdk.util.GridSpacingItemDecoration;
import com.picsdream.picsdreamsdk.util.SharedPrefsUtil;
import com.picsdream.picsdreamsdk.util.Utils;

import java.util.List;

public class NotificationsActivity extends BaseActivity {
    private Toolbar toolbar;
    private RecyclerView rvNotifications;
    InitialAppDataResponse initialAppDataResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        initialAppDataResponse = SharedPrefsUtil.getInitialDataResponse();
        showNotifications();
    }

    private void showNotifications() {
        toolbar = findViewById(R.id.toolbar);
        rvNotifications = findViewById(R.id.rvNotifications);
        setupToolbar(toolbar);

        Utils.setRecyclerViewProperties(this, rvNotifications, LinearLayoutManager.VERTICAL);
        List<Notification> notifications = initialAppDataResponse.getNotifications();
        SharedPrefsUtil.setInt("readNotifications", notifications.size());

        rvNotifications.setAdapter(new NotificationsAdapter(this, notifications));

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
        rvNotifications.setLayoutManager(layoutManager);
        rvNotifications.addItemDecoration(new GridSpacingItemDecoration(1, Utils.dpToPx(10), true));
        rvNotifications.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_help, menu);
        final MenuItem menuItem = menu.findItem(R.id.menu_notification);
        View actionView = menuItem.getActionView();
        TextView count = (TextView) actionView.findViewById(R.id.notification_pill);
        int size = Utils.notificationCount();
        count.setText(String.valueOf(size));
        if(size == 0)
            count.setVisibility(View.GONE);
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Utils.onHelpItemsClicked(item, this, "Notification Screen");
        return super.onOptionsItemSelected(item);
    }
}
