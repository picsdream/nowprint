package com.picsdream.picsdreamsdk.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.picsdream.picsdreamsdk.R;
import com.picsdream.picsdreamsdk.activity.OnMobileCoverSelector;
import com.picsdream.picsdreamsdk.activity.SearchMobileCoverFragment;
import com.picsdream.picsdreamsdk.model.Medium;

import java.util.List;

public class SearchMobileCoverAdapter extends RecyclerView.Adapter<SearchMobileCoverAdapter.PrefViewHolder>
        implements View.OnClickListener {
    private List<Medium> mediumList;
    private Context context;
    private OnMobileCoverSelector onMobileCoverSelector;
    private SearchMobileCoverFragment searchMobileCoverFragment;

    public SearchMobileCoverAdapter(Context context, List<Medium> mediumList,
                                    OnMobileCoverSelector onMobileCoverSelector, SearchMobileCoverFragment searchMobileCoverFragment) {
        this.mediumList = mediumList;
        this.context = context;
        this.onMobileCoverSelector = onMobileCoverSelector;
        this.searchMobileCoverFragment = searchMobileCoverFragment;
    }

    @NonNull
    @Override
    public PrefViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mobile_cover, parent, false);

        return new PrefViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final PrefViewHolder holder, int position) {
        setValues(holder);
    }

    private void setValues(final PrefViewHolder holder) {
        final Medium medium = mediumList.get(holder.getAdapterPosition());
        holder.tvCountryName.setText(medium.getText());
        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMobileCoverSelector.onMobileCoverSelected(mediumList.get(holder.getAdapterPosition()));
                searchMobileCoverFragment.dismiss();
            }
        });
    }

    public void updateList(List<Medium> list){
        mediumList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mediumList.size();
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
        TextView tvCountryName;

        private PrefViewHolder(@NonNull View view) {
            super(view);
            findViewById(view);
        }

        private void findViewById(@NonNull View view) {
            rootLayout = view.findViewById(R.id.root_layout);
            tvCountryName = view.findViewById(R.id.tvName);
        }
    }
}