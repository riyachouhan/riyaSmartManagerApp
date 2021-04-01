package com.ddkcommunity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ddkcommunity.R;
import com.ddkcommunity.model.FreeFlightVoucherListData;

import java.util.List;

public class FreeFlightVoucherAdapter extends RecyclerView.Adapter<FreeFlightVoucherAdapter.MyViewHolder> {
    Context mContext;
    List<FreeFlightVoucherListData>freeFlightVoucherLists;

    public FreeFlightVoucherAdapter(Context mContext, List<FreeFlightVoucherListData> freeFlightVoucherLists) {
        this.mContext = mContext;
        this.freeFlightVoucherLists = freeFlightVoucherLists;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_free_flight_voucher, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        try {
            holder.tv_activityData.setText(freeFlightVoucherLists.get(position).getContent());
            holder.tv_activityDate.setText(freeFlightVoucherLists.get(position).getDate());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return freeFlightVoucherLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_activityData,tv_activityDate;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_activityData = itemView.findViewById(R.id.tv_activityData);
            tv_activityDate = itemView.findViewById(R.id.tv_activityDate);
        }
    }
}
