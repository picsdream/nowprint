package com.picsdream.picsdreamsdk.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.picsdream.picsdreamsdk.R;
import com.picsdream.picsdreamsdk.activity.PrefsActivity;
import com.picsdream.picsdreamsdk.model.Country;
import com.picsdream.picsdreamsdk.util.NavigationUtil;
import com.picsdream.picsdreamsdk.util.SharedPrefsUtil;
import com.picsdream.picsdreamsdk.util.Utils;

import java.util.List;

public class CountriesAdapter extends RecyclerView.Adapter<CountriesAdapter.PrefViewHolder>
        implements View.OnClickListener {
    private List<Country> countryList;
    private Context context;

    public CountriesAdapter(Context context, List<Country> countryList) {
        this.countryList = countryList;
        this.context = context;
    }

    @NonNull
    @Override
    public PrefViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_country, parent, false);

        return new PrefViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final PrefViewHolder holder, int position) {
        setValues(holder);
    }

    private void setValues(final PrefViewHolder holder) {
        final Country country = countryList.get(holder.getAdapterPosition());
        holder.tvCountryName.setText(country.getCountry());
        holder.tvContinentName.setText(country.getContinent());
        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPrefsUtil.setCountry(country);
                Utils.setRegionAfterCountry();
                Intent intent = new Intent(context, PrefsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                NavigationUtil.startActivity(context, intent);
            }
        });
    }

    public void updateList(List<Country> list){
        countryList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return countryList.size();
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
        TextView tvCountryName, tvContinentName;

        private PrefViewHolder(@NonNull View view) {
            super(view);
            findViewById(view);
        }

        private void findViewById(@NonNull View view) {
            rootLayout = view.findViewById(R.id.root_layout);
            tvCountryName = view.findViewById(R.id.tvCountryName);
            tvContinentName = view.findViewById(R.id.tvContinentName);
        }
    }
}