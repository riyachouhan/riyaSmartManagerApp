package com.ddkcommunity.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ddkcommunity.R;
import com.ddkcommunity.model.BankList;

import java.util.List;

public class BankListAdapter extends RecyclerView.Adapter<BankListAdapter.MyViewHolder> {


    private List<BankList.BankData> data;
    private String cardType;
    private SetOnItemClickListener setOnItemClickListener;
    private Context mContext;
    private String imagePath;

    public BankListAdapter(Context mContext, String imagePath, List<BankList.BankData> data, SetOnItemClickListener setOnItemClickListener) {
        this.data = data;
        this.mContext = mContext;
        this.imagePath = imagePath;
        this.setOnItemClickListener = setOnItemClickListener;
    }

    public void updateData(List<BankList.BankData> data, String image_path) {
        this.data = data;
        this.imagePath = image_path;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_banl, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.tvBankNameDDk.setText(data.get(position).bank_name);
        Glide.with(mContext).load(imagePath + data.get(position).image).into(holder.ivBankIconDDK);
        Log.d("response banklist","adapter set");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivBankIconDDK,ic_addicon;
        private TextView tvBankNameDDk;

        public MyViewHolder(View view) {
            super(view);

            ivBankIconDDK = view.findViewById(R.id.ivBankIconDDK);
            tvBankNameDDk = view.findViewById(R.id.tvBankNameDDk);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setOnItemClickListener.onItemClick(data.get(getAdapterPosition()).bank_name, imagePath + data.get(getAdapterPosition()).image, "" + data.get(getAdapterPosition()).id);
                }
            });
        }
    }

    public interface SetOnItemClickListener {
        public void onItemClick(String name, String image, String id);
    }
}
