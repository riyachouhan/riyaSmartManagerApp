package com.ddkcommunity.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ddkcommunity.Constant;
import com.ddkcommunity.R;
import com.ddkcommunity.dialog.ReferralLevelDetailsDialog;
import com.ddkcommunity.model.ReferalSubWalletListModel;
import com.ddkcommunity.model.SubModelReeralList;
import com.ddkcommunity.model.referral.ChainData;
import com.ddkcommunity.model.referral.ReferralChain;
import com.ddkcommunity.model.referral.ReferralLevelList;
import com.ddkcommunity.model.referral.refrrelchainRewardAdapter;
import com.ddkcommunity.utilies.AppConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class ReferralChainAdapterNew extends RecyclerView.Adapter<ReferralChainAdapterNew.MyViewHolder> {

    private final Context mContext;
    private List<refrrelchainRewardAdapter.Refferallist> data;
    private SetOnItemClick setOnItemClick;

    public ReferralChainAdapterNew(List<refrrelchainRewardAdapter.Refferallist> data, Context mContext) {
       this.data = data;
        this.mContext = mContext;
    }

    public void updateData(List<refrrelchainRewardAdapter.Refferallist> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_referral_chain_level, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
//        int i = position + 1;
        holder.lblLevelCount.setText(data.get(position).getLevel());
        holder.tvCountryName.setText(data.get(position).getCountry().getCountry());
        holder.tvName.setText(data.get(position).getName());
        Log.d("adapter call",position+"");
        holder.tvAmount.setText(String.format(Locale.getDefault(), "%.4f", data.get(position).getAmount()));

        AssetManager assetManager = mContext.getAssets();
        try {
            InputStream ims = assetManager.open(data.get(position).getCountry().getSortname().toLowerCase()+".png");
            Drawable d = Drawable.createFromStream(ims, null);
            holder.ivFlag.setImageDrawable(d);
        } catch (IOException ex) {
            return;
        }

        if (data.get(position).getAmount() == 0 || data.get(position).getAmount() == 0.0) {
            holder.lytMain.setBackgroundColor(ContextCompat.getColor(mContext, R.color.neworange));
            holder.lblLevelCount.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            holder.tvCountryName.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            holder.tvName.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            holder.tvAmount.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        } else {
            holder.lytMain.setBackgroundColor(Color.WHITE);
            holder.lblLevelCount.setTextColor(ContextCompat.getColor(mContext, R.color.colorBlackText));
            holder.tvCountryName.setTextColor(ContextCompat.getColor(mContext, R.color.colorBlackText));
            holder.tvName.setTextColor(ContextCompat.getColor(mContext, R.color.colorBlackText));
            holder.tvAmount.setTextColor(ContextCompat.getColor(mContext, R.color.colorBlackText));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView lblLevelCount, tvName, tvCountryName, tvAmount;
        public ImageView ivFlag;
        public LinearLayout lytMain;

        public MyViewHolder(View view) {
            super(view);
            lytMain = view.findViewById(R.id.lytMain);
            tvCountryName = view.findViewById(R.id.tvCountryName);
            tvAmount = view.findViewById(R.id.tvAmount);
            tvName = view.findViewById(R.id.tvName);
            lblLevelCount = view.findViewById(R.id.tvLevel);
            ivFlag = view.findViewById(R.id.ivFlag);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(mContext, "" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                  //  ReferralLevelDetailsDialog cdd = new ReferralLevelDetailsDialog((Activity) mContext, data.get(getAdapterPosition()));
                   // cdd.show();
                }
            });

        }
    }

    public interface SetOnItemClick {
        public void onItemClick(int position);
    }
}


/*  private void getSubListData(final ProgressBar progressBar,final RecyclerView walletdlistview, String user_id, String transactionid)
    {
        progressBar.setVisibility(View.VISIBLE);
        HashMap<String, String> hm = new HashMap<>();
        hm.put("transaction_id", transactionid);
        hm.put("user_id",user_id);
        Log.d("referal para",hm.toString());
        AppConfig.getLoadInterface().getSubWalletReferralList(AppConfig.getStringPreferences(mContext, Constant.JWTToken), hm).enqueue(new Callback<ReferalSubWalletListModel>()
        {
            @Override
            public void onResponse(Call<ReferalSubWalletListModel> call, Response<ReferalSubWalletListModel> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful())
                {
                    if (response.body().getStatus() == 1)
                    {
                        if (response.body().getData() != null && response.body().getData().size() > 0)
                        {
                            walletlisr=new ArrayList<>();
                            walletlisr.addAll(response.body().getData());
                            ReferralPayoutDetailsAdapter mAdapter = new ReferralPayoutDetailsAdapter(walletlisr, mContext);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
                            walletdlistview.setLayoutManager(layoutManager);
                            walletdlistview.setItemAnimator(new DefaultItemAnimator());
                            walletdlistview.setAdapter(mAdapter);

                        } else {
                            AppConfig.showToast("Data Not Available");
                        }
                    }else
                    {
                        AppConfig.showToast(response.body().getMsg());
                    }
                } else
                {
                    progressBar.setVisibility(View.GONE);
                    AppConfig.showToast("Server error");
                }
            }

            @Override
            public void onFailure(Call<ReferalSubWalletListModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });

    }
*/
   /* public interface SetOnItemClick {
        public void onItemClick(int position);
    }
}
*/