package com.ddkcommunity.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ddkcommunity.Constant;
import com.ddkcommunity.R;
import com.ddkcommunity.dialog.PayoutDetailsDialog;
import com.ddkcommunity.model.ReferalSubWalletListModel;
import com.ddkcommunity.model.ReferralPayout;
import com.ddkcommunity.model.SubModelReeralList;
import com.ddkcommunity.utilies.AppConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReferralPayoutLevelAdapter extends RecyclerView.Adapter<ReferralPayoutLevelAdapter.MyViewHolder> {


    private final Context mContext;
    private List<SubModelReeralList.Datum> data;
    private SetOnItemClick setOnItemClick;
    String transactionid;
    ArrayList<ReferalSubWalletListModel.Datum> walletlisr;

    public ReferralPayoutLevelAdapter(String transactionid,List<SubModelReeralList.Datum> data, Context mContext, SetOnItemClick setOnItemClick) {
        this.setOnItemClick = setOnItemClick;
        this.transactionid=transactionid;
        this.data = data;
        this.mContext = mContext;
    }

    public void updateData(List<SubModelReeralList.Datum> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_referral_payout_level, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.lblLevelCount.setText(data.get(position).getLevel());
//      holder.tvDaily5Per.setText("$"+data.get(position).daily);
        holder.tvName.setText(data.get(position).getName());
        holder.tvPerReward.setText(data.get(position).getRewardPer() + "%");
        //holder.tvRewards.setText("₮" + String.format(Locale.getDefault(), "%.4f", data.get(position).getReward()));
        //holder.tvTotalIn5Days.setText(String.format(Locale.getDefault(), "%.4f", data.get(position).getTotalPayout()));
        holder.tvRewards.setText("₮" + data.get(position).getReward()+"");
        holder.tvTotalIn5Days.setText(data.get(position).getTotalPayout().toString()+"");

        holder.payout_walletview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(holder.lytCollapse_sub.getVisibility()==View.VISIBLE)
                {
                    holder.subview.setVisibility(View.GONE);
                    holder.lytCollapse_sub.setVisibility(View.GONE);
                }else
                {
                    holder.subview.setVisibility(View.VISIBLE);
                    holder.lytCollapse_sub.setVisibility(View.VISIBLE);
                    String userid=data.get(position).getId();
                    getSubListData(holder.progressBar,holder.recycler_view_subchid,userid,transactionid);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView lblLevelCount, tvName, tvPerReward, tvDaily5Per, tvTotalIn5Days, tvRewards;
        public CheckBox checkBox;
        public View subview;
        public RecyclerView recycler_view_subchid;
        public LinearLayout payout_walletview,lytMain,lytCollapse_sub;
        public ProgressBar progressBar;
        public MyViewHolder(View view) {
            super(view);
            subview=view.findViewById(R.id.subview);
            progressBar=view.findViewById(R.id.progressBar);
            payout_walletview=view.findViewById(R.id.payout_walletview);
            lytCollapse_sub=view.findViewById(R.id.lytCollapse_sub);
            recycler_view_subchid=view.findViewById(R.id.recycler_view_subchid);
            lytMain = view.findViewById(R.id.lytMain);
            tvTotalIn5Days = view.findViewById(R.id.tvTotalIn5Days);
            tvDaily5Per = view.findViewById(R.id.tvDaily5Per);
            tvName = view.findViewById(R.id.tvName);
            lblLevelCount = view.findViewById(R.id.tvLevel);
            tvPerReward = view.findViewById(R.id.tvPerReward);
            tvRewards = view.findViewById(R.id.tvRewards);
        }
    }

    private void getSubListData(final ProgressBar progressBar,final RecyclerView walletdlistview, String user_id, String transactionid)
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

    public interface SetOnItemClick {
        public void onItemClick(int position);
    }
}
