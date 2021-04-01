package com.ddkcommunity.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.ddkcommunity.R;
import com.ddkcommunity.model.ReferalSubWalletListModel;
import com.ddkcommunity.model.ReferralPayout;

import java.util.List;
import java.util.Locale;

public class ReferralPayoutDetailsAdapter extends RecyclerView.Adapter<ReferralPayoutDetailsAdapter.MyViewHolder> {


    private List<ReferalSubWalletListModel.Datum> data;
    private Context activity;

    public ReferralPayoutDetailsAdapter(List<ReferalSubWalletListModel.Datum> data, Context activity) {
        this.activity = activity;
        this.data = data;
    }

    public void updateData(List<ReferalSubWalletListModel.Datum> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_payout_details, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.tvPayout.setText(String.format(Locale.getDefault(), "%.4f",  data.get(position).getPayouts()));
        holder.tvDDkAddress.setText(data.get(position).getWalletsAddress());
        holder.tvAmtLend.setText("₮");
        holder.tvsno.setText((position+1)+"");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvsno,tvDDkAddress,tvAmtLend,tvPayout;

        public MyViewHolder(View view) {
            super(view);
            tvsno=view.findViewById(R.id.tvsno);
            tvDDkAddress = view.findViewById(R.id.tvDDk);
            tvAmtLend = view.findViewById(R.id.tvAmtLend);
            tvPayout = view.findViewById(R.id.tvPayout);

        }
    }
}
