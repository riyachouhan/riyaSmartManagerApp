package com.ddkcommunity.adapters;

import android.os.Build;
import android.text.Html;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ddkcommunity.R;
import com.ddkcommunity.fragment.HomeFragment;
import com.ddkcommunity.model.Announcement;

import java.util.List;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.MyViewHolder> {


    private List<Announcement.AnnouncementData> data;
    private SetOnItemClick setOnItemClick;

    public AnnouncementAdapter(List<Announcement.AnnouncementData> data, SetOnItemClick setOnItemClick) {
        this.setOnItemClick = setOnItemClick;
        this.data = data;
    }

    public void updateData(List<Announcement.AnnouncementData> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_announcement, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.tvHeader.setText(data.get(position).title);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.tvDescription.setText(Html.fromHtml(data.get(position).description, Html.FROM_HTML_MODE_COMPACT, new HomeFragment.ImageGetter(), null));
        } else {
            holder.tvDescription.setText(Html.fromHtml(data.get(position).description, new HomeFragment.ImageGetter(), null));
        }
        Linkify.addLinks( holder.tvDescription, Linkify.WEB_URLS);
        if (data.get(position).isChecked) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvHeader, tvDescription;
        public CheckBox checkBox;

        public MyViewHolder(View view) {
            super(view);

            tvHeader = view.findViewById(R.id.tvHeader);
            tvDescription = view.findViewById(R.id.tvDescription);
            checkBox = view.findViewById(R.id.btnDoNotShowA);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    data.get(getAdapterPosition()).isChecked = isChecked;
                }
            });

            view.findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
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
