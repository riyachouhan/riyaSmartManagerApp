package com.ddkcommunity.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ddkcommunity.R;
import com.ddkcommunity.model.CreateRequestModel;
import com.ddkcommunity.model.RequestModel;

import java.util.List;

public class RequestCancellaitonAdapter extends RecyclerView.Adapter<RequestCancellaitonAdapter.MyViewHolder> {


    private List<RequestModel.Datum> data;
    private Activity activity;

    public RequestCancellaitonAdapter(List<RequestModel.Datum> data, Activity activity) {
        this.activity = activity;
        this.data = data;
    }

    public void updateData(List<RequestModel.Datum> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.iteam_request_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        try {
            holder.tvAddress.setText("#"+data.get(position).getId());
            holder.tvDate.setText(data.get(position).getCreatedAt());
            if(data.get(position).getPaymentMethodType().equalsIgnoreCase("SAM"))
            {
                holder.subddkamount.setText("SAM");
                holder.tvAmount.setText(data.get(position).getDdkAmount()+" SAM");
            }else
            {
                holder.subddkamount.setText("DDK Amount");
                holder.tvAmount.setText(data.get(position).getDdkAmount()+" DDK");
            }
            String upperString = data.get(position).getStatus().toUpperCase();
            if (data.get(position).getStatus().toString().equalsIgnoreCase("pending")) {
                holder.tvStatus.setText(upperString);
                holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.orange));
                holder.tvStatus.setBackground(ContextCompat.getDrawable(activity, R.drawable.light_orange_back));

            } else if (data.get(position).getStatus().toString().equalsIgnoreCase("completed"))
            {

                holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.colorGreen));
                holder.tvStatus.setBackground(ContextCompat.getDrawable(activity, R.drawable.light_green_back));
                holder.tvStatus.setText(upperString);
            } else if (data.get(position).getStatus().toString().equalsIgnoreCase("denied")) {
                holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.colorRed));
                holder.tvStatus.setBackground(ContextCompat.getDrawable(activity, R.drawable.light_red_back));
                holder.tvStatus.setText(upperString);
            }
            holder.subview_layout.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(holder.request_main_layout_view.getVisibility()==View.VISIBLE)
                    {
                        holder.drop_img_icon.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.drop_down_icon));
                        holder.request_main_layout_view.setVisibility(View.GONE);
                    }else {
                        holder.drop_img_icon.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.rotate_drop_down_open));
                        holder.request_main_layout_view.setVisibility(View.VISIBLE);
                    }
                }
            });

             holder.amount_ddk.setText(data.get(position).getDdkAmount().toString()+"");
            holder.charge_fee.setText(data.get(position).getCharge().toString()+"");
            holder.subtotal_fees.setText(data.get(position).getSubTotal().toString()+"");
            holder.conversion_fee.setText(data.get(position).getConversion().toString()+"");

            RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(activity);
            holder.rvCreateRecycle.setLayoutManager(mLayoutManager1);
            holder.rvCreateRecycle.setItemAnimator(new DefaultItemAnimator());
            if(data.get(position).getSubs().size()!=0)
            {
                SubCreateCancellaitonAdapter mAdapter = new SubCreateCancellaitonAdapter(data.get(position).getSubs(), activity);
                holder.rvCreateRecycle.setAdapter(mAdapter);
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
        public TextView  subddkamount,amount_ddk,charge_fee,subtotal_fees,conversion_fee,tvStatus,tvAddress,tvAmount,tvDate;
        public LinearLayout request_main_layout_view,subview_layout,viewclickdata;
        public RecyclerView rvCreateRecycle;
        ImageView drop_img_icon;

        public MyViewHolder(View view) {
            super(view);
            subddkamount=view.findViewById(R.id.subddkamount);
            amount_ddk=view.findViewById(R.id.amount_ddk);
            charge_fee=view.findViewById(R.id.charge_fee);
            subtotal_fees=view.findViewById(R.id.subtotal_fees);
            conversion_fee=view.findViewById(R.id.conversion_fee);
            drop_img_icon=view.findViewById(R.id.drop_img_icon);
            rvCreateRecycle=view.findViewById(R.id.rvCreateRecycle);
            subview_layout=view.findViewById(R.id.subview_layout);
            tvStatus=view.findViewById(R.id.tvStatus);
            tvAddress=view.findViewById(R.id.tvAddress);
            tvAmount=view.findViewById(R.id.tvAmount);
            tvDate=view.findViewById(R.id.tvDate);
            request_main_layout_view=view.findViewById(R.id.request_main_layout_view);
            viewclickdata=view.findViewById(R.id.viewclickdata);
            }
    }
}
