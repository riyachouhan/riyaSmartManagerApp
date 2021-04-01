package com.ddkcommunity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ddkcommunity.R;
import com.ddkcommunity.model.BankList;

import java.util.List;

import static com.ddkcommunity.Constant.SLIDERIMG;
import static com.ddkcommunity.adapters.AllTypeCashoutFragmentAdapter.usercountryselect;

public class AllTypeAvailableFragmentAdapter extends RecyclerView.Adapter<AllTypeAvailableFragmentAdapter.MyViewHolder> {

    private List<BankList.BankData> samkoinList;
    private Context activity;
    private SetOnItemClickListener setOnItemClickListener;
    String image_path;

    public AllTypeAvailableFragmentAdapter(List<BankList.BankData> samkoinList, Context activity,String imagepath, SetOnItemClickListener setOnItemClickListener) {
        this.samkoinList=samkoinList;
        this.activity = activity;
        this.setOnItemClickListener = setOnItemClickListener;
        this.image_path = imagepath;
     }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_all_available_banl, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {
            if(!samkoinList.get(position).getBank_name().equals("All"))
            {
                if (usercountryselect==1)
                {
                    holder.iv_AllAvailableBankName.setText(samkoinList.get(position).getBank_name());
                    Glide.with(activity).load(SLIDERIMG+ samkoinList.get(position).image).into(holder.iv_AllAvailableBankIcon);
                }else
                {
                    holder.iv_AllAvailableBankName.setText(samkoinList.get(position).getBank_name());
                    Glide.with(activity).load(SLIDERIMG+samkoinList.get(position).image).into(holder.iv_AllAvailableBankIcon);
                    holder.iv_AllAvailableBankIcon.setVisibility(View.GONE);
                 }

                holder.ll_ParticularBankId.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            setOnItemClickListener.onItemClick(samkoinList.get(position).getBank_name(),samkoinList.get(position).getType(),samkoinList.get(position).getId());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return samkoinList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_AllAvailableBankIcon;
        LinearLayout ll_ParticularBankId;
        TextView iv_AllAvailableBankName;

        public MyViewHolder(View view) {
            super(view);
            iv_AllAvailableBankIcon=view.findViewById(R.id.iv_AllAvailableBankIcon);
            ll_ParticularBankId=view.findViewById(R.id.ll_ParticularBankId);
            iv_AllAvailableBankName=view.findViewById(R.id.iv_AllAvailableBankName);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setOnItemClickListener.onItemClick(samkoinList.get(getAdapterPosition()).getBank_name(),samkoinList.get(getAdapterPosition()).getType(),samkoinList.get(getAdapterPosition()).getId());
                }
            });
        }
    }

    public interface SetOnItemClickListener {
        public void onItemClick(String bank_name, String type, String id);
    }

}
