package com.ddkcommunity.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ddkcommunity.R;
import com.ddkcommunity.model.withdrawal.WithdrawalData;
import com.ddkcommunity.utilies.AppConfig;

import java.util.ArrayList;
import java.util.List;

public class WithdrawalTransactionAdapter extends RecyclerView.Adapter<WithdrawalTransactionAdapter.MyViewHolder> {


    private List<WithdrawalData> data;
    private ArrayList<WithdrawalData> selectedData = new ArrayList<>();
    private SetOnItemClick setOnItemClick;

    public WithdrawalTransactionAdapter(List<WithdrawalData> data, SetOnItemClick setOnItemClick) {
        this.setOnItemClick = setOnItemClick;
        this.data = data;
    }

    public void updateData(List<WithdrawalData> data) {
        this.data = data;
        selectedData.clear();
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_withdrawal_pooling, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {


        holder.tvDate.setText(AppConfig.dateFormat(data.get(position).transactionDate,"yyyy-MM-dd"));
        holder.tvNo.setText(data.get(position).no + data.get(position).id);
        holder.tvAmount.setText("" + data.get(position).amount);
        holder.tvDDkAddress.setText(data.get(position).receiverAddress);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDDkAddress, tvAmount, tvNo, tvDate;

        private CheckBox btnCheckbox;

        public MyViewHolder(View view) {
            super(view);
//
            tvAmount = view.findViewById(R.id.tvAmount);
            tvNo = view.findViewById(R.id.tvNo);
            tvDate = view.findViewById(R.id.tvDate);
            tvDDkAddress = view.findViewById(R.id.tvDDkAddress);
            btnCheckbox = view.findViewById(R.id.btnCheckWith);

            btnCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (btnCheckbox.isChecked()) {
                        selectedData.add(data.get(getAdapterPosition()));
                        Log.d("Check-====>> 1 ", "" + btnCheckbox.isChecked());
                    } else {
                        selectedData.remove(data.get(getAdapterPosition()));
                        Log.d("Check-====>> 2 ", "" + btnCheckbox.isChecked());
                    }
                    setOnItemClick.onItemClick(selectedData);
                }
            });
        }
    }

    public interface SetOnItemClick {
        public void onItemClick(ArrayList<WithdrawalData> selectedData);
    }
}
