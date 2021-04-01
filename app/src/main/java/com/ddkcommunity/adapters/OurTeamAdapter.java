package com.ddkcommunity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ddkcommunity.Constant;
import com.ddkcommunity.R;
import com.ddkcommunity.model.OurTeamData;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class OurTeamAdapter extends RecyclerView.Adapter<OurTeamAdapter.MyViewHolder> {


    private List<OurTeamData> data;
    private Context activity;
    private SetOnClickListener setOnClickListener;

    public OurTeamAdapter(List<OurTeamData> data, Context activity, SetOnClickListener setOnClickListener) {
        this.activity = activity;
        this.data = data;
        this.setOnClickListener = setOnClickListener;
    }

    public void updateData(List<OurTeamData> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_our_team, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        Picasso.with(activity).load(Constant.SLIDERIMG+data.get(position).drawable).into(holder.ivImage);
        if (position % 2 == 0) {
            holder.lytMain.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorLightPurpal));
        } else {
            holder.lytMain.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorApp));
        }
        holder.tvDesignation.setText(data.get(position).designation);
        holder.tvUserName.setText(data.get(position).name);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDesignation, tvUserName;
        public CircleImageView ivImage, ivImage1;
        private LinearLayout lytMain;

        public MyViewHolder(View view) {
            super(view);

            lytMain = view.findViewById(R.id.lytMain);
            tvUserName = view.findViewById(R.id.tvUserName);
            tvDesignation = view.findViewById(R.id.tvDesignation);
            ivImage = view.findViewById(R.id.ivImage);
            ivImage1 = view.findViewById(R.id.ivImage1);

            view.setOnClickListener(new View.OnClickListener() {
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
