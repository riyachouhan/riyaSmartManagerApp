package com.ddkcommunity.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ddkcommunity.R;
import com.ddkcommunity.fragment.send.summaryModel;
import com.ddkcommunity.model.PollingHistoryTransction;

import java.text.DecimalFormat;
import java.util.List;

import static com.ddkcommunity.utilies.dataPutMethods.ShowTransationHistory;

public class SummarAdapter extends RecyclerView.Adapter<SummarAdapter.MyViewHolder> {


    private List<summaryModel> data;
    private Activity activity;

    public SummarAdapter(List<summaryModel> data, Activity activity) {
        this.activity = activity;
        this.data = data;
    }

    public void updateData(List<summaryModel> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_summary, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        try {
            if (data.get(position).getHeadername() != null) {
                if (data.get(position).getTransaction_type().toString().equalsIgnoreCase("Cashout")) {
                    holder.tvOrderType.setText(data.get(position).getHeadername().toString());
                    Double amountnaim= Double.valueOf(data.get(position).getHeadreramount());
                    holder.tvAmount.setText(new DecimalFormat("##.####").format(amountnaim)+" DDK");
                    holder.tvAmount.setTextColor(ContextCompat.getColor(activity, R.color.colorApp));
                    holder.ivHistoryIcon.setImageResource(R.drawable.cashout);
                    holder.ivHistoryIcon.setBackground(ContextCompat.getDrawable(activity, R.drawable.card_cashout_subs));

                } else if (data.get(position).getTransaction_type().toString().equalsIgnoreCase("Subscription")) {
                    holder.tvOrderType.setText(data.get(position).getHeadername().toString());
                    Double amountnaim= Double.valueOf(data.get(position).getHeadreramount());
                    holder.tvAmount.setText(new DecimalFormat("##.####").format(amountnaim)+" USDT");
                    holder.ivHistoryIcon.setImageResource(R.drawable.subscription_new);
                    holder.tvAmount.setTextColor(ContextCompat.getColor(activity, R.color.darkblue));
                    holder.ivHistoryIcon.setBackground(ContextCompat.getDrawable(activity, R.drawable.card_blue_cornor));

                } else if (data.get(position).getTransaction_type().toString().equalsIgnoreCase("Buy")) {
                    holder.tvOrderType.setText(data.get(position).getHeadername().toString());
                    Double amountnaim= Double.valueOf(data.get(position).getHeadreramount());
                    holder.tvAmount.setText(new DecimalFormat("##.####").format(amountnaim)+" DDK");
                    holder.ivHistoryIcon.setImageResource(R.drawable.buy_icon);
                    holder.tvAmount.setTextColor(ContextCompat.getColor(activity, R.color.orange));
                    holder.ivHistoryIcon.setBackground(ContextCompat.getDrawable(activity, R.drawable.card_cornor_subscription));

                } else if (data.get(position).getTransaction_type().toString().equalsIgnoreCase("reward")) {

                    holder.tvOrderType.setText(data.get(position).getHeadername().toString());
                    Double amountnaim= Double.valueOf(data.get(position).getHeadreramount());
                    holder.tvAmount.setText(new DecimalFormat("##.####").format(amountnaim)+" DDK");
                    holder.ivHistoryIcon.setImageResource(R.drawable.rewards);
                    holder.tvAmount.setTextColor(ContextCompat.getColor(activity, R.color.colorGreen));
                    holder.ivHistoryIcon.setBackground(ContextCompat.getDrawable(activity, R.drawable.card_cornor_bg_green_5));

                } else if (data.get(position).getTransaction_type().toString().equalsIgnoreCase("Send"))
                {
                    holder.tvOrderType.setText(data.get(position).getHeadername().toString());
                    Double amountnaim= Double.valueOf(data.get(position).getHeadreramount());
                    holder.tvAmount.setText(new DecimalFormat("##.####").format(amountnaim)+" DDK");
                    holder.ivHistoryIcon.setImageResource(R.drawable.ic_send);
                    holder.tvAmount.setTextColor(ContextCompat.getColor(activity, R.color.darkblue));
                    holder.ivHistoryIcon.setBackground(ContextCompat.getDrawable(activity, R.drawable.card_blue_cornor));

                }else if(data.get(position).getTransaction_type().toString().equalsIgnoreCase("redeem"))
                {
                    holder.tvOrderType.setText(data.get(position).getHeadername().toString());
                    Double amountnaim= Double.valueOf(data.get(position).getHeadreramount());
                    holder.tvAmount.setText(new DecimalFormat("##.####").format(amountnaim)+" SAM");
                    holder.ivHistoryIcon.setImageResource(R.drawable.reedeem_icon);
                    holder.tvAmount.setTextColor(ContextCompat.getColor(activity, R.color.reedam));
                    holder.ivHistoryIcon.setBackground(ContextCompat.getDrawable(activity, R.drawable.card_reedeem));

                }else if(data.get(position).getTransaction_type().toString().equalsIgnoreCase("SAM Reward"))
                {
                    holder.tvOrderType.setText(data.get(position).getHeadername().toString());
                    Double amountnaim= Double.valueOf(data.get(position).getHeadreramount());
                    holder.tvAmount.setText(new DecimalFormat("##.####").format(amountnaim)+" SAM");
                    holder.ivHistoryIcon.setImageResource(R.drawable.rewards);
                    holder.tvAmount.setTextColor(ContextCompat.getColor(activity, R.color.reward_color));
                    holder.ivHistoryIcon.setBackground(ContextCompat.getDrawable(activity, R.drawable.card_reward));

                }
            }

         }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView  tvOrderType,tvAmount;
        public ImageView ivHistoryIcon;
        public LinearLayout viewclickdata;

        public MyViewHolder(View view) {
            super(view);
            tvOrderType=view.findViewById(R.id.tvOrderType);
            ivHistoryIcon = view.findViewById(R.id.ivHistoryIcon);
            tvAmount = view.findViewById(R.id.tvAmount);
        }
    }
}
