package com.ddkcommunity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ddkcommunity.R;

import java.util.ArrayList;

public class tronListAdapter extends RecyclerView.Adapter<tronListAdapter.MyViewHolder> {

    private ArrayList<String> data;
    private Context activity;
    private SetOnItemClickListener setOnItemClickListener;
    private TextView tvSelectDdkAddress;

    public tronListAdapter(TextView tvSelectDdkAddress,ArrayList<String> data,
                           Context activity, SetOnItemClickListener setOnItemClickListener) {
        this.activity = activity;
        this.tvSelectDdkAddress=tvSelectDdkAddress;
        this.data = data;
        this.setOnItemClickListener = setOnItemClickListener;
    }

    public void updateData(ArrayList<String> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_wallet_tron_sub, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        String sss=data.get(position);
        holder.title.setText(sss);
        if(tvSelectDdkAddress.getText().toString().equalsIgnoreCase(sss))
        {
            holder.online.setVisibility(View.VISIBLE);
        }else
        {
            holder.online.setVisibility(View.INVISIBLE);

        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, ddk;
        ImageView online;

        public MyViewHolder(View view) {
            super(view);

            online = view.findViewById(R.id.online);
            title = view.findViewById(R.id.title_TV);
            ddk = view.findViewById(R.id.ddk_code_TV);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setOnItemClickListener.onItemClick(data.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface SetOnItemClickListener {
        public void onItemClick(String wallet_code);
    }
}
