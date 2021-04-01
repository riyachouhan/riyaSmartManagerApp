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

import com.ddkcommunity.R;
import com.ddkcommunity.model.CreateRequestModel;
import com.ddkcommunity.model.Exchangesdata;

import java.util.List;

public class ExchangeAdapter extends RecyclerView.Adapter<ExchangeAdapter.MyViewHolder> {


    private List<Exchangesdata> data;
    String linestatus;
    private Activity activity;

    public ExchangeAdapter(List<Exchangesdata> data, Activity activity) {
        this.activity = activity;
        this.linestatus=linestatus;
        this.data = data;
    }

    public void updateData(List<Exchangesdata> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exchange, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        try {
            if(data.get(position).getAmountstatus().equalsIgnoreCase("true"))
            {
                holder.tvprice.setTextColor(ContextCompat.getColor(activity, R.color.colorGreen));
            }else
            {
                holder.tvprice.setTextColor(ContextCompat.getColor(activity, R.color.colorRed));
            }
            holder.tvprice.setText(data.get(position).getPriceddk());
            holder.tvamount.setText(data.get(position).getAmountsam());
            holder.tvtotal.setText(data.get(position).getTotalsamddk());

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
        public TextView tvprice,tvamount,tvtotal;

        public MyViewHolder(View view) {
            super(view);
            tvprice=view.findViewById(R.id.tvprice);
            tvamount=view.findViewById(R.id.tvamount);
            tvtotal=view.findViewById(R.id.tvtotal);
            }
    }
}
