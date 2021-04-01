package com.ddkcommunity.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.ddkcommunity.model.referral.ReferralLevelList;
import com.ddkcommunity.model.referralSublistModel;
import com.ddkcommunity.utilies.AppConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReferralChainLevelAdapter extends RecyclerView.Adapter<ReferralChainLevelAdapter.MyViewHolder> {


    private final Context mContext;
    private List<ReferralLevelList> data;
    private SetOnItemClick setOnItemClick;

    public ReferralChainLevelAdapter(List<ReferralLevelList> data, Context mContext, SetOnItemClick setOnItemClick) {
        this.setOnItemClick = setOnItemClick;
        this.data = data;
        this.mContext = mContext;
    }

    public void updateData(List<ReferralLevelList> data) {
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
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
//        int i = position + 1;
        holder.lblLevelCount.setText(data.get(position).level);
        holder.tvCountryName.setText(data.get(position).country.country);
        holder.tvName.setText(data.get(position).name);

        holder.tvAmount.setText(String.format(Locale.getDefault(), "%.4f", data.get(position).amount));

        AssetManager assetManager = mContext.getAssets();
        try {
            InputStream ims = assetManager.open(data.get(position).country.sortname.toLowerCase()+".png");
            Drawable d = Drawable.createFromStream(ims, null);
            holder.ivFlag.setImageDrawable(d);
        } catch (IOException ex) {
            return;
        }

        if (data.get(position).amount == 0 || data.get(position).amount == 0.0) {
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

        //........
        holder.lytMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userid=data.get(position).id;
                getSubListData(mContext,userid,data.get(position).name,data.get(position).email,data.get(position).mobile);
            }
        });

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


        }
    }

    public interface SetOnItemClick {
        public void onItemClick(int position);
    }

    private void getSubListData(Context activty,String user_id, final String name, final String email, final String mobile)
    {
        final ProgressDialog pd=new ProgressDialog(activty);
        pd.setMessage("Please wait......");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        HashMap<String, String> hm = new HashMap<>();
        hm.put("user_id",user_id);
        Log.d("referal para",hm.toString());
        AppConfig.getLoadInterface().getWalletReferrlList(AppConfig.getStringPreferences(mContext, Constant.JWTToken), hm).enqueue(new Callback<referralSublistModel>()
        {
            @Override
            public void onResponse(Call<referralSublistModel> call, Response<referralSublistModel> response) {
                pd.dismiss();
                if(response.isSuccessful())
                {
                    if (response.body().getStatus() == 1)
                    {
                        if (response.body().getData() != null && response.body().getData().size() > 0)
                        {
                            ArrayList<referralSublistModel.Datum> walletlisr=new ArrayList<>();
                            walletlisr.addAll(response.body().getData());
                            ReferralLevelDetailsDialog cdd = new ReferralLevelDetailsDialog((Activity) mContext,walletlisr,name,email,mobile);
                            cdd.show();
                        } else {
                            AppConfig.showToast("Data Not Available");
                        }
                    }else
                    {
                        AppConfig.showToast(response.body().getMsg());
                    }
                } else
                {
                    AppConfig.showToast("Server error");
                }
            }

            @Override
            public void onFailure(Call<referralSublistModel> call, Throwable t) {
             pd.dismiss();
            }
        });

    }

}
