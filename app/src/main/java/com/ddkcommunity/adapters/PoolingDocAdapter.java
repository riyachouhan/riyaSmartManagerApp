package com.ddkcommunity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.ddkcommunity.R;
import com.ddkcommunity.model.projects.PoolingDoc;

import java.util.List;

public class PoolingDocAdapter extends RecyclerView.Adapter<PoolingDocAdapter.MyViewHolder> {


    private List<PoolingDoc> data;
    private Context activity;
    private SetOnClickListener setOnClickListener;

    public PoolingDocAdapter(List<PoolingDoc> data, Context activity, SetOnClickListener setOnClickListener) {
        this.activity = activity;
        this.data = data;
        this.setOnClickListener = setOnClickListener;
    }

    public void updateData(List<PoolingDoc> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pooling_doc, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        holder.tvDocHeader.setText(data.get(position).description);
        holder.ivBanner.setImageDrawable(data.get(position).drawable);


        /*holder.vote_TV.setText(data.get(position).getVoteCount());*/
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDocHeader;
        public ImageView ivBanner;

        public MyViewHolder(View view) {
            super(view);


            tvDocHeader = view.findViewById(R.id.tvDocHeader);
            ivBanner = view.findViewById(R.id.ivBanner);

            tvDocHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setOnClickListener.setClick(getAdapterPosition());
                }
            });
        }
    }

    public interface SetOnClickListener {
        public void setClick(int position);
    }
}
