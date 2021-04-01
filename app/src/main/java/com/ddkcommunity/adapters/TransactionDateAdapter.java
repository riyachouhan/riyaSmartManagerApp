package com.ddkcommunity.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.ddkcommunity.R;
import com.ddkcommunity.model.TransactionDate;

import java.util.List;

public class TransactionDateAdapter extends RecyclerView.Adapter<TransactionDateAdapter.MyViewHolder> {


    private List<TransactionDate.TransactionDateData> data;
    private SetOnItemClick setOnItemClick;

    public TransactionDateAdapter(List<TransactionDate.TransactionDateData> data, SetOnItemClick setOnItemClick) {
        this.setOnItemClick = setOnItemClick;
        this.data = data;
    }

    public void updateData(List<TransactionDate.TransactionDateData> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction_date, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.tvDate.setText(data.get(position).from_date + " to " + data.get(position).to_date);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDate;

        public MyViewHolder(View view) {
            super(view);

            tvDate = view.findViewById(R.id.tvDate);

            view.findViewById(R.id.tvDate).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setOnItemClick.onItemClick(getAdapterPosition());
                }
            });

        }
    }

    public interface SetOnItemClick {
        public void onItemClick(int position);
    }
}
