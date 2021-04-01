package com.ddkcommunity.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;
import com.ddkcommunity.R;
import com.ddkcommunity.model.CardName;

import java.util.List;

public class CardTypeAdapter extends RecyclerView.Adapter<CardTypeAdapter.MyViewHolder> {


    private List<CardName> data;
    private String cardType;

    public CardTypeAdapter(List<CardName> data) {
        this.data = data;
    }

    public void updateData(String cardType) {
        this.cardType = cardType;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card_type, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.ivCardType.setImageDrawable(data.get(position).drawable);

        if (data.get(position).cardName.equalsIgnoreCase(cardType)) {
            holder.viewBack.setVisibility(View.GONE);
        } else {
            holder.viewBack.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivCardType;
        public View viewBack;

        public MyViewHolder(View view) {
            super(view);
            ivCardType = view.findViewById(R.id.ivCardType);
            viewBack = view.findViewById(R.id.backView);
        }
    }

}
