package com.ddkcommunity.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ddkcommunity.R;
import com.ddkcommunity.model.ReferalSubWalletListModel;
import com.ddkcommunity.model.referral.WalletList;
import com.ddkcommunity.model.referralSublistModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ReferralWalletAddAdapter extends RecyclerView.Adapter<ReferralWalletAddAdapter.MyViewHolder> {


    private ArrayList<referralSublistModel.Datum> walletlisr;
    private Activity activity;

    public ReferralWalletAddAdapter(ArrayList<referralSublistModel.Datum> walletlisr, Activity activity) {
        this.activity = activity;
        this.walletlisr=walletlisr;
    }

    public void updateData(ArrayList<referralSublistModel.Datum> walletlisr) {
        this.walletlisr = walletlisr;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_referral_wallet_add, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        if(walletlisr.get(position).getType().equalsIgnoreCase("ddk"))
        {
            holder.button.setText("DDK Address: ");
        }else if(walletlisr.get(position).getType().equalsIgnoreCase("eth"))
        {
            holder.button.setText("ETH Address: ");
        }else if(walletlisr.get(position).getType().equalsIgnoreCase("btc"))
        {
            holder.button.setText("BTC Address: ");
        }else if(walletlisr.get(position).getType().equalsIgnoreCase("usdt"))
        {
            holder.button.setText("USDT Address: ");
        }
        holder.tvTotalLend.setVisibility(View.VISIBLE);
        holder.tvTotalLend.setText(walletlisr.get(position).getAmount());
        holder.tvDDkAddress.setText(walletlisr.get(position).getWalletsAddress());
    }

    @Override
    public int getItemCount() {
        return walletlisr.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView button,tvDDkAddress,tvTotalLend;

        public MyViewHolder(View view) {
            super(view);
            button=view.findViewById(R.id.button);
            tvDDkAddress = view.findViewById(R.id.tvDDkAddress);
            tvTotalLend = view.findViewById(R.id.tvTotalLend);

        }
    }
}
