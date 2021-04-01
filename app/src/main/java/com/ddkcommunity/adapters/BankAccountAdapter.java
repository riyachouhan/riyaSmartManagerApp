package com.ddkcommunity.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ddkcommunity.R;
import com.ddkcommunity.model.BankAccounts;

import java.util.List;

public class BankAccountAdapter extends RecyclerView.Adapter<BankAccountAdapter.MyViewHolder> {


    private List<BankAccounts> data;
    private SetOnItemClickListener setOnItemClickListener;

    public BankAccountAdapter(List<BankAccounts> data, SetOnItemClickListener setOnItemClickListener) {
        this.data = data;
        this.setOnItemClickListener = setOnItemClickListener;
    }

    public void updateData(List<BankAccounts> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bank_accounts, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.tvBankAccount.setText(data.get(position).bankAcNumber);
        holder.tvBankHolderName.setText(data.get(position).bankHolderName);
        holder.tvMobileNumber.setText(data.get(position).mobileNumber);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvBankHolderName, tvBankAccount, tvMobileNumber;

        public MyViewHolder(View view) {
            super(view);
            tvBankAccount = view.findViewById(R.id.tvBankAccount);
            tvBankHolderName = view.findViewById(R.id.tvBankHolderName);
            tvMobileNumber = view.findViewById(R.id.tvMobileNumber);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setOnItemClickListener.onItemClick(data.get(getAdapterPosition()).bankHolderName, data.get(getAdapterPosition()).bankAcNumber, data.get(getAdapterPosition()).mobileNumber);
                }
            });
        }
    }

    public interface SetOnItemClickListener {
        public void onItemClick(String name, String accountNo, String mobileNumber);
    }
}
