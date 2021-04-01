package com.ddkcommunity.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ddkcommunity.Constant;
import com.ddkcommunity.R;
import com.ddkcommunity.model.RequestModel;
import com.ddkcommunity.model.ShowRequestApiModel;
import com.ddkcommunity.utilies.AppConfig;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class SubCreateCancellaitonAdapter extends RecyclerView.Adapter<SubCreateCancellaitonAdapter.MyViewHolder> {

    private List<RequestModel.Sub> createCancellationRequestlist;
    String linestatus;
    private Activity activity;
    LinearLayout sendrequestlayout;
    BigDecimal totalselection;

    public SubCreateCancellaitonAdapter(List<RequestModel.Sub> createCancellationRequestlist, Activity activity) {
        this.createCancellationRequestlist=createCancellationRequestlist;
        this.activity = activity;
    }

    public void updateData(List<RequestModel.Sub> data) {
        this.createCancellationRequestlist = data;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subitem_cancellation, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        try {
            holder.tvAddress.setText(createCancellationRequestlist.get(position).getDdkAddress());
            holder.tvDate.setText(createCancellationRequestlist.get(position).getCreatedAt());
            holder.tvAmount.setText(createCancellationRequestlist.get(position).getSubscriptionAmount()+" USDT");

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return createCancellationRequestlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView  tvAddress,tvAmount,tvDate;
        public CheckBox chbContent;
        public LinearLayout viewclickdata;

        public MyViewHolder(View view) {
            super(view);
            tvAddress=view.findViewById(R.id.tvAddress);
            tvAmount=view.findViewById(R.id.tvAmount);
            tvDate=view.findViewById(R.id.tvDate);
            viewclickdata=view.findViewById(R.id.viewclickdata);
            chbContent= view.findViewById(R.id.chbContent);
            }
    }

}
