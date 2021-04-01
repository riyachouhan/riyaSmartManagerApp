package com.ddkcommunity.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.ddkcommunity.R;
import com.ddkcommunity.model.projects.PoolingTransactionHistory;

import java.util.List;

public class PoolingTransactionAdapter extends RecyclerView.Adapter<PoolingTransactionAdapter.MyViewHolder> {


    private List<PoolingTransactionHistory.PoolingHistoryData> data;
    private SetOnItemClick setOnItemClick;
    private Context mContext;

    public PoolingTransactionAdapter(Context context,List<PoolingTransactionHistory.PoolingHistoryData> data, SetOnItemClick setOnItemClick) {
        this.setOnItemClick = setOnItemClick;
        this.data = data;
        this.mContext=context;
    }

    public void updateData(List<PoolingTransactionHistory.PoolingHistoryData> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pooling_trans, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        int i = position + 1;
//        holder.lblLevelCount.setText("" + i);
        if (data.get(position).transactionDate != null) {
            holder.tvTransactionDate.setText(data.get(position).transactionDate);
        }
        if (data.get(position).amount != null) {
            holder.tvAmount.setText("₮ " + data.get(position).amount);
        }
        if (data.get(position).ddk != null) {
            holder.tvDDk.setText(data.get(position).ddk);
        }
        if (data.get(position).conversion != null) {
            holder.tvConversion.setText("₮ " + data.get(position).conversion);
        }
        if (data.get(position).status != null) {
            holder.tvStatus.setText(data.get(position).status);
        }
        if (data.get(position).status != null && data.get(position).status.equalsIgnoreCase("lend")) {
            holder.tvStatus.setText("Active");
            holder.tvStatus.setTextColor(Color.RED);
            holder.tvStatus.setBackground(ContextCompat.getDrawable(mContext,R.drawable.status_bg_lightred));
        }
        if (data.get(position).status != null && data.get(position).status.equalsIgnoreCase("Reward")) {
            holder.tvStatus.setBackground(ContextCompat.getDrawable(mContext,R.drawable.status_bg_lightgreen));
            holder.tvStatus.setTextColor(ContextCompat.getColor(mContext,R.color.colorGreen));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTransactionDate, tvStatus, tvAmount, tvConversion, tvDDk;
//        public CheckBox checkBox;

        public MyViewHolder(View view) {
            super(view);

            tvTransactionDate = view.findViewById(R.id.tvTransactionDate);
            tvStatus = view.findViewById(R.id.tvStatus);
            tvAmount = view.findViewById(R.id.tvAmount);
            tvConversion = view.findViewById(R.id.tvConversion);
            tvDDk = view.findViewById(R.id.tvDDk);


        }
    }

    public interface SetOnItemClick {
        public void onItemClick(int position);
    }
}
