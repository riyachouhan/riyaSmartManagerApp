package com.ddkcommunity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ddkcommunity.R;
import com.ddkcommunity.model.exchange.CurrencyList;

import java.util.List;

public class CurrencyListAdapter extends RecyclerView.Adapter<CurrencyListAdapter.MyViewHolder> {

    private List<CurrencyList.CurrencyData> data;
    private Context activity;
    private SetOnItemClickListener setOnItemClickListener;

    public CurrencyListAdapter(List<CurrencyList.CurrencyData> data, Context activity, SetOnItemClickListener setOnItemClickListener) {
        this.activity = activity;
        this.data = data;
        this.setOnItemClickListener = setOnItemClickListener;
    }


    public void updateData(List<CurrencyList.CurrencyData> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_currency_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.tvCurrencyName.setText(data.get(position).currency_name);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvCurrencyName;

        public MyViewHolder(View view) {
            super(view);

            tvCurrencyName = view.findViewById(R.id.tvCurrencyName);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setOnItemClickListener.onItemClick(data.get(getAdapterPosition()).code,data.get(getAdapterPosition()).currency_name);
                }
            });
        }
    }

    public interface SetOnItemClickListener {
        public void onItemClick(String code, String currencyName);
    }
}
